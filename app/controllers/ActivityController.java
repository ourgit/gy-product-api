package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.product.Product;
import models.promotion.AssistConfig;
import models.promotion.CardCouponConfig;
import models.user.AssistMember;
import models.user.Member;
import models.user.MemberAssist;
import models.user.MemberCardCoupon;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static constants.BusinessConstant.CARD_COUPON_FOR_ASSIST;

/**
 * 用户控制类
 */
public class ActivityController extends BaseController {
    public static final String ASSIST_LIST_JSON_CACHE = "ASSIST_LIST_JSON_CACHE:";
    public static final String ASSIST_LIST_LAUNCHERS_JSON_CACHE = "ASSIST_LIST_LAUNCHERS_JSON_CACHE:";

    /**
     * @api {GET} /v1/p/assist_config_list/?page=&status 01助力配置列表
     * @apiName listAssistConfigs
     * @apiGroup ASSIST
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){int} pages 页码
     * @apiSuccess (Success 200){JsonArray} list 订单列表
     * @apiSuccess (Success 200){String} title  标题
     * @apiSuccess (Success 200){String} content 详情
     * @apiSuccess (Success 200){int} requireInvites 需要邀请人数
     * @apiSuccess (Success 200){String} ruleContent 规则说明
     * @apiSuccess (Success 200){String} beginTime 开始时间
     * @apiSuccess (Success 200){String} endTime 结束时间
     * @apiSuccess (Success 200){String} expireDays 用户领取后的有效天数，默认3天
     * @apiSuccess (Success 200){int} status 1生效，2失效
     * @apiSuccess (Success 200){String} imgUrl 封面图片
     * @apiSuccess (Success 200){boolean} needShow 是否展示 true/false
     * @apiSuccess (Success 200){boolean} needUniTime 是否需要统一时间
     */
    public CompletionStage<Result> listAssistConfigs(Http.Request request, int page) {
        Member member = businessUtils.getUserIdByAuthToken(request);
        return CompletableFuture.supplyAsync(() -> {
            String key = ASSIST_LIST_JSON_CACHE + page;
            if (null == member) {
                Optional<String> jsonCache = cache.getOptional(key);
                if (jsonCache.isPresent()) {
                    String result = jsonCache.get();
                    if (!ValidationUtil.isEmpty(result)) return ok(Json.parse(result));
                }
            }

            ExpressionList<AssistConfig> expressionList = AssistConfig.find.query().where()
                    .ge("status", AssistConfig.STATUS_NOT_START)
                    .le("status", AssistConfig.STATUS_PROCESSING);
            List<AssistConfig> list;
            int pages = 0;
            if (page > 0) {
                PagedList<AssistConfig> pagedList = expressionList.orderBy().desc("id")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_20).findPagedList();
                pages = pagedList.getTotalPageCount();
                list = pagedList.getList();
            } else list = expressionList.orderBy().desc("id").findList();
            list.parallelStream().forEach((each) -> {
                if (null != member) {
                    MemberAssist memberAssist = MemberAssist.find.query().where()
                            .eq("uid", member.id)
                            .eq("status", MemberAssist.STATUS_PROCESSING)
                            .eq("assistId", each.id)
                            .orderBy().desc("id")
                            .setMaxRows(1)
                            .findOne();
                    if (null != memberAssist) {
                        if (memberAssist.status == MemberAssist.STATUS_SUCCEED) {
                            MemberCardCoupon memberCardCoupon = MemberCardCoupon.find.query().where()
                                    .eq("subId", memberAssist.id)
                                    .eq("code", CARD_COUPON_FOR_ASSIST)
                                    .findOne();
                            memberAssist.memberCardCoupon = memberCardCoupon;
                        }
                    }
                    each.memberAssist = memberAssist;
                }
                if (each.cardCouponId > 0) {
                    CardCouponConfig cardCouponConfig = CardCouponConfig.find.byId(each.cardCouponId);
                    if (null != cardCouponConfig) {
                        Product product = Product.find.byId(cardCouponConfig.productId);
                        if (null != product) {
                            each.couponProductCoverImgUrl = product.coverImgUrl;
                            each.couponProductPrice = product.price;
                        }
                    }
                }
            });
            ObjectNode result = Json.newObject();
            result.put("pages", pages);
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            if (null == member) cache.set(key, Json.stringify(result), 10);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/member_assist/:id/ 02用户助力详情
     * @apiName getMemberAssistDetail
     * @apiGroup ASSIST
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {long} uid 用户ID
     * @apiSuccess (Success 200) {String} userName 用户名字
     * @apiSuccess (Success 200) {long} assistId 助力ID
     * @apiSuccess (Success 200) {String} assistTitle 助力标题
     * @apiSuccess (Success 200) {long} beginTime 生效时间
     * @apiSuccess (Success 200) {long} endTime 失效时间
     * @apiSuccess (Success 200) {long} inviteAmount 已邀请人数
     * @apiSuccess (Success 200) {String} updateTime 最后更新时间
     * @apiSuccess (Success 200) {int} status 状态 -1失败，1进行中,2已成功
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40002) {int} code 40002 该助力不存在
     */
    public CompletionStage<Result> getMemberAssistDetail(Http.Request request, long id) {
        return CompletableFuture.supplyAsync(() -> {
            MemberAssist memberAssist = MemberAssist.find.byId(id);
            if (null == memberAssist) return okCustomJson(CODE40001, "该助力不存在");
            List<AssistMember> assistMembers = AssistMember.find.query().where()
                    .eq("userAssistId", memberAssist.id)
                    .orderBy().asc("id")
                    .findList();
            memberAssist.assistMemberList.addAll(assistMembers);
            if (memberAssist.status == MemberAssist.STATUS_SUCCEED) {
                MemberCardCoupon memberCardCoupon = MemberCardCoupon.find.query().where()
                        .eq("subId", memberAssist.id)
                        .eq("code", CARD_COUPON_FOR_ASSIST)
                        .findOne();
                memberAssist.memberCardCoupon = memberCardCoupon;
            }
            ObjectNode node = (ObjectNode) Json.toJson(memberAssist);
            node.put(CODE, CODE200);
            AssistConfig assistConfig = AssistConfig.find.byId(memberAssist.assistId);
            if (null != assistConfig) {
                if (assistConfig.cardCouponId > 0) {
                    CardCouponConfig cardCouponConfig = CardCouponConfig.find.byId(assistConfig.cardCouponId);
                    if (null != cardCouponConfig) {
                        Product product = Product.find.byId(cardCouponConfig.productId);
                        if (null != product) {
                            assistConfig.couponProductCoverImgUrl = product.coverImgUrl;
                            assistConfig.couponProductPrice = product.price;
                        }
                    }
                }
                node.set("assistConfig", Json.toJson(assistConfig));
            }
            return ok(node);
        });
    }


    /**
     * @api {GET} /v1/p/assist_succeed_launchers/:assistId/?page= 03助力成功列表
     * @apiName listAssistSucceedLaunchers
     * @apiGroup ASSIST
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){int} pages 页码
     * @apiSuccess (Success 200){JsonArray} list 列表
     */
    public CompletionStage<Result> listAssistSucceedLaunchers(Http.Request request, long assistId, int page) {
        String jsonCacheKey = ASSIST_LIST_LAUNCHERS_JSON_CACHE + assistId + ":" + page;
        return asyncCacheApi.getOptional(jsonCacheKey).thenApplyAsync((jsonCache) -> {
            if (jsonCache.isPresent()) {
                String result = (String) jsonCache.get();
                if (!ValidationUtil.isEmpty(result)) return ok(Json.parse(result));
            }
            PagedList<MemberAssist> pagedList = MemberAssist.find.query().where()
                    .eq("assistId", assistId)
                    .eq("status", MemberAssist.STATUS_SUCCEED)
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                    .findPagedList();
            List<MemberAssist> list = pagedList.getList();
            ObjectNode result = Json.newObject();
            result.put("hasNext", pagedList.hasNext());
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            cache.set(jsonCacheKey, Json.stringify(result), 2);
            return ok(result);
        });
    }

}
