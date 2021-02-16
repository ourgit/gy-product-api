package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.ExpressionList;
import models.system.SystemCarousel;
import play.cache.AsyncCacheApi;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * 轮播控制类
 */
public class CarouselController extends BaseController {
    @Inject
    protected AsyncCacheApi asyncCacheApi;

    /**
     * @api {GET} /v1/n/carousels/?regionCode= 01查看轮播列表
     * @apiName listCarousel
     * @apiGroup Carousel
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess {JsonArray} list 列表
     * @apiSuccess {int} id 轮播id
     * @apiSuccess {string} name 轮播名称
     * @apiSuccess {string} imgUrl 图片链接地址
     * @apiSuccess {string} linkUrl 链接地址
     * @apiSuccess {string} description 备注
     * @apiSuccess {string} regionCode 区域编号
     * @apiSuccess {string} regionName 区域名字
     * @apiSuccess {int} sort 显示顺序
     * @apiSuccess (Error 500) {int} code 500 未知错误
     */
    public CompletionStage<Result> listCarousel(Http.Request request, int bizType, int clientType) {
        String key = cacheUtils.getCarouselJsonCache();
        return asyncCacheApi.getOptional(key).thenApplyAsync((json) -> {
            if (json.isPresent()) {
                String jsonCache = (String) json.get();
                if (!ValidationUtil.isEmpty(jsonCache)) {
                    return ok(jsonCache);
                }
            }
            ExpressionList<SystemCarousel> expressionList = SystemCarousel.find.query().where()
                    .eq("needShow", true);
            List<SystemCarousel> list = expressionList.orderBy().desc("displayOrder")
                    .findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            asyncCacheApi.set(key, Json.stringify(result), 24 * 3600);
            return ok(result);
        });
    }
}
