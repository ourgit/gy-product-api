package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.activity.ActivityConfig;
import models.activity.ActivityLog;
import models.activity.ActivityShopTotalLog;
import models.activity.ActivityUserTotalLog;
import models.product.Product;
import models.promotion.*;
import models.user.AssistMember;
import models.user.Member;
import models.user.MemberAssist;
import models.user.MemberCardCoupon;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.ArrayList;
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
    public static final String BARGAIN_LIST_JSON_CACHE = "BARGAIN_LIST_JSON_CACHE:";
    public static final String BARGAIN_LIST_LAUNCHERS_JSON_CACHE = "BARGAIN_LIST_LAUNCHERS_JSON_CACHE:";
    public static final String LATEST_ACTIVITY_LIST_JSON_CACHE = "LATEST_ACTIVITY_LIST_JSON_CACHE:";
    public static final String LATEST_ACTIVITY_ATTENDS_LIST_JSON_CACHE = "LATEST_ACTIVITY_ATTENDS_LIST_JSON_CACHE:";

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

    /**
     * @api {GET} /v1/p/bargain_config_list/?page=&status 04砍价配置列表
     * @apiName listBargainConfigs
     * @apiGroup ASSIST
     * @apiSuccess (Success 200){JsonArray} list 订单列表
     * @apiSuccess (Success 200){String} title  标题
     * @apiSuccess (Success 200){String} content 详情
     * @apiSuccess (Success 200){String} ruleContent 规则说明
     * @apiSuccess (Success 200){int} requireInvites 需要邀请人数
     * @apiSuccess (Success 200){String} imgUrl 封面图片
     * @apiSuccess (Success 200){String} beginTime 开始时间
     * @apiSuccess (Success 200){String} endTime 结束时间
     * @apiSuccess (Success 200){String} expireHours 用户领取后的有效时间,以小时为单位
     * @apiSuccess (Success 200){int} status 1生效，2失效
     * @apiSuccess (Success 200){long} needPayMoney 底价
     * @apiSuccess (Success 200){long} productId productId
     * @apiSuccess (Success 200){long} skuId skuId
     * @apiSuccess (Success 200){long} alreadySucceedCount 已成功砍价数
     * @apiSuccess (Success 200){boolean} needShow 是否展示 true/false
     * @apiSuccess (Success 200){boolean} needUniTime 是否需要统一时间
     * @apiSuccess (Success 200){boolean} useSystemRules 是否使用统一规则
     * @apiSuccess (Success 200){boolean} useNeedAddress 是否需要配送地址，自提为false
     */
    public CompletionStage<Result> listBargainConfigs(Http.Request request, int page) {
        Member member = businessUtils.getUserIdByAuthToken(request);
        return CompletableFuture.supplyAsync(() -> {
            String key = BARGAIN_LIST_JSON_CACHE + page;
            if (null == member) {
                Optional<String> jsonCache = cache.getOptional(key);
                if (jsonCache.isPresent()) {
                    String result = jsonCache.get();
                    if (!ValidationUtil.isEmpty(result)) return ok(Json.parse(result));
                }
            }

            ExpressionList<BargainConfig> expressionList = BargainConfig.find.query().where()
                    .ge("status", BargainConfig.STATUS_NOT_START)
                    .le("status", BargainConfig.STATUS_PROCESSING);
            List<BargainConfig> list;
            int pages = 0;
            if (page > 0) {
                PagedList<BargainConfig> pagedList = expressionList.orderBy().desc("id")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_20).findPagedList();
                pages = pagedList.getTotalPageCount();
                list = pagedList.getList();
            } else list = expressionList.orderBy().desc("id").findList();
            list.parallelStream().forEach((each) -> {
                if (null != member) {
                    Bargain bargain = Bargain.find.query().where()
                            .eq("uid", member.id)
                            .eq("status", Bargain.STATUS_PROCESSING)
                            .eq("bargainId", each.id)
                            .orderBy().desc("id")
                            .setMaxRows(1)
                            .findOne();
                    each.bargain = bargain;
                }
                Product product = Product.find.byId(each.productId);
                if (null != product) {
                    each.couponProductCoverImgUrl = product.coverImgUrl;
                    each.couponProductPrice = product.price;
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
     * @api {GET} /v1/p/bargain/:id/ 05用户砍价详情
     * @apiName getBargainDetail
     * @apiGroup ASSIST
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {long} uid 用户ID
     * @apiSuccess (Success 200) {String} userName 用户名字
     * @apiSuccess (Success 200) {long} bargainId 砍价ID
     * @apiSuccess (Success 200) {String} bargainTitle 砍价标题
     * @apiSuccess (Success 200) {long} beginTime 生效时间
     * @apiSuccess (Success 200) {long} endTime 失效时间
     * @apiSuccess (Success 200) {long} inviteAmount 已邀请人数
     * @apiSuccess (Success 200) {String} updateTime 最后更新时间
     * @apiSuccess (Success 200) {int} status 状态 -1失败，1进行中,2已成功
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40002) {int} code 40002 该助力不存在
     */
    public CompletionStage<Result> getBargainDetail(Http.Request request, long id) {
        return CompletableFuture.supplyAsync(() -> {
            Bargain bargain = Bargain.find.byId(id);
            if (null == bargain) return okCustomJson(CODE40001, "该砍价不存在");
            List<BargainMember> bargainMembers = BargainMember.find.query().where()
                    .eq("userBargainId", bargain.id)
                    .orderBy().asc("id")
                    .findList();
            bargain.bargainMemberList.addAll(bargainMembers);
            ObjectNode node = (ObjectNode) Json.toJson(bargain);
            node.put(CODE, CODE200);
            BargainConfig bargainConfig = BargainConfig.find.byId(bargain.bargainId);
            if (null != bargainConfig) {
                if (bargainConfig.productId > 0) {
                    Product product = Product.find.byId(bargainConfig.productId);
                    if (null != product) {
                        bargainConfig.couponProductCoverImgUrl = product.coverImgUrl;
                        bargainConfig.couponProductPrice = product.price;
                    }
                }
                node.set("bargainConfig", Json.toJson(bargainConfig));
            }
            return ok(node);
        });
    }


    /**
     * @api {GET} /v1/p/bargain_succeed_launchers/:bargainId/?page= 06砍价成功列表
     * @apiName listBargainSucceedLaunchers
     * @apiGroup ASSIST
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){int} pages 页码
     * @apiSuccess (Success 200){JsonArray} list 列表
     */
    public CompletionStage<Result> listBargainSucceedLaunchers(Http.Request request, long bargainId, int page) {
        String jsonCacheKey = BARGAIN_LIST_LAUNCHERS_JSON_CACHE + bargainId + ":" + page;
        return asyncCacheApi.getOptional(jsonCacheKey).thenApplyAsync((jsonCache) -> {
            if (jsonCache.isPresent()) {
                String result = (String) jsonCache.get();
                if (!ValidationUtil.isEmpty(result)) return ok(Json.parse(result));
            }
            PagedList<Bargain> pagedList = Bargain.find.query().where()
                    .eq("bargainId", bargainId)
                    .eq("status", Bargain.STATUS_SUCCEED)
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                    .findPagedList();
            List<Bargain> list = pagedList.getList();
            ObjectNode result = Json.newObject();
            result.put("hasNext", pagedList.hasNext());
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            cache.set(jsonCacheKey, Json.stringify(result), 2);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/latest_activity_config/ 07获取最新活动详情
     * @apiName getLatestActivityConfig
     * @apiGroup ACTIVITY
     * @apiSuccess (Success 200){int} id id
     * @apiSuccess (Success 200){String} images 图片
     * @apiSuccess (Success 200){String} title 标题
     * @apiSuccess (Success 200){String} note 活动说明
     * @apiSuccess (Success 200){long}  beginTime 开始时间
     * @apiSuccess (Success 200){long} openTime 活动时间
     * @apiSuccess (Success 200){long} attenders 参与人数
     * @apiSuccess (Success 200){long} createdTime 提交时间
     */
    public CompletionStage<Result> getLatestActivityConfig(Http.Request request) {
        String jsonCacheKey = LATEST_ACTIVITY_LIST_JSON_CACHE;
        return asyncCacheApi.getOptional(jsonCacheKey).thenApplyAsync((jsonCache) -> {
            if (jsonCache.isPresent()) {
                String result = (String) jsonCache.get();
                if (!ValidationUtil.isEmpty(result)) return ok(Json.parse(result));
            }
            ActivityConfig activityConfig = ActivityConfig.find.query().where()
                    .ge("status", ActivityConfig.STATUS_NOT_START)
                    .le("status", ActivityConfig.STATUS_PROCESSING)
                    .orderBy().desc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null != activityConfig) {
                ObjectNode result = (ObjectNode) Json.toJson(activityConfig);
                String realName = "";
                String phoneNumber = "";
                String place = "";
                Member member = businessUtils.getUserIdByAuthToken(request);
                if (null != member) {
                    ActivityLog firstLog = ActivityLog.find.query().where()
                            .eq("configId", activityConfig.id)
                            .eq("uid", member.id)
                            .orderBy().asc("id")
                            .setMaxRows(1)
                            .findOne();
                    if (null != firstLog) {
                        realName = firstLog.userName;
                        phoneNumber = firstLog.phoneNumber;
                        place = firstLog.place;
                    }
                }
                result.put(CODE, CODE200);
                result.put("realName", realName);
                result.put("phoneNumber", phoneNumber);
                result.put("place", place);
                asyncCacheApi.set(jsonCacheKey, Json.stringify(result), 60);
                return ok(result);
            } else return okJSON200();
        });
    }


    /**
     * @api {GET} /v1/p/top_attends/ 08联单排行
     * @apiName getTopAttends
     * @apiGroup ACTIVITY
     * @apiSuccess (Success 200){Array}  userStatList 买家排行榜
     * @apiSuccess (Success 200){Array}  shopStatList 商家排行榜
     * @apiSuccess (Success 200){String} images 图片
     */
    public CompletionStage<Result> getTopAttends() {
        String jsonCacheKey = LATEST_ACTIVITY_ATTENDS_LIST_JSON_CACHE;
        return asyncCacheApi.getOptional(jsonCacheKey).thenApplyAsync((jsonCache) -> {
            if (jsonCache.isPresent()) {
                String result = (String) jsonCache.get();
                if (!ValidationUtil.isEmpty(result)) return ok(Json.parse(result));
            }
            List<ActivityUserTotalLog> userStatList = new ArrayList<>();
            List<ActivityShopTotalLog> shopStatList = new ArrayList<>();
            List<ActivityShopTotalLog> leadStatList = new ArrayList<>();
            ActivityConfig activityConfig = ActivityConfig.find.query().where()
                    .eq("status", ActivityConfig.STATUS_PROCESSING)
                    .orderBy().desc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null != activityConfig) {
                userStatList = ActivityUserTotalLog.find.query().where()
                        .eq("configId", activityConfig.id)
                        .orderBy().desc("amount")
                        .orderBy().asc("id")
                        .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                        .findList();
                shopStatList = ActivityShopTotalLog.find.query().where()
                        .eq("configId", activityConfig.id)
                        .orderBy().desc("amount")
                        .orderBy().asc("id")
                        .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                        .findList();
                leadStatList = ActivityShopTotalLog.find.query().where()
                        .eq("configId", activityConfig.id)
                        .orderBy().desc("leadAmount")
                        .orderBy().asc("id")
                        .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                        .findList();
            }
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("userStatList", Json.toJson(userStatList));
            result.set("shopStatList", Json.toJson(shopStatList));
            result.set("leadStatList", Json.toJson(leadStatList));
            asyncCacheApi.set(jsonCacheKey, Json.stringify(result), 60);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/user_attends/:uid/?page= 09用户参与记录
     * @apiName listUserAttends
     * @apiGroup ACTIVITY
     */
    public CompletionStage<Result> listUserAttends(long uid, int page) {
        return CompletableFuture.supplyAsync(() -> {
            ActivityConfig activityConfig = ActivityConfig.find.query().where()
                    .eq("status", ActivityConfig.STATUS_PROCESSING)
                    .orderBy().desc("id")
                    .setMaxRows(1)
                    .findOne();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            List<ActivityLog> list = new ArrayList<>();
            if (null != activityConfig) {
                PagedList<ActivityLog> pagedList = ActivityLog.find.query().where()
                        .eq("configId", activityConfig.id)
                        .eq("uid", uid)
                        .orderBy().asc("id")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                        .findPagedList();
                list = pagedList.getList();
                int pages = pagedList.getTotalPageCount();
                boolean hasNext = pagedList.hasNext();
                result.put("pages", pages);
                result.put("hasNext", hasNext);
            }
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/shop_attends/:shopId/?page= 09店铺参与记录
     * @apiName listShopAttends
     * @apiGroup ACTIVITY
     */
    public CompletionStage<Result> listShopAttends(long shopId, int page) {
        return CompletableFuture.supplyAsync(() -> {
            ActivityConfig activityConfig = ActivityConfig.find.query().where()
                    .eq("status", ActivityConfig.STATUS_PROCESSING)
                    .orderBy().desc("id")
                    .setMaxRows(1)
                    .findOne();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            List<ActivityLog> list = new ArrayList<>();
            if (null != activityConfig) {
                PagedList<ActivityLog> pagedList = ActivityLog.find.query().where()
                        .eq("configId", activityConfig.id)
                        .eq("shopId", shopId)
                        .orderBy().asc("id")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                        .findPagedList();
                list = pagedList.getList();
                int pages = pagedList.getTotalPageCount();
                boolean hasNext = pagedList.hasNext();
                result.put("pages", pages);
                result.put("hasNext", hasNext);
                if (page == 1) {
                    ActivityShopTotalLog shopStat = ActivityShopTotalLog.find.query().where()
                            .eq("shopId", shopId)
                            .setMaxRows(1)
                            .findOne();
                    if (null != shopStat) {
                        result.set("shopStat", Json.toJson(shopStat));
                    }
                }
            }
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }


    /**
     * @api {GET} /v1/p/my_attends/?page= 09我的参与记录
     * @apiName listMyAttends
     * @apiGroup ACTIVITY
     */
    public CompletionStage<Result> listMyAttends(Http.Request request, int page) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((memberInCache) -> {
            if (null == memberInCache) return unauth403();
            PagedList<ActivityLog> pagedList = ActivityLog.find.query().where()
                    .eq("uid", memberInCache.id)
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                    .findPagedList();
            List<ActivityLog> list = pagedList.getList();
            int pages = pagedList.getTotalPageCount();
            boolean hasNext = pagedList.hasNext();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("pages", pages);
            result.put("hasNext", hasNext);
            if (page == 1) {
                ActivityUserTotalLog userTotalLog = ActivityUserTotalLog.find.query().where()
                        .eq("uid", memberInCache.id)
                        .setMaxRows(1)
                        .findOne();
                if (null != userTotalLog) {
                    result.set("userStat", Json.toJson(userTotalLog));
                }
            }
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }


    /**
     * @api {GET} /v1/p/top_trends_by_type/?trendsType=&page= 08根据类型查询联单列表
     * @apiName listTopAttendsByType
     * @apiGroup ACTIVITY
     * @apiSuccess (Success 200){Array}  userStatList 买家排行榜
     * @apiSuccess (Success 200){Array}  shopStatList 商家排行榜
     * @apiSuccess (Success 200){String} images 图片
     */
    public CompletionStage<Result> listTopAttendsByType(int trendsType, int page) {
        return CompletableFuture.supplyAsync(() -> {
            ActivityConfig activityConfig = ActivityConfig.find.query().where()
                    .eq("status", ActivityConfig.STATUS_PROCESSING)
                    .orderBy().desc("id")
                    .setMaxRows(1)
                    .findOne();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            int pages = 0;
            boolean hasNext = false;
            if (null != activityConfig) {
                switch (trendsType) {
                    case 1: {
                        PagedList<ActivityUserTotalLog> pagedList = ActivityUserTotalLog.find.query().where()
                                .eq("configId", activityConfig.id)
                                .orderBy().desc("amount")
                                .orderBy().asc("id")
                                .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                                .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                                .findPagedList();
                        List<ActivityUserTotalLog> userStatList = pagedList.getList();
                        pages = pagedList.getTotalPageCount();
                        hasNext = pagedList.hasNext();
                        result.set("userStatList", Json.toJson(userStatList));
                        break;
                    }
                    case 2: {
                        PagedList<ActivityShopTotalLog> pagedList = ActivityShopTotalLog.find.query().where()
                                .eq("configId", activityConfig.id)
                                .orderBy().desc("amount")
                                .orderBy().asc("id")
                                .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                                .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                                .findPagedList();
                        List<ActivityShopTotalLog> shopStatList = pagedList.getList();
                        pages = pagedList.getTotalPageCount();
                        hasNext = pagedList.hasNext();
                        result.set("shopStatList", Json.toJson(shopStatList));
                        break;
                    }
                    case 3: {
                        PagedList<ActivityShopTotalLog> pagedList = ActivityShopTotalLog.find.query().where()
                                .eq("configId", activityConfig.id)
                                .orderBy().desc("leadAmount")
                                .orderBy().asc("id")
                                .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                                .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                                .findPagedList();
                        List<ActivityShopTotalLog> leadStatList = pagedList.getList();
                        pages = pagedList.getTotalPageCount();
                        hasNext = pagedList.hasNext();
                        result.set("leadStatList", Json.toJson(leadStatList));
                        break;
                    }
                }
            }
            result.put("pages", pages);
            result.put("hasNext", hasNext);
            return ok(result);
        });
    }
}
