package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import io.ebean.Ebean;
import io.ebean.Expr;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.cart.ShoppingCart;
import models.enroll.*;
import models.groupon.Groupon;
import models.groupon.GrouponOrder;
import models.order.Order;
import models.order.OrderDetail;
import models.order.TypeOrderStat;
import models.product.*;
import models.promotion.CardCouponConfig;
import models.promotion.CouponConfig;
import models.region.Region;
import models.shop.Shop;
import models.shop.ShopProductCategory;
import models.shop.ShopTag;
import models.system.ParamConfig;
import models.system.SystemCarousel;
import models.user.Member;
import models.user.MemberBalance;
import models.user.MemberCoupon;
import models.user.MemberProfile;
import play.Logger;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;
import utils.BusinessItem;
import utils.ValidationUtil;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Array;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

import static constants.BusinessConstant.*;
import static constants.RedisKeyConstant.MAIL_FEE_KEY_FREE_MAIL_FEE_UP_TO;
import static constants.RedisKeyConstant.WEATHER_JSON_CACHE;
import static models.product.Brand.STATUS_NORMAL;
import static models.promotion.CouponConfig.*;
import static utils.BizUtils.*;

/**
 * 用户控制类
 */
public class ProductController extends BaseController {

    Logger.ALogger logger = Logger.of(ProductController.class);
    public static final int TYPE_ADD = 1;
    public static final int TYPE_SUBTRACT = 2;

    public static final int SCORE_TO_ONE_TENTH = 2;//积分对分的比例，1积分=2分钱
    public static final String REGION_JSON_KEY = "REGION_JSON_KEY";

    public static final String ATTR_KEY = "attr";
    public static final String VALUE_KEY = "value";


    /**
     * @api {GET} /v1/p/categories/?filter 01商品分类列表
     * @apiName listCategories
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){long} id 分类id
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} imgUrl 图片
     * @apiSuccess (Success 200){String} poster 海报图片
     * @apiSuccess (Success 200){long} soldAmount 已售数量
     * @apiSuccess (Success 200){int} show 1显示2不显示
     * @apiSuccess (Success 200){int} sort 排序顺序
     * @apiSuccess (Success 200){JsonArray} children 子分类列表
     * @apiSuccess (Success 200){string} updateTime 更新时间
     */
    public CompletionStage<Result> listCategories(final String filter) {
        return CompletableFuture.supplyAsync(() -> {
            String key = cacheUtils.getCategoryJsonCache();
            //第一页从缓存读取
            if (ValidationUtil.isEmpty(filter)) {
                Optional<String> cacheOptional = cache.getOptional(key);
                if (cacheOptional.isPresent()) {
                    String node = cacheOptional.get();
                    if (ValidationUtil.isEmpty(node)) return ok(Json.parse(node));
                }
            }
            ExpressionList<Category> expressionList = Category.find.query().where()
                    .eq("show", Category.SHOW_CATEGORY);
            if (!ValidationUtil.isEmpty(filter)) expressionList.icontains("name", filter);
            List<Category> list = expressionList.orderBy()
                    .asc("path")
                    .orderBy().desc("sort")
                    .findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            List<Category> resultList = convertListToTreeNode(list);
            result.set("list", Json.toJson(resultList));
            if (ValidationUtil.isEmpty(filter)) cache.set(key, Json.stringify(result), 30 * 60);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/filter_product_categories/:parentId/ 02根据父类ID取出子类
     * @apiName listProductCategoriesByParentId
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){long} id 分类id
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} imgUrl 图片
     * @apiSuccess (Success 200){String} poster 海报图片
     * @apiSuccess (Success 200){long} soldAmount 已售数量
     * @apiSuccess (Success 200){int} show 1显示2不显示
     * @apiSuccess (Success 200){int} sort 排序顺序
     * @apiSuccess (Success 200){JsonArray} children 子分类列表
     * @apiSuccess (Success 200){string} updateTime 更新时间
     */
    public CompletionStage<Result> listProductCategoriesByParentId(Long parentId) {
        return CompletableFuture.supplyAsync(() -> {
            String key = cacheUtils.getCategoryJsonCacheByParentId(parentId);
            //第一页从缓存读取
            Optional<String> cacheOptional = cache.getOptional(key);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (ValidationUtil.isEmpty(node)) return ok(Json.parse(node));
            }
            List<Category> list = Category.find.query().where()
                    .eq("show", Category.SHOW_CATEGORY)
                    .eq("parentId", parentId)
                    .orderBy()
                    .desc("sort")
                    .findList();
            ObjectNode result = Json.newObject();
            ArrayNode nodes = Json.newArray();
            list.forEach((category) -> {
                ObjectNode node = Json.newObject();
                node.put("id", category.id);
                node.put("name", category.name);
                nodes.add(node);
            });
            result.put(CODE, CODE200);
            result.set("list", nodes);
            cache.set(key, result.toString(), 10 * 60);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/top_merchant_categories/:childrenId/ 03根据子类ID取出最顶级父类ID
     * @apiName listTopParentCategory
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200){long} parentId 最顶级分类id
     */
    public CompletionStage<Result> listTopParentCategory(long childrenId) {
        return CompletableFuture.supplyAsync(() -> {
            if (childrenId < 1) return okCustomJson(CODE40001, "参数错误");
            Category category = Category.find.byId(childrenId);
            if (null == category) return okCustomJson(CODE40002, "分类不存在");
            long parentId = 0;
            if (!ValidationUtil.isEmpty(category.path)) {
                String[] pathArray = category.path.split("/");
                if (null != pathArray && pathArray.length > 1) {
                    String parentIdStr = pathArray[1];
                    try {
                        parentId = Long.parseLong(parentIdStr);
                    } catch (Exception e) {
                        logger.error("字符串转换错误" + e.toString());
                    }
                }
            }
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("parentId", parentId);
            return ok(result);
        });
    }

    public List<Category> convertListToTreeNode(List<Category> categoryList) {
        List<Category> nodeList = new ArrayList<>();
        if (null == categoryList) return nodeList;
        for (Category node : categoryList) {
            if (null != node) {
                if (!ValidationUtil.isEmpty(node.path) && node.path.equalsIgnoreCase("/")) {
                    //根目录
                    nodeList.add(node);
                } else {
                    updateChildren(node, categoryList);
                }
            }

        }
        return nodeList;
    }

    private void updateChildren(Category category, List<Category> nodeList) {
        for (Category parentCategory : nodeList) {
            if (null != parentCategory && category.parentId == parentCategory.id) {
                if (parentCategory.children == null) parentCategory.children = new ArrayList<>();
                parentCategory.children.add(category);
                break;
            }
        }
    }

    /**
     * @api {GET} /v1/p/products_by_classify/?classifyCode= 04根据分类列出商品列表
     * @apiName listProductsByClassify
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){long} id 商品id
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    public CompletionStage<Result> listProductsByClassify(Http.Request request, String classifyCode, int page) {
        return CompletableFuture.supplyAsync(() -> {
            if (ValidationUtil.isEmpty(classifyCode)) return okCustomJson(CODE40001, "分类编号不可为空");
            try {
                String encodeClassify = URLEncoder.encode(classifyCode, "UTF-8");
                String jsonCacheKey = cacheUtils.getClassifyJsonCache(encodeClassify, page);
                Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
                if (cacheOptional.isPresent()) {
                    String node = cacheOptional.get();
                    if (null != node) return ok(Json.parse(node));
                }
                ObjectNode result = Json.newObject();
                result.put(CODE, CODE200);
                ProductClassify classify = ProductClassify.find.query()
                        .where().eq("classifyCode", classifyCode)
                        .setMaxRows(1).findOne();
                if (null == classify) return okCustomJson(CODE40001, "该归集不存在");
                PagedList<ProductClassifyDetails> pagedList = ProductClassifyDetails.find.query().where()
                        .eq("classifyId", classify.id)
                        .orderBy().desc("sort")
                        .orderBy().asc("id")
                        .setFirstRow((page - 1) * PAGE_SIZE_20)
                        .setMaxRows(PAGE_SIZE_20)
                        .findPagedList();
                List<ProductClassifyDetails> list = pagedList.getList();
                list.forEach((each) -> {
                    Product product = businessUtils.getProduct(each.productId);
                    if (null != product) {
                        setProductDetail(product);
                        classify.productList.add(product);
                    }
                });
                result.set("list", Json.toJson(classify.productList));
                result.put("pages", pagedList.getTotalPageCount());
                cache.set(jsonCacheKey, Json.stringify(result), 2 * 60);
                return ok(result);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return okCustomJson(CODE500, "发生异常，请重试");
            }
        });
    }

    /**
     * @api {POST} /v1/p/products/?page=&brandId=&mixOrder=&orderByPrice=&orderBySoldAmount= 05商品列表
     * @apiName listProducts
     * @apiGroup Product
     * @apiParam {int} [page] 页面
     * @apiParam {int} [brandId] 品牌ID
     * @apiParam {int} [mixOrder] 综合排序 0不使用综合 1使用综合排序 三个排序中默认使用综合排序
     * @apiParam {int} [orderByPrice] 按价格排序 0不按价格排序 1价格升序 2价格降序
     * @apiParam {int} [orderBySoldAmount] 按销量排序 0不按销量排序 1销量升序 2销量降序
     * @apiParam {int} activityType activityType
     * @apiParam {Array} [param] 筛选参数数组
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面大图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> listProducts(Http.Request request) {
        JsonNode requestNode = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            List<Product> emptyList = new ArrayList<>();
            result.set("list", Json.toJson(emptyList));
            if (null == requestNode) {
                return ok(result);
            }
            long brandId = requestNode.findPath("brandId").asLong();
            long shopId = requestNode.findPath("shopId").asLong();
            int page = requestNode.findPath("page").asInt();
            int activityType = requestNode.findPath("activityType").asInt();
            int productType = requestNode.findPath("productType").asInt();
//            //TODO ADD LATER
//            String jsonCacheKey = cacheUtils.getProductsJsonCache(page, brandId);
//            //第一页需要缓存，从缓存读取
//            Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
//            if (cacheOptional.isPresent()) {
//                String node = cacheOptional.get();
//                if (null != node) return ok(Json.parse(node));
//            }
            ExpressionList<Product> expressionList = Product.find.query().where()
                    .ge("status", Product.STATUS_ON_SHELVE)
                    .eq("placeHomeTop", true);
            if (shopId > 0) expressionList.eq("shopId", shopId);
            if (brandId > 0) expressionList.eq("brandId", brandId);
            if (activityType > 0) expressionList.eq("activityType", activityType);
            if (productType > 0) expressionList.eq("productType", productType);

            PagedList<Product> pagedList = expressionList.setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                    .findPagedList();
            int pages = pagedList.getTotalPageCount();
            List<Product> list = pagedList.getList();
            list.parallelStream().forEach((each) -> setProductDetail(each));
            result.put("pages", pages);
            result.set("list", Json.toJson(list));
//            cache.set(jsonCacheKey, Json.stringify(result), 2 * 60);
            return ok(result);
        });
    }

    private void setProductDetail(Product each) {
        if (null != each) {
            each.setDetails("");
            each.soldAmount = each.soldAmount + each.virtualAmount;
            each.virtualAmount = 0;
            ProductSku sku = ProductSku.find.query().where()
                    .eq("productId", each.id)
                    .orderBy().asc("price")
                    .orderBy().asc("id")
                    .setMaxRows(1).findOne();
            if (null != sku) {
                each.defaultSku = sku;
                if (!ValidationUtil.isEmpty(each.coverImgUrl)) {
                    each.defaultSku.imgUrl = each.coverImgUrl;
                }
                ProductFavorProducts favorProducts = ProductFavorProducts.find.query().where()
                        .eq("skuId", sku.id)
                        .eq("status", ProductFavor.STATUS_ENABLE)
                        .setMaxRows(1).findOne();
                each.favorProducts = favorProducts;
                if (null != each.favorProducts) {
                    List<ProductFavorDetail> details = ProductFavorDetail.find.query().where()
                            .eq("favorId", each.favorProducts.favorId)
                            .orderBy().asc("id")
                            .findList();
                    each.favorDetailList.addAll(details);
                }
            }
            List<ProductParam> list = ProductParam.find.query().where()
                    .eq("productId", each.id)
                    .eq("needShow", true)
                    .order().asc("sort")
                    .order().asc("id")
                    .findList();
            each.paramList.addAll(list);
            FlashSaleProduct flashSaleProduct = getFlashsaleProduct(each.id);
            each.flashSaleProduct = flashSaleProduct;
        }
    }


    private Set<Long> filterProductsByParam(ArrayNode paramNode) {
        Set<Long> set = new HashSet<>();
        Map<String, String> map = new HashMap<>();
        for (JsonNode param : paramNode) {
            String attr = param.findPath(ATTR_KEY).asText();
            String value = param.findPath(VALUE_KEY).asText();
            int attrLen = attr.length();
            int valueLen = attr.length();
            if (attrLen > 8 || attrLen < 1) return set;
            if (valueLen > 8 || valueLen < 1) return set;
            map.put(attr, value);
        }
        if (map.size() > 10) return set;
        Map<Long, Integer> hitMap = new ConcurrentHashMap<>();
        map.forEach((k, v) -> {
            ExpressionList<ProductParam> expressionList = ProductParam.find.query().select("productId").setDistinct(true)
                    .where().eq("name", k);
            if (v.startsWith("LT")) expressionList.lt("value", v.substring(2));
            else if (v.startsWith("EQ")) expressionList.eq("value", v.substring(2));
            else if (v.startsWith("GT")) expressionList.ge("value", v.substring(2));
            else if (v.indexOf("-") >= 0) {
                String[] between = v.split("-");
                if (between.length == 2) {
                    String first = between[0];
                    String second = between[1];
                    if (!ValidationUtil.isEmpty(first) && !ValidationUtil.isEmpty(second)) {
                        double firstNum = Double.parseDouble(first);
                        double secondNum = Double.parseDouble(second);
                        expressionList.ge("value", firstNum);
                        expressionList.le("value", secondNum);
                    }
                }
            } else expressionList.eq("value", v);
            List<Long> resultProductList = expressionList.findSingleAttributeList();
            resultProductList.parallelStream().forEach((each) -> {
                Integer hitCount = hitMap.get(each);
                if (null == hitCount) hitMap.put(each, 1);
                else hitMap.put(each, hitCount + 1);
            });
        });
        int paramSize = map.size();
        hitMap.forEach((k, v) -> {
            if (v >= paramSize) set.add(k);
        });
        return set;
    }


    /**
     * @api {GET} /v1/p/products/:productId/ 06商品详情
     * @apiName getProductDetail
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200){long} id 商品id
     * @apiSuccess {String} name 名称
     * @apiSuccess {long} productId 商品ID
     * @apiSuccess {long} categoryId 分类id
     * @apiSuccess {long} brandId 品牌id
     * @apiSuccess {long} mailFeeId 运费id
     * @apiSuccess {long} typeId 类型编号
     * @apiSuccess {String} [sketch] 产品简介
     * @apiSuccess {String} [keywords] 关键字
     * @apiSuccess {String} [tag] 标签
     * @apiSuccess {String} [marque] 商品型号
     * @apiSuccess {String} [barcode] 条形码
     * @apiSuccess {String} [mixCode] 混搭编码，如要参与混搭，必须填写一致的混搭编码
     * @apiSuccess {String} unit 单位
     * @apiSuccess {String} details 产品详情
     * @apiSuccess {int} stock 库存
     * @apiSuccess {int} warningStock 警告库存
     * @apiSuccess {int} [virtualCount] 虚拟购物量，也就是初始刷量
     * @apiSuccess {boolean} [isCombo] 是否是套餐
     * @apiSuccess {boolean} [allowUseScore] 是否允许积分抵扣
     * @apiSuccess {string} details 详情
     * @apiSuccess {int} status  -1=>下架,1=>上架,2=>预售,0=>草稿',
     * @apiSuccess {int} state '审核状态 -1 审核失败 0 未审核 1 审核成功',
     * @apiSuccess {String} coverImgUrl 商品封面图片
     * @apiSuccess {String} poster 首页宣传图
     * @apiSuccess {long} marketPrice 市场价，以分为单位
     * @apiSuccess {long} price 现价，以分为单位
     * @apiSuccess {long} wholesalePrice 批发价，以分为单位
     * @apiSuccess {long} costPrice 成本价，以分为单位
     * @apiSuccess {long} maxScoreUsed 最多可使用积分抵消
     * @apiSuccess {boolean} showAtHome 是否在主页显示true显示，false不显示
     * @apiSuccess {int} sort 排序
     * @apiSuccess {JsonArray} attrList 规格列表
     * @apiSuccess {long} attrId 规格ID
     * @apiSuccess {String} options 规格值，以逗号隔开
     * @apiSuccess {JsonArray} skuList sku列表
     * @apiSuccess {String} imgUrl 主图
     * @apiSuccess {long} price 价格，以分为单位
     * @apiSuccess {long} soldAmount 已售数量
     * @apiSuccess {long} stock 库存
     * @apiSuccess {String} code 商品SKU编码
     * @apiSuccess {String} barcode 商品条形码，唯一性，对应商品厂家的条形码
     * @apiSuccess {String} data sku串，预留，用于商品选项选择时定位
     * @apiSuccess {JsonArray} imgList 图片列表
     * @apiSuccess {string} imgUrl_ 图片链接地址
     * @apiSuccess {string} imgTips 图片提示信息
     * @apiSuccess {JsonArray} paramList 商品参数列表
     * @apiSuccess {string} name_ 参数名字
     * @apiSuccess {string} value_ 参数内容，以/隔开
     * @apiSuccess {Object} wine 对应的酒库详情
     */
    public CompletionStage<Result> getProductDetail(Http.Request request, long productId) {
        String key = cacheUtils.getProductJsonCache(productId);
        return asyncCacheApi.getOptional(key).thenApplyAsync((jsonCache) -> {
            Member memberInCache = businessUtils.getUserIdByAuthToken(request);
            if (productId < 1) return okCustomJson(CODE40001, "参数错误");
            businessUtils.increaseProductViews(productId, HOT_VIEW_DETAIL);
            if (null != memberInCache) businessUtils.updateViewsAvatars(productId, memberInCache);
            addBrowseLog(productId, memberInCache);
//            if (jsonCache.isPresent()) {
//                String result = (String) jsonCache.get();
//                if (!ValidationUtil.isEmpty(result)) {
//                    ObjectNode node = (ObjectNode) Json.parse(result);
//                    if (null != memberInCache) {
//                        setUserFav(productId, memberInCache, node);
//                    }
//                    return ok(node);
//                }
//            }
            Product product = Product.find.query().where().eq("id", productId)
                    .eq("status", Product.STATUS_ON_SHELVE).setMaxRows(1).findOne();
            if (null == product) return okCustomJson(CODE40002, "该商品不存在或已下架");

            List<ProductAttr> attrs = ProductAttr.find.query().where()
                    .eq("productId", productId)
                    .order().asc("id").findList();
            attrs.parallelStream().forEach((attr) -> {
                List<ProductAttrOption> options = ProductAttrOption.find.query().where()
                        .eq("attrId", attr.id)
                        .orderBy().asc("sort")
                        .findList();
                attr.options.addAll(options);
            });
            List<ProductSku> skuList = getProductSkuList(product);
            List<ProductImage> images = ProductImage.find.query().where()
                    .eq("productId", product.id)
                    .orderBy().desc("sort")
                    .findList();

            product.setSoldAmount(product.soldAmount + product.virtualAmount);
            LocalDateTime today = LocalDateTime.now();
            long currentTime = Timestamp.valueOf(today).getTime() / 1000;
            int hour = today.getHour();
            businessUtils.autoSetProduct(currentTime, hour, product, HOT_VIEW_DETAIL);
            if (product.activityType == Order.ORDER_ACTIVITY_TYPE_GROUPON_SUPER) {
                getGrouponDetailForProduct(product);
            }
            ObjectNode node = (ObjectNode) Json.toJson(product);
            node.remove("virtualCount");
            node.remove("virtualAmount");
            node.set("attrs", Json.toJson(attrs));
            node.set("skuList", Json.toJson(skuList));
            node.set("images", Json.toJson(images));
            ProductUniImage productUniImage = ProductUniImage.find.query().orderBy().asc("id").setMaxRows(1).findOne();
            if (null != productUniImage) {
                node.set("productUniImage", Json.toJson(productUniImage));
            }
            //是否收收藏
            setUserFav(productId, memberInCache, node);
            List<Product> recommendList = businessUtils.getRecommendProductList(productId);
            node.set("recommendList", Json.toJson(recommendList));
            if (product.isCombo) {
                List<Product> relateList = businessUtils.getRelateProductList(productId);
                node.set("relateList", Json.toJson(relateList));
            }
            if (product.shopId > 0) {
                Shop shop = Shop.find.byId(product.shopId);
                if (null != shop) {
                    shop.setFilter("");
                    node.set("shop", Json.toJson(shop));
                }
            }
            if (product.activityType == Order.ORDER_ACTIVITY_TYPE_GROUPON_SUPER && null != product.groupon) {
                int grouponCount = GrouponOrder.find.query().where()
                        .eq("grouponId", product.groupon.id)
                        .findCount();
                node.put("grouponCount", grouponCount);
            }
            node.put(CODE, CODE200);
            if (product.activityType == Product.ACTIVITY_TYPE_GROUPON) {
                node.put("grouponRequireCount", businessUtils.getGrouponRequireOrders(product.activityType));
                node.put("grouponTimelimit", businessUtils.getGrouponTimeLimit());
            }
            asyncCacheApi.set(key, Json.stringify(node), 7 * 24 * 3600);
            return ok(node);
        });
    }

    private boolean isHit(Set<Long> selfCategorySet, ArrayNode categoryIdArray) {
        boolean hit = false;
        for (int i = 0; i < categoryIdArray.size(); i++) {
            JsonNode id = categoryIdArray.get(0);
            if (selfCategorySet.contains(id.asLong())) {
                hit = true;
                break;
            }
        }
        return hit;
    }

    private void setUserFav(long productId, Member memberInCache, ObjectNode node) {
        boolean isFav = false;
        if (null != memberInCache) {
            ProductFav productFav = ProductFav.find.query().where()
                    .eq("uid", memberInCache.id)
                    .eq("productId", productId)
                    .setMaxRows(1).findOne();
            if (null != productFav) {
                isFav = productFav.isEnable();
            }
        }
        node.put("isFav", isFav);
    }

    private FlashSaleProduct getFlashsaleProduct(long productId) {
        List<FlashSaleProduct> flashSaleProducts = FlashSaleProduct.find.query().where()
                .eq("productId", productId)
                .ge("status", FlashSale.STATUS_PROCESSING)
//                    .ge("beginTime", minTime)
//                    .le("endTime", maxTime)
                .orderBy().asc("status")
                .orderBy().desc("id")
                .setMaxRows(1)
                .findList();
        if (flashSaleProducts.size() > 0) {
            FlashSaleProduct flashSaleProduct = flashSaleProducts.get(0);
            return flashSaleProduct;
        }
        return null;
    }

    private List<ProductSku> getProductSkuList(Product product) {
        List<ProductSku> skuList = ProductSku.find.query().where()
                .eq("productId", product.id)
                .orderBy().asc("sort")
                .orderBy().asc("id")
                .findList();
        skuList.parallelStream().forEach((sku) -> {
            setSKUDetail(sku);
            businessUtils.setSkuAward(sku);
        });
        return skuList;
    }


    private void setSKUDetail(ProductSku sku) {
        ProductFavorProducts favorProducts = ProductFavorProducts.find.query().where()
                .eq("skuId", sku.id)
                .eq("status", ProductFavor.STATUS_ENABLE)
                .setMaxRows(1).findOne();
        sku.favor = favorProducts;
        if (null != sku.favor) {
            List<ProductFavorDetail> details = ProductFavorDetail.find.query().where()
                    .eq("favorId", sku.favor.favorId)
                    .orderBy().asc("id")
                    .findList();
            sku.detailList.addAll(details);
        }
    }

    private void addBrowseLog(long productId, Member memberInCache) {
        if (null != memberInCache) {
            CompletableFuture.runAsync(() -> {
                BrowseLog log = BrowseLog.find.query().where()
                        .eq("uid", memberInCache.id)
                        .eq("productId", productId)
                        .setMaxRows(1)
                        .findOne();
                if (null == log) {
                    log = new BrowseLog();
                    log.setProductId(productId);
                    log.setUid(memberInCache.id);
                    log.setCreateTime(dateUtils.getCurrentTimeBySecond());
                    log.save();
                }
            });
        }
    }


    private List<Product> setRecommendList(Product Product) {
        List<Product> list = Product.find.query().where()
                .eq("categoryId", Product.categoryId)
                .eq("status", Product.STATUS_ON_SHELVE)
                .ne("id", Product.id).setMaxRows(100).findList();
        //随机选出5个返回
        List<Product> filterList = randomRecommendItem(list);
        return filterList;
    }


    /**
     * 随机选出5个
     *
     * @param list
     * @return
     */
    private List<Product> randomRecommendItem(List<Product> list) {
        int MAX_LEN = list.size() - 1;
        int Min = 0;
        List<Product> result = new ArrayList<>();
        int MAX_RECOMMEND = 5;
        if (MAX_LEN <= MAX_RECOMMEND) return list;
        for (int i = 0; i < MAX_RECOMMEND; i++) {
            int index = Min + (int) (Math.random() * ((MAX_LEN - Min) + 1));
            result.add(list.get(index));
        }
        return result;
    }


    /**
     * @api {GET} /v1/p/mix_options/:mixCode/ 07混搭选项列表
     * @apiName listMixOptions
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){long} id 商品id
     * @apiSuccess (Success 200){int} amount 混搭数量规格
     */
    public CompletionStage<Result> listMixOptions(String mixCode) {
        return CompletableFuture.supplyAsync(() -> {
            if (ValidationUtil.isEmpty(mixCode)) return okCustomJson(CODE40001, "混搭编号不可为空");
            String keySet = cacheUtils.getMixOptionCacheSet();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            Optional<Map<String, Set<Integer>>> optional = cache.getOptional(keySet);
            if (optional.isPresent()) {
                Map<String, Set<Integer>> map = optional.get();
                if (null != map) {
                    Set<Integer> set = map.get(mixCode);
                    if (null != set) {
                        result.set("list", Json.toJson(set));
                        return ok(result);
                    }
                }
            }
            List<MixOption> list = MixOption.find.query().where()
                    .eq("mixCode", mixCode)
                    .orderBy().asc("id").findList();
            Set<Integer> set = new HashSet<>();
            list.stream().forEach((mixOption) -> set.add(mixOption.amount));
            businessUtils.updateMixOptionCache();
            result.set("list", Json.toJson(set));
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/shopping_cart/  08购物车商品列表
     * @apiName listProductsOnMyCart
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {int} productCounts 商品总数
     * @apiSuccess (Success 200) {double} totalMoney 商品总价
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){long} cartId 购物车ID
     * @apiSuccess (Success 200){Object} product 商品属性，参数参考商品详情中的字段
     * @apiSuccess (Success 200){Object} sku 商品SKU属性，参数参考商品详情中的字段
     */
    public CompletionStage<Result> listProductsOnMyCart(Http.Request request) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            ObjectNode resultNode = Json.newObject();
            resultNode.put(CODE, CODE200);
            ArrayNode todayNodes = Json.newArray();
            ArrayNode tomorrowNodes = Json.newArray();
            if (null == member) {
                resultNode.set("todayList", Json.toJson(todayNodes));
                resultNode.set("tomorrowList", Json.toJson(tomorrowNodes));
                return ok(resultNode);
            }
            List<ShoppingCart> list = ShoppingCart.find.query().where()
                    .eq("uid", member.id)
                    .order().desc("id")
                    .findList();
            list.stream().forEach((each) -> {
                ObjectNode node = (ObjectNode) Json.toJson(each);
                Product product = businessUtils.getProduct(each.productId);
                if (null != product) {
                    node.set("product", Json.toJson(product));
                    ProductSku sku = ProductSku.find.byId(each.skuId);
                    if (null != sku) {
                        long todayBuyAmount = todayBuyAmount(sku, member);
                        node.put("todayBuyAmount", todayBuyAmount);
                        node.set("sku", Json.toJson(sku));
                        node.put("skuEnable", true);
                    } else node.put("skuEnable", false);
                    todayNodes.add(node);
                }
            });
            resultNode.set("todayList", Json.toJson(todayNodes));
            resultNode.set("tomorrowList", Json.toJson(tomorrowNodes));
            return ok(resultNode);
        });
    }

    private long todayBuyAmount(ProductSku productSku, Member member) {
        long beginFrom = dateUtils.getTodayMinTimestamp();
        long endTo = dateUtils.getTodayMidnightTimestamp();
        List<OrderDetail> list = OrderDetail.find.query().where()
                .eq("productSkuId", productSku.id)
                .le("returnStatus", OrderDetail.STATUS_APPLY_RETURN)
                .ge("status", Order.ORDER_STATUS_UNPAY)
                .eq("uid", member.id)
                .ge("createTime", beginFrom)
                .lt("createTime", endTo)
                .findList();
        long totalAmount = list.parallelStream().mapToLong((each) -> each.number).sum();
        return totalAmount;
    }

    /**
     * @api {POST} /v1/p/shopping_cart/new/ 09添加商品到购物车
     * @apiName addToCart
     * @apiGroup Product
     * @apiParam {long} productId 商品ID
     * @apiParam {long} skuId 商品sku ID
     * @apiParam {long} amount 购买数量
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Error 40001){int} code 40001 参数错误
     * @apiSuccess (Error 40002){int} code 40002 只能确认自己的订单
     * @apiSuccess (Error 40003){int} code 40003 该商品不存在
     * @apiSuccess (Error 40004){int} code 40004 该商品只剩下:
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    @Transactional
    public CompletionStage<Result> addToCart(Http.Request request) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            JsonNode requestNode = request.body().asJson();
            if (null == member) return unauth403();
            long productId = requestNode.findPath("productId").asLong();
            long skuId = requestNode.findPath("skuId").asLong();
            long amount = requestNode.findPath("amount").asLong();
            if (productId < 1) return okCustomJson(CODE40001, "参数错误");
            ObjectNode resultNode = Json.newObject();
            resultNode.put(CODE, CODE200);
            ShoppingCart shoppingCart = ShoppingCart.find.query().where()
                    .eq("uid", member.id)
                    .eq("productId", productId)
                    .eq("skuId", skuId).setMaxRows(1).findOne();
            if (amount < 1) {
                if (null != shoppingCart) {
                    shoppingCart.delete();
                }
                return okJSON200();
            } else {
                long currentTime = dateUtils.getCurrentTimeBySecond();
                Product product = Product.find.query().where()
                        .eq("id", productId)
                        .ge("status", Product.STATUS_ON_SHELVE)
                        .setMaxRows(1).findOne();
                if (null == product) return okCustomJson(CODE40001, "该产品不存在或已下架");
                ProductSku sku;
                if (skuId < 1) {
                    ProductSku defaultSku = ProductSku.find.query().where()
                            .eq("productId", product.id)
                            .orderBy().asc("sort")
                            .orderBy().asc("id")
                            .setMaxRows(1).findOne();
                    if (null == defaultSku) return okCustomJson(CODE40001, "SKU参数有误");
                    sku = defaultSku;
                    skuId = defaultSku.id;
                } else {
                    sku = ProductSku.find.byId(skuId);
                }
                if (null == sku) return okCustomJson(CODE40001, "SKU参数有误");
                businessUtils.increaseProductViews(product.id, HOT_VIEW_SHOPPING_CART);
                long leftAmount = amount;
                boolean exceedStock = false;
                if (sku.stock < amount) {
                    leftAmount = sku.stock;
                    exceedStock = true;
                }

                List<ShoppingCart> list = ShoppingCart.find.query().where()
                        .eq("uid", member.id).findList();
                if (list.size() > 100) return okCustomJson(CODE40001, "购物车满了~");
                if (null == shoppingCart) {
                    shoppingCart = new ShoppingCart();
                    shoppingCart.setProductId(productId);
                    shoppingCart.setCreateTime(currentTime);
                    shoppingCart.setUid(member.id);
                    shoppingCart.setSkuId(skuId);
                    shoppingCart.setEnable(true);
                }
                if (sku.limitAmount > 0) {
                    leftAmount = checkUptoMaxAmount(sku, member);
                }
                if (leftAmount < 0) return okCustomJson(CODE40002, "下单数量超出限购数量");
                boolean exceedLimitAmount = false;
                if (leftAmount < amount) {
                    exceedLimitAmount = true;
                }
                if (leftAmount > amount) leftAmount = amount;
                shoppingCart.setAmount(leftAmount);
                shoppingCart.setUpdateTime(currentTime);
                shoppingCart.save();
                if (exceedStock) return okCustomJson(CODE40002, "超出库存数量");
                if (exceedLimitAmount) return okCustomJson(CODE40002, "下单数量超出库存数量");
                resultNode.put("leftAmount", leftAmount);
            }
            return ok(resultNode);
        });
    }

    private long checkUptoMaxAmount(ProductSku productSku, Member member) {
        long beginFrom = dateUtils.getTodayMinTimestamp();
        long endTo = dateUtils.getTodayMidnightTimestamp();
        List<OrderDetail> list = OrderDetail.find.query().where()
                .ge("status", Order.ORDER_STATUS_UNPAY)
                .le("returnStatus", OrderDetail.STATUS_APPLY_RETURN)
                .eq("productSkuId", productSku.id)
                .eq("uid", member.id)
                .ge("createTime", beginFrom)
                .lt("createTime", endTo)
                .findList();
        long totalAmount = list.parallelStream().mapToLong((each) -> each.number).sum();
        long leftAmount = productSku.limitAmount - totalAmount;
        return leftAmount;
    }

    /**
     * @api {POST}  /v1/p/shopping_cart/ 10从购物车移除商品
     * @apiName removeFromCart
     * @apiGroup Product
     * @apiParam {long} id 购物车记录Id，以逗号隔开
     * @apiParam {String} operation 操作,"del"为删除
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Error 40001){int} code 40001 参数错误
     * @apiSuccess (Error 40002){int} code 40002 该商品不在购物车中
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    @Transactional
    public CompletionStage<Result> removeFromCart(Http.Request request) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            JsonNode jsonNode = request.body().asJson();
            String operation = jsonNode.findPath("operation").asText();
            if (ValidationUtil.isEmpty(operation) || !operation.equals("del")) return okCustomJson(CODE40001, "参数错误");
            if (null == member) return unauth403();
            String cartIdStr = jsonNode.findPath("id").asText();
            if (ValidationUtil.isEmpty(cartIdStr)) return okCustomJson(CODE40001, "请输入购物车ID");
            Object[] cartIdArray = cartIdStr.split(",");
            if (null == cartIdArray || cartIdArray.length < 1) return okCustomJson(CODE40001, "请输入购物车ID");
            List<ShoppingCart> shoppingCartList = ShoppingCart.find.query().where()
                    .eq("uid", member.id)
                    .idIn(cartIdArray).findList();
            if (shoppingCartList.size() > 0) {
                Ebean.deleteAll(shoppingCartList);
            }
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v1/p/shopping_cart/:shoppingCartId/ 11修改购物车中的商品数量
     * @apiName changeProductAmountAtCart
     * @apiGroup Product
     * @apiParam {long} amount 变更的数量
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Error 40001){int} code 40001 参数错误
     * @apiSuccess (Error 40002){int} code 40002 只能确认自己的订单
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    @Transactional
    public CompletionStage<Result> changeProductAmountAtCart(Http.Request request, long shoppingCartId) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            JsonNode jsonNode = request.body().asJson();
            if (null == member) return unauth403();
            if (shoppingCartId < 1) return okCustomJson(CODE40001, "参数错误");
            long amount = jsonNode.findPath("amount").asLong();
            ShoppingCart shoppingCart = ShoppingCart.find.query().where()
                    .eq("id", shoppingCartId)
                    .eq("uid", member.id)
                    .setMaxRows(1).findOne();
            if (null == shoppingCart) return okCustomJson(CODE40002, "该商品不在购物车中");
            ObjectNode resultNode = Json.newObject();
            resultNode.put(CODE, CODE200);
            if (amount < 1) {
                shoppingCart.delete();
            } else {
                ProductSku sku = ProductSku.find.byId(shoppingCart.skuId);
                if (null == sku) return okCustomJson(CODE40002, "该商品已下架");
                Product product = Product.find.query().where()
                        .eq("id", sku.productId)
                        .ge("status", Product.STATUS_ON_SHELVE)
                        .setMaxRows(1).findOne();
                if (null == product) return okCustomJson(CODE40001, "该产品不存在或已下架");
                long leftAmount = amount;
                boolean exceedStock = false;
                businessUtils.increaseProductViews(sku.productId, HOT_VIEW_SHOPPING_CART);
                if (amount > sku.stock) {
                    leftAmount = sku.stock;
                    exceedStock = true;
                }
                shoppingCart.setAmount(leftAmount);
                if (sku.limitAmount > 0) {
                    leftAmount = checkUptoMaxAmount(sku, member);
                }
                if (leftAmount < 0) return okCustomJson(CODE40002, "下单数量超出限购数量");
                boolean exceedLimitAmount = false;
                if (leftAmount < amount) {
                    exceedLimitAmount = true;
                }
                if (leftAmount > amount) leftAmount = amount;
                shoppingCart.setAmount(leftAmount);
                shoppingCart.setUpdateTime(dateUtils.getCurrentTimeBySecond());
                shoppingCart.save();
                if (exceedStock) return okCustomJson(CODE40002, "超出库存数量");
                if (exceedLimitAmount) return okCustomJson(CODE40002, "下单数量超出限购数量");
                resultNode.put("leftAmount", leftAmount);
            }
            return ok(resultNode);
        });
    }

    /**
     * @api {POST} /v1/p/products/by_category/ 12列出分类的商品列表
     * @apiName listProductsByCategoryId
     * @apiGroup Product
     * @apiParam {int} [page] 分页
     * @apiParam {int} [categoryId] 分类ID
     * @apiParam {int} [mixOrder] 综合排序 0不使用综合 1使用综合排序 三个排序中默认使用综合排序
     * @apiParam {int} [orderByPrice] 按价格排序 0不按价格排序 1价格升序 2价格降序
     * @apiParam {int} [orderBySoldAmount] 按销量排序 0不按销量排序 1销量升序 2销量降序
     * @apiParam {Array} [param] 筛选参数数组
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面大图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    public CompletionStage<Result> listProductsByCategoryId(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode requestNode = request.body().asJson();
            long categoryId = requestNode.findPath("categoryId").asLong();
            int page = requestNode.findPath("page").asInt();
//            int mixOrder = requestNode.findPath("mixOrder").asInt();
//            int orderByPrice = requestNode.findPath("orderByPrice").asInt();
//            int orderBySoldAmount = requestNode.findPath("orderBySoldAmount").asInt();
//            ArrayNode paramNode = Json.newArray();
            //第一页从缓存读取
            if (categoryId < 1) return okCustomJson(CODE40001, "该分类不存在");
            String jsonCacheKey = cacheUtils.getProductsByCategoryFromCache(categoryId, page);
            Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (!ValidationUtil.isEmpty(node)) return ok(node);
            }
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);

            List<Product> list;
            LocalDateTime today = LocalDateTime.now();
            long currentTime = Timestamp.valueOf(today).getTime() / 1000;
            int hour = today.getHour();
            ExpressionList<Product> expressionList = businessUtils.autoGetProductsExpressionList();
            PagedList<Product> pagedList = expressionList
                    .icontains("categoryId", categoryId + "")
                    .orderBy().desc("sort")
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * 10)
                    .setMaxRows(10)
                    .findPagedList();
            boolean hasNext = pagedList.hasNext();
            result.put("hasNext", hasNext);
            list = pagedList.getList();
            list.parallelStream().forEach((each) -> businessUtils.autoSetProduct(currentTime, hour, each, HOT_VIEW_LIST));
            result.set("list", Json.toJson(list));
            List<CategoryClassify> classifyList = CategoryClassify.find.query().where()
                    .eq("categoryId", categoryId)
                    .orderBy().desc("sort")
                    .findList();
            result.set("classifyList", Json.toJson(classifyList));
            cache.set(jsonCacheKey, Json.stringify(result), 60);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/mail_fee/?totalFee=&provinceCode=&totalWeight 13查询免邮条件
     * @apiName getMailFee
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} fee 邮费
     * @apiSuccess (Success 200) {int} freeMail 包邮条件
     */
    public CompletionStage<Result> getMailFee(double totalFee, String provinceCode, double totalWeight) {
        return CompletableFuture.supplyAsync(() -> {
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("fee", businessUtils.calcMailFee(totalFee, provinceCode, totalWeight));
            String globalMailFeeKey = cacheUtils.getParamConfigCacheKey() + MAIL_FEE_KEY_FREE_MAIL_FEE_UP_TO;
            Optional<ParamConfig> optional = cache.getOptional(globalMailFeeKey);
            if (optional.isPresent()) {
                ParamConfig freeMailFeeWhenUpto = optional.get();
                if (null != freeMailFeeWhenUpto && !ValidationUtil.isEmpty(freeMailFeeWhenUpto.value)) {
                    int upTo = Integer.parseInt(freeMailFeeWhenUpto.value);
                    result.put("freeMail", upTo);
                }
            }
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/region_json/ 14查询地区json格式
     * @apiName getRegionJson
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} fee 邮费
     * @apiSuccess (Success 200) {int} freeMail 包邮条件
     */
    public CompletionStage<Result> getRegionJson() {
        return CompletableFuture.supplyAsync(() -> {
            Optional<String> cacheOptional = cache.getOptional(REGION_JSON_KEY);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (null != node) return ok(Json.parse(node));
            }
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            List<Region> provinceList = Region.find.query().where()
                    .eq("parentId", 1).orderBy().asc("id").findList();
            ArrayNode root = Json.newArray();
            provinceList.forEach((region) -> {
                ObjectNode provinceNode = Json.newObject();
                provinceNode.put("value", region.regionCode);
                provinceNode.put("text", region.regionName);
                ArrayNode mallCityListNodes = Json.newArray();
                List<Region> mallCityList = Region.find.query().where()
                        .eq("parentId", region.id).orderBy().asc("id").findList();
                mallCityList.forEach((city) -> {
                    ObjectNode cityNode = Json.newObject();
                    cityNode.put("value", city.regionCode);
                    cityNode.put("text", city.regionName);
                    ArrayNode mallAreaListNodes = Json.newArray();
                    List<Region> mallAreaList = Region.find.query().where()
                            .eq("parentId", city.id).orderBy().asc("id").findList();
                    mallAreaList.forEach((area) -> {
                        ObjectNode areaNode = Json.newObject();
                        areaNode.put("value", area.regionCode);
                        areaNode.put("text", area.regionName);
                        mallAreaListNodes.add(areaNode);
                    });
                    cityNode.set("children", mallAreaListNodes);
                    mallCityListNodes.add(cityNode);
                });
                provinceNode.set("children", mallCityListNodes);
                root.add(provinceNode);
            });
            result.set("cityData", root);
            cache.set(REGION_JSON_KEY, result.toString());
            return ok(result);
        });
    }


    /**
     * @api {GET} /v1/p/comment_list/?productId=&page=&hasAppendComment 15评论列表
     * @apiName listComment
     * @apiGroup Product
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){int} pages 页数
     * @apiSuccess (Success 200){JsonArray} list 列表
     * @apiSuccess (Success 200) {long} id 评论id
     * @apiSuccess (Success 200) {long} ProductId 商品id
     * @apiSuccess (Success 200) {long} replyId 针对评论的Id
     * @apiSuccess (Success 200) {Array}  imgList 图片列表
     * @apiSuccess (Success 200) {String} content 评论内容
     * @apiSuccess (Success 200) {int} score 1－10，10为五星，1为半星
     * @apiSuccess (Error 40001){int} code 40001 参数错误
     * @apiSuccess (Error 40002){int} code 40002 找不到该订单
     */
    public CompletionStage<Result> listComment(long productId, int page, int hasAppendComment) {
        return CompletableFuture.supplyAsync(() -> {
            ExpressionList<Comment> expressionList = Comment.find.query().where()
                    .eq("productId", productId)
                    .eq("type", Comment.TYPE_NORMAL);
            if (hasAppendComment > 0) expressionList.eq("hasAppend", true);
            PagedList<Comment> pagedList = expressionList
                    .orderBy().asc("type")
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * PAGE_SIZE_20)
                    .setMaxRows(PAGE_SIZE_20)
                    .findPagedList();
            List<Comment> list = pagedList.getList();
            ArrayNode nodes = Json.newArray();
            list.forEach((comment) -> {
                ObjectNode node = getCommentNode(comment);
                nodes.add(node);
            });
            int pages = pagedList.getTotalPageCount();
            ObjectNode resultNode = Json.newObject();
            resultNode.put(CODE, CODE200);
            resultNode.put("pages", pages);
            resultNode.set("list", nodes);
            return ok(resultNode);
        });
    }

    @NotNull
    public ObjectNode getCommentNode(Comment comment) {
        List<CommentImage> commentImageList = CommentImage.find.query().where()
                .eq("commentId", comment.id).findList();
        comment.imgList.addAll(commentImageList);
        ObjectNode node = (ObjectNode) Json.toJson(comment);
        Member member = Member.find.byId(comment.uid);
        if (null != member) {
            node.put("avatar", member.avatar);
        }
        String key = cacheUtils.getProductJsonCacheKey(comment.productId);
        Optional<Product> optionalProduct = cache.getOptional(key);
        Product product;
        if (optionalProduct.isPresent()) {
            product = optionalProduct.get();
        } else {
            product = Product.find.byId(comment.productId);
        }
        if (null != product) {
            node.put("ProductName", product.name);
            node.put("thumbImg", product.coverImgUrl);
        }

        List<Comment> appendList = Comment.find.query().where()
                .eq("uid", comment.uid)
                .eq("orderId", comment.orderId)
                .eq("productId", comment.productId)
                .eq("type", Comment.TYPE_APPEND).findList();
        appendList.forEach((each) -> {
            List<CommentImage> imageList = CommentImage.find.query().where()
                    .eq("commentId", each.id).findList();
            each.imgList.addAll(imageList);
        });
        node.set("appendList", Json.toJson(appendList));
        List<Comment> replyList = Comment.find.query().where()
                .eq("orderId", comment.orderId)
                .eq("productId", comment.productId)
                .eq("type", Comment.TYPE_REPLY).findList();
        node.set("replyList", Json.toJson(replyList));
        return node;
    }

    /**
     * @api {POST} /v1/p/comment/  16发表评论
     * @apiName placeComment
     * @apiGroup Product
     * @apiParam {long} orderId 订单ID
     * @apiParam {JsonArray} list 参数列表
     * @apiParam {long} productId 商品id
     * @apiParam {long} skuId skuId
     * @apiParam {String} content 评论内容
     * @apiParam {Array} imgList 图片列表
     * @apiParam {int} type 1为普通评论，3为追加
     * @apiParam {int} score 评分1-10分
     * @apiParam {long} [replyId] 初评的ID
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Error 40001){int} code 40001 参数错误
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    @Transactional
    public CompletionStage<Result> placeComment(Http.Request request) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            JsonNode requestNode = request.body().asJson();
            if (null == requestNode) return okCustomJson(CODE40001, "参数错误");
            if (null == member) return unauth403();
            long currentTime = dateUtils.getCurrentTimeBySecond();
            long orderId = requestNode.findPath("orderId").asLong();
            if (orderId < 1) return okCustomJson(CODE40001, "订单ID有误");
            Order order = Order.find.query().where()
                    .eq("uid", member.id)
                    .eq("id", orderId)
                    .ge("status", Order.ORDER_STATUS_TAKEN)
                    .setMaxRows(1).findOne();
            if (null == order) return okCustomJson(CODE40003, "需要购买商品后才能评论");
            ArrayNode paramList = (ArrayNode) requestNode.findPath("list");
            if (null == paramList || paramList.size() < 1) return okCustomJson(CODE40001, "请选择评论");
            List<Comment> commentList = new ArrayList<>();
            String nickName = businessUtils.hidepartialChar(member.nickName);
            List<Comment> comments = Comment.find.query().where()
                    .eq("uid", member.id)
                    .eq("orderId", orderId)
                    .eq("type", Comment.TYPE_NORMAL)
                    .findList();
            if (comments.size() > 0) if (null == order) return okCustomJson(CODE40003, "您已评论");
            for (JsonNode node : paramList) {
                Comment param = Json.fromJson(node, Comment.class);
                if (null == param) return okCustomJson(CODE40001, "参数有误");
                if (param.type != Comment.TYPE_NORMAL && param.type != Comment.TYPE_APPEND)
                    return okCustomJson(CODE40001, "评论类型有误");
                Product product = Product.find.byId(param.productId);
                if (null == product) return okCustomJson(CODE40001, "产品ID有误");
                if (ValidationUtil.isEmpty(param.content)) return okCustomJson(CODE40001, "请输入评论内容");
                ProductSku productSku = ProductSku.find.byId(param.skuId);
                if (null == productSku) return okCustomJson(CODE40001, "sku ID有误");
                if (productSku.productId != product.id) return okCustomJson(CODE40001, "skuID跟产品不匹配");
//                Comment firstComment = Comment.find.query().where()
//                        .eq("uid", member.id)
//                        .eq("productId", product.id)
//                        .orderBy().asc("id")
//                        .setMaxRows(1).findOne();
//                if (null == firstComment) {
//                    param.type = Comment.TYPE_NORMAL;
//                    if (param.score < 1 && param.score > 10) return okCustomJson(CODE40001, "评分1-10分");
//                } else {
//                    param.type = Comment.TYPE_APPEND;
//                }
//                if (param.type == Comment.TYPE_APPEND) {
//                    if (!firstComment.hasAppend) {
//                        firstComment.setHasAppend(true);
//                        firstComment.save();
//                    }
//                    param.setReplyId(firstComment.id);
//                    param.setType(Comment.TYPE_APPEND);
//                }
                param.setProductId(param.productId);
                param.setSkuId(param.skuId);
                param.setUid(member.id);
                param.setSkuName(productSku.name);
                param.setOrderId(param.orderId);
                param.setName(nickName);
                param.setUpdateTime(currentTime);
                commentList.add(param);
            }
            if (commentList.size() > 0) {
                Ebean.saveAll(commentList);
                List<CommentImage> imageList = new ArrayList<>();
                commentList.forEach((each) -> {
                    each.imgList.forEach((commentImage) -> {
                        if (null != commentImage) {
                            commentImage.setCommentId(each.id);
                            imageList.add(commentImage);
                        }
                    });
                });
                if (imageList.size() > 0) Ebean.saveAll(imageList);
                order.setStatus(Order.ORDER_STATUS_COMMENTED);
                order.save();
            }
            return okJSON200();
        });

    }


    /**
     * @api {GET} /v1/p/brands/:brandId/ 17品牌详情
     * @apiName getBrand
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200){long} id 品牌记录id
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} url 官方链接
     * @apiSuccess (Success 200){String} logo 官方logo
     * @apiSuccess (Success 200){String} poster 宣传图
     * @apiSuccess (Success 200){String} content 官方说明
     * @apiSuccess (Success 200){int} sort 排序
     * @apiSuccess (Success 200){int} status 1正常，2暂停使用3下架
     * @apiSuccess (Success 200){String} seoTitle seo标题
     * @apiSuccess (Success 200){String} seoKeywords seo关键字
     * @apiSuccess (Success 200){String} seoDescription seo描述
     * @apiSuccess (Success 200){String} poster 首页宣传图
     */
    public CompletionStage<Result> getBrand(long brandId) {
        return CompletableFuture.supplyAsync(() -> {
            if (brandId < 1) return okCustomJson(CODE40001, "参数错误");
            String key = cacheUtils.getBrandKey(brandId);
            Optional<Brand> optional = cache.getOptional(key);
            if (optional.isPresent()) {
                Brand brand = optional.get();
                if (null != brand) {
                    ObjectNode result = Json.newObject();
                    result.put(CODE, CODE200);
                    result.put("name", brand.name);
                    result.put("logo", brand.logo);
                    result.put("url", brand.url);
                    result.put("poster", brand.poster);
                    return ok(result);
                }
            }
            Brand brand = Brand.find.byId(brandId);
            if (null == brand) return okCustomJson(CODE40002, "该商品不存在");
            ObjectNode result = (ObjectNode) Json.toJson(brand);
            result.put(CODE, CODE200);
            return ok(result);
        });
    }


    /**
     * @api {GET} /v1/p/score_to_use/ 18可使用的积分
     * @apiName calcScoreToUse
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200){long} scoreToMoney 积分可抵用的人民币，以元为单位
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> calcScoreToUse(Http.Request request, double totalAmount) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            if (null == member) return unauth403();
            MemberBalance score = MemberBalance.find.query().where()
                    .eq("uid", member.id)
                    .eq("itemId", BusinessItem.SCORE)
                    .setMaxRows(1).findOne();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            long scoreBalance = 0;
            BigDecimal scoreToMoney = BigDecimal.ZERO;
            if (null != score) {
                scoreBalance = (long) score.leftBalance;
                scoreToMoney = new BigDecimal(score.leftBalance * SCORE_TO_ONE_TENTH / 100.00 + "");
            }
            BigDecimal totalAmountBD = new BigDecimal(totalAmount).setScale(2, BigDecimal.ROUND_DOWN);
            if (totalAmount > 0 && scoreToMoney.compareTo(BigDecimal.ZERO) > 0) {
                if (totalAmountBD.compareTo(scoreToMoney) < 0) {
                    scoreToMoney = totalAmountBD;
                    scoreBalance = (int) (totalAmount * 100.00 / SCORE_TO_ONE_TENTH);
                }
            }
            result.put("score", scoreBalance);
            result.put("scoreToMoney", scoreToMoney);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/products_by_tag/?tag=&page= 19通过标签获取商品列表
     * @apiName listProductsByTag
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200) {Object} product 商品详情
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面大图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    public CompletionStage<Result> listProductsByTag(Http.Request request, final String tag, int page) {
        return CompletableFuture.supplyAsync(() -> {
            if (ValidationUtil.isEmpty(tag)) return okCustomJson(CODE40001, "tag为空");
            String jsonCacheKey = cacheUtils.getProductsByTagJsonCache(tag);
            //第一页需要缓存，从缓存读取
            if (page <= 1) {
                Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
                if (cacheOptional.isPresent()) {
                    String node = cacheOptional.get();
                    if (null != node) return ok(Json.parse(node));
                }
            }
            PagedList<ProductTag> pagedList = ProductTag.find.query().where().eq("tag", tag)
                    .orderBy().asc("id")
                    .setFirstRow((page - 1) * PAGE_SIZE_20)
                    .setMaxRows(PAGE_SIZE_20).findPagedList();
            List<ProductTag> list = pagedList.getList();
            list.parallelStream().forEach((each) -> {
                each.product = businessUtils.getProduct(each.productId);
                if (null != each.product) {
                    ProductSku sku = ProductSku.find.query().where()
                            .eq("productId", each.product.id)
                            .orderBy().asc("sort")
                            .orderBy().asc("id")
                            .setMaxRows(1).findOne();
                    if (null != sku) {
                        each.product.defaultSku = sku;
                    }
                }
            });
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("pages", pagedList.getTotalPageCount());
            result.set("list", Json.toJson(list));
            if (page <= 1) cache.set(jsonCacheKey, result.toString(), 10 * 60);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/special_topics/ 19专题列表
     * @apiName listSpecialTopics
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){String} coverImgUrl 封面图
     * @apiSuccess (Success 200){String} title 标题
     * @apiSuccess (Success 200){String} details 详情
     * @apiSuccess (Success 200){int} productCount 专题商品数
     * @apiSuccess (Success 200){int} status 状态 1草稿 2上线 3下线
     * @apiSuccess (Success 200){long} createTime 创建时间
     * @apiSuccess (Success 200){JsonArray} list_  商品列表
     */
    public CompletionStage<Result> listSpecialTopics(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            String jsonCacheKey = cacheUtils.getSpecialTopicJsonCache();
            Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (null != node) return ok(Json.parse(node));
            }
            ExpressionList<SpecialTopic> expressionList = SpecialTopic.find.query().where()
                    .eq("status", SpecialTopic.STATUS_ENABLE);
            List<SpecialTopic> topics = expressionList.orderBy().desc("id").findList();
            topics.parallelStream().forEach((each) -> {
                List<SpecialTopicProductList> productLists = SpecialTopicProductList.find.query().where()
                        .eq("topicId", each.id)
                        .orderBy().asc("id")
                        .findList();
                productLists.parallelStream().forEach((productList) -> {
                    Product product = businessUtils.getProduct(productList.productId);
                    if (null != product) {
                        ProductSku sku = ProductSku.find.query().where()
                                .eq("productId", product.id)
                                .orderBy().asc("sort")
                                .orderBy().asc("id")
                                .setMaxRows(1).findOne();
                        if (null != sku) {
                            product.defaultSku = sku;
                        }
                        each.productList.add(product);
                    }
                });
            });
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(topics));
            cache.set(jsonCacheKey, result.toString(), 30 * 24 * 3600);
            return ok(result);
        });
    }


    /**
     * @api {GET} /v1/p/brands/ 20品牌列表
     * @apiName listBrands
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){long} id 品牌记录id
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} url 官方链接
     * @apiSuccess (Success 200){String} logo 官方logo
     * @apiSuccess (Success 200){String} poster 宣传图
     * @apiSuccess (Success 200){String} content 官方说明
     * @apiSuccess (Success 200){int} sort 排序
     * @apiSuccess (Success 200){int} status 1正常，2暂停使用3下架
     * @apiSuccess (Success 200){String} seoTitle seo标题
     * @apiSuccess (Success 200){String} seoKeywords seo关键字
     * @apiSuccess (Success 200){String} seoDescription seo描述
     * @apiSuccess (Success 200){String} poster 首页宣传图
     */
    public CompletionStage<Result> listBrands() {
        return CompletableFuture.supplyAsync(() -> {
            String key = cacheUtils.getBrandJsonCache();
            Optional<String> jsonCache = cache.getOptional(key);
            if (jsonCache.isPresent()) {
                String result = jsonCache.get();
                if (!ValidationUtil.isEmpty(result)) return ok(Json.parse(result));
            }
            List<Brand> list = Brand.find.query().where().eq("status", STATUS_NORMAL).findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            cache.set(key, result.toString(), 30 * 24 * 3600);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/flash_sales/ 20抢购列表
     * @apiName listFlashsales
     * @apiGroup Product
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){JsonArray} list 列表
     * @apiSuccess (Success 200) {long} id id
     * @apiSuccess (Success 200) {String} displayTime 展示的时间
     * @apiSuccess (Success 200) {long} beginTime 开始时间
     * @apiSuccess (Success 200) {long} endTime 结束时间
     * @apiSuccess (Success 200) {int} status 状态 1正在进行 2未开始 3已抢购（抢购过了2个小时，但还没结束的） 4已结束
     * @apiSuccess (Success 200){JsonArray} productList 商品列表
     * @apiSuccess (Success 200) {String} headPic 商品封面图
     * @apiSuccess (Success 200) {String} title 标题
     * @apiSuccess (Success 200) {long} price 价格，分为单位
     * @apiSuccess (Success 200) {long} totalCount 价格，分为单位
     * @apiSuccess (Success 200) {long} soldCount 价格，分为单位
     * @apiSuccess (Success 200) {long} beginTime_ 价格，分为单位
     * @apiSuccess (Success 200) {long} endTime_ 价格，分为单位
     * @apiSuccess (Success 200) {long} duration 抢购历时
     * @apiSuccess (Success 200) {long} sort 排序
     */
    public CompletionStage<Result> listFlashsales() {
        String key = cacheUtils.getFlashsaleJsonCache();
        return asyncCacheApi.getOptional(key).thenApplyAsync((jsonCache) -> {
            if (jsonCache.isPresent()) {
                String result = (String) jsonCache.get();
                if (!ValidationUtil.isEmpty(result)) return ok(result);
            }
            long minToday = dateUtils.getTodayMinTimestamp();
            long maxToday = minToday + 24 * 3600;
            List<Product> list = Product.find.query().where()
                    .eq("status", Product.STATUS_ON_SHELVE)
                    .ge("beginTime", minToday)
                    .le("endTime", maxToday)
                    .orderBy().desc("sort")
                    .orderBy().desc("id")
                    .findList();
            LocalDateTime today = LocalDateTime.now();
            int hour = today.getHour();
            long currentTime = dateUtils.getCurrentTimeBySecond();
            list.parallelStream().forEach((each) -> {
                each.setDetails("");
                each.setSketch("");
                each.soldAmount = each.soldAmount + each.virtualAmount;
                businessUtils.autoSetProduct(currentTime, hour, each, HOT_VIEW_LIST);
            });
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            cache.set(key, result.toString(), 24 * 3600);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/special_topics/:id/ 21专题详情
     * @apiName getSpecialTopics
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200){String} coverImgUrl 封面图
     * @apiSuccess (Success 200){String} title 标题
     * @apiSuccess (Success 200){String} details 详情
     * @apiSuccess (Success 200){int} productCount 专题商品数
     * @apiSuccess (Success 200){int} status 状态 1草稿 2上线 3下线
     * @apiSuccess (Success 200){long} createTime 创建时间
     * @apiSuccess (Success 200){JsonArray} list_  商品列表
     */
    public CompletionStage<Result> getSpecialTopics(long id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id < 1) return okCustomJson(CODE40001, "ID有误");
            SpecialTopic specialTopic = SpecialTopic.find.byId(id);
            if (null == specialTopic) return okCustomJson(CODE40001, "该专题不存在");
            if (specialTopic.status != SpecialTopic.STATUS_ENABLE) return okCustomJson(CODE40001, "该专题不存在，或已下架");
            List<SpecialTopicProductList> productLists = SpecialTopicProductList.find.query().where()
                    .eq("topicId", id)
                    .orderBy().asc("id")
                    .findList();
            productLists.parallelStream().forEach((productList) -> {
                Product product = businessUtils.getProduct(productList.productId);
                if (null != product) specialTopic.productList.add(product);
            });
            ObjectNode result = (ObjectNode) Json.toJson(specialTopic);
            result.put(CODE, CODE200);
            return ok(result);
        });
    }


    /**
     * @api {POST} /v1/p/product_search/ 22商品搜索
     * @apiName searchProducts
     * @apiParam {int} [page] 分页
     * @apiParam {String} [filter] 关键字
     * @apiParam {int} [mixOrder] 综合排序 0不使用综合 1使用综合排序 三个排序中默认使用综合排序
     * @apiParam {int} [orderByPrice] 按价格排序 0不按价格排序 1价格升序 2价格降序
     * @apiParam {int} [orderBySoldAmount] 按销量排序 0不按销量排序 1销量升序 2销量降序
     * @apiParam {Array} [param] 筛选参数数组
     * @apiParam {int} searchType 搜索类型 1为产品（默认） 2为品牌
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} resultCount 匹配条数
     * @apiSuccess (Success 200) {JsonArray} brands 品牌列表
     * @apiSuccess (Success 200) {JsonArray} productList 产品列表
     */
    public CompletionStage<Result> searchProducts(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            long uid = 0;
            JsonNode requestNode = request.body().asJson();
            int page = requestNode.findPath("page").asInt();
            int searchType = requestNode.findPath("searchType").asInt();
            String filter = requestNode.findPath("filter").asText();
            String filterTrim = filter.trim().replaceAll("\\s+", " ");
            String filterTrimWithoutSpace = filterTrim.replaceAll(" ", "");
            if (ValidationUtil.isEmpty(filterTrimWithoutSpace))
                return okCustomJson(CODE40001, "请输入查询关键字");
            if (filterTrimWithoutSpace.length() > 20) return okCustomJson(CODE40001, "请减少关键字");
            String jsonCacheKey = cacheUtils.getProductSearchJsonCache(filterTrimWithoutSpace, searchType, page);
            String[] splitResult = filterTrim.split(" ");
//            //第一页需要缓存，从缓存读取
            boolean needCache = (page <= 1);
            if (needCache) {
                Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
                if (cacheOptional.isPresent()) {
                    String node = cacheOptional.get();
                    if (null != node) return ok(Json.parse(node));
                }
            }
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            LocalDateTime today = LocalDateTime.now();
            ExpressionList<Product> expressionList = Product.find.query().where()
                    .eq("status", Product.STATUS_ON_SHELVE);
            if (searchType == 1) {
                Arrays.stream(splitResult).forEach((keyword) -> {
                    if (!ValidationUtil.isEmpty(keyword)) {
                        expressionList.or(Expr.icontains("name", keyword), Expr.icontains("keywords", keyword));
                    }
                });
                PagedList<Product> pagedList = expressionList
                        .orderBy().desc("sort")
                        .orderBy().desc("id")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                        .findPagedList();
                List<Product> list = pagedList.getList();
                long currentTime = Timestamp.valueOf(today).getTime() / 1000;
                int hour = today.getHour();
                list.parallelStream().forEach((each) -> businessUtils.autoSetProduct(currentTime, hour, each, HOT_VIEW_LIST));
                if (list.size() > 0) {
                    Member member = businessUtils.getUserIdByAuthToken(request);
                    if (null != member) uid = member.id;
                    saveSearchKeyword(splitResult, uid);
                    int resultCount = pagedList.getTotalCount();
                    result.put("resultCount", resultCount);
                }
                result.set("list", Json.toJson(list));
                result.put("pages", pagedList.getTotalPageCount());
                boolean hasNext = pagedList.hasNext();
                result.put("hasNext", hasNext);
                if (page <= 1) cache.set(jsonCacheKey, result.toString(), 30);
            } else if (searchType == 2) {
                ExpressionList<Brand> brandExpressionList = Brand.find.query().where().eq("status", STATUS_NORMAL);
                Arrays.stream(splitResult).forEach((keyword) -> brandExpressionList.icontains("name", keyword));
                PagedList<Brand> pagedList = brandExpressionList
                        .orderBy().desc("sort")
                        .orderBy().desc("id")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                        .findPagedList();
                List<Brand> list = pagedList.getList();
                if (list.size() > 0) {
                    saveSearchKeyword(splitResult, uid);
                    int resultCount = pagedList.getTotalCount();
                    result.put("resultCount", resultCount);
                }
                result.set("list", Json.toJson(list));
                result.put("pages", pagedList.getTotalPageCount());
                boolean hasNext = pagedList.hasNext();
                result.put("hasNext", hasNext);
                cache.set(jsonCacheKey, result.toString(), 30);
            }
            return ok(result);
        });
    }

    private void saveSearchKeyword(String[] filter, long uid) {
        CompletableFuture.runAsync(() -> {
            if (filter.length > 0) {
                List<ProductSearch> list = new ArrayList<>();
                List<SearchLog> searchLogList = new ArrayList<>();
                Arrays.stream(filter).parallel().forEach((each) -> {
                    ProductSearch search = ProductSearch.find.query().where().eq("keyword", each)
                            .orderBy().asc("id").setMaxRows(1).findOne();
                    if (null == search) {
                        search = new ProductSearch();
                        search.setKeyword(each);
                        search.setViews(1);
                    } else {
                        search.setViews(search.views + 1);
                    }
                    list.add(search);
                    if (uid > 0) {
                        SearchLog searchLog = SearchLog.find.query().where()
                                .eq("keyword", each)
                                .eq("uid", uid)
                                .orderBy().asc("id")
                                .setMaxRows(1).findOne();
                        if (null == searchLog) {
                            searchLog = new SearchLog();
                            searchLog.setKeyword(each);
                            searchLog.setViews(1);
                            searchLog.setUid(uid);
                        } else {
                            searchLog.setViews(searchLog.views + 1);
                        }
                        searchLogList.add(searchLog);
                    }
                });
                if (list.size() > 0) {
                    Ebean.saveAll(list);
                    String jsonCacheKey = cacheUtils.getProductKeywordsJsonCache();
                    cache.remove(jsonCacheKey);
                }
                if (searchLogList.size() > 0) {
                    Ebean.saveAll(searchLogList);
                }
            }

        });
    }


    /**
     * @api {GET} /v1/p/search_keywords/ 23商品搜索关键字
     * @apiName searchKeywords
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {JsonArray} list 关键字列表
     * @apiSuccess (Success 200) {String} keyword 关键字
     * @apiSuccess (Success 200) {JsonArray} searchLogList 历史搜索（自己的）
     * @apiSuccess (Success 200) {String} keyword_ 关键字
     */
    public CompletionStage<Result> searchKeywords(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            String jsonCacheKey = cacheUtils.getProductKeywordsJsonCache();
            Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (null != node) {
                    ObjectNode result = (ObjectNode) Json.parse(node);
                    List<SearchLog> searchLogList = listSearchLog(request);
                    if (null != searchLogList) {
                        result.set("searchLogList", Json.toJson(searchLogList));
                    }
                    return ok(result);
                }
            }
            List<ProductSearch> list = ProductSearch.find.query().orderBy()
                    .desc("views").setMaxRows(30).findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            cache.set(jsonCacheKey, result.toString());
            List<SearchLog> searchLogList = listSearchLog(request);
            if (null != searchLogList) {
                result.set("searchLogList", Json.toJson(searchLogList));
            }
            return ok(result);
        });
    }

    public List<SearchLog> listSearchLog(Http.Request request) {
        Member member = businessUtils.getUserIdByAuthToken(request);
        if (null != member) {
            List<SearchLog> list = SearchLog.find.query().where()
                    .eq("uid", member.id)
                    .orderBy().desc("views")
                    .setMaxRows(100).findList();
            return list;
        }
        return new ArrayList<>();
    }

    /**
     * @api {GET} /v1/p/product_tabs/ 24商品TAB列表
     * @apiName listProductTabs
     * @apiGroup Product
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){JsonArray} list 列表
     * @apiSuccess (Success 200) {long} id id
     * @apiSuccess (Success 200) {string} classifyCode 归集编号
     * @apiSuccess (Success 200) {int} sort 排序值
     */
    public CompletionStage<Result> listProductTabs() {
        String jsonCacheKey = cacheUtils.getProductTabJsonCache();
        return asyncCacheApi.getOptional(jsonCacheKey).thenApplyAsync((cacheOptional) -> {
            if (cacheOptional.isPresent()) {
                String node = (String) cacheOptional.get();
                if (null != node) return ok(node);
            }
            List<ProductTab> list = ProductTab.find.query().where()
                    .eq("enable", true)
                    .orderBy().desc("sort")
                    .orderBy().desc("id")
                    .findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            cache.set(jsonCacheKey, result.toString(), 2 * 60);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/product_tabs/:id/?page 25商品TAB详情
     * @apiName getProductTab
     * @apiGroup Product
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {JsonArray} tabClassifyList 归集列表
     * @apiSuccess (Success 200){JsonArray} productList  商品列表
     */
    public CompletionStage<Result> getProductTab(Http.Request request, long id, int page) {
        String key = cacheUtils.getProductTabDetail(id, page);
        return asyncCacheApi.getOptional(key).thenApplyAsync((jsonCache) -> {
            if (jsonCache.isPresent()) {
                String result = (String) jsonCache.get();
                if (!ValidationUtil.isEmpty(result)) {
                    businessUtils.updateHotView(result, HOT_VIEW_LIST);
                    return ok(result);
                }
            }
            ProductTab productTab = ProductTab.find.byId(id);
            if (null == productTab) return okCustomJson(CODE40001, "该TAB不存在");
            LocalDateTime now = LocalDateTime.now();
            long currentTime = Timestamp.valueOf(now).getTime() / 1000;
            PagedList<ProductTabProducts> pagedList = ProductTabProducts.find.query().where()
                    .eq("productTabId", id)
                    .orderBy().desc("sort")
                    .orderBy().asc("id")
                    .setFirstRow((page - 1) * PAGE_SIZE_10)
                    .setMaxRows(PAGE_SIZE_10)
                    .findPagedList();
            List<ProductTabProducts> list = pagedList.getList();
            ArrayNode idNodes = Json.newArray();
            list.forEach((each) -> {
                Product product = businessUtils.getProduct(each.productId);
                if (null != product) {
                    businessUtils.autoSetProduct(currentTime, now.getHour(), product, HOT_VIEW_LIST);
                    productTab.productList.add(product);
                    ObjectNode node = Json.newObject();
                    node.put("id", product.id);
                    idNodes.add(node);
                }
            });
            ObjectNode tabResult = (ObjectNode) Json.toJson(productTab);
            tabResult.put(CODE, CODE200);
            tabResult.set("list", idNodes);
            tabResult.put("hasNext", pagedList.hasNext());
            asyncCacheApi.set(key, Json.stringify(tabResult), 30 * 60);
            return ok(tabResult);
        });
    }

    /**
     * @api {GET} /v1/p/products_by_classify_id/?classifyId=&page= 26根据归集ID列出商品列表
     * @apiName listProductsByClassifyId
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){long} id 商品id
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    public CompletionStage<Result> listProductsByClassifyId(Http.Request request, long classifyId, int page) {
        String key = cacheUtils.getClassifyJsonCache(classifyId, page);
        return asyncCacheApi.getOptional(key).thenApplyAsync((jsonCache) -> {
            if (jsonCache.isPresent()) {
                String node = (String) jsonCache.get();
                if (!ValidationUtil.isEmpty(node)) return ok(node);
            }
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            ProductClassify classify = ProductClassify.find.byId(classifyId);
            if (null == classify) return okCustomJson(CODE40001, "该归集不存在");
            List<Long> productList = ProductClassifyDetails.find.query()
                    .select("productId")
                    .where().eq("classifyId", classify.id)
                    .orderBy().desc("sort")
                    .orderBy().desc("id")
                    .findSingleAttributeList();
            LocalDateTime today = LocalDateTime.now();
            long currentTime = Timestamp.valueOf(today).getTime() / 1000;
            int hour = today.getHour();
            List<Product> list = new ArrayList<>();
            ExpressionList<Product> expressionList = businessUtils.autoGetProductsExpressionList();
            if (productList.size() > 0) {
                PagedList<Product> pagedList = expressionList
                        .idIn(productList)
                        .orderBy().desc("sort")
                        .orderBy().desc("id")
                        .setFirstRow((page - 1) * PAGE_SIZE_20)
                        .setMaxRows(PAGE_SIZE_20)
                        .findPagedList();
                list = pagedList.getList();
                boolean hasNext = pagedList.hasNext();
                result.put("hasNext", hasNext);
                result.put("pages", pagedList.getTotalPageCount());
                list.parallelStream().forEach((each) -> businessUtils.autoSetProduct(currentTime, hour, each, HOT_VIEW_LIST));
            }

            result.set("classify", Json.toJson(classify));
            List<ProductClassify> subClassifyList = new ArrayList<>();
            if (classify.isParent) {
                List<ProductClassify> classifyList = ProductClassify.find.query().where()
                        .eq("parentId", classifyId)
                        .orderBy().desc("sort")
                        .findList();
                subClassifyList.addAll(classifyList);
            }
            result.set("subClassifyList", Json.toJson(subClassifyList));
            result.set("list", Json.toJson(list));
            cache.set(key, Json.stringify(result), 30 * 60);
            return ok(result);
        });

    }

    private boolean checkClassifyAuth(Member member, ProductClassify classify) {
        if (classify.activityType == Product.ACTIVITY_TYPE_WHOLESALE) {
            if (null == member) return true;
            MemberProfile memberProfile = MemberProfile.find.query().where()
                    .eq("uid", member.id)
                    .orderBy().asc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null == memberProfile) return true;
            if (!memberProfile.isPickupPlace) return true;
        }
        return false;
    }


    /**
     * @api {post} /v1/p/fav/ 27收藏/取消收藏
     * @apiName fav
     * @apiGroup Product
     * @apiParam {boolean} enable true收藏 false取消收藏
     * @apiParam {long} productId  商品ID
     * @apiParam {long} skuId  skuID
     * @apiSuccess (Success 200) {int} code 200成功修改
     */
    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> fav(Http.Request request) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((memberInCache) -> {
            JsonNode jsonNode = request.body().asJson();
            if (null == memberInCache) return unauth403();
            try {
                if (!businessUtils.setLock(String.valueOf(memberInCache.id), OPERATION_FAV_PRODUCT))
                    return okCustomJson(CODE40004, "正在处理中,请稍等");
                Member member = Member.find.byId(memberInCache.id);
                if (null == member) return unauth403();
                long productId = jsonNode.findPath("productId").asLong();
                long skuId = jsonNode.findPath("skuId").asLong();
                boolean enable = jsonNode.findPath("enable").asBoolean();
                if (productId < 1) {
                    return okCustomJson(CODE40001, "商品不存在");
                }
                if (skuId < 1) {
                    return okCustomJson(CODE40001, "商品SKU不存在");
                }
                Product product = businessUtils.getProduct(productId);
                if (null == product) {
                    return okCustomJson(CODE40001, "商品不存在");
                }
                ProductSku productSku = ProductSku.find.query().where()
                        .eq("productId", productId)
                        .eq("id", skuId)
                        .setMaxRows(1).findOne();
                if (null == productSku) {
                    return okCustomJson(CODE40001, "商品SKU不存在");
                }

                ProductFav productFav = ProductFav.find.query().where()
                        .eq("uid", memberInCache.id)
                        .eq("productId", productId)
                        .setMaxRows(1).findOne();
                long currentTime = dateUtils.getCurrentTimeBySecond();
                if (null == productFav) {
                    productFav = new ProductFav();
                    productFav.setProductId(productId);
                    productFav.setSkuId(skuId);
                    productFav.setUid(memberInCache.id);
                    productFav.setEnable(enable);
                    productFav.setCreateTime(currentTime);
                } else {
                    productFav.setEnable(enable);
                }
                productFav.save();
                long favsCount = member.favsCount;
                if (enable) {
                    favsCount = favsCount + 1;
                } else favsCount = favsCount - 1;
                if (favsCount < 1) favsCount = 0;
                member.setFavsCount(favsCount);
                member.save();
                return okJSON200();
            } catch (Exception e) {
                logger.error("fav:" + e.getMessage());
                return okCustomJson(CODE500, "收藏发生异常，请稍后再试");
            } finally {
                businessUtils.unLock(String.valueOf(memberInCache.id), OPERATION_FAV_PRODUCT);
            }
        });
    }

    /**
     * @api {GET} /v1/p/products_by_coupon/?configId= 28优惠券商品列表
     * @apiName listProductsByCouponId
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面大图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    public CompletionStage<Result> listProductsByCouponId(long configId) {
        String key = cacheUtils.getProductsByCoupon(configId);
        return asyncCacheApi.getOptional(key).thenApplyAsync((jsonCache) -> {
            if (jsonCache.isPresent()) {
                String node = (String) jsonCache.get();
                if (!ValidationUtil.isEmpty(node)) return ok(node);
            }
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            List<Product> list = new ArrayList<>();
            CouponConfig couponConfig = CouponConfig.find.byId(configId);
            if (null == couponConfig || couponConfig.type == CouponConfig.TYPE_BAR_USE) {

            } else {
                if (couponConfig.idType == ID_TYPE_SPECIFIED_ID) {
                    String productIds = couponConfig.merchantIds;
                    String[] productIdArray = productIds.split("/");
                    Arrays.stream(productIdArray).parallel().forEach((each) -> {
                        if (!ValidationUtil.isEmpty(each)) {
                            list.add(businessUtils.getProduct(Long.parseLong(each)));
                        }
                    });
                    String brandIds = couponConfig.brandIds;
                    String[] productInBrandId = brandIds.split("/");
                    Arrays.stream(productInBrandId).parallel().forEach((each) -> {
                        if (!ValidationUtil.isEmpty(each)) {
                            List<Product> products = Product.find.query().where()
                                    .eq("status", Product.STATUS_ON_SHELVE)
                                    .eq("brandId", each)
                                    .findList();
                            list.addAll(products);
                        }
                    });
                }
            }
            result.set("list", Json.toJson(list));
            cache.set(key, result.toString(), 20);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/coupon_list/?page= 29优惠券列表
     * @apiName couponList
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){String} couponTitle 标题
     * @apiSuccess (Success 200){String} couponContent 内容
     * @apiSuccess (Success 200){int} amount 面值，以分为单位
     * @apiSuccess (Success 200){int} claimLimitPerMember 每人限领张数
     * @apiSuccess (Success 200){int} totalAmount 总数
     * @apiSuccess (Success 200){int} claimAmount 已认领数量
     * @apiSuccess (Success 200){String} ruleContent 规则
     * @apiSuccess (Success 200){String} imgUrl 图片地址
     * @apiSuccess (Success 200){long} beginTime 起始时间
     * @apiSuccess (Success 200){long} endTime 失效时间
     * @apiSuccess (Success 200){int} expireDays 有效天数
     * @apiSuccess (Success 200){int} oldPrice 原价，以分为单位
     * @apiSuccess (Success 200){int} currentPrice 现价，以分为单位
     * @apiSuccess (Success 200){long} updateTime 时间
     */
    public CompletionStage<Result> couponList(Http.Request request, int page, long shopId) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((memberInCache) -> {
            ExpressionList<CouponConfig> expressionList = CouponConfig.find.query().where();
            if (shopId > 0) expressionList.icontains("shopIds", "/" + shopId + "/");
            PagedList<CouponConfig> pagedList = expressionList
                    .eq("type", TYPE_ALL_CAN_USE)
                    .eq("status", STATUS_ENABLE)
                    .eq("needShow", true)
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * PAGE_SIZE_10)
                    .setMaxRows(PAGE_SIZE_10)
                    .findPagedList();
            List<CouponConfig> list = pagedList.getList();
            if (null != memberInCache) {
                list.parallelStream().forEach((each) -> {
                    if (each.claimLimitPerMember > 0) {
                        List<MemberCoupon> coupons = MemberCoupon.find.query().where()
                                .eq("uid", memberInCache.id)
                                .eq("couponId", each.id)
                                .findList();
                        if (coupons.size() < each.claimLimitPerMember) each.available = true;
                    }
                    String title = "";
                    if (!ValidationUtil.isEmpty(each.brandIds)) {
                        String[] brandArray = each.brandIds.split("/");
                        if (brandArray.length == 2) {
                            String brandId = brandArray[1];
                            if (!ValidationUtil.isEmpty(brandId)) {
                                Brand brand = businessUtils.getBrand(Long.parseLong(brandId));
                                if (null != brand) {
                                    title = brand.name;
                                }
                            }
                        }
                    }
                    if (ValidationUtil.isEmpty(title)) {
                        String[] productArray = each.merchantIds.split("/");
                        if (productArray.length == 2) {
                            String productId = productArray[1];
                            if (!ValidationUtil.isEmpty(productId)) {
                                Product product = businessUtils.getProduct(Long.parseLong(productId));
                                if (null != product) {
                                    title = product.name;
                                }
                            }
                        }
                    }
                    each.title = title;
                });
            }
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            result.put("pages", pagedList.getTotalPageCount());
            return ok(result);
        });
    }


    /**
     * @api {GET} /v1/p/default_recommend_products/?page= 30默认推荐商品列表
     * @apiName listDefaultRecommendProducts
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面大图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    public CompletionStage<Result> listDefaultRecommendProducts(Http.Request request, int page) {
        return CompletableFuture.supplyAsync(() -> {
            PagedList<ProductDefaultRecommend> pagedList = ProductDefaultRecommend.find.query().where()
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * PAGE_SIZE_10)
                    .setMaxRows(PAGE_SIZE_10)
                    .findPagedList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            List<ProductDefaultRecommend> list = pagedList.getList();
            list.parallelStream().forEach((each) -> {
                Product product = businessUtils.getProduct(each.productId);
                if (null != product) {
                    setProductDetail(product);
                    each.product = product;
                }
            });
            result.set("list", Json.toJson(list));
            result.put("pages", pagedList.getTotalPageCount());
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/more_products/?productId= 31更多推广的商品列表
     * @apiName listMoreProducts
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面大图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    public CompletionStage<Result> listMoreProducts(Http.Request request, long productId) {
        return CompletableFuture.supplyAsync(() -> {
            List<Product> list = Product.find.query().where()
                    .ne("id", productId)
                    .setMaxRows(100)
                    .findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            List<Product> resultList = new ArrayList<>();
            list.parallelStream().filter((each) -> null != each).limit(10).forEach((each) -> {
                if (null != each) {
                    setProductDetail(each);
                    resultList.add(each);
                }
            });
            result.set("list", Json.toJson(resultList));
            return ok(result);
        });
    }

    /**
     * @api {post} /v1/p/batch_del_fav/ 32批量取消收藏
     * @apiName batchDeleteFav
     * @apiGroup Product
     * @apiParam {Array} list 收藏ID数组
     * @apiSuccess (Success 200) {int} code 200成功修改
     */
    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> batchDeleteFav(Http.Request request) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((memberInCache) -> {
            JsonNode jsonNode = request.body().asJson();
            if (null == memberInCache) return unauth403();
            try {
                if (!businessUtils.setLock(String.valueOf(memberInCache.id), OPERATION_FAV_PRODUCT))
                    return okCustomJson(CODE40004, "正在处理中,请稍等");
                Member member = Member.find.byId(memberInCache.id);
                if (null == member) return unauth403();
                ArrayNode list = (ArrayNode) jsonNode.findPath("list");
                if (null != list && list.size() > 0) {
                    List<Long> favIds = new ArrayList<>();
                    list.forEach((node) -> favIds.add(node.asLong()));
                    List<ProductFav> favList = ProductFav.find.query().where()
                            .eq("uid", memberInCache.id)
                            .in("id", favIds)
                            .findList();
                    List<ProductFav> updateList = new ArrayList<>();
                    favList.parallelStream().forEach((each) -> {
                        each.setEnable(false);
                        updateList.add(each);
                    });
                    Ebean.saveAll(updateList);
                    long favsCount = member.favsCount - favList.size();
                    if (favsCount < 1) favsCount = 0;
                    member.setFavsCount(favsCount);
                    member.save();
                }
                businessUtils.unLock(String.valueOf(memberInCache.id), OPERATION_FAV_PRODUCT);
                return okJSON200();
            } catch (Exception e) {
                logger.error("batchDeleteFav:" + e.getMessage());
                businessUtils.unLock(String.valueOf(memberInCache.id), OPERATION_FAV_PRODUCT);
                return okCustomJson(CODE500, "批量删除收藏发生异常，请稍后再试");
            } finally {
                businessUtils.unLock(String.valueOf(memberInCache.id), OPERATION_FAV_PRODUCT);
            }
        });
    }

    /**
     * @api {POST}  /v1/p/shopping_cart_item/ 33勾选/去掉勾选购物车
     * @apiName enableCartItem
     * @apiGroup Product
     * @apiParam {long} id 购物车记录Id
     * @apiParam {boolean} enable true勾选 false去除勾选
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Error 40001){int} code 40001 参数错误
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    @Transactional
    public CompletionStage<Result> enableCartItem(Http.Request request) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            JsonNode jsonNode = request.body().asJson();
            if (null == member) return unauth403();
            String cartId = jsonNode.findPath("id").asText();
//            if (ValidationUtil.isEmpty(cartId)) return okCustomJson(CODE40001, "参数有误");
            if (!ValidationUtil.isEmpty(cartId)) {
                boolean enable = jsonNode.findPath("enable").asBoolean();
                String[] cartIdArray = cartId.split(",");
                List<ShoppingCart> list = new ArrayList<>();
                Arrays.stream(cartIdArray).parallel().forEach((cartIdStr) -> {
                    if (!ValidationUtil.isEmpty(cartIdStr)) {
                        long eachCartId = Long.parseLong(cartIdStr);
                        ShoppingCart shoppingCart = ShoppingCart.find.query().where()
                                .eq("uid", member.id)
                                .eq("id", eachCartId)
                                .setMaxRows(1)
                                .findOne();
                        if (null != shoppingCart) {
                            shoppingCart.setEnable(enable);
                            list.add(shoppingCart);
                        }
                    }
                });
                if (list.size() > 0) Ebean.saveAll(list);
            }
            return okJSON200();
        });
    }


    /**
     * @api {GET} /v1/p/browse_logs/ 34浏览记录
     * @apiName listBrowseLogs
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {JsonArray} list 关键字列表
     * @apiSuccess (Success 200) {Object} product 商品对象
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> listBrowseLogs(Http.Request request, int page) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            if (null == member) return unauth403();
            PagedList<BrowseLog> pagedList = BrowseLog.find.query()
                    .where().eq("uid", member.id)
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * PAGE_SIZE_20)
                    .setMaxRows(PAGE_SIZE_20)
                    .findPagedList();
            List<BrowseLog> list = pagedList.getList();
            list.parallelStream().forEach((each) ->
                    {
                        each.product = businessUtils.getProduct(each.getProductId());
                        if (null != each.product) setProductDetail(each.product);
                    }
            );
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("pages", pagedList.getTotalPageCount());
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }

    /**
     * @api {POST} /v1/p/out_stock_reg/ 35缺货统计
     * @apiName outStockReg
     * @apiGroup Product
     * @apiParam {long} skuId skuId
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> outStockReg(Http.Request request) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            if (null == member) return unauth403();
            JsonNode jsonNode = request.body().asJson();
            long skuId = jsonNode.findPath("skuId").asLong();
            if (skuId < 1) return okCustomJson(CODE40001, "skuId有误");
            ProductSku productSku = ProductSku.find.byId(skuId);
            if (null == productSku) return okCustomJson(CODE40001, "该sku不存在");
            OutStockReg reg = OutStockReg.find.query().where().eq("uid", member.id)
                    .eq("skuId", skuId)
                    .setMaxRows(1)
                    .findOne();
            if (null == reg) {
                reg = new OutStockReg();
                reg.setUid(member.id);
                reg.setProductId(productSku.productId);
                reg.setSkuId(skuId);
                reg.setCreateTime(dateUtils.getCurrentTimeBySecond());
                reg.save();
            }
            return okJSON200();
        });
    }

    /**
     * @api {POST}  /v1/p/cancel_out_stock_reg/ 36取消缺货统计
     * @apiName cancelOutStockReg
     * @apiGroup Product
     * @apiParam {long} skuId skuId
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> cancelOutStockReg(Http.Request request) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            if (null == member) return unauth403();
            JsonNode jsonNode = request.body().asJson();
            long skuId = jsonNode.findPath("skuId").asLong();
            if (skuId < 1) return okCustomJson(CODE40001, "skuId有误");
            ProductSku productSku = ProductSku.find.byId(skuId);
            if (null == productSku) return okCustomJson(CODE40001, "该sku不存在");
            OutStockReg reg = OutStockReg.find.query().where().eq("uid", member.id)
                    .eq("skuId", skuId)
                    .setMaxRows(1)
                    .findOne();
            if (null == reg) return okCustomJson(CODE40002, "您还未对该商品缺货登记，不需要取消");
            reg.delete();
            return okJSON200();
        });
    }

    /**
     * @api {GET} /v1/p/regular_purchase_products/?page=&buyType= 37常购列表
     * @apiName listRegularPurchaseOrders
     * @apiGroup Product
     * @apiParam {int} buyType 1常购列表 2复购列表
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200){int} pages 页数
     * @apiSuccess (Success 200){JsonArray} list 商品列表，具体字段参数商品列表中的字段
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> listRegularPurchaseOrders(Http.Request request, int buyType, int page) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            if (null == member) return unauth403();
            ExpressionList<RegularBuyProduct> expressionList = RegularBuyProduct.find.query().where()
                    .eq("uid", member.id);
            if (buyType == 1) {
                expressionList.lt("totalCount", 3);
            } else expressionList.ge("totalCount", 3);
            PagedList<RegularBuyProduct> pagedList = expressionList.orderBy().desc("id")
                    .setFirstRow((page - 1) * PAGE_SIZE_20)
                    .setMaxRows(PAGE_SIZE_20)
                    .findPagedList();
            List<RegularBuyProduct> list = pagedList.getList();
            list.parallelStream().forEach((each) -> {
                Product product = businessUtils.getProduct(each.getProductId());
                each.product = product;
                setProductDetail(each.product);
            });
            int pages = pagedList.getTotalPageCount();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("pages", pages);
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/classify_list/ 38归集列表
     * @apiName listClassify
     * @apiGroup Product
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){long} id 商品id
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    public CompletionStage<Result> listClassify(Http.Request request) {
        String jsonCacheKey = cacheUtils.getClassifyListJsonCache();
        return asyncCacheApi.getOptional(jsonCacheKey).thenApplyAsync((cacheOptional) -> {
            if (cacheOptional.isPresent()) {
                String node = (String) cacheOptional.get();
                if (null != node) return ok(node);
            }
            List<ProductClassify> list = ProductClassify.find.query()
                    .where().eq("status", ProductClassify.STATUS_SHOW)
                    .orderBy().desc("sort")
                    .findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            asyncCacheApi.set(jsonCacheKey, Json.stringify(result), 10 * 24 * 3600);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/mail_fee_list/ 39邮费列表
     * @apiName listMailFee
     * @apiGroup Product
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){JsonArray} list 订单列表
     * @apiSuccess (Success 200){String} regionCode 地区代码 0表示通用，除了0全局通用外，其他省份/地区如果多选以逗号,隔开
     * @apiSuccess (Success 200){String} regionName 地区名字 如果为空，就显示成"默认"
     * @apiSuccess (Success 200){double} firstWeightFee 首重费用
     * @apiSuccess (Success 200){double} nextWeightFee 续重费用
     */
    public CompletionStage<Result> listMailFee() {
        return CompletableFuture.supplyAsync(() -> {
            String jsonCacheKey = cacheUtils.getMailFeeJsonCache();
            Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (null != node) return ok(Json.parse(node));
            }
            List<MailFee> feeList = MailFee.find.query().orderBy().desc("id").findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(feeList));
            cache.set(jsonCacheKey, result.toString(), 120);
            return ok(result);
        });
    }


    /**
     * @api {POST} /v1/p/clear_search_keyword/ 40删除商品搜索关键字
     * @apiName clearSearchKeyword
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    public CompletionStage<Result> clearSearchKeyword(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            Member memberInCache = businessUtils.getUserIdByAuthToken(request);
            if (null == memberInCache) return unauth403();
            List<SearchLog> list = SearchLog.find.query().where()
                    .eq("uid", memberInCache.id).findList();
            if (list.size() > 0) Ebean.deleteAll(list);
            return okJSON200();
        });
    }


    /**
     * @api {GET} /v1/p/my_comment_list/?&page= 41我的评论列表
     * @apiName listMyComment
     * @apiGroup Product
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){int} pages 页数
     * @apiSuccess (Success 200){JsonArray} list 列表
     * @apiSuccess (Success 200) {long} id 评论id
     * @apiSuccess (Success 200) {long} ProductId 商品id
     * @apiSuccess (Success 200) {long} replyId 针对评论的Id
     * @apiSuccess (Success 200) {String} content 评论内容
     * @apiSuccess (Success 200) {int} score 1－10，10为五星，1为半星
     * @apiSuccess (Error 40001){int} code 40001 参数错误
     * @apiSuccess (Error 40002){int} code 40002 找不到该订单
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> listMyComment(Http.Request request, int page) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            if (null == member) return unauth403();
            ExpressionList<Comment> expressionList = Comment.find.query().where()
                    .eq("uid", member.id)
                    .eq("type", Comment.TYPE_NORMAL);
            PagedList<Comment> pagedList = expressionList
                    .orderBy().asc("type")
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * PAGE_SIZE_20)
                    .setMaxRows(PAGE_SIZE_20)
                    .findPagedList();
            List<Comment> list = pagedList.getList();
            ArrayNode nodes = Json.newArray();
            list.forEach((comment) -> {
                ObjectNode node = getCommentNode(comment);
                nodes.add(node);
            });
            int pages = pagedList.getTotalPageCount();
            ObjectNode resultNode = Json.newObject();
            resultNode.put(CODE, CODE200);
            resultNode.put("pages", pages);
            resultNode.set("list", nodes);
            return ok(resultNode);
        });
    }

    /**
     * @api {GET} /v1/shop/:id/ 42店铺详情
     * @apiName getShop
     * @apiGroup Product
     * @apiSuccess (Success 200) {long} id id
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} contactNumber 联系电话
     * @apiSuccess (Success 200){String} contactName 联系人
     * @apiSuccess (Success 200){String} contactAddress 联系地址
     * @apiSuccess (Success 200){String} licenseNumber 营业执照号
     * @apiSuccess (Success 200){String} licenseImg 营业执照图片
     * @apiSuccess (Success 200){String} description 备注
     * @apiSuccess (Success 200){long} updateTime 更新时间
     * @apiSuccess (Success 200){double} lat 纬度
     * @apiSuccess (Success 200){double} lon 经度
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    public CompletionStage<Result> getShop(Http.Request request, long id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id < 1) return okCustomJson(CODE40001, "参数错误");
            Shop shop = Shop.find.byId(id);
            if (null == shop) return okCustomJson(CODE40002, "该店铺不存在");
            shop.setFilter("");
            ObjectNode result = (ObjectNode) Json.toJson(shop);
            result.put(CODE, CODE200);

            boolean isFavShop = false;
            Member memberInCache = businessUtils.getUserIdByAuthToken(request);
            if (null != memberInCache) {
                ShopFav shopFav = ShopFav.find.query().where()
                        .eq("uid", memberInCache.id)
                        .eq("shopId", id)
                        .setMaxRows(1).findOne();
                if (null != shopFav) {
                    isFavShop = shopFav.isEnable();
                }
            }
            result.put("isFavShop", isFavShop);
            return ok(result);
        });
    }


    /**
     * @api {GET} /v1/p/today_flash_sales/?page= 43当日抢购列表
     * @apiName listTodayFlashsales
     * @apiGroup Product
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){JsonArray} list 列表
     * @apiSuccess (Success 200) {long} id id
     * @apiSuccess (Success 200) {String} displayTime 展示的时间
     * @apiSuccess (Success 200) {long} beginTime 开始时间
     * @apiSuccess (Success 200) {long} endTime 结束时间
     * @apiSuccess (Success 200) {int} status 状态 1正在进行 2未开始 3已抢购（抢购过了2个小时，但还没结束的） 4已结束
     * @apiSuccess (Success 200) {String} headPic 商品封面图
     * @apiSuccess (Success 200) {String} title 标题
     * @apiSuccess (Success 200) {long} price 价格，分为单位
     * @apiSuccess (Success 200) {long} totalCount 价格，分为单位
     * @apiSuccess (Success 200) {long} soldCount 价格，分为单位
     * @apiSuccess (Success 200) {long} beginTime 价格，分为单位
     * @apiSuccess (Success 200) {long} endTime 价格，分为单位
     * @apiSuccess (Success 200) {long} duration 抢购历时
     * @apiSuccess (Success 200) {long} sort 排序
     */
    public CompletionStage<Result> listTodayFlashsales(int page) {
        String key = cacheUtils.getFlashsaleTodayJsonCache(page);
        return asyncCacheApi.getOptional(key).thenApplyAsync((jsonCache) -> {
            if (jsonCache.isPresent()) {
                String result = (String) jsonCache.get();
                if (!ValidationUtil.isEmpty(result)) {
                    businessUtils.updateHotView(result, HOT_VIEW_LIST);
                    return ok(result);
                }
            }
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);

            LocalDateTime today = LocalDateTime.now();
            long currentTime = Timestamp.valueOf(today).getTime() / 1000;

            List<Product> list;
            int hour = today.getHour();
            ExpressionList<Product> expressionList = businessUtils.autoGetProductsExpressionList();
            PagedList<Product> pagedList = expressionList
                    .le("activityType", Product.ACTIVITY_TYPE_SCORE)
                    .orderBy().desc("sort")
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * PAGE_SIZE_10)
                    .setMaxRows(PAGE_SIZE_10)
                    .findPagedList();
            list = pagedList.getList();
            boolean hasNext = pagedList.hasNext();
            result.put("hasNext", hasNext);
            list.parallelStream().forEach((each) -> businessUtils.autoSetProduct(currentTime, hour, each, HOT_VIEW_LIST));
            result.set("list", Json.toJson(list));
            asyncCacheApi.set(key, result.toString(), 24 * 3600);
            return ok(result);
        });

    }

    /**
     * @api {GET} /v1/p/tomorrow_flash_sales/?page= 44明日预售列表
     * @apiName listTomorrowFlashsales
     * @apiGroup Product
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){JsonArray} list 列表
     * @apiSuccess (Success 200) {long} id id
     * @apiSuccess (Success 200) {String} displayTime 展示的时间
     * @apiSuccess (Success 200) {long} beginTime 开始时间
     * @apiSuccess (Success 200) {long} endTime 结束时间
     * @apiSuccess (Success 200) {int} status 状态 1正在进行 2未开始 3已抢购（抢购过了2个小时，但还没结束的） 4已结束
     * @apiSuccess (Success 200) {String} headPic 商品封面图
     * @apiSuccess (Success 200) {String} title 标题
     * @apiSuccess (Success 200) {long} price 价格，分为单位
     * @apiSuccess (Success 200) {long} totalCount 价格，分为单位
     * @apiSuccess (Success 200) {long} soldCount 价格，分为单位
     * @apiSuccess (Success 200) {long} beginTime 价格，分为单位
     * @apiSuccess (Success 200) {long} endTime 价格，分为单位
     * @apiSuccess (Success 200) {long} duration 抢购历时
     * @apiSuccess (Success 200) {long} sort 排序
     */
    public CompletionStage<Result> listTomorrowFlashsales(int page) {
        return CompletableFuture.supplyAsync(() -> {
            String key = cacheUtils.getFlashsaleTomorrowJsonCache(page);
            Optional<String> jsonCache = cache.getOptional(key);
            if (jsonCache.isPresent()) {
                String result = jsonCache.get();
                if (!ValidationUtil.isEmpty(result)) return ok(Json.parse(result));
            }
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            LocalDateTime tomorrowMin = LocalDateTime.of(tomorrow.getYear(), tomorrow.getMonth(), tomorrow.getDayOfMonth(), 0, 0, 0);
            long minTomorrow = Timestamp.valueOf(tomorrowMin).getTime() / 1000;

            long maxTomorrow = Timestamp.valueOf(tomorrowMin.plusDays(1)).getTime() / 1000;
            PagedList<Product> pagedList = Product.find.query().where()
                    .le("activityType", Product.ACTIVITY_TYPE_SCORE)
                    .eq("status", Product.STATUS_ON_SHELVE)
                    .ge("beginTime", minTomorrow)
                    .le("endTime", maxTomorrow)
                    .orderBy().desc("sort")
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * PAGE_SIZE_20)
                    .setMaxRows(PAGE_SIZE_20)
                    .findPagedList();
            List<Product> list = pagedList.getList();
            LocalDateTime today = LocalDateTime.now();
            long currentTime = Timestamp.valueOf(today).getTime() / 1000;
            int hour = today.getHour();
            list.parallelStream().forEach((each) -> {
                businessUtils.autoSetProduct(currentTime, hour, each, HOT_VIEW_LIST);
            });
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            cache.set(key, result.toString(), 5);
            return ok(result);
        });
    }


    /**
     * @api {GET}  /v1/p/out_stock_reg_list/?page= 45缺货登记列表
     * @apiName outStockRegList
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> outStockRegList(Http.Request request, int page) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            if (null == member) return unauth403();
            PagedList<OutStockReg> pagedList = OutStockReg.find.query().where()
                    .eq("uid", member.id)
                    .setFirstRow((page - 1) * PAGE_SIZE_10)
                    .setMaxRows(PAGE_SIZE_10)
                    .findPagedList();
            int pages = pagedList.getTotalPageCount();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("pages", pages);
            result.set("list", Json.toJson(pagedList.getList()));
            return ok(result);
        });
    }

    /**
     * @api {GET}  /v1/p/stock_sold_amount/ 46库存销量列表
     * @apiName stockSoldAmount
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    public CompletionStage<Result> stockSoldAmount(Http.Request request) {
        String key = cacheUtils.getStockSoldAmount();
        return asyncCacheApi.getOptional(key).thenApplyAsync((jsonCache) -> {
            if (jsonCache.isPresent()) {
                String result = (String) jsonCache.get();
                if (!ValidationUtil.isEmpty(result)) return ok(result);
            }
            List<ProductSku> list = ProductSku.find.query().where().eq("enable", true)
                    .findList();
            ArrayNode nodes = Json.newArray();
            list.forEach((each) -> {
                        if (null != each) {
                            ObjectNode node = Json.newObject();
                            node.put("productId", each.productId);
                            node.put("skuId", each.id);
                            node.put("stock", each.stock);
                            node.put("flashLeftAmount", each.flashLeftAmount);
                            long wishAmount = businessUtils.getProductViews(each.productId);
                            node.put("wishAmount", wishAmount);
                            long soldAmount = each.soldAmount + each.virtualAmount;
                            node.put("soldAmount", soldAmount);
                            nodes.add(node);
                        }
                    }
            );
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", nodes);
            asyncCacheApi.set(key, result.toString(), 10);
            return ok(result);
        });
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
                    .setMaxRows(5)
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
            asyncCacheApi.set(key, resultNode.toString(), 20 * 60);
            return ok(resultNode);
        }, new BlockingExecutor(100000, executor));
    }
//}, new BlockingExecutor(100000, executor));

    public CompletionStage<Result> preFetch2(Http.Request request) {
        Executor executor = Executors.newCachedThreadPool();
        String key = cacheUtils.getPrefetchJsonCache();
        return asyncCacheApi.getOptional(key).thenApplyAsync((jsonCache) -> {
            if (jsonCache.isPresent()) {
                String result = (String) jsonCache.get();
                if (!ValidationUtil.isEmpty(result)) {
                    businessUtils.updateHotView(result, HOT_VIEW_LIST);
                    return ok(result);
                }
            }
            ObjectNode resultNode = Json.newObject();
            resultNode.put(CODE, CODE200);
            long currentTime = System.currentTimeMillis() / 1000;
            List<SystemCarousel> carouselList = SystemCarousel.find.query().where()
                    .eq("clientType", 2)
                    .orderBy().desc("displayOrder")
                    .setMaxRows(5)
                    .findList();
            resultNode.set("carouselList", Json.toJson(carouselList));
            businessUtils.setProductList(resultNode);
            List<Product> flashSalesProductList = businessUtils.getFlashSalesProduct();
            resultNode.set("flashSalesProductList", Json.toJson(flashSalesProductList));
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
            asyncCacheApi.set(key, resultNode.toString(), 20 * 60);
            logger.info("2");
            return ok(resultNode);
        }, new BlockingExecutor(100000, executor));
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


    /**
     * @api {POST} /v1/p/shop_products/:shopId/?page= 47商户商品列表，用于搜索
     * @apiName listShopProducts
     * @apiGroup Product
     * @apiParam {int} [page] 页面
     * @apiParam {long} shopId shopId
     * @apiParam {String} filter filter
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面大图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> listShopProducts(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode jsonNode = request.body().asJson();
            int page = jsonNode.findPath("page").asInt();
            int status = jsonNode.findPath("status").asInt();
            long shopId = jsonNode.findPath("shopId").asLong();
            String filter = jsonNode.findPath("filter").asText();
            ExpressionList<Product> expressionList = Product.find.query().where()
                    .eq("shopId", shopId);
            if (status > 0) {
                expressionList.eq("status", status);
            }
            if (!ValidationUtil.isEmpty(filter)) {
                expressionList.or(Expr.icontains("name", filter), Expr.icontains("keywords", filter));
            }
            PagedList<Product> pagedList = expressionList.setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                    .findPagedList();
            int pages = pagedList.getTotalPageCount();
            List<Product> list = pagedList.getList();
            list.parallelStream().forEach((each) -> setProductDetail(each));
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("pages", pages);
            result.put("hasNext", pagedList.hasNext());
            result.set("list", Json.toJson(list));
            return ok(result);
        });

    }

    /**
     * @api {GET} /v1/p/shop_list/?page= 47商户列表
     * @apiName listShops
     * @apiGroup Product
     * @apiParam {int} [page] 页面
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     */
    public CompletionStage<Result> listShops(Http.Request request, int page, String filter, String tag) {
        if (ValidationUtil.isEmpty(filter) && ValidationUtil.isEmpty(tag)) {
            String key = cacheUtils.getShopListJsonCache(page);
            return asyncCacheApi.getOptional(key).thenApplyAsync((jsonCache) -> {
                if (jsonCache.isPresent()) {
                    String result = (String) jsonCache.get();
                    if (!ValidationUtil.isEmpty(result)) return ok(result);
                }
                ObjectNode result = getShopNodes(page, filter, tag, request);
                cache.set(key, Json.stringify(result), 2 * 60);
                return ok(result);
            });
        } else {
            return CompletableFuture.supplyAsync(() -> {
                ObjectNode result = getShopNodes(page, filter, tag, request);
                return ok(result);
            });
        }
    }


    private ObjectNode getShopNodes(int page, String filter, String tag, Http.Request request) {
        ExpressionList<Shop> expressionList = Shop.find.query().where().eq("status", Shop.STATUS_NORMAL);
        if (!ValidationUtil.isEmpty(tag)) {
            List<Long> shopIdList = ShopTag.find.query().select("shopId").where().eq("tag", tag).findSingleAttributeList();
            if (shopIdList.size() > 0) {
                expressionList.in("id", shopIdList);
            } else expressionList.eq("id", 0);
        }
        String filterTrim = filter.trim().replaceAll("\\s+", " ");
        String filterTrimWithoutSpace = filterTrim.replaceAll(" ", "");
        if (!ValidationUtil.isEmpty(filterTrimWithoutSpace)) {
            expressionList.icontains("filter", filterTrimWithoutSpace);
            saveShopSearchKeyword(request, filterTrimWithoutSpace);
        }

        PagedList<Shop> pagedList = expressionList
                .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                .orderBy().desc("sort")
                .orderBy().asc("id")
                .findPagedList();
        List<Shop> list = pagedList.getList();
        list.parallelStream().forEach((each) -> {
            each.setFilter("");
            each.setBranches("");
            List<Product> products = Product.find.query().where()
                    .eq("shopId", each.id)
                    .eq("placeShopTop", true)
                    .orderBy().desc("sort")
                    .setMaxRows(3)
                    .findList();
            each.homepageShops.addAll(products);
        });
        ObjectNode result = Json.newObject();
        result.put(CODE, CODE200);
        result.put("hasNext", pagedList.hasNext());
        result.set("list", Json.toJson(list));
        return result;
    }


    /**
     * @api {post} /v1/p/shop_fav/ 48收藏/取消收藏店铺
     * @apiName favShop
     * @apiGroup Product
     * @apiParam {boolean} enable true收藏 false取消收藏
     * @apiParam {long} shopId  shopId
     * @apiSuccess (Success 200) {int} code 200成功修改
     */
    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> favShop(Http.Request request) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((memberInCache) -> {
            JsonNode jsonNode = request.body().asJson();
            if (null == memberInCache) return unauth403();
            try {
                if (!businessUtils.setLock(String.valueOf(memberInCache.id), OPERATION_FAV_SHOP))
                    return okCustomJson(CODE40004, "正在处理中,请稍等");
                Member member = Member.find.byId(memberInCache.id);
                if (null == member) return unauth403();
                long shopId = jsonNode.findPath("shopId").asLong();
                boolean enable = jsonNode.findPath("enable").asBoolean();
                Shop shop = Shop.find.byId(shopId);
                if (null == shop) {
                    businessUtils.unLock(String.valueOf(memberInCache.id), OPERATION_FAV_SHOP);
                    return okCustomJson(CODE40001, "店铺不存在");
                }
                ShopFav shopFav = ShopFav.find.query().where()
                        .eq("uid", memberInCache.id)
                        .eq("shopId", shopId)
                        .setMaxRows(1).findOne();
                long currentTime = dateUtils.getCurrentTimeBySecond();
                if (null == shopFav) {
                    shopFav = new ShopFav();
                    shopFav.setShopId(shopId);
                    shopFav.setUid(memberInCache.id);
                    shopFav.setEnable(enable);
                    shopFav.setCreateTime(currentTime);
                } else {
                    shopFav.setEnable(enable);
                }
                shopFav.save();
                long favsCount = member.favsCount;
                if (enable) {
                    favsCount = favsCount + 1;
                } else favsCount = favsCount - 1;
                if (favsCount < 1) favsCount = 0;
                member.setFavsCount(favsCount);
                member.save();
                businessUtils.unLock(String.valueOf(memberInCache.id), OPERATION_FAV_SHOP);
                return okJSON200();
            } catch (Exception e) {
                logger.error("favShop:" + e.getMessage());
                businessUtils.unLock(String.valueOf(memberInCache.id), OPERATION_FAV_SHOP);
                return okCustomJson(CODE500, "收藏发生异常，请稍后再试");
            } finally {
                businessUtils.unLock(String.valueOf(memberInCache.id), OPERATION_FAV_SHOP);
            }
        });
    }


    /**
     * @api {GET} /v1/p/search_shop_keywords/ 49店铺搜索关键字
     * @apiName searchShopKeywords
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {JsonArray} list 关键字列表
     * @apiSuccess (Success 200) {String} keyword 关键字
     * @apiSuccess (Success 200) {JsonArray} searchLogList 历史搜索（自己的）
     * @apiSuccess (Success 200) {String} keyword_ 关键字
     */
    public CompletionStage<Result> searchShopKeywords(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            String jsonCacheKey = cacheUtils.getShopKeywordsJsonCache();
            Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (null != node) {
                    ObjectNode result = (ObjectNode) Json.parse(node);
                    List<ShopSearchLog> searchLogList = listShopSearchLog(request);
                    if (null != searchLogList) {
                        result.set("searchLogList", Json.toJson(searchLogList));
                    }
                    return ok(result);
                }
            }
            List<ShopSearch> list = ShopSearch.find.query().orderBy()
                    .desc("views").setMaxRows(30).findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            cache.set(jsonCacheKey, result.toString());
            List<ShopSearchLog> searchLogList = listShopSearchLog(request);
            if (null != searchLogList) {
                result.set("searchLogList", Json.toJson(searchLogList));
            }
            return ok(result);
        });
    }

    public List<ShopSearchLog> listShopSearchLog(Http.Request request) {
        Member member = businessUtils.getUserIdByAuthToken(request);
        if (null != member) {
            List<ShopSearchLog> list = ShopSearchLog.find.query().where()
                    .eq("uid", member.id)
                    .orderBy().desc("views")
                    .setMaxRows(50).findList();
            return list;
        }
        return new ArrayList<>();
    }


    private void saveShopSearchKeyword(Http.Request request, String filter) {
        CompletableFuture.runAsync(() -> {
            if (!ValidationUtil.isEmpty(filter)) {
                ShopSearch search = ShopSearch.find.query().where().eq("keyword", filter)
                        .orderBy().asc("id").setMaxRows(1).findOne();
                if (null == search) {
                    search = new ShopSearch();
                    search.setKeyword(filter);
                    search.setViews(1);
                } else {
                    search.setViews(search.views + 1);
                }
                search.save();
                String jsonCacheKey = cacheUtils.getShopKeywordsJsonCache();
                cache.remove(jsonCacheKey);
                Member member = businessUtils.getUserIdByAuthToken(request);
                if (null != member) {
                    ShopSearchLog searchLog = ShopSearchLog.find.query().where()
                            .eq("keyword", filter)
                            .eq("uid", member.id)
                            .orderBy().asc("id")
                            .setMaxRows(1).findOne();
                    if (null == searchLog) {
                        searchLog = new ShopSearchLog();
                        searchLog.setKeyword(filter);
                        searchLog.setViews(1);
                        searchLog.setUid(member.id);
                    } else {
                        searchLog.setViews(searchLog.views + 1);
                    }
                    searchLog.save();
                }
            }
        });
    }

    /**
     * @api {POST} /v1/p/clear_search_shop_keywords/ 50清空店铺搜索关键字
     * @apiName clearShopKeywords
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    public CompletionStage<Result> clearShopKeywords(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            Member member = businessUtils.getUserIdByAuthToken(request);
            if (null == member) return unauth403();
            List<ShopSearchLog> searchLogList = ShopSearchLog.find.query().where().eq("uid", member.id).findList();
            if (searchLogList.size() > 0) Ebean.deleteAll(searchLogList);
            return okJSON200();
        });
    }


    /**
     * @api {POST} /v1/p/clear_search_product_keywords/ 51清空商品搜索关键字
     * @apiName clearProductKeywords
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    public CompletionStage<Result> clearProductKeywords(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            Member member = businessUtils.getUserIdByAuthToken(request);
            if (null == member) return unauth403();
            List<SearchLog> searchLogList = SearchLog.find.query().where().eq("uid", member.id).findList();
            if (searchLogList.size() > 0) Ebean.deleteAll(searchLogList);
            return okJSON200();
        });
    }

    /**
     * @api {GET} /v1/p/my_favs/ 52我的收藏列表
     * @apiName listMyFavs
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {JsonObject} articleFavlist 文章收藏列表
     * @apiSuccess (Success 200) {JsonObject} barFavList 酒吧收藏列表
     * @apiSuccess (Success 200) {JsonObject} productFavList 商品收藏列表
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> listMyFavs(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            Member memberInCache = businessUtils.getUserIdByAuthToken(request);
            if (null == memberInCache) return unauth403();
            ObjectNode node = Json.newObject();
            node.put(CODE, CODE200);
            List<ProductFav> productFavList = ProductFav.find.query().where()
                    .eq("uid", memberInCache.id)
                    .eq("enable", true)
                    .orderBy().desc("id")
                    .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                    .findList();
            LocalDateTime today = LocalDateTime.now();
            long currentTime = Timestamp.valueOf(today).getTime() / 1000;
            int hour = today.getHour();
            productFavList.parallelStream().forEach((each) -> {
                each.product = businessUtils.getProduct(each.getProductId());
                if (null != each.product) businessUtils.autoSetProduct(currentTime, hour, each.product, HOT_VIEW_LIST);
            });
            node.set("productFavList", Json.toJson(productFavList));
            return ok(node);
        });
    }


    /**
     * @api {POST} /v1/p/groupon_products/ 53团购商品列表
     * @apiName listGrouponProducts
     * @apiGroup Product
     * @apiParam {int} page
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> listGrouponProducts(Http.Request request) {
        JsonNode requestNode = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            List<Product> emptyList = new ArrayList<>();
            result.set("list", Json.toJson(emptyList));
            if (null == requestNode) return ok(result);
            int page = requestNode.findPath("page").asInt();
            String jsonCacheKey = cacheUtils.getGrouponProductsJsonCache(page);
            //第一页需要缓存，从缓存读取
            Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (null != node) return ok(Json.parse(node));
            }

            Set<Long> productIdSet = new HashSet<>();
            ExpressionList<Product> expressionList = Product.find.query().where()
                    .ge("status", Product.STATUS_ON_SHELVE)
                    .ge("activityType", Order.ORDER_ACTIVITY_TYPE_GROUPON);
            if (productIdSet.size() > 0) expressionList.idIn(productIdSet);
            PagedList<Product> pagedList = expressionList.setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                    .findPagedList();
            List<Product> list = pagedList.getList();
            list.parallelStream().forEach((each) -> {

                each.setDetails("");
                each.soldAmount = each.soldAmount + each.virtualAmount;
                each.virtualAmount = 0;
                if (each.activityType == Order.ORDER_ACTIVITY_TYPE_GROUPON_SUPER) {
                    getGrouponDetailForProduct(each);
                }
                setProductDetail(each);
            });
            result.set("list", Json.toJson(list));
            cache.set(jsonCacheKey, result.toString(), 2 * 30);
            return ok(result);
        });
    }

    private void getGrouponDetailForProduct(Product each) {
        Groupon groupon = Groupon.find.query().where()
                .eq("productId", each.id)
                .eq("status", Groupon.STATUS_PROCESSING)
                .orderBy().desc("status")
                .setMaxRows(1)
                .findOne();
        each.groupon = groupon;
        List<GrouponPrice> grouponSkuPriceList = GrouponPrice.find.query().where()
                .eq("productId", each.id)
                .orderBy().asc("upto")
                .findList();
        each.grouponSkuPriceList = grouponSkuPriceList;
    }

    /**
     * @api {GET}  /v1/p/recommend_products/:productId/ 54推荐商品列表
     * @apiName listRecommendProducts
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    public CompletionStage<Result> listRecommendProducts(Http.Request request, long productId) {
        return CompletableFuture.supplyAsync(() -> {
            List<Product> recommendList = businessUtils.getRecommendProductList(productId);
            ObjectNode resultNode = Json.newObject();
            resultNode.put(CODE, CODE200);
            resultNode.set("recommendList", Json.toJson(recommendList));
            return ok(resultNode);
        });
    }

    /**
     * @api {GET}  /v1/p/activity_avatars/:activityType/  55活动用户头表
     * @apiName listActivityAvatars
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    public CompletionStage<Result> listActivityAvatars(Http.Request request, long activityType) {
        return CompletableFuture.supplyAsync(() -> {
            long minTime = dateUtils.getTodayMinTimestamp();
            List<String> avatars = ProductSkuAvatar.find.query()
                    .select("avatar")
                    .where().eq("activityType", activityType)
                    .ge("createTime", minTime)
                    .orderBy().desc("id")
                    .setMaxRows(10)
                    .findSingleAttributeList();
            ObjectNode resultNode = Json.newObject();
            resultNode.put(CODE, CODE200);
            resultNode.set("avatars", Json.toJson(avatars));
            return ok(resultNode);
        });
    }

    /**
     * @api {GET}  /v1/p/activity_users/  56活动用户数量
     * @apiName listActivityUsers
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    /**
     * @api {GET}  /v1/p/activity_users/  18活动用户数量
     * @apiName listActivityUsers
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    public CompletionStage<Result> listActivityUsers(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            String today = dateUtils.getToday();
            long minTime = dateUtils.getTodayMinTimestamp();
            long flashSalesJoinUsers = 0;
            long grouponJoinUsers = 0;
            TypeOrderStat flashSales = TypeOrderStat.find.query()
                    .where().eq("date", today)
                    .eq("activityType", Product.ACTIVITY_TYPE_FLASH)
                    .orderBy().desc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null != flashSales) flashSalesJoinUsers = flashSales.amount;

            TypeOrderStat groupon = TypeOrderStat.find.query()
                    .where().eq("date", today)
                    .ge("activityType", Order.ORDER_ACTIVITY_TYPE_GROUPON)
                    .orderBy().desc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null != groupon) grouponJoinUsers = groupon.amount;

            List<String> flashSaleAvatars = ProductSkuAvatar.find.query()
                    .select("avatar")
                    .where().eq("activityType", Product.ACTIVITY_TYPE_FLASH)
                    .ge("createTime", minTime)
                    .orderBy().desc("id")
                    .setMaxRows(10)
                    .findSingleAttributeList();

            List<String> grouponAvatars = ProductSkuAvatar.find.query()
                    .select("avatar").where()
                    .ge("activityType", Order.ORDER_ACTIVITY_TYPE_GROUPON)
                    .ge("createTime", minTime)
                    .orderBy().desc("id")
                    .setMaxRows(10)
                    .findSingleAttributeList();

            ObjectNode resultNode = Json.newObject();
            resultNode.put(CODE, CODE200);
            resultNode.put("flashSalesJoinUsers", flashSalesJoinUsers);
            resultNode.put("grouponJoinUsers", grouponJoinUsers);
            resultNode.set("flashSaleAvatars", Json.toJson(flashSaleAvatars));
            resultNode.set("grouponAvatars", Json.toJson(grouponAvatars));
            return ok(resultNode);
        });
    }


    /**
     * @api {POST} /v1/p/combo_products/?page= 57组合商品列表
     * @apiName listProducts
     * @apiGroup Product
     * @apiParam {int} [page] 页面
     * @apiParam {int} [brandId] 品牌ID
     * @apiParam {int} [mixOrder] 综合排序 0不使用综合 1使用综合排序 三个排序中默认使用综合排序
     * @apiParam {int} [orderByPrice] 按价格排序 0不按价格排序 1价格升序 2价格降序
     * @apiParam {int} [orderBySoldAmount] 按销量排序 0不按销量排序 1销量升序 2销量降序
     * @apiParam {int} activityType activityType
     * @apiParam {Array} [param] 筛选参数数组
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面大图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> listComboProducts(Http.Request request) {
        JsonNode requestNode = request.body().asJson();
        int page = requestNode.findPath("page").asInt();
        String jsonCacheKey = cacheUtils.getComboProductsJsonCache(page);
        return asyncCacheApi.getOptional(jsonCacheKey).thenApplyAsync((jsonCache) -> {
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            List<Product> emptyList = new ArrayList<>();
            result.set("list", Json.toJson(emptyList));
            if (null == requestNode) return ok(result);
            if (jsonCache.isPresent()) {
                String node = (String) jsonCache.get();
                if (null != node) return ok(Json.parse(node));
            }
            ExpressionList<Product> expressionList = Product.find.query().where()
                    .eq("isCombo", true)
                    .ge("status", Product.STATUS_ON_SHELVE);
            PagedList<Product> pagedList = expressionList
                    .orderBy().desc("sort")
                    .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                    .findPagedList();
            List<Product> list = pagedList.getList();
            list.parallelStream().forEach((each) -> setProductDetail(each));
            result.put("hasNext", pagedList.hasNext());
            result.set("list", Json.toJson(list));
            cache.set(jsonCacheKey, Json.stringify(result), 2 * 60);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/shop_categories/?filter 58商品分类列表
     * @apiName listShopCategories
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){long} id 分类id
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} imgUrl 图片
     * @apiSuccess (Success 200){String} poster 海报图片
     * @apiSuccess (Success 200){long} soldAmount 已售数量
     * @apiSuccess (Success 200){int} show 1显示2不显示
     * @apiSuccess (Success 200){int} sort 排序顺序
     * @apiSuccess (Success 200){JsonArray} children 子分类列表
     * @apiSuccess (Success 200){string} updateTime 更新时间
     */
    public CompletionStage<Result> listShopCategories(long shopId, final String filter) {
        return CompletableFuture.supplyAsync(() -> {
            String key = cacheUtils.getShopCategoryJsonCache(shopId);
//            //第一页从缓存读取
//            if (ValidationUtil.isEmpty(filter)) {
//                Optional<String> cacheOptional = cache.getOptional(key);
//                if (cacheOptional.isPresent()) {
//                    String node = cacheOptional.get();
//                    if (ValidationUtil.isEmpty(node)) return ok(Json.parse(node));
//                }
//            }
            ExpressionList<ShopProductCategory> expressionList = ShopProductCategory.find.query().where()
                    .eq("shopId", shopId)
                    .eq("show", Category.SHOW_CATEGORY);
            if (!ValidationUtil.isEmpty(filter)) expressionList.icontains("name", filter);
            List<ShopProductCategory> list = expressionList.orderBy()
                    .asc("path")
                    .orderBy().desc("sort")
                    .findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            List<ShopProductCategory> resultList = convertShopCategoryToTreeNode(list);
            result.set("list", Json.toJson(resultList));
            if (ValidationUtil.isEmpty(filter)) cache.set(key, Json.stringify(result), 30 * 60);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/filter_shop_product_categories/:parentId/ 59根据父类ID取出子类
     * @apiName listShopProductCategoriesByParentId
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){long} id 分类id
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} imgUrl 图片
     * @apiSuccess (Success 200){String} poster 海报图片
     * @apiSuccess (Success 200){long} soldAmount 已售数量
     * @apiSuccess (Success 200){int} show 1显示2不显示
     * @apiSuccess (Success 200){int} sort 排序顺序
     * @apiSuccess (Success 200){JsonArray} children 子分类列表
     * @apiSuccess (Success 200){string} updateTime 更新时间
     */
    public CompletionStage<Result> listShopProductCategoriesByParentId(long parentId) {
        return CompletableFuture.supplyAsync(() -> {
            String key = cacheUtils.getShopCategoryJsonCacheByParentId(parentId);
            //第一页从缓存读取
            Optional<String> cacheOptional = cache.getOptional(key);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (ValidationUtil.isEmpty(node)) return ok(Json.parse(node));
            }
            List<ShopProductCategory> list = ShopProductCategory.find.query().where()
                    .eq("show", Category.SHOW_CATEGORY)
                    .eq("parentId", parentId)
                    .orderBy()
                    .desc("sort")
                    .findList();
            ObjectNode result = Json.newObject();
            ArrayNode nodes = Json.newArray();
            list.forEach((category) -> {
                ObjectNode node = Json.newObject();
                node.put("id", category.id);
                node.put("name", category.name);
                nodes.add(node);
            });
            result.put(CODE, CODE200);
            result.set("list", nodes);
            cache.set(key, result.toString(), 10 * 60);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/shop_top_product_categories/:childrenId/ 60根据子类ID取出最顶级父类ID
     * @apiName listShopTopParentCategory
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200){long} parentId 最顶级分类id
     */
    public CompletionStage<Result> listShopTopParentCategory(long childrenId) {
        return CompletableFuture.supplyAsync(() -> {
            if (childrenId < 1) return okCustomJson(CODE40001, "参数错误");
            ShopProductCategory category = ShopProductCategory.find.byId(childrenId);
            if (null == category) return okCustomJson(CODE40002, "分类不存在");
            long parentId = 0;
            if (!ValidationUtil.isEmpty(category.path)) {
                String[] pathArray = category.path.split("/");
                if (null != pathArray && pathArray.length > 1) {
                    String parentIdStr = pathArray[1];
                    try {
                        parentId = Long.parseLong(parentIdStr);
                    } catch (Exception e) {
                        logger.error("字符串转换错误" + e.toString());
                    }
                }
            }
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("parentId", parentId);
            return ok(result);
        });
    }

    public List<ShopProductCategory> convertShopCategoryToTreeNode(List<ShopProductCategory> categoryList) {
        List<ShopProductCategory> nodeList = new ArrayList<>();
        if (null == categoryList) return nodeList;
        for (ShopProductCategory node : categoryList) {
            if (null != node) {
                if (!ValidationUtil.isEmpty(node.path) && node.path.equalsIgnoreCase("/")) {
                    //根目录
                    nodeList.add(node);
                } else {
                    updateChildren(node, categoryList);
                }
            }

        }
        return nodeList;
    }

    private void updateChildren(ShopProductCategory category, List<ShopProductCategory> nodeList) {
        for (ShopProductCategory parentCategory : nodeList) {
            if (null != parentCategory && category.parentId == parentCategory.id) {
                if (parentCategory.children == null) parentCategory.children = new ArrayList<>();
                parentCategory.children.add(category);
                break;
            }
        }
    }

    /**
     * @api {GET} /v1/p/shop_top_products/:shopId/?page= 61店铺首页推荐商品列表
     * @apiName listShopTopProducts
     * @apiGroup Product
     * @apiParam {int} [page] 页面
     * @apiParam {Array} [param] 筛选参数数组
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面大图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    public CompletionStage<Result> listShopTopProducts(Http.Request request, long shopId, int page) {
        return CompletableFuture.supplyAsync(() -> {
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            String jsonCacheKey = cacheUtils.getShopTopProductsJsonCache(shopId, page);
            //第一页需要缓存，从缓存读取
            Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (null != node) return ok(Json.parse(node));
            }
            ExpressionList<Product> expressionList = Product.find.query().where()
                    .ge("status", Product.STATUS_ON_SHELVE)
                    .eq("shopId", shopId)
                    .eq("placeShopTop", true);
            PagedList<Product> pagedList = expressionList.setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                    .findPagedList();
            int pages = pagedList.getTotalPageCount();
            List<Product> list = pagedList.getList();
            list.parallelStream().forEach((each) -> setProductDetail(each));
            result.put("pages", pages);
            result.set("list", Json.toJson(list));
            cache.set(jsonCacheKey, Json.stringify(result), 2 * 60);
            return ok(result);
        });
    }

    /**
     * @api {POST} /v1/p/shop_products_by_category/ 62列出店铺分类的商品列表
     * @apiName listShopProductsByCategoryId
     * @apiGroup Product
     * @apiParam {int} [page] 分页
     * @apiParam {int} [categoryId] 分类ID
     * @apiParam {int} [mixOrder] 综合排序 0不使用综合 1使用综合排序 三个排序中默认使用综合排序
     * @apiParam {int} [orderByPrice] 按价格排序 0不按价格排序 1价格升序 2价格降序
     * @apiParam {int} [orderBySoldAmount] 按销量排序 0不按销量排序 1销量升序 2销量降序
     * @apiParam {Array} [param] 筛选参数数组
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面大图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    public CompletionStage<Result> listShopProductsByCategoryId(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode requestNode = request.body().asJson();
            long categoryId = requestNode.findPath("categoryId").asLong();
            int page = requestNode.findPath("page").asInt();
            //第一页从缓存读取
            String jsonCacheKey = cacheUtils.getShopProductsByCategoryFromCache(categoryId, page);
            Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (!ValidationUtil.isEmpty(node)) return ok(node);
            }
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);

            List<Product> list;
            LocalDateTime today = LocalDateTime.now();
            long currentTime = Timestamp.valueOf(today).getTime() / 1000;
            int hour = today.getHour();
            ExpressionList<Product> expressionList = businessUtils.autoGetProductsExpressionList();
            if (categoryId > 0) expressionList.icontains("shopCategoryId", categoryId + "");
            PagedList<Product> pagedList = expressionList
                    .orderBy().desc("sort")
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * 10)
                    .setMaxRows(10)
                    .findPagedList();
            boolean hasNext = pagedList.hasNext();
            result.put("hasNext", hasNext);
            list = pagedList.getList();
            list.parallelStream().forEach((each) -> businessUtils.autoSetProduct(currentTime, hour, each, HOT_VIEW_LIST));
            result.set("list", Json.toJson(list));
            cache.set(jsonCacheKey, Json.stringify(result), 60);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/top_shop_list/ 63推荐商户列表
     * @apiName listTopShops
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     */
    public CompletionStage<Result> listTopShops(Http.Request request) {
        String key = cacheUtils.getTopShopListJsonCache();
        return asyncCacheApi.getOptional(key).thenApplyAsync((jsonCache) -> {
            if (jsonCache.isPresent()) {
                String result = (String) jsonCache.get();
                if (!ValidationUtil.isEmpty(result)) return ok(result);
            }
            List<Shop> list = Shop.find.query().where()
                    .eq("status", Shop.STATUS_NORMAL)
                    .eq("placeTop", true)
                    .orderBy().desc("sort")
                    .orderBy().asc("id")
                    .setMaxRows(9)
                    .findList();
//            list.parallelStream().forEach((each) -> {
//                CouponConfig couponConfig = CouponConfig.find.query()
//                        .where().eq("status", CouponConfig.STATUS_ENABLE)
//                        .icontains("shopIds", "/" + each.id + "/")
//                        .orderBy().desc("amount")
//                        .setMaxRows(1)
//                        .findOne();
//                if (null != couponConfig) {
//                    couponConfig.setShopIds("");
//                    couponConfig.setShopNames("");
//                    each.couponConfig = couponConfig;
//                }
//            });
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            cache.set(key, Json.stringify(result), 10 * 60);
            return ok(result);
        });
    }


    /**
     * @api {GET} /v1/p/enroll_content/:orderId/ 64活动报名内容
     * @apiName getEnrollContent
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {Object} enrollConfig 活动配置
     * @apiSuccess (Success 200) {JsonArray} groupList 表单列表
     * @apiSuccess (Success 200) {JsonArray} contentPollList 用户填写的列表
     */
    public CompletionStage<Result> getEnrollContent(Http.Request request, long orderId) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            if (null == member) return unauth403();
            Order order = Order.find.byId(orderId);
            if (null == order) return okCustomJson(CODE40001, "参数错误");
            if (order.uid != member.id) return unauth403();
            EnrollConfig enrollConfig = EnrollConfig.find.byId(order.enrollId);
            if (null == enrollConfig) return okJSON200();
            List<EnrollContentItemGroup> groupList = EnrollContentItemGroup.find.query().where()
                    .eq("configId", enrollConfig.id)
                    .orderBy().desc("sort")
                    .findList();
            groupList.parallelStream().forEach((each) -> {
                List<EnrollContentItem> itemList = EnrollContentItem.find.query().where()
                        .eq("groupId", each.id)
                        .orderBy().desc("sort")
                        .findList();
                each.items.addAll(itemList);
            });
            ObjectNode resultNode = Json.newObject();
            resultNode.put(CODE, CODE200);

            List<OrderDetail> orderDetailList = OrderDetail.find.query().where()
                    .eq("orderId", order.id)
                    .orderBy().asc("id")
                    .findList();
            if (orderDetailList.size() > 0) {
                OrderDetail orderDetail = orderDetailList.get(0);
                Product product = Product.find.byId(orderDetail.productId);
                if (null != product) {
                    List<ProductSku> skuList = getProductSkuList(product);
                    List<ProductParam> list = ProductParam.find.query().where()
                            .eq("productId", product.id)
                            .order().asc("sort")
                            .order().asc("id")
                            .findList();
                    product.paramList.addAll(list);
                    resultNode.set("product", Json.toJson(product));
                    resultNode.set("skuList", Json.toJson(skuList));
                }
            }
            resultNode.set("orderDetailList", Json.toJson(orderDetailList));
            resultNode.set("enrollConfig", Json.toJson(enrollConfig));
            resultNode.set("groupList", Json.toJson(groupList));
            if (null != member) {
                List<EnrollContentPoll> contentPollList = EnrollContentPoll.find.query().where()
                        .eq("orderId", orderId)
                        .orderBy().asc("id")
                        .findList();
                resultNode.set("contentPollList", Json.toJson(contentPollList));
            }
            EnrollContentUserInfo enrollContentUserInfo = EnrollContentUserInfo.find.query().where()
                    .eq("uid", member.id)
                    .eq("orderId", order.id)
                    .orderBy().desc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null != enrollContentUserInfo)
                resultNode.set("enrollContentUserInfo", Json.toJson(enrollContentUserInfo));
            return ok(resultNode);
        });

    }


    /**
     * @api {GET} /v1/p/platform_top_products/?page= 65平台首页推荐商品列表
     * @apiName listPlatformTopProducts
     * @apiGroup Product
     * @apiParam {int} [page] 页面
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面大图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> listPlatformTopProducts(Http.Request request, int page) {
        return CompletableFuture.supplyAsync(() -> {
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            String jsonCacheKey = cacheUtils.getPlatformTopProductsJsonCache(page);
            //第一页需要缓存，从缓存读取
            Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (null != node) return ok(Json.parse(node));
            }
            ExpressionList<Product> expressionList = Product.find.query().where()
                    .ge("status", Product.STATUS_ON_SHELVE)
                    .eq("placeHomeTop", true);
            PagedList<Product> pagedList = expressionList.setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                    .findPagedList();
            int pages = pagedList.getTotalPageCount();
            List<Product> list = pagedList.getList();
            list.parallelStream().forEach((each) -> setProductDetail(each));
            result.put("pages", pages);
            result.set("list", Json.toJson(list));
            cache.set(jsonCacheKey, Json.stringify(result), 2 * 60);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/my_shop_favs/?page= 66我的店铺收藏列表
     * @apiName listFavShops
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> listFavShops(Http.Request request, int page) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((memberInCache) -> {
            if (null == memberInCache) return unauth403();
            ObjectNode node = Json.newObject();
            node.put(CODE, CODE200);
            PagedList<ShopFav> pagedList = ShopFav.find.query().where()
                    .eq("uid", memberInCache.id)
                    .eq("enable", true)
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * PAGE_SIZE_10)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                    .findPagedList();
            List<ShopFav> shopFavList = pagedList.getList();
            shopFavList.parallelStream().forEach((each) -> {
                Shop shop = Shop.find.byId(each.getShopId());
                each.shop = shop;
            });
            node.put("hasNext", pagedList.hasNext());
            node.set("list", Json.toJson(shopFavList));
            return ok(node);
        });
    }

    /**
     * @api {GET}  /v1/p/activity_users_views/  67围观人数及头像
     * @apiName listActivityViewsAndAvatar
     * @apiGroup Product
     * @apiSuccess (Success 200) {String} avatars 头像列表，图像地址以逗号隔开
     * @apiSuccess (Success 200) {int} productViews 人数
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    public CompletionStage<Result> listActivityViewsAndAvatar(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            List<Long> idList = Product.find.query().where()
                    .ge("status", Product.STATUS_ON_SHELVE)
                    .ge("activityType", Order.ORDER_ACTIVITY_TYPE_GROUPON)
                    .findIds();
            ObjectNode resultNode = Json.newObject();
            resultNode.put(CODE, CODE200);
            ArrayNode nodes = Json.newArray();
            idList.parallelStream().forEach((productId) -> {
                ObjectNode node = Json.newObject();
                node.put("productId", productId);
                List<String> avatars = ProductSkuAvatar.find.query()
                        .select("avatar")
                        .where().eq("productId", productId)
                        .orderBy().desc("id")
                        .setMaxRows(10)
                        .findSingleAttributeList();
                node.set("avatars", Json.toJson(avatars));
                List<Long> amountList = ProductSkuAvatar.find.query()
                        .select("amount")
                        .where().eq("productId", productId)
                        .findSingleAttributeList();
                long views = amountList.parallelStream().reduce((a, b) -> a + b).orElse(0L);
                node.put("views", views);
                nodes.add(node);
            });
            resultNode.set("list", nodes);
            return ok(resultNode);
        });
    }

    /**
     * @api {POST} /v1/p/my_shop_products/ 68店铺商品列表，店铺人员使用
     * @apiName listMyShopProducts
     * @apiGroup Product
     * @apiParam {String} filter 关键字
     * @apiParam {int} page page
     * @apiParam {long} categoryId
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> listMyShopProducts(Http.Request request) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            JsonNode requestNode = request.body().asJson();
            if (null == requestNode) return okCustomJson(CODE40001, "参数错误");
            int page = requestNode.findPath("page").asInt();
            long categoryId = requestNode.findPath("categoryId").asLong();
            int status = requestNode.findPath("status").asInt();
            String filter = requestNode.findPath("filter").asText();
            if (null == member) return unauth403();
            MemberProfile memberProfile = MemberProfile.find.query().where().eq("uid", member.id)
                    .orderBy().asc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null == memberProfile) return unauth403();
            ExpressionList<Product> expressionList = Product.find.query().where()
                    .eq("shopId", memberProfile.orgId);
            if (status != 0) expressionList.eq("status", status);
            if (categoryId != 0) expressionList.icontains("categoryId", categoryId + "");
            if (!ValidationUtil.isEmpty(filter)) {
                expressionList.or(Expr.icontains("name", filter), Expr.icontains("details", filter));
            }
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            PagedList<Product> pagedList = expressionList.orderBy().desc("sort")
                    .orderBy().desc("id")
                    .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                    .findPagedList();
            int pages = pagedList.getTotalPageCount();
            result.put("pages", pages);
            result.put("hasNext", pagedList.hasNext());
            List<Product> list = pagedList.getList();
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }


    /**
     * @api {POST} /v1/p/products/:productId/ 69修改商品信息
     * @apiName updateProduct
     * @apiGroup Product
     * @apiParam {String} [barcode] 条形码
     * @apiParam {int} stock 库存
     * @apiParam {long} marketPrice 市场价，以分为单位
     * @apiParam {long} virtualAmount 虚拟数量
     * @apiParam {long} stock 库存
     * @apiParam {String} name 商品名字
     * @apiParam {String} coverImgUrl 商品首图
     * @apiParam {Array} imgList 图片列表，参考管理台送的参数
     * @apiParam {Array} skuList sku列表，参考管理台送的参数
     * @apiSuccess (Success 200){int} code 200
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> updateProduct(Http.Request request, long productId) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            if (null == member) return unauth403();
            MemberProfile memberProfile = MemberProfile.find.query().where().eq("uid", member.id)
                    .orderBy().asc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null == memberProfile) return unauth403();
            JsonNode requestNode = request.body().asJson();
            Product param = Json.fromJson(requestNode, Product.class);
            Product updateProduct = Product.find.byId(productId);
            if (null == updateProduct) return okCustomJson(CODE40002, "该商品不存在");
            if (updateProduct.shopId != memberProfile.orgId) return okCustomJson(CODE40002, "非本店铺的商品");
            String name = requestNode.findPath("name").asText();
            if (!ValidationUtil.isEmpty(param.name)) updateProduct.setName(name);
            if (requestNode.has("status")) {
                updateProduct.setStatus(param.status);
            }
            if (requestNode.has("barcode")) {
                updateProduct.setBarcode(param.barcode);
            }
            if (requestNode.has("categoryId")) {
                updateProduct.setCategoryId(param.categoryId);
            }
            if (requestNode.has("virtualAmount")) {
                updateProduct.setVirtualAmount(param.virtualAmount);
            }
            if (requestNode.has("coverImgUrl")) updateProduct.setCoverImgUrl(param.coverImgUrl);
            if (requestNode.has("placeHomeTop")) {
                updateProduct.setPlaceHomeTop(param.placeHomeTop);
            }
            if (requestNode.has("placeShopTop")) {
                updateProduct.setPlaceShopTop(param.placeShopTop);
            }

            long currentTime = dateUtils.getCurrentTimeBySecond();
            updateProduct.setUpdateTime(currentTime);
            updateProduct.save();


            if (requestNode.has("imgList")) {
                //图片列表
                ArrayNode imgNode = (ArrayNode) requestNode.findPath("imgList");
                List<ProductImage> imageList = new ArrayList<>();
                if (null != imgNode && imgNode.size() > 0) {
                    imgNode.forEach((node) -> {
                        String imgUrl = node.findPath("imgUrl").asText();
                        String imgTips = node.findPath("imgTips").asText();
                        int sort = node.findPath("sort").asInt();
                        if (!ValidationUtil.isEmpty(imgUrl)) {
                            ProductImage image = new ProductImage();
                            image.setProductId(productId);
                            image.setImgUrl(imgUrl);
                            image.setTips(imgTips);
                            image.setSort(sort);
                            image.setUpdateTime(currentTime);
                            image.setCreateTime(currentTime);
                            imageList.add(image);
                        }
                    });
                    //删除旧的
                    List<ProductImage> oldImgList = ProductImage.find.query().where().eq("productId", productId).findList();
                    if (oldImgList.size() > 0) Ebean.deleteAll(oldImgList);
                    Ebean.saveAll(imageList);
                }
            }
            List<OutStockReg> regList = new ArrayList<>();
            if (requestNode.has("skuList")) {
                ArrayNode skuNode = (ArrayNode) requestNode.findPath("skuList");
                if (null != skuNode && skuNode.size() > 0) {
                    List<ProductSku> skuList = new ArrayList<>();
                    List<ProductSku> newSkuList = new ArrayList<>();
                    List<ProductSku> skuDeleteList = new ArrayList<>();
                    skuNode.forEach((node) -> {
                        ProductSku skuParam = Json.fromJson(node, ProductSku.class);
                        if (null != skuParam) {
                            String operation = node.findPath("op").asText();
                            if (!ValidationUtil.isEmpty(operation) && operation.equalsIgnoreCase("del")) {
                                skuDeleteList.add(skuParam);
                            } else if (skuParam.id > 0) {
                                ProductSku productSku = ProductSku.find.byId(skuParam.id);
                                if (null != productSku) {
                                    if (productSku.stock < productSku.minOrderAmount && skuParam.stock >= productSku.minOrderAmount) {
                                        List<OutStockReg> outStockRegList = OutStockReg.find.query().where()
                                                .eq("skuId", productSku.id)
                                                .eq("notify", false)
                                                .findList();
                                        outStockRegList.parallelStream().forEach((each) -> {
                                            each.productName = updateProduct.name;
                                        });
                                        regList.addAll(outStockRegList);
                                    }
                                    setSkuFromParam(node, skuParam, productSku);
                                    syncSkuTime(updateProduct, productSku);
                                    skuList.add(productSku);
                                }
                            } else {
                                skuParam.setProductId(productId);
                                skuParam.setStatus(updateProduct.status);
                                syncSkuTime(updateProduct, skuParam);
                                trimSku(skuParam);
                                skuList.add(skuParam);
                                newSkuList.add(skuParam);
                            }
                        }
                    });
                    if (skuDeleteList.size() > 0) Ebean.deleteAll(skuDeleteList);
                    if (skuList.size() > 0) {
                        Ebean.saveAll(skuList);
                        saveCardCoupon(newSkuList, updateProduct);
                        saveSkuFavor(skuList);
                        updateAttrSortBySku(skuList);
                    } else {
                        //生成默认的sku
                        generateDefaultSKU(updateProduct);
                    }
                }
            }
            cacheUtils.updateProductJsonCache(updateProduct);
            handleProductOffShell(updateProduct);
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("id", updateProduct.id);
            return ok(result);
        });
    }

    private void setSkuFromParam(JsonNode node, ProductSku param, ProductSku productSku) {
        if (null != param && null != productSku) {
            if (!ValidationUtil.isEmpty(param.name)) productSku.setName(param.name);
            if (!ValidationUtil.isEmpty(param.imgUrl)) productSku.setImgUrl(param.imgUrl);
            if (!ValidationUtil.isEmpty(param.code)) productSku.setCode(param.code);
            if (!ValidationUtil.isEmpty(param.barcode)) productSku.setBarcode(param.barcode);
            if (!ValidationUtil.isEmpty(param.data)) productSku.setData(param.data);
            if (!ValidationUtil.isEmpty(param.favorName)) productSku.setFavorName(param.favorName);
            if (param.stock >= 0) productSku.setStock(param.stock);
            if (param.weight >= 0) productSku.setWeight(param.weight);
            if (param.warningStock >= 0) productSku.setWarningStock(param.warningStock);
            if (param.oldPrice >= 0) productSku.setOldPrice(param.oldPrice);
            if (param.price >= 0) productSku.setPrice(param.price);
            if (param.grouponPrice >= 0) productSku.setGrouponPrice(param.grouponPrice);
            if (param.flashPrice >= 0) productSku.setFlashPrice(param.flashPrice);
            if (param.distPrice >= 0) productSku.setDistPrice(param.distPrice);
            if (param.award >= 0) productSku.setAward(param.award);
            if (param.indirectAward >= 0) productSku.setIndirectAward(param.indirectAward);
            if (param.recommendPrice >= 0) productSku.setRecommendPrice(param.recommendPrice);
            if (param.minOrderAmount >= 0) productSku.setMinOrderAmount(param.minOrderAmount);
            if (param.soldAmount >= 0) productSku.setSoldAmount(param.soldAmount);
            if (param.virtualAmount >= 0) productSku.setVirtualAmount(param.virtualAmount);
            if (param.sort >= 0) productSku.setSort(param.sort);
            if (param.source > 0) productSku.setSource(param.source);
            if (param.beginTime >= 0) productSku.setBeginTime(param.beginTime);
            if (param.endTime >= 0) productSku.setEndTime(param.endTime);
            if (param.requireType >= 0) productSku.setRequireType(param.requireType);
            if (param.status >= 0) productSku.setStatus(param.status);
            if (param.deliverHours >= 0) productSku.setDeliverHours(param.deliverHours);
            if (param.limitAmount >= 0) productSku.setLimitAmount(param.limitAmount);
            if (param.presaleStock >= 0) productSku.setPresaleStock(param.presaleStock);
            productSku.setEnable(param.enable);
            if (param.serviceAmount >= 0) productSku.setServiceAmount(param.serviceAmount);
            if (param.serviceDays >= 0) productSku.setServiceDays(param.serviceDays);
            if (param.requireScore >= 0) productSku.setRequireScore(param.requireScore);
            if (param.requireInvites >= 0) productSku.setRequireInvites(param.requireInvites);
            if (param.bidPrice > 0) productSku.setBidPrice(param.bidPrice);
            if (param.memberFavor >= 0) productSku.setMemberFavor(param.memberFavor);
            if (node.has("cardCoupons")) productSku.setCardCoupons(param.cardCoupons);
            if (null != param.detailList && param.detailList.size() > 0) {
                productSku.detailList.addAll(param.detailList);
            }
        }
    }

    private void syncSkuTime(Product product, ProductSku sku) {
        sku.setBeginTime(product.beginTime);
        sku.setEndTime(product.endTime);
        sku.setBeginHour(product.beginHour);
    }

    private void trimSku(ProductSku sku) {
        if (!ValidationUtil.isEmpty(sku.name)) sku.name = sku.name.trim();
        if (!ValidationUtil.isEmpty(sku.imgUrl)) sku.imgUrl = sku.imgUrl.trim();
        if (!ValidationUtil.isEmpty(sku.code)) sku.code = sku.code.trim();
        if (!ValidationUtil.isEmpty(sku.barcode)) sku.barcode = sku.barcode.trim();
        if (!ValidationUtil.isEmpty(sku.data)) sku.data = sku.data.trim();
    }

    private void saveSkuFavor(List<ProductSku> skuList) {
        skuList.forEach((each) -> businessUtils.saveSKUFavor(each));
    }

    private void updateAttrSortBySku(List<ProductSku> skuList) {
        CompletableFuture.runAsync(() -> {
            if (null != skuList) {
                skuList.forEach((each) -> businessUtils.updateAttrSort(each));
            }
        });

    }

    private void saveCardCoupon(List<ProductSku> skuList, Product product) {
        if (product.productType == Product.TYPE_SERVICE_PRODUCT) {
            List<CardCouponConfig> list = new ArrayList<>();
            skuList.forEach((each) -> {
                if (ValidationUtil.isEmpty(each.cardCoupons) || each.cardCoupons.equalsIgnoreCase("[]")) {
                    String title = each.name.replaceAll("销售规格:", "");
                    CardCouponConfig couponConfig = CardCouponConfig.find.query().where().eq("title", title)
                            .setMaxRows(1)
                            .findOne();
                    if (null == couponConfig) {
                        couponConfig = new CardCouponConfig();
                        couponConfig.setShopId(product.supplierUid);
                        couponConfig.setTitle(title);
                        couponConfig.setContent("");
                        couponConfig.setDigest("");
                        couponConfig.setImgUrl(product.coverImgUrl);
                        couponConfig.setProductId(product.id);
                        couponConfig.setSkuId(each.id);
                        couponConfig.setDays(30);
                        if (couponConfig.giveCount < 1) couponConfig.giveCount = 1;
                        long currentTime = dateUtils.getCurrentTimeBySecond();
                        couponConfig.setUpdateTime(currentTime);
                        couponConfig.setCreateTime(currentTime);
                        couponConfig.setFilter(Json.stringify(Json.toJson(couponConfig)));
                        list.add(couponConfig);
                    }
                }
            });
            if (list.size() > 0) {
                Ebean.saveAll(list);
                list.forEach((each) -> {
                    ProductSku productSku = ProductSku.find.byId(each.skuId);
                    if (null != productSku) {
                        productSku.setCardCoupons("[" + each.id + "]");
                        productSku.save();
                    }
                });
            }
        }
    }


    private void generateDefaultSKU(Product productParam) {
        ProductSku sku = new ProductSku();
        sku.setName("");
        sku.setProductId(productParam.id);
        sku.setImgUrl(productParam.coverImgUrl);
        sku.setPrice(productParam.price);
        sku.setStock(100);
        sku.setSort(1);
        sku.setCode("DEFAULT:" + productParam.id);
        sku.setBarcode("");
        sku.setData("");
        sku.setEnable(true);
        sku.setStatus(Product.STATUS_ON_SHELVE);
        sku.setSource(ProductSku.SOURCE_FROM_SELF);
        syncSkuTime(productParam, sku);
        sku.save();
    }


    private void handleProductOffShell(Product updateProduct) {
        if (updateProduct.status == Product.STATUS_OFF_SHELVE || updateProduct.status == Product.STATUS_DRAFT) {
            List<FlashSaleProduct> flashSaleProducts = FlashSaleProduct.find.query().where()
                    .eq("productId", updateProduct.id).findList();
            Ebean.deleteAll(flashSaleProducts);
            String flashsaleJsonCacheKey = cacheUtils.getFlashsaleJsonCache();
            cache.remove(flashsaleJsonCacheKey);

            List<ProductClassifyDetails> classifyDetailsList = ProductClassifyDetails.find.query().where()
                    .eq("productId", updateProduct.id).findList();
            classifyDetailsList.parallelStream().forEach((each) -> {
                ProductClassify classify = ProductClassify.find.byId(each.classifyId);
                if (null != classify) businessUtils.updateClassifyCache(classify);
            });
            Ebean.deleteAll(classifyDetailsList);
            cache.remove(cacheUtils.getSpecialTopicJsonCache());

            List<ProductDefaultRecommend> defaultRecommends = ProductDefaultRecommend.find.query().where()
                    .eq("productId", updateProduct.id).findList();
            Ebean.deleteAll(defaultRecommends);
            businessUtils.refreshRecommendProduct();


            List<ProductRecommend> recommends = ProductRecommend.find.query().where()
                    .or(Expr.eq("productId", updateProduct.id), Expr.eq("recommendProductId", updateProduct.id))
                    .findList();
            Ebean.deleteAll(recommends);
            recommends.parallelStream().forEach((each) -> businessUtils.refreshRecommendProduct(updateProduct.id));

            List<ProductFavorProducts> favorProducts = ProductFavorProducts.find.query().where()
                    .eq("productId", updateProduct.id).findList();
            Ebean.deleteAll(favorProducts);

            List<ProductTabProducts> tabProducts = ProductTabProducts.find.query().where()
                    .eq("productId", updateProduct.id).findList();
            Ebean.deleteAll(tabProducts);
            businessUtils.updateTabCache();

            List<ProductTag> productTags = ProductTag.find.query().where()
                    .eq("productId", updateProduct.id).findList();
            Ebean.deleteAll(productTags);

            List<SpecialTopicProductList> specialTopicProductLists = SpecialTopicProductList.find.query().where()
                    .eq("productId", updateProduct.id).findList();
            Ebean.deleteAll(specialTopicProductLists);
            cache.remove(cacheUtils.getSpecialTopicJsonCache());

            List<ProductRelate> relateList = ProductRelate.find.query().where()
                    .or(Expr.eq("comboProductId", updateProduct.id),
                            Expr.eq("relateProductId", updateProduct.id))
                    .findList();
            Ebean.deleteAll(relateList);
            String relateKey = cacheUtils.getRelateProductList(updateProduct.id);
            cache.remove(relateKey);
        }
    }


    /**
     * @api {POST} /v1/p/shop/ 70修改店铺状态
     * @apiName updateShopStatus
     * @apiGroup Product
     * @apiParam {long} shopId shopId
     * @apiParam {int} status 1开业，7休业
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> updateShopStatus(Http.Request request) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            JsonNode requestNode = request.body().asJson();
            if (null == requestNode) return okCustomJson(CODE40001, "参数错误");
            long shopId = requestNode.findPath("shopId").asLong();
            int status = requestNode.findPath("status").asInt();
            Member memberInCache = businessUtils.getUserIdByAuthToken(request);
            if (null == memberInCache) return unauth403();
            MemberProfile memberProfile = MemberProfile.find.query().where().eq("uid", memberInCache.id)
                    .orderBy().asc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null == memberProfile) return unauth403();
            if (shopId != memberProfile.orgId) return okCustomJson(CODE40002, "非本店铺，不可修改");
            Shop shop = Shop.find.byId(shopId);
            shop.setStatus(status);
            shop.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v1/p/has_enroll/ 71根据商品ID查询报名情况
     * @apiName listEnrollStatus
     * @apiGroup Product
     * @apiParam {Array} productIdList 商品IDList
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> listEnrollStatus(Http.Request request) {
        return businessUtils.getUserIdByAuthToken2(request).thenApplyAsync((member) -> {
            JsonNode requestNode = request.body().asJson();
            if (null == requestNode) return okCustomJson(CODE40001, "参数错误");
            if (!requestNode.has("productIdList")) okCustomJson(CODE40001, "请传送商品ID数组");
            ArrayNode nodes = (ArrayNode) requestNode.findPath("productIdList");
            Set<Long> set = new HashSet<>();
            nodes.forEach((each) -> set.add(each.asLong()));
            if (set.size() < 1) return okJSON200();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            ArrayNode list = Json.newArray();
            if (null == member) {
                set.forEach((each) -> {
                    ObjectNode node = Json.newObject();
                    node.put("productId", each);
                    node.put("hasEnroll", false);
                    list.add(node);
                });
                result.set("list", list);
                return ok(result);
            }
            Map<Long, Boolean> map = new ConcurrentHashMap<>();
            set.parallelStream().forEach((each) -> {
                EnrollContentUserInfo exist = EnrollContentUserInfo.find.query().where()
                        .eq("uid", member.id)
                        .eq("productId", each)
                        .setMaxRows(1)
                        .findOne();
                boolean hasEnroll = null != exist ? true : false;
                map.put(each, hasEnroll);
            });
            map.forEach((k, v) -> {
                ObjectNode node = Json.newObject();
                node.put("productId", k);
                node.put("hasEnroll", v);
                list.add(node);
            });
            result.set("list", list);
            return ok(result);
        });
    }


    /**
     * @api {POST} /v1/p/score_products/ 72积分商品列表
     * @apiName listScoreProducts
     * @apiGroup Product
     * @apiParam {int} [page] 页面
     * @apiParam {long} categoryId 分类id
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} coverImgUrl 商品封面大图
     * @apiSuccess (Success 200){String} poster 商品海报大图
     * @apiSuccess (Success 200){long} categoryId 分类id
     * @apiSuccess (Success 200){int} stock 库存
     * @apiSuccess (Success 200){long} soldAmount 已卖数量
     * @apiSuccess (Success 200){int} marketPrice 市场价,以元为单位
     * @apiSuccess (Success 200){int} price 现价,以元为单位
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> listScoreProducts(Http.Request request) {
        JsonNode requestNode = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);

            long categoryId = requestNode.findPath("categoryId").asLong();
            int page = requestNode.findPath("page").asInt();
            String jsonCacheKey = cacheUtils.getScoreProductsJsonCache(categoryId, page);
            //第一页需要缓存，从缓存读取
            Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (null != node) return ok(Json.parse(node));
            }
            ExpressionList<Product> expressionList = Product.find.query().where()
                    .ge("status", Product.STATUS_ON_SHELVE)
                    .eq("productType", Product.TYPE_SCORE);
            if (categoryId > 0) expressionList.icontains("categoryId", categoryId + "");
            PagedList<Product> pagedList = expressionList.setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_10)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_10)
                    .findPagedList();
            int pages = pagedList.getTotalPageCount();
            List<Product> list = pagedList.getList();
            list.parallelStream().forEach((each) -> setProductDetail(each));
            result.put("pages", pages);
            result.put("hasNext", pagedList.hasNext());
            result.set("list", Json.toJson(list));
            cache.set(jsonCacheKey, Json.stringify(result), 1 * 60);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/score_product_categories/ 73获取积分商城的分类
     * @apiName listScoreProductCategories
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){long} id 分类id
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} imgUrl 图片
     * @apiSuccess (Success 200){String} poster 海报图片
     * @apiSuccess (Success 200){long} soldAmount 已售数量
     * @apiSuccess (Success 200){int} show 1显示2不显示
     * @apiSuccess (Success 200){int} sort 排序顺序
     * @apiSuccess (Success 200){JsonArray} children 子分类列表
     * @apiSuccess (Success 200){string} updateTime 更新时间
     */
    public CompletionStage<Result> listScoreProductCategories() {
        return CompletableFuture.supplyAsync(() -> {
            String key = cacheUtils.getScoreCategories();
            Optional<String> cacheOptional = cache.getOptional(key);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (ValidationUtil.isEmpty(node)) return ok(Json.parse(node));
            }
            Category category = Category.find.query().where().eq("name", "积分商城")
                    .orderBy().asc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null == category) return okJSON200();

            List<Category> list = Category.find.query().where()
                    .eq("show", Category.SHOW_CATEGORY)
                    .eq("parentId", category.id)
                    .orderBy()
                    .desc("sort")
                    .findList();
            ObjectNode result = Json.newObject();
            ArrayNode nodes = Json.newArray();
            list.forEach((each) -> {
                ObjectNode node = Json.newObject();
                node.put("id", each.id);
                node.put("name", each.name);
                nodes.add(node);
            });
            result.put(CODE, CODE200);
            result.set("list", nodes);
            cache.set(key, Json.stringify(result), 1 * 60);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/p/homepage_search_keywords/ 74首页搜索关键字
     * @apiName listHomepageSearchKeywords
     * @apiGroup Product
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {JsonArray} list 关键字列表
     * @apiSuccess (Success 200) {String} keyword 关键字
     * @apiSuccess (Success 200) {JsonArray} searchLogList 历史搜索（自己的）
     * @apiSuccess (Success 200) {String} keyword_ 关键字
     */
    public CompletionStage<Result> listHomepageSearchKeywords(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            String jsonCacheKey = cacheUtils.getShopKeywordsJsonCache();
            Optional<String> cacheOptional = cache.getOptional(jsonCacheKey);
            if (cacheOptional.isPresent()) {
                String node = cacheOptional.get();
                if (!ValidationUtil.isEmpty(node)) {
                    return ok(node);
                }
            }
            List<SearchKeyword> list = SearchKeyword.find.query().orderBy()
                    .desc("sort").setMaxRows(30).findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            cache.set(jsonCacheKey, Json.stringify(result));
            return ok(result);
        });
    }


}
