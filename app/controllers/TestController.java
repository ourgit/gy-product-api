package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Ebean;
import io.ebean.PagedList;
import models.product.*;
import models.system.SystemCarousel;
import play.Logger;
import play.cache.AsyncCacheApi;
import play.cache.SyncCacheApi;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.BizUtils;
import utils.CacheUtils;
import utils.EncodeUtils;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

import static constants.BusinessConstant.PAGE_SIZE_10;
import static constants.RedisKeyConstant.WEATHER_JSON_CACHE;
import static security.ApiSecured.API_NONE_KEY;
import static utils.BizUtils.HOT_VIEW_LIST;

/**
 * BaseController 所有的业务逻辑必须都继续自该controller以便方便管理
 *
 * @link BaseSecurityController
 */
public class TestController extends Controller {
    Logger.ALogger logger = Logger.of(TestController.class);
    public static final String CODE = "code";
    public static final int CODE200 = 200;
    public static final int CODE403 = 403;
    public static final int CODE404 = 404;
    public static final int CODE500 = 500;
    public static final int CODE40001 = 40001;
    public static final int CODE40002 = 40002;
    public static final int CODE40003 = 40003;
    public static final int CODE40004 = 40004;
    public static final int CODE40005 = 40005;
    public static final int CODE40006 = 40006;
    public static final int CODE40007 = 40007;
    public static final int CODE40008 = 40008;
    public static final int CODE40009 = 40009;
    public static final int CODE40010 = 40010;

    @Inject
    WSClient wsClient;

    @Inject
    BizUtils bizUtils;
    @Inject
    protected SyncCacheApi cache;
    @Inject
    protected AsyncCacheApi asyncCacheApi;
    @Inject
    EncodeUtils encodeUtils;
    @Inject
    CacheUtils cacheUtils;

    public Result unauth403() {
        ObjectNode node = Json.newObject();
        node.put("code", 403);
        node.put("reason", "未授权");
        return ok(node);
    }

    public Result okJSON200() {
        ObjectNode node = Json.newObject();
        node.put("code", 200);
        return ok(node);
    }

    public Result okCustomJson(int code, String reason) {
        ObjectNode node = Json.newObject();
        node.put("code", code);
        node.put("reason", reason);
        return ok(node);
    }

    public Result index() {
        ObjectNode node = Json.newObject();
        node.put("code", 200);
        node.put("result", "success");
        return ok(node);
    }


    /**
     * @api {POST}  /v1/p/other/ 36统计
     * @apiName updateXS
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> updateXS(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode requestNode = request.body().asJson();
            if (null == request) return okCustomJson(CODE40001, "参数错误");
            String sign = requestNode.findPath("sign").asText();
            if (ValidationUtil.isEmpty(sign)) return okCustomJson(CODE40001, "no auth");
            if (!sign.equalsIgnoreCase("SUDOfjslj@#4jlskdjfpoi23upS*d-09f8-sdfjsl;kdfj"))
                return okCustomJson(CODE40001, "auth erro");
            if (!requestNode.has("list")) return okCustomJson(CODE40001, "no list");
            ArrayNode list = (ArrayNode) requestNode.findPath("list");
            List<ProductXS> updateList = new ArrayList<>();
            List<ProductXSPriceLog> updateLogList = new ArrayList<>();
            list.forEach((each) -> {
                ProductXS param = Json.fromJson(each, ProductXS.class);
                if (null != param && !ValidationUtil.isEmpty(param.sku)) {
                    ProductXS exist = ProductXS.find.query().where()
                            .eq("sku", param.sku)
                            .orderBy().asc("id")
                            .setMaxRows(1)
                            .findOne();
                    long currentTime = System.currentTimeMillis() / 1000;
                    if (null == exist) {
                        try {
                            param.setCreateTime(currentTime);
                            param.setFilter(Json.stringify(Json.toJson(param)));
                            param.save();
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        }
                        ProductXSPriceLog log = addPriceLog(param, currentTime);
                        updateLogList.add(log);
                    } else {
                        if (!ValidationUtil.isEmpty(param.tmBuyStart)) exist.setTmBuyStart(param.tmBuyStart);
                        if (!ValidationUtil.isEmpty(param.tmBuyEnd)) exist.setTmBuyEnd(param.tmBuyEnd);
                        if (!ValidationUtil.isEmpty(param.tmShowStart)) exist.setTmShowStart(param.tmShowStart);
                        if (!ValidationUtil.isEmpty(param.tmShowEnd)) exist.setTmShowEnd(param.tmShowEnd);
                        exist.setLimitQty(param.limitQty);
                        exist.setUlimitQty(param.ulimitQty);
                        if (param.marketAmt > 0) exist.setMarketAmt(param.marketAmt);
                        if (param.saleAmt > 0) exist.setSaleAmt(param.saleAmt);
                        exist.setConsumers(param.consumers);
                        exist.setDaySaleQty(param.daySaleQty);
                        exist.setFolQty(param.folQty);
                        exist.setLikeNum(param.likeNum);
                        exist.setSaleQty(param.saleQty);
                        exist.setViewNum(param.viewNum);
                        exist.setWantBuyQty(param.wantBuyQty);
                        exist.setFilter("");
                        exist.setFilter(Json.stringify(Json.toJson(exist)));
                        updateList.add(exist);
                        ProductXSPriceLog log = addPriceLog(exist, currentTime);
                        updateLogList.add(log);
                    }
                }
            });
            try {
                if (updateList.size() > 0) Ebean.saveAll(updateList);
                if (updateLogList.size() > 0) Ebean.saveAll(updateLogList);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }

            return okJSON200();
        });
    }

    private ProductXSPriceLog addPriceLog(ProductXS productXS, long currentTime) {
        ProductXSPriceLog log = new ProductXSPriceLog();
        log.setProductId(productXS.id);
        log.setPrId(productXS.prId);
        log.setSku(productXS.sku);
        log.setTmBuyStart(productXS.tmBuyStart);
        log.setTmBuyEnd(productXS.tmBuyEnd);
        log.setTmShowStart(productXS.getTmShowStart());
        log.setTmShowEnd(productXS.getTmShowEnd());
        log.setLimitQty(productXS.getLimitQty());
        log.setUlimitQty(productXS.getUlimitQty());
        log.setMarketAmt(productXS.getMarketAmt());
        log.setSaleAmt(productXS.getSaleAmt());
        log.setConsumers(productXS.consumers);
        log.setDaySaleQty(productXS.daySaleQty);
        log.setFolQty(productXS.folQty);
        log.setLikeNum(productXS.likeNum);
        log.setSaleQty(productXS.saleQty);
        log.setViewNum(productXS.viewNum);
        log.setWantBuyQty(productXS.wantBuyQty);
        log.setCreateTime(currentTime);
        return log;
    }


    public CompletionStage<Result> preFetch(Http.Request request) {
        Executor executor = Executors.newCachedThreadPool();
        String key = cacheUtils.getPrefetchJsonCache();
        return asyncCacheApi.getOptional(key).thenApplyAsync((jsonCache) -> {
            if (jsonCache.isPresent()) {
                String result = (String) jsonCache.get();
                if (!ValidationUtil.isEmpty(result)) {
                    return ok(result);
                }
            }
            ObjectNode resultNode = Json.newObject();
            resultNode.put(CODE, CODE200);
            long currentTime = System.currentTimeMillis() / 1000;
            List<SystemCarousel> carouselList = SystemCarousel.find.query().where()
                    .eq("clientType", 2)
                    .eq("needShow", true)
                    .orderBy().desc("displayOrder")
                    .findList();
            resultNode.set("carouselList", Json.toJson(carouselList));
            List<ProductClassify> classifyList = ProductClassify.find.query()
                    .where().eq("status", ProductClassify.STATUS_SHOW)
                    .orderBy().desc("sort")
                    .findList();
            resultNode.set("classifyList", Json.toJson(classifyList));
            ObjectNode weatherNode = Json.newObject();
            Optional<String> weatherOption = cache.getOptional(WEATHER_JSON_CACHE);
            if (weatherOption.isPresent()) {
                String weather = weatherOption.get();
                if (!ValidationUtil.isEmpty(weather)) {
                    weatherNode = (ObjectNode) Json.parse(weather);
                }
            }
            weatherNode.put("currentTime", currentTime);
            resultNode.set("weather", weatherNode);

            List<ProductTab> productTabList = ProductTab.find.query().where()
                    .orderBy().desc("sort")
                    .orderBy().desc("id")
                    .findList();
            resultNode.set("productTabList", Json.toJson(productTabList));
            if (productTabList.size() > 0) {
                LocalDateTime now = LocalDateTime.now();
                long tabId = productTabList.get(0).id;
                ProductTab productTab = ProductTab.find.byId(tabId);
                if (null != productTab) {
                    PagedList<ProductTabProducts> pagedList = ProductTabProducts.find.query().where()
                            .eq("productTabId", tabId)
                            .orderBy().desc("sort")
                            .orderBy().desc("id")
                            .setFirstRow(0)
                            .setMaxRows(PAGE_SIZE_10)
                            .findPagedList();
                    List<ProductTabProducts> list = pagedList.getList();
                    list.forEach((each) -> {
                        Product product = bizUtils.getProduct(each.productId);
                        if (null != product) {
                            bizUtils.autoSetProduct(currentTime, now.getHour(), product, HOT_VIEW_LIST);
                            product.soldAmount = product.soldAmount + product.virtualAmount;
                            productTab.productList.add(product);
                        }
                    });
                    ObjectNode firstProductTabDetail = (ObjectNode) Json.toJson(productTab);
                    firstProductTabDetail.put("hasNext", pagedList.hasNext());
                    resultNode.set("firstProductTabDetail", Json.toJson(firstProductTabDetail));
                }
            }
            asyncCacheApi.set(key, Json.stringify(resultNode), 3600);
            return ok(resultNode);
        }, new TestController.BlockingExecutor(100000, executor));
    }

    private static class BlockingExecutor implements Executor {

        final Semaphore semaphore;
        final Executor delegate;

        private BlockingExecutor(final int concurrentTasksLimit, final Executor delegate) {
            semaphore = new Semaphore(concurrentTasksLimit);
            this.delegate = delegate;
        }

        @Override
        public void execute(final Runnable command) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            final Runnable wrapped = () -> {
                try {
                    command.run();
                } finally {
                    semaphore.release();
                }
            };

            delegate.execute(wrapped);

        }
    }

    public boolean checkAuth(Http.Request request) {
        Map<String, String[]> map = request.queryString();
        String appId = "";
        String token = "";
        String nonce = "";
        String[] appIdArray = map.get("appid");
        if (null == appIdArray) {
            logger.error("auth appIdArray empty");
            return false;
        }
        if (appIdArray.length > 0) appId = appIdArray[0];
        else {
            logger.error("auth appIdArray length = 0");
            return false;
        }

        String[] tokenArray = map.get("token");
        if (null == tokenArray) {
            logger.error("auth tokenArray empty");
            return false;
        }
        if (tokenArray.length > 0) {
            String tempToken = tokenArray[0];
            String[] temp = tempToken.split("-");
            if (temp.length < 2) {
                logger.error("auth array < 2");
                return false;
            }
            token = temp[0];
            nonce = temp[1];
        } else {
            logger.error("auth tokenArray length = 0");
            return false;
        }
        if (ValidationUtil.isEmpty(token)) {
            logger.error("auth token empty");
            return false;
        }
        String key = API_NONE_KEY + token;
        Optional<String> nonceKeyOptional = cache.getOptional(key);
        if (nonceKeyOptional.isPresent()) {
            logger.error("auth nonce key exist:" + key);
            return false;
        }

        String salt = appId + EncodeUtils.API_SALT + nonce;
        String md5FirstTime = encodeUtils.getMd5(salt);
        String md5SecondTime = encodeUtils.getMd5(appId + md5FirstTime);
        if (md5SecondTime.equalsIgnoreCase(token)) {
            cache.set(key, key, 24 * 3600);
            return true;
        }
        logger.error("auth token not equal:" + md5SecondTime + ":::" + token);
        return false;
    }
}
