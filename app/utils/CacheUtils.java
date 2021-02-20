package utils;

import constants.BusinessConstant;
import constants.RedisKeyConstant;
import models.product.Product;
import models.product.ProductClassify;
import models.product.ProductClassifyDetails;
import play.cache.AsyncCacheApi;
import play.cache.SyncCacheApi;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.IntStream;

import static constants.RedisKeyConstant.*;

/**
 * cache utils
 */
@Singleton
public class CacheUtils {

    @Inject
    SyncCacheApi cache;

    @Inject
    AsyncCacheApi asyncCacheApi;

    /**
     * 获取用户表的cache key
     *
     * @param id
     * @return
     */
    public String getMemberKey(long id) {
        return RedisKeyConstant.KEY_MEMBER_ID + id;
    }

    public String getMemberKey(String id) {
        return RedisKeyConstant.KEY_MEMBER_ID + id;
    }

    public String getMemberKey(int deviceType, String token) {
        return RedisKeyConstant.KEY_MEMBER_ID + deviceType + ":" + token;
    }

    /**
     * 获取手机号短信限制缓存key
     *
     * @param phoneNumber
     * @return
     */
    public String getSmsPhoneNumberLimitKey(String phoneNumber) {
        return SMS_PHONENUMBER_LIMIT_PREFIX_KEY + phoneNumber;
    }


    public String getSMSLastVerifyCodeKey(String phoneNumber) {
        return BusinessConstant.KEY_LAST_VERIFICATION_CODE_PREFIX_KEY + phoneNumber;
    }

    public String getAllLevelKeySet() {
        return ALL_LEVELS_KEY_SET;
    }

    public String getEachLevelKey(int level) {
        return LEVEL_KEY_PREFIX + level;
    }

    public String getAllScoreConfigKeySet() {
        return ALL_SCORE_CONFIGS_KEY_SET;
    }

    public String getEachScoreConfigKey(int type) {
        return SCORE_CONFIG_KEY_PREFIX + type;
    }

    public String getSoldAmountCacheKey(long merchantId) {
        return RedisKeyConstant.MERCHANTS_SOLD_AMOUNT_CACHE_KEY + merchantId;
    }

    /**
     * 商品分类列表集合
     *
     * @return
     */
    public String getCategoryPrefix(long categoryId) {
        return RedisKeyConstant.MERCHANTS_CATEGORIES_EACH_PREFIX + categoryId;
    }

    /**
     * 兑换分类缓存列表
     *
     * @return
     */
    public String getCategoryJsonCache() {
        return RedisKeyConstant.MERCHANTS_CATEGORIES_LIST_CACHE_KEY_PREFIX;
    }

    public String getCategoryJsonCacheByParentId(long parentId) {
        return RedisKeyConstant.MERCHANTS_CATEGORIES_LIST_CACHE_BY_PARENT_ID_PREFIX + parentId;
    }

    public String getScoreCategories() {
        return RedisKeyConstant.SCORE_CATEGORIES;
    }

    public String getShopCategoryJsonCacheByParentId(long parentId) {
        return RedisKeyConstant.SHOP_PRODUCT_CATEGORIES_LIST_CACHE_BY_PARENT_ID_PREFIX + parentId;
    }

    public String getShopCategoryJsonCache(long shopId) {
        return RedisKeyConstant.SHOP_PRODUCT_CATEGORIES_LIST_CACHE_KEY_PREFIX;
    }

    /**
     * 品牌集合
     *
     * @return
     */
    public String getBrandKeySet() {
        return RedisKeyConstant.BRAND_KEY_SET;
    }

    /**
     * 单个品牌的KEY
     *
     * @param brandId
     * @return
     */
    public String getBrandKey(long brandId) {
        return RedisKeyConstant.BRAND_EACH_KEY + brandId;
    }

    /**
     * 首页商品图缓存
     *
     * @return
     */
    public String homepageBrandJsonCache() {
        return HOMEPAGE_BRAND_JSON_CACHE;
    }

    public String homepageOnArrivalJsonCache() {
        return ON_NEW_ARRIVAL_JSON_CACHE;
    }

    public String homepageOnPromotionJsonCache() {
        return ON_PROMOTION_JSON_CACHE;
    }

    public String getMailFeeCacheKeyList() {
        return RedisKeyConstant.MAIL_FEE_CACHE_KEYSET;
    }

    public String getMailFeeKey(String regionCode) {
        return RedisKeyConstant.MAIL_FEE_CACHE_KEY_PREFIX + regionCode;
    }


    public String getCouponConfigCacheKeyset() {
        return RedisKeyConstant.COUPON_CONFIG_KEYSET;
    }

    public String getCouponConfigCacheKey(long id) {
        return RedisKeyConstant.COUPON_CONFIG_KEY_PREFIX + id;
    }

    public String getParamConfigCacheKeyset() {
        return RedisKeyConstant.PARAM_CONFIG_KEYSET;
    }

    public String getParamConfigCacheKey() {
        return RedisKeyConstant.PARAM_CONFIG_KEY_PREFIX;
    }

    public String getMerchantCacheKey(long id) {
        return RedisKeyConstant.MERCHANT_CACHE_KEY_PREFIX + id;
    }

    public String getProductJsonCacheKey(long id) {
        return RedisKeyConstant.MERCHANT_JSON_CACHE_KEY_PREFIX + id;
    }

    public String getMerchantCacheKeySet() {
        return RedisKeyConstant.MERCHANT_CACHE_KEYSET;
    }


    public void updateMerchantJsonCache(Product product) {
        String key = getProductJsonCacheKey(product.id);
        cache.remove(key);
    }

    public String getMemberTokenKey(int deviceType, long uid) {
        return RedisKeyConstant.KEY_MEMBER_TOKEN_KEY + deviceType + ":" + uid;
    }

    public String getMixOptionCacheSet() {
        return RedisKeyConstant.MIX_OPTION_KEY_SET;
    }

    public String getProductClassifyCacheSet() {
        return RedisKeyConstant.PRODUCT_CLASSIFY_KEY_SET;
    }

    public String getClassifyJsonCache(String classifyCode, int page) {
        return RedisKeyConstant.PRODUCT_CLASSIFY_JSON_CACHE + classifyCode + ":" + page;
    }

    public String getClassifyJsonCache(long classifyId, int page) {
        return RedisKeyConstant.PRODUCT_CLASSIFY_BY_ID_JSON_CACHE + classifyId + ":" + page;
    }

    public String getClassifyListJsonCache() {
        return RedisKeyConstant.PRODUCT_CLASSIFY_LIST_JSON_CACHE;
    }

    public String getMailFeeJsonCache() {
        return RedisKeyConstant.PRODUCT_MAIL_FEE_LIST_JSON_CACHE;
    }

    public String getProductsJsonCache(int page, long brandId, int paramSize, int mixOrder, int orderByPrice, int orderBySoldAmount) {
        return PRODUCT_JSON_CACHE + page + ":" + brandId + ":" + paramSize + ":" + mixOrder + ":" + orderByPrice + ":" + orderBySoldAmount;
    }

    public String getPlatformTopProductsJsonCache(int page) {
        return PLATFORM_TOP_PRODUCT_JSON_CACHE + page;
    }

    public String getShopTopProductsJsonCache(long shopId, int page) {
        return SHOP_TOP_PRODUCT_JSON_CACHE + shopId + ":" + page;
    }

    public String getProductsByTagJsonCache(String tag) {
        return PRODUCT_BY_TAG_JSON_CACHE + tag;
    }

    public String getProductsByCategoryFromCache(long categoryId, int page) {
        return GET_PRODUCT_BY_CATEGORY_FROM_JSON_CACHE + categoryId + ":" + page;
    }

    public String getShopProductsByCategoryFromCache(long categoryId, int page) {
        return GET_SHOP_PRODUCT_BY_CATEGORY_FROM_JSON_CACHE + categoryId + ":" + page;
    }

    public String getSpecialTopicJsonCache() {
        return SPECIAL_TOPIC_JSON_CACHE;
    }

    public String getBrandJsonCache() {
        return BRAND_JSON_CACHE;
    }

    public String getFlashsaleTodayJsonCache(int page) {
        return FLASH_SALE_TODAY_JSON_CACHE + page;
    }

    public String getFlashsaleTomorrowJsonCache(int page) {
        return FLASH_SALE_TOMORROW_JSON_CACHE + page;
    }

    public String getFlashsaleJsonCache() {
        return FLASH_SALE_JSON_CACHE;
    }

    public String getProductSearchJsonCache(String filter, int searchType, int page) {
        return PRODUCT_SEARCH_JSON_CACHE + filter + ":" + searchType + ":" + page;
    }

    public String getProductKeywordsJsonCache() {
        return PRODUCT_SEARCH_KEYWORDS_JSON_CACHE;
    }

    public String getShopKeywordsJsonCache() {
        return SHOP_SEARCH_KEYWORDS_JSON_CACHE;
    }

    public String getProductTabJsonCache() {
        return PRODUCT_TAB_JSON_CACHE;
    }

    public String getProductsByCoupon(long couponId) {
        return RedisKeyConstant.PRODUCT_COUPON_JSON_CACHE + couponId;
    }

    public String defaultRecommendProductList() {
        return DEFAULT_RECOMMEND_PRODUCT;
    }

    public String getRecommendProductList(long productId) {
        return RECOMMEND_PRODUCT_KEY + productId;
    }

    public String getRelateProductList(long productId) {
        return RELATE_PRODUCT_KEY + productId;
    }

    public String getFavorCache(long favorId) {
        return PRODUCT_FAVOR_CACHE + favorId;
    }

    public String getProductViewKey(long productId) {
        return PRODUCT_VIEW_KEY + productId;
    }

    public String getStockSoldAmount() {
        return STOCK_SOLD_AMOUNT_JSON_CACHE;
    }


    public String getPrefetchJsonCache() {
        return PREFETCH_JSON_CACHE;
    }

    public String getProductJsonCache(long productId) {
        return EACH_PRODUCT_JSON_CACHE + productId;
    }

    public String getShopProductsJsonCache(long shopId, int page) {
        return SHOP_PRODUCT_JSON_CACHE + shopId + ":" + page;
    }

    public String getShopListJsonCache(int page) {
        return SHOP_LIST_JSON_CACHE + page;
    }

    public String getTopShopListJsonCache() {
        return TOP_SHOP_LIST_JSON_CACHE;
    }

    public String getProductTabDetail(long tabId, int page) {
        return RedisKeyConstant.PRODUCT_TAB_DETAIL_JSON_CACHE + tabId + ":" + page;
    }

    public String getArticleCategoryKey(String cateName) {
        return RedisKeyConstant.ARTICLE_CATEGORY_KEY_PREFIX + cateName;
    }

    public String getArticleCategoryKey(int categoryId) {
        return RedisKeyConstant.ARTICLE_CATEGORY_KEY_BY_ID_PREFIX + categoryId;
    }

    public String getCarouselJsonCache() {
        return RedisKeyConstant.CAROUSEL_JSON_CACHE;
    }

    public String getComboProductsJsonCache(int page) {
        return COMBO_PRODUCT_JSON_CACHE + page;
    }

    public String getGrouponProductsJsonCache(int page) {
        return GROUPON_PRODUCT_JSON_CACHE + page;
    }

    public String getProductsJsonCache() {
        return PRODUCT_JSON_CACHE;
    }

    public String getScoreProductsJsonCache(long categoryId, int page) {
        return SCORE_PRODUCT_JSON_CACHE + categoryId + ":" + page;
    }

    public void updateProductJsonCache(Product product) {
        if (null != product) {
            String key = getProductJsonCacheKey(product.id);
            asyncCacheApi.set(key, product);
            List<ProductClassifyDetails> productClassifyDetails = ProductClassifyDetails.find.query().where().eq("productId", product.id).findList();
            productClassifyDetails.forEach((each) -> {
                ProductClassify productClassify = ProductClassify.find.byId(each.classifyId);
                if (null != productClassify) {
                    try {
                        String encodeClassify = URLEncoder.encode(productClassify.classifyCode, "UTF-8");
                        IntStream.range(1, 20).forEachOrdered((page) -> {
                            String jsonCacheKey = getClassifyJsonCache(encodeClassify, page);
                            asyncCacheApi.remove(jsonCacheKey);
                        });
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        asyncCacheApi.remove(getProductsJsonCache());
    }

}
