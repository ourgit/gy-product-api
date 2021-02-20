package models.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import models.groupon.Groupon;
import myannotation.EscapeHtmlSerializer;
import myannotation.EscapeHtmlSerializerForKeepSomeHtml;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品表
 */
@Entity
@Table(name = "v1_product")
public class Product extends Model {
    //state 审核状态 -1 审核失败 0 未审核 1 审核成功
    //status 商品状态 -1=>下架,1=>上架,2=>预售,0=>草稿',
    public static final int STATUS_OFF_SHELVE = -1;
    public static final int STATUS_ON_SHELVE = 1;
    public static final int STATUS_PRE_SALE = 2;
    public static final int STATUS_DRAFT = 3;

    public static final int STATE_FAILED = -1;
    public static final int STATE_NOT_AUDIT = 0;
    public static final int STATE_AUDITTED = 1;

    public static final int UNIT_CASE = 1;
    public static final int UNIT_BOTTLE = 2;
    public static final int UNIT_PIECE = 3;

    public static final int TYPE_NORMAL_PRODUCT = 1;
    public static final int TYPE_SERVICE_PRODUCT = 2;
    public static final int TYPE_THIRD_RUN = 3;
    public static final int TYPE_ENROLL = 4;
    public static final int TYPE_SCORE = 10;

    public static final int ACTIVITY_TYPE_NORMAL = 1;
    public static final int ACTIVITY_TYPE_GROUPON = 2;
    public static final int ACTIVITY_TYPE_FLASH = 3;
    public static final int ACTIVITY_TYPE_SCORE = TYPE_SCORE;
    public static final int ACTIVITY_TYPE_WHOLESALE = 5;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "category_id")
    public String categoryId = "";

    @Column(name = "shop_category_id")
    public String shopCategoryId = "";

    @Column(name = "brand_id")
    public long brandId;

    @Column(name = "product_type")
    public int productType;

    @Column(name = "org_id")
    public long shopId;//商家编号

    @Column(name = "type_id")
    public long typeId;//类型编号

    @Column(name = "self_run")
    public int selfRun;

    @Column(name = "sketch")
    public String sketch = "";//简述

    @Column(name = "details")
    @JsonDeserialize(using = EscapeHtmlSerializerForKeepSomeHtml.class)
    public String details = "";//商品描述

    @Column(name = "keywords")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String keywords = "";

    @Column(name = "tag")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String tag = "";

    @Column(name = "marque")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String marque = "";//商品型号

    @Column(name = "barcode")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String barcode = "";

    @Column(name = "price")
    public long price;

    @Column(name = "max_score_used")
    public int maxScoreUsed;//最多可使用积分抵消

    @Column(name = "sold_amount")
    public long soldAmount;//已售数量

    @Column(name = "virtual_amount")
    @JsonIgnore
    public long virtualAmount;

    @Column(name = "weight")
    public double weight;//重量

    @Column(name = "volume")
    public double volume;//体积

    @Column(name = "cover_img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String coverImgUrl = "";//''封面图''

    @Column(name = "poster")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String poster = "";

    @Column(name = "mix_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String mixCode = "";//混搭编号

    @Column(name = "supplier_uid")
    public long supplierUid;

    @Column(name = "supplier_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String supplierName = "";

    @Column(name = "status")
    public int status;// '状态 -1=>下架,1=>上架,2=>预售,0=>未上架',

    @Column(name = "state")
    public int state;// '审核状态 -1 审核失败 0 未审核 1 审核成功',

    @Column(name = "is_combo")
    public boolean isCombo;//'是否是套餐'

    @Column(name = "place_home_top")
    public boolean placeHomeTop;

    @Column(name = "place_shop_top")
    public boolean placeShopTop;

    @Column(name = "allow_use_score")
    public boolean allowUseScore;//'是否是积分产品'

    @Column(name = "sort")
    public int sort;

    @Column(name = "cover_frame_id")
    public long coverFrameId;

    @Column(name = "cover_frame_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String coverFrameUrl = "";

    @Column(name = "unit")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String unit = "";

    @Column(name = "product_tab_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String productTabId = "";

    @Column(name = "delivery_methods")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String deliveryMethods = "";

    @Column(name = "deleted_at")
    public long deletedAt;

    @Column(name = "activity_type")
    public int activityType;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "create_time")
    public long createTime;

    @Column(name = "begin_time")
    public long beginTime;

    @Column(name = "end_time")
    public long endTime;

    @Column(name = "begin_hour")
    public int beginHour;

    @Transient
    public long wishAmount;

    @Transient
    public long grouponRequireCount;

    @Transient
    public long grouponTimelimit;

    @Transient
    public ProductSku defaultSku;

    @Transient
    public List<ProductSku> favorSkuList = new ArrayList<>();

    @Transient
    public ProductFavorProducts favorProducts;

    @Transient
    public FlashSaleProduct flashSaleProduct;

    @Transient
    public List<ProductFavorDetail> favorDetailList = new ArrayList<>();

    @Transient
    public List<ProductParam> paramList = new ArrayList<>();

    @Transient
    public List<String> avatarList = new ArrayList<>();

    @Transient
    public List<GrouponPrice> grouponSkuPriceList = new ArrayList<>();

    @Transient
    public Groupon groupon;

    public static Finder<Long, Product> find = new Finder<>(Product.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getSketch() {
        return sketch;
    }

    public void setSketch(String sketch) {
        this.sketch = sketch;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getMaxScoreUsed() {
        return maxScoreUsed;
    }

    public void setMaxScoreUsed(int maxScoreUsed) {
        this.maxScoreUsed = maxScoreUsed;
    }


    public long getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(long soldAmount) {
        this.soldAmount = soldAmount;
    }


    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getMixCode() {
        return mixCode;
    }

    public void setMixCode(String mixCode) {
        this.mixCode = mixCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isCombo() {
        return isCombo;
    }

    public void setCombo(boolean combo) {
        isCombo = combo;
    }

    public boolean isAllowUseScore() {
        return allowUseScore;
    }

    public void setAllowUseScore(boolean allowUseScore) {
        this.allowUseScore = allowUseScore;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public long getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(long deletedAt) {
        this.deletedAt = deletedAt;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public long getCoverFrameId() {
        return coverFrameId;
    }

    public void setCoverFrameId(long coverFrameId) {
        this.coverFrameId = coverFrameId;
    }

    public String getCoverFrameUrl() {
        return coverFrameUrl;
    }

    public void setCoverFrameUrl(String coverFrameUrl) {
        this.coverFrameUrl = coverFrameUrl;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getSelfRun() {
        return selfRun;
    }

    public void setSelfRun(int selfRun) {
        this.selfRun = selfRun;
    }

    public long getVirtualAmount() {
        return virtualAmount;
    }

    public void setVirtualAmount(long virtualAmount) {
        this.virtualAmount = virtualAmount;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getSupplierUid() {
        return supplierUid;
    }

    public void setSupplierUid(long supplierUid) {
        this.supplierUid = supplierUid;
    }

    public int getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    public String getProductTabId() {
        return productTabId;
    }

    public void setProductTabId(String productTabId) {
        this.productTabId = productTabId;
    }

    public String getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(String shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public boolean isPlaceHomeTop() {
        return placeHomeTop;
    }

    public void setPlaceHomeTop(boolean placeHomeTop) {
        this.placeHomeTop = placeHomeTop;
    }

    public boolean isPlaceShopTop() {
        return placeShopTop;
    }

    public void setPlaceShopTop(boolean placeShopTop) {
        this.placeShopTop = placeShopTop;
    }

    public String getDeliveryMethods() {
        return deliveryMethods;
    }

    public void setDeliveryMethods(String deliveryMethods) {
        this.deliveryMethods = deliveryMethods;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", brandId=" + brandId +
                ", productType=" + productType +
                ", orgId=" + shopId +
                ", typeId=" + typeId +
                ", selfRun=" + selfRun +
                ", sketch='" + sketch + '\'' +
                ", details='" + details + '\'' +
                ", keywords='" + keywords + '\'' +
                ", tag='" + tag + '\'' +
                ", marque='" + marque + '\'' +
                ", barcode='" + barcode + '\'' +
                ", price=" + price +
                ", maxScoreUsed=" + maxScoreUsed +
                ", soldAmount=" + soldAmount +
                ", virtualAmount=" + virtualAmount +
                ", weight=" + weight +
                ", volume=" + volume +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", poster='" + poster + '\'' +
                ", mixCode='" + mixCode + '\'' +
                ", supplierUid=" + supplierUid +
                ", supplierName='" + supplierName + '\'' +
                ", status=" + status +
                ", state=" + state +
                ", isCombo=" + isCombo +
                ", allowUseScore=" + allowUseScore +
                ", sort=" + sort +
                ", coverFrameId=" + coverFrameId +
                ", coverFrameUrl='" + coverFrameUrl + '\'' +
                ", unit='" + unit + '\'' +
                ", productTabId='" + productTabId + '\'' +
                ", deletedAt=" + deletedAt +
                ", activityType=" + activityType +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", beginHour=" + beginHour +
                ", wishAmount=" + wishAmount +
                ", defaultSku=" + defaultSku +
                '}';
    }
}
