package utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.enroll.EnrollConfig;
import models.product.*;
import models.system.ParamConfig;
import models.user.Member;
import play.Logger;
import play.api.Configuration;
import play.cache.SyncCacheApi;
import play.cache.redis.AsyncCacheApi;
import play.libs.Json;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

import static constants.BusinessConstant.*;
import static constants.BusinessConstant.PARAM_KEY_GROUPON_SUPER_REQUIRE_ORDERS;
import static constants.RedisKeyConstant.*;
import static models.order.Order.*;
import static models.order.Order.ORDER_ACTIVITY_TYPE_GROUPON_SUPER;


@Singleton
public class BizUtils {
    public static final int HOT_VIEW_LIST = 1;
    public static final int HOT_VIEW_DETAIL = 2;
    public static final int HOT_VIEW_SHOPPING_CART = 5;
    public static final int HOT_VIEW_ORDER = 100;
    public static final int SALE_END_HOUR = 22;
    Logger.ALogger logger = Logger.of(BizUtils.class);

    public static final int DEFAULT_MAIL_FEE = 10;//邮费默认10元

    public static final int TOKEN_EXPIRE_TIME = 2592000;

    @Inject
    CacheUtils cacheUtils;

    @Inject
    SyncCacheApi cache;

    @Inject
    AsyncCacheApi asyncCacheApi;

    @Inject
    HttpRequestDeviceUtils httpRequestDeviceUtils;
    @Inject
    Configuration config;
    @Inject
    DateUtils dateUtils;


    public Member getUserIdByAuthToken(Http.Request request) {
        String uidToken = getUIDFromRequest(request);
        if (ValidationUtil.isEmpty(uidToken)) return null;
        Optional<String> tokenOptional = cache.getOptional(uidToken);
        if (!tokenOptional.isPresent()) return null;

        String authToken = tokenOptional.get();//uid token对应的是用户uid
        if (ValidationUtil.isEmpty(authToken)) return null;
        Optional<String> platformKeyOptional = cache.getOptional(authToken);
        if (!platformKeyOptional.isPresent()) return null;
        String platformKey = platformKeyOptional.get();
        if (ValidationUtil.isEmpty(platformKey)) return null;
        Optional<Member> optional = cache.getOptional(platformKey);
        if (!optional.isPresent()) return null;
        Member member = optional.get();
        return member;
    }

    public CompletionStage<Member> getUserIdByAuthToken2(Http.Request request) {
        String uidToken = getUIDFromRequest(request);
        if (ValidationUtil.isEmpty(uidToken)) return CompletableFuture.supplyAsync(() -> null);
        return asyncCacheApi.getOptional(uidToken).thenApplyAsync((tokenOptional) -> {
            if (tokenOptional.isPresent()) {
                String authtoken = (String) tokenOptional.get();
                if (ValidationUtil.isEmpty(authtoken)) return Optional.empty();
                return cache.getOptional(authtoken);
            } else return Optional.empty();
        }).thenApplyAsync((platformKeyOptional) -> {
            if (platformKeyOptional.isPresent()) {
                String platformKey = (String) platformKeyOptional.get();
                if (ValidationUtil.isEmpty(platformKey)) return Optional.empty();
                return cache.getOptional(platformKey);
            }
            return Optional.empty();
        }).thenApplyAsync((memberOption) -> {
            if (memberOption.isPresent()) {
                Member member = (Member) memberOption.get();
                return member;
            }
            return null;
        });
    }

    public String getUIDFromRequest(Http.Request request) {
        Optional<String> authTokenHeaderValues = request.getHeaders().get(KEY_AUTH_TOKEN_UID);
        if (authTokenHeaderValues.isPresent()) {
            String authToken = authTokenHeaderValues.get();
            return authToken;
        }
        return "";
    }

    public long getCurrentTimeBySecond() {
        return System.currentTimeMillis() / 1000;
    }

    public int getTokenExpireTime() {
        int tokenExpireTime = (Integer) config.getInt("token_expire_time").get();
        if (tokenExpireTime < 1) tokenExpireTime = TOKEN_EXPIRE_TIME;
        return tokenExpireTime;
    }

    public boolean upToIPLimit(Http.Request request, String key, int max) {
        String ip = getRequestIP(request);
        if (!ValidationUtil.isEmpty(ip)) {
            String accessCount = cache.getOrElseUpdate(key + ip, () -> "");
            if (ValidationUtil.isEmpty(accessCount)) {
                cache.set(key + ip, "1", BusinessConstant.KEY_EXPIRE_TIME_2M);
            } else {
                int accessCountInt = Integer.parseInt(accessCount) + 1;
                if (accessCountInt > max) return true;
                cache.set(key + ip, String.valueOf(accessCountInt), BusinessConstant.KEY_EXPIRE_TIME_2M);
            }
        }
        return false;
    }

    public String getRequestIP(Http.Request request) {
        String ip = null;
        try {
            String remoteAddr = request.remoteAddress();
            String forwarded = request.getHeaders().get("X-Forwarded-For").get();
            String realIp = request.getHeaders().get(BusinessConstant.X_REAL_IP_HEADER).get();
            if (forwarded != null) {
                ip = forwarded.split(",")[0];
            }
            if (ValidationUtil.isEmpty(ip)) {
                ip = realIp;
            }
            if (ValidationUtil.isEmpty(ip)) {
                ip = remoteAddr;
            }
        } catch (Exception e) {
            play.Logger.error(e.getMessage());
        }
        return ip == null ? "" : ip;
    }

    public boolean checkVcode(String accountName, String vcode) {
        if (ValidationUtil.isPhoneNumber(accountName)) {
            String correctVcode = cache.getOrElseUpdate(cacheUtils.getSMSLastVerifyCodeKey(accountName), () -> "");
            if (!ValidationUtil.isVcodeCorrect(vcode) || !ValidationUtil.isVcodeCorrect(correctVcode) || !vcode.equals(correctVcode)) {
                return false;
            }
        } else return false;
        return true;
    }

    public void deleteVcodeCache(String accountName) {
        String key = cacheUtils.getSMSLastVerifyCodeKey(accountName);
        if (!ValidationUtil.isEmpty(key)) cache.remove(key);
    }


    public Optional<String> getWechatOpenId(Http.Session session) {
        return session.getOptional(BusinessConstant.KEY_SESSION_WECHAT_OPEN_ID);
    }

    public void setWechatOpenIdToCookie(Http.Session session, String openId, String authToken) {
        if (null == session || ValidationUtil.isEmpty(openId)) return;
        session.adding(KEY_SESSION_WECHAT_OPEN_ID, openId);
        session.adding(KEY_AUTH_TOKEN_COOKIE, authToken);
    }

    public void handleCacheToken(Member member, String authToken, int deviceType) {
        //缓存新的token数据
        String tokenKey = cacheUtils.getMemberTokenKey(deviceType, member.id);
        String key = cacheUtils.getMemberKey(deviceType, tokenKey);
        cache.set(authToken, tokenKey, getTokenExpireTime());
        cache.set(tokenKey, authToken, getTokenExpireTime());
        cache.set(key, member, getTokenExpireTime());
    }


    public String generateAuthToken(Http.Request request) {
        String authToken = UUID.randomUUID().toString();
        int deviceType = httpRequestDeviceUtils.getMobileDeviceType(request);
        authToken = deviceType + ":" + authToken;
        return authToken;
    }

    public String generateAuthToken(int deviceType) {
        String authToken = UUID.randomUUID().toString();
        authToken = deviceType + ":" + authToken;
        return authToken;
    }

    /**
     * 转义html脚本
     *
     * @param value
     * @return
     */
    public String escapeHtml(String value) {
        if (ValidationUtil.isEmpty(value)) return "";
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "（").replaceAll("\\)", "）");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        value = value.replaceAll("select", "");
        value = value.replaceAll("insert", "");
        value = value.replaceAll("update", "");
        value = value.replaceAll("delete", "");
        value = value.replaceAll("%", "\\%");
        value = value.replaceAll("union", "");
        value = value.replaceAll("load_file", "");
        value = value.replaceAll("outfile", "");
        return value;
    }


    public void updateMixOptionCache() {
        String keySet = cacheUtils.getMixOptionCacheSet();
        List<MixOption> op = MixOption.find.query().orderBy().asc("id").findList();
        Map<String, Set<Integer>> map = new HashMap<>();
        op.stream().forEach((option) -> {
                    Set<Integer> set = map.get(option.mixCode);
                    if (null == set) set = new HashSet<>();
                    set.add(option.amount);
                    map.put(option.mixCode, set);
                }
        );
        cache.remove(keySet);
        cache.set(keySet, map);
    }

    public double calcMailFee(double totalMoney, String provinceCode, double totalWeight) {
        String globalMailFeeKey = cacheUtils.getParamConfigCacheKey() + MAIL_FEE_KEY_FREE_MAIL_FEE_UP_TO;
        Optional<ParamConfig> optional = cache.getOptional(globalMailFeeKey);
        if (optional.isPresent()) {
            ParamConfig freeMailFeeWhenUpto = optional.get();
            if (null != freeMailFeeWhenUpto && !ValidationUtil.isEmpty(freeMailFeeWhenUpto.value)) {
                int upTo = Integer.parseInt(freeMailFeeWhenUpto.value);
                if (totalMoney >= upTo) return 0;
            }
        }
        if (!ValidationUtil.isEmpty(provinceCode) && provinceCode.length() == 6) {
            provinceCode = provinceCode.substring(0, 2) + "0000";
            String key = cacheUtils.getMailFeeKey(provinceCode);
            Optional<MailFee> mailFeeOptional = cache.getOptional(key);
            if (mailFeeOptional.isPresent()) {
                MailFee mailFee = mailFeeOptional.get();
                if (null != mailFee) {
                    return calcMailFeeByTotalWeight(mailFee, totalWeight);
                }
            } else {
                MailFee mailFee = MailFee.find.query().where().icontains("regionCode", provinceCode + ",")
                        .setMaxRows(1).findOne();
                if (null != mailFee) return calcMailFeeByTotalWeight(mailFee, totalWeight);
                else {
                    mailFee = MailFee.find.query().where().eq("regionCode", "0")
                            .setMaxRows(1).findOne();
                    if (null != mailFee) return calcMailFeeByTotalWeight(mailFee, totalWeight);
                }
            }
        }

        return DEFAULT_MAIL_FEE;
    }

    private double calcMailFeeByTotalWeight(MailFee mailFee, double totalWeight) {
        if (totalWeight <= 1) return mailFee.firstWeightFee;
        return mailFee.firstWeightFee + (totalWeight - 1) * mailFee.nextWeightFee;
    }

    /**
     * 替换成*号，中间的5个字符
     *
     * @param userName
     * @return
     */
    public String hidepartialChar(String userName) {
        if (ValidationUtil.isEmpty(userName)) return "";
        if (ValidationUtil.isValidEmailAddress(userName)) {
            int index = userName.indexOf("@");
            if (index < 2) return userName;
            if (index > 5) {
                return userName.substring(0, 3) + "*****" + userName.substring(8, userName.length());
            } else {
                String toReplaced = userName.substring(1, index);
                String result = toReplaced.replaceAll(".", "*");
                return userName.substring(0, 1) + result + userName.substring(index, userName.length());
            }
        }
        if (ValidationUtil.isPhoneNumber(userName)) {
            String toReplaced = userName.substring(3, 8);
            String result = toReplaced.replaceAll(".", "*");
            return userName.substring(0, 3) + result + userName.substring(8, userName.length());
        }
        String toReplaced = userName.substring(1, userName.length());
        String result = toReplaced.replaceAll(".", "*");
        return userName.substring(0, 1) + result;
    }

    public Product getProduct(long productId) {
        Product product = null;
        String key = cacheUtils.getMerchantCacheKey(productId);
        try {
            Optional<Product> optional = cache.getOptional(key);
            if (optional.isPresent()) {
                product = optional.get();
            }
        } catch (Exception e) {
            logger.error("getProduct:" + e.getMessage());
        }

        if (null == product) {
            cache.remove(key);
            product = Product.find.byId(productId);
            if (null != product) {
                ProductFavorProducts favorProducts = ProductFavorProducts.find.query().where()
                        .eq("productId", product.id)
                        .setMaxRows(1).findOne();
                product.favorProducts = favorProducts;
                cache.set(key, product, 60);
            }
        }
        return product;
    }

    public ProductFavor getProductFavor(long favorId) {
        ProductFavor productFavor = null;
        String key = cacheUtils.getFavorCache(favorId);
        Optional<ProductFavor> optional = cache.getOptional(key);
        if (optional.isPresent()) {
            productFavor = optional.get();
        }
        if (null == productFavor) {
            productFavor = ProductFavor.find.byId(favorId);
            if (null != productFavor) {
                cache.set(key, productFavor, 60);
            }
        }
        return productFavor;
    }

    public Brand getBrand(long brandId) {
        Brand brand = null;
        String key = cacheUtils.getBrandKey(brandId);
        Optional<Brand> optional = cache.getOptional(key);
        if (optional.isPresent()) {
            brand = optional.get();
        }
        if (null == brand) {
            brand = Brand.find.byId(brandId);
            if (null != brand) {
                cache.set(key, brand, 30 * 24 * 3600);
            }
        }
        return brand;
    }

    public boolean setLock(String id, String operationType) {
        String key = operationType + ":" + id;
        try {
            return asyncCacheApi.setIfNotExists(key, 1, 5).toCompletableFuture().get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("getLock:" + e.getMessage());
            asyncCacheApi.remove(key);
        }
        return true;
    }

    /**
     * 解锁
     *
     * @param uid
     * @param operationType
     */
    public void unLock(String uid, String operationType) {
        asyncCacheApi.remove(operationType + ":" + uid);
    }


    public List<Product> getRecommendProductList(long productId) {
        List<Product> recommendList = null;
        String key = cacheUtils.getRecommendProductList(productId);
        Optional<List<Product>> optional = cache.getOptional(key);
        if (optional.isPresent()) {
            recommendList = optional.get();
        }
        if (null == recommendList) {
            recommendList = new ArrayList<>();
            List<Product> finalRecommendList = new ArrayList<>();
            List<ProductRecommend> recommendProductList = ProductRecommend.find.query().where()
                    .eq("productId", productId)
                    .orderBy().desc("id")
                    .findList();
            recommendProductList.forEach((each) -> {
                Product product = getProduct(each.recommendProductId);
                if (null != product) {
                    finalRecommendList.add(product);
                }
            });
            if (recommendProductList.size() < 1) {
                String defaultRecommendKey = cacheUtils.defaultRecommendProductList();
                Optional<List<Product>> defaultOption = cache.getOptional(defaultRecommendKey);
                List<Product> defaultRecommendList = null;
                if (defaultOption.isPresent()) {
                    defaultRecommendList = defaultOption.get();
                }
                if (null == defaultRecommendList) {
                    List<ProductDefaultRecommend> productDefaultRecommends = ProductDefaultRecommend.find.all();
                    productDefaultRecommends.forEach((each) -> {
                        Product product = getProduct(each.productId);
                        if (null != product) finalRecommendList.add(product);
                    });
                } else {
                    finalRecommendList.addAll(defaultRecommendList);
                }
            }
            recommendList.addAll(finalRecommendList);
        }
        List<Product> finalList = recommendList;
        finalList.parallelStream().forEach((product) -> {
            ProductSku defaultSku = ProductSku.find.query().where()
                    .eq("productId", product.id)
                    .orderBy().asc("sort")
                    .orderBy().asc("id")
                    .setMaxRows(1).findOne();
            product.defaultSku = defaultSku;
        });
        cache.set(key, finalList, 10 * 60);
        return finalList;
    }

    public List<Product> getRelateProductList(long productId) {
        List<Product> relateList = null;
        String key = cacheUtils.getRelateProductList(productId);
        Optional<List<Product>> optional = cache.getOptional(key);
        if (optional.isPresent()) {
            relateList = optional.get();
        }
        if (null == relateList) {
            relateList = new ArrayList<>();
            List<Product> tempList = new ArrayList<>();
            List<ProductRelate> list = ProductRelate.find.query().where()
                    .eq("comboProductId", productId)
                    .orderBy().desc("id")
                    .findList();
            list.forEach((each) -> {
                Product product = getProduct(each.relateProductId);
                if (null != product) {
                    tempList.add(product);
                }
            });
            relateList.addAll(tempList);
        }
        return relateList;
    }

    public long getFlashSalePrice(long productId) {
        long price = 0;
        long minTime = dateUtils.getTodayMinTimestamp();
        long maxTime = dateUtils.getTodayMidnightTimestamp();
        List<FlashSaleProduct> flashSaleProducts = FlashSaleProduct.find.query().where()
                .eq("productId", productId)
                .ge("status", FlashSale.STATUS_PROCESSING)
                .ge("beginTime", minTime)
                .le("endTime", maxTime)
                .orderBy().asc("status")
                .orderBy().desc("id")
                .setMaxRows(1)
                .findList();
        if (flashSaleProducts.size() > 0) {
            FlashSaleProduct flashSaleProduct = flashSaleProducts.get(0);
            if (null != flashSaleProduct) {
                price = flashSaleProduct.price;
            }
        }
        return price;
    }

    public int getGrouponRequireOrders(int grouponType) {
        String key = "";
        switch (grouponType) {
            case ORDER_ACTIVITY_TYPE_GROUPON: {
                key = PARAM_KEY_GROUPON_REQUIRE_ORDERS;
                break;
            }
            case ORDER_ACTIVITY_TYPE_GROUPON_LOTTERY: {
                key = PARAM_KEY_GROUPON_LOTTERY_REQUIRE_ORDERS;
                break;
            }
            case ORDER_ACTIVITY_TYPE_GROUPON_TRY: {
                key = PARAM_KEY_GROUPON_TRY_REQUIRE_ORDERS;
                break;
            }
            case ORDER_ACTIVITY_TYPE_GROUPON_SUPER: {
                key = PARAM_KEY_GROUPON_SUPER_REQUIRE_ORDERS;
                break;
            }
        }
        String value = getConfigValue(key);
        if (!ValidationUtil.isEmpty(value)) {
            return Integer.parseInt(value);
        }
        return GROUPON_REQUIRE_ORDERS;
    }

    public int getGrouponTimeLimit() {
        String value = getConfigValue(PARAM_KEY_GROUPON_TIME_LIMIT_IN_SERCONDS);
        if (!ValidationUtil.isEmpty(value)) {
            return Integer.parseInt(value);
        }
        return GROUPON_TIME_LIMIT_SECONDS;
    }

    public String getConfigValue(String key) {
        String value = "";
        try {
            Optional<Object> accountOptional = asyncCacheApi.getOptional(key).toCompletableFuture().get(10, TimeUnit.SECONDS);
            if (accountOptional.isPresent()) {
                value = (String) accountOptional.get();
                if (!ValidationUtil.isEmpty(value)) return value;
            }
        } catch (Exception e) {
            logger.error("getConfigValue:" + e.getMessage());
        }

        if (ValidationUtil.isEmpty(value)) {
            ParamConfig config = ParamConfig.find.query().where()
                    .eq("key", key)
                    .orderBy().asc("id")
                    .setMaxRows(1).findOne();
            if (null != config) {
                value = config.value;
                asyncCacheApi.set(key, value);
            }
        }
        return value;
    }

    public int getDirectDealerAwardPercentage() {
        int directAwardPercentage = 80;
        String value = getConfigValue(PARAM_KEY_AWARD_DIRECT_DEALER_PERCENTAGE);
        if (!ValidationUtil.isEmpty(value)) {
            directAwardPercentage = Integer.parseInt(value);
        }
        return directAwardPercentage;
    }


    public void autoSetProduct(long currentTime, int hour, Product product, int viewType) {
        boolean hasBegin = false;
        if (product.beginTime < currentTime) {
            if (hour >= product.beginHour) {
                hasBegin = true;
            } else hasBegin = true;
        }
        setFlashSaleProductDetail(product, viewType);
        increaseProductViews(product.id, viewType);
        if (hasBegin) {
            setBuyerAvatar(product);
        } else {
            product.wishAmount = getProductViews(product.id);
        }
        if (viewType != HOT_VIEW_DETAIL) deductProduct(product);
        setEnrollButtonName(product);
    }

    private void deductProduct(Product product) {
        if (null != product) {
            product.setCategoryId("");
            product.setSketch("");
            product.setKeywords("");
            product.setTag("");
            product.setMarque("");
            product.setBarcode("");
            product.setPoster("");
            product.setMixCode("");
            product.setSupplierUid(0);
        }
    }


    public void setSkuAward(ProductSku sku) {
        sku.award = getDirectDealerAwardPercentage() * sku.award / 100;
    }

    public void setFlashSaleProductDetail(Product each, int type) {
        if (null != each) {
            if (type != HOT_VIEW_DETAIL) each.setDetails("");
            each.soldAmount = each.soldAmount + each.virtualAmount;
            each.virtualAmount = 0;
            ProductSku sku = ProductSku.find.query().where()
                    .eq("productId", each.id)
                    .orderBy().asc("price")
                    .orderBy().asc("id")
                    .setMaxRows(1).findOne();
            if (null != sku) {
                sku.award = getMaxAward(each.id);
                each.defaultSku = sku;
            }
            if (type == HOT_VIEW_DETAIL) {
                ExpressionList<ProductParam> expressionList = ProductParam.find.query().where()
                        .eq("productId", each.id);
                List<ProductParam> list = expressionList
                        .order().asc("sort")
                        .order().asc("id")
                        .findList();
                each.paramList.addAll(list);
            }
        }
    }

    public long getMaxAward(long productId) {
        ProductSku sku = ProductSku.find.query().where()
                .eq("productId", productId)
                .orderBy().desc("award")
                .orderBy().asc("id")
                .setMaxRows(1).findOne();
        long award = 0;
        if (null != sku) {
            award = getDirectDealerAwardPercentage() * sku.award / 100;
        }
        return award;
    }

    public void increaseProductViews(long productId, int views) {
        String key = cacheUtils.getProductViewKey(productId);
        CompletableFuture.runAsync(() -> {
            ProductViews productViews = ProductViews.find.query().where().eq("productId", productId)
                    .orderBy().asc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null == productViews) {
                productViews = new ProductViews();
                productViews.setProductId(productId);
            }
            long updateViews = productViews.views + views;
            productViews.setViews(updateViews);
            productViews.save();
            asyncCacheApi.set(key, updateViews, 10 * 24 * 3600);
        });
    }

    public void updateViewsAvatars(long productId, Member member) {
        CompletableFuture.runAsync(() -> {
            long currentTime = dateUtils.getCurrentTimeBySecond();
            ProductSkuAvatar productSkuAvatar = ProductSkuAvatar.find.query().where()
                    .eq("uid", member.id)
                    .eq("productId", productId)
                    .orderBy().desc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null == productSkuAvatar) {
                productSkuAvatar = new ProductSkuAvatar();
                productSkuAvatar.setUid(member.id);
                productSkuAvatar.setUserName(getUserName(member));
                productSkuAvatar.setProductId(productId);
                productSkuAvatar.setAvatar(member.avatar);
                productSkuAvatar.setCreateTime(currentTime);
            }
            productSkuAvatar.setAmount(productSkuAvatar.amount + 1);
            productSkuAvatar.save();
        });
    }

    public String getUserName(Member member) {
        String userName = "";
        if (null != member) {
            userName = member.realName;
            if (ValidationUtil.isEmpty(userName)) userName = member.nickName;
        }
        return userName;
    }


    public void setBuyerAvatar(Product each) {
        if (null != each.defaultSku) {
            List<String> avatars = ProductSkuAvatar.find.query()
                    .select("avatar")
                    .where().eq("skuId", each.defaultSku.id)
                    .orderBy().desc("id")
                    .setMaxRows(5)
                    .findSingleAttributeList();
            each.avatarList.addAll(avatars);
        }
    }


    public long getProductViews(long productId) {
        String key = cacheUtils.getProductViewKey(productId);
        Optional<Object> optional = null;
        long views = 0;
        try {
            optional = asyncCacheApi.getOptional(key).toCompletableFuture().get(10, TimeUnit.SECONDS);
            if (optional.isPresent()) views = (Long) optional.get();
        } catch (Exception e) {
            logger.error("getProductViews:" + e.getMessage());
        }
        if (views < 1) {
            ProductViews productViews = ProductViews.find.query().where().eq("productId", productId)
                    .orderBy().asc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null != productViews) {
                views = productViews.views;
                cache.set(key, views, 1);
            }
        }
        return views;
    }

    public ExpressionList<Product> autoGetProductsExpressionList() {
        ExpressionList<Product> expressionList = Product.find.query().where()
                .eq("status", Product.STATUS_ON_SHELVE);
        return expressionList;
    }

    public void setProductList(ObjectNode resultNode) {
        LocalDateTime today = LocalDateTime.now();
        long currentTime = Timestamp.valueOf(today).getTime() / 1000;
        List<Product> list;
        int hour = today.getHour();
        ExpressionList<Product> expressionList = autoGetProductsExpressionList();
        PagedList<Product> pagedList = expressionList
                .le("activityType", Product.ACTIVITY_TYPE_SCORE)
                .orderBy().desc("sort")
                .orderBy().desc("id")
                .setFirstRow(0)
                .setMaxRows(PAGE_SIZE_10)
                .findPagedList();
        boolean hasNext = pagedList.hasNext();
        list = pagedList.getList();
        list.parallelStream().forEach((each) -> autoSetProduct(currentTime, hour, each, HOT_VIEW_LIST));
        resultNode.put("hasNext", hasNext);
        resultNode.set("productList", Json.toJson(list));
    }

    public List<Product> getFlashSalesProduct() {
        LocalDateTime today = LocalDateTime.now();
        ExpressionList<Product> expressionList = autoGetProductsExpressionList();
        List<Product> list = expressionList
                .orderBy().desc("sort")
                .orderBy().desc("id")
                .findList();
        long currentTime = Timestamp.valueOf(today).getTime() / 1000;
        int hour = today.getHour();
        list.parallelStream().forEach((each) -> autoSetProduct(currentTime, hour, each, HOT_VIEW_LIST));
        return list;
    }


    public void updateHotView(String jsonCache, int hotViewType) {
        CompletableFuture.runAsync(() -> {
            if (!ValidationUtil.isEmpty(jsonCache)) {
                ObjectNode node = (ObjectNode) Json.parse(jsonCache);
                if (node.has("list")) {
                    ArrayNode nodes = (ArrayNode) node.findPath("list");
                    nodes.forEach((each) -> {
                        long id = each.findPath("id").asLong();
                        increaseProductViews(id, hotViewType);
                    });
                }
            }
        });
    }

    public void updateHotView(List<Product> list, int hotViewType) {
        list.parallelStream().forEach((product) -> {
            increaseProductViews(product.id, hotViewType);
        });
    }

    public void saveSKUFavor(ProductSku each) {
        if (each.detailList.size() > 0) {
            List<ProductFavorDetail> updateList = new ArrayList<>();
            List<ProductFavorDetail> deleteList = new ArrayList<>();
            Ebean.deleteAll(deleteList);
            Ebean.saveAll(updateList);
            List<ProductFavorDetail> favorDetailList = ProductFavorDetail.find.query().where()
                    .eq("skuFavorId", each.id).findList();
            if (favorDetailList.size() > 0) {
                ProductFavorProducts productFavor = ProductFavorProducts.find.query().where()
                        .eq("skuId", each.id)
                        .order().asc("id")
                        .setMaxRows(1)
                        .findOne();
                if (null == productFavor) productFavor = new ProductFavorProducts();
                else {
                    if (productFavor.favorId != each.id) {
                        //不是sku的优惠，说明已经有优惠了，不允许 加入
                        return;
                    }
                }
                productFavor.setFavorId(each.id);
                productFavor.setFavorName(each.favorName);
                productFavor.setProductId(each.productId);
                productFavor.setSkuId(each.id);
                productFavor.setSkuName(each.name);
                productFavor.setBeginTime(each.beginTime);
                productFavor.setEndTime(each.endTime);
                productFavor.setRequireType(each.requireType);
                if (each.beginTime < 1) {
                    productFavor.setStatus(ProductFavor.STATUS_ENABLE);
                } else {
                    long currentTime = System.currentTimeMillis() / 1000;
                    if (each.beginTime <= currentTime) {
                        productFavor.setStatus(ProductFavor.STATUS_ENABLE);
                    } else productFavor.setStatus(ProductFavor.STATUS_NOT_START);
                    if (each.endTime > 0 && currentTime > each.endTime)
                        productFavor.setStatus(ProductFavor.STATUS_DISABLE);
                }
                productFavor.setStatus(ProductFavor.STATUS_ENABLE);
                productFavor.setLimitAmount(each.limitAmount);
                productFavor.save();
            } else {
                List<ProductFavorProducts> list = ProductFavorProducts.find.query().where()
                        .eq("favorId", each.id)
                        .findList();
                Ebean.deleteAll(list);
            }
        }
    }

    public void updateAttrSort(ProductSku each) {
        if (null != each && !ValidationUtil.isEmpty(each.name)) {
            String[] attrArray = each.name.split("/");
            if (null != attrArray && attrArray.length > 0) {
                String eachAttr = attrArray[0];
                String[] optionArray = eachAttr.split(":");
                if (null != optionArray && optionArray.length == 2) {
                    String attr = optionArray[0];
                    String value = optionArray[1];
                    if (!ValidationUtil.isEmpty(attr) && !ValidationUtil.isEmpty(value)) {
                        ProductAttr productAttr = ProductAttr.find.query().where()
                                .eq("productId", each.productId)
                                .eq("name", attr)
                                .orderBy().asc("id")
                                .setMaxRows(1)
                                .findOne();
                        if (null != productAttr) {
                            ProductAttrOption option = ProductAttrOption.find.query().where()
                                    .eq("attrId", productAttr.id)
                                    .eq("name", value)
                                    .orderBy().asc("id")
                                    .setMaxRows(1)
                                    .findOne();
                            if (null != option) {
                                option.setSort(each.sort);
                                option.save();
                            }
                        }
                    }
                }
            }

        }
    }


    public void updateClassifyCache(ProductClassify productClassify) {
        try {
            String classifyCode = productClassify.classifyCode;
            String encodeClassify = URLEncoder.encode(classifyCode, "UTF-8");

            long classifyId = productClassify.id;
            for (int i = 1; i < 100; i++) {
                String jsonCacheKey = cacheUtils.getClassifyJsonCache(encodeClassify, i);
                cache.remove(jsonCacheKey);
                String eachClassifyCacheKey = cacheUtils.getClassifyJsonCache(classifyId, i);
                cache.remove(eachClassifyCacheKey);
            }

            String jsonListCache = cacheUtils.getClassifyListJsonCache();
            cache.remove(jsonListCache);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void refreshRecommendProduct() {
        List<ProductDefaultRecommend> recommendList = ProductDefaultRecommend.find.all();
        String key = cacheUtils.defaultRecommendProductList();
        List<Product> defaultList = new ArrayList<>();
        recommendList.forEach((each) -> {
            Product product = getProduct(each.productId);
            if (null != product) defaultList.add(product);
        });
        cache.set(key, defaultList);
    }

    public void refreshRecommendProduct(long id) {
        List<ProductRecommend> recommendList = ProductRecommend.find.query().where()
                .eq("productId", id)
                .orderBy().desc("id")
                .findList();
        String key = cacheUtils.getRecommendProductList(id);
        List<Product> defaultList = new ArrayList<>();
        recommendList.forEach((each) -> {
            Product product = getProduct(each.recommendProductId);
            if (null != product) defaultList.add(product);
        });
        cache.set(key, defaultList);
    }

    public void updateTabCache() {
        String jsonCacheKey = cacheUtils.getProductTabJsonCache();
        cache.remove(jsonCacheKey);
        List<ProductTab> list = ProductTab.find.query().where()
                .eq("enable", true)
                .orderBy().desc("sort")
                .orderBy().desc("id")
                .findList();
        list.parallelStream().forEach((each) -> {
            List<ProductTabClassify> classifies = ProductTabClassify.find.query().where()
                    .eq("productTabId", each.id)
                    .orderBy().asc("sort")
                    .orderBy().asc("id")
                    .findList();
            each.tabClassifyList.addAll(classifies);
        });
        ObjectNode result = Json.newObject();
        result.put("code", 200);
        result.set("list", Json.toJson(list));
        cache.set(jsonCacheKey, Json.stringify(result));
    }

    public  void setEnrollButtonName(Product product){
        String buttonName = "";
        if(product.productType == Product.TYPE_ENROLL){
            EnrollConfig enrollConfig = EnrollConfig.find.query().where().eq("productId",product.id)
                    .orderBy().desc("id")
                    .setMaxRows(1)
                    .findOne();
            if(null != enrollConfig){
                buttonName = enrollConfig.buttonName;
            }
        }
      product.buttonName = buttonName;
    }

}
