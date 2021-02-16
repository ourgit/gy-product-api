package models.promotion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券
 */
@Entity
@Table(name = "v1_coupon_config")
public class CouponConfig extends Model {
    //1生效，2失效
    public static final int STATUS_ENABLE = 1;
    public static final int STATUS_DISABLE = 2;

    //1全场，2指定ID
    public static final int ID_TYPE_ALL = 1;
    public static final int ID_TYPE_SPECIFIED_ID = 2;

    //1为满减券，2为注册用,3为酒吧线下用
    public static final int TYPE_ALL_CAN_USE = 1;
    public static final int TYPE_REG_USE = 2;
    public static final int TYPE_BAR_USE = 3;
    public static final int TYPE_POP_UP = 4;
    public static final int TYPE_POP_UP_NOT_NEED_GENERATE_RECORD = 5;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "coupon_title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String couponTitle;//标题

    @Column(name = "coupon_content")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String couponContent;//内容

    @Column(name = "shop_ids")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String shopIds;

    @Column(name = "shop_names")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String shopNames;

    @Column(name = "amount")
    public int amount;//面值，以分为单位

    @Column(name = "type")
    public int type;//类型

    @Column(name = "status")
    public int status;//1生效，2失效

    @Column(name = "need_show")
    public boolean needShow;

    @Column(name = "claim_per_member")
    public int claimLimitPerMember;//每人限领张数

    @Column(name = "total_amount")
    public int totalAmount;//总数

    @Column(name = "claim_amount")
    public int claimAmount;//已认领数量

    @Column(name = "id_type")
    public int idType;//ID类型,1全场，2品牌，3商品ID

    @Column(name = "rule_content")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String ruleContent;//规则内容

    @Column(name = "merchant_ids")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String merchantIds;//适用商品ID，以逗号隔开

    @Column(name = "brand_ids")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String brandIds;//适用品牌ID，以逗号隔开

    @Column(name = "bar_ids")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String barIds;//适用酒吧ID，多个ID以逗号隔开

    @Column(name = "img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String imgUrl;//图片

    @Column(name = "begin_time")
    public long beginTime;//生效时间

    @Column(name = "end_time")
    public long endTime;//失效时间

    @Column(name = "expire_days")
    public long expireDays;//有效期天数

    @Column(name = "expire_time")
    public long expireTime;

    @Column(name = "old_price")
    public int oldPrice;//原价

    @Column(name = "current_price")
    public int currentPrice;//现价

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "combo_coupon_id_list")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String comboCouponIdList;

    @Column(name = "combo_coupon_title_list")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String comboCouponTitleList;

    @Transient
    public boolean available;

    @Transient
    public List<CouponConfig> comboCouponList = new ArrayList<>();

    @Transient
    public String title;

    public static Finder<Long, CouponConfig> find = new Finder<>(CouponConfig.class);

    public void setId(long id) {
        this.id = id;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public void setCouponContent(String couponContent) {
        this.couponContent = couponContent;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRuleContent(String ruleContent) {
        this.ruleContent = ruleContent;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setExpireDays(long expireDays) {
        this.expireDays = expireDays;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setClaimLimitPerMember(int claimLimitPerMember) {
        this.claimLimitPerMember = claimLimitPerMember;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMerchantIds(String merchantIds) {
        this.merchantIds = merchantIds;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setClaimAmount(int claimAmount) {
        this.claimAmount = claimAmount;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public void setBrandIds(String brandIds) {
        this.brandIds = brandIds;
    }


    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public long getId() {
        return id;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public String getCouponContent() {
        return couponContent;
    }

    public int getAmount() {
        return amount;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public int getClaimLimitPerMember() {
        return claimLimitPerMember;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getClaimAmount() {
        return claimAmount;
    }

    public int getIdType() {
        return idType;
    }

    public String getRuleContent() {
        return ruleContent;
    }

    public String getMerchantIds() {
        return merchantIds;
    }

    public String getBrandIds() {
        return brandIds;
    }

    public String getBarIds() {
        return barIds;
    }

    public void setBarIds(String barIds) {
        this.barIds = barIds;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getExpireDays() {
        return expireDays;
    }

    public int getOldPrice() {
        return oldPrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public boolean isNeedShow() {
        return needShow;
    }

    public void setNeedShow(boolean needShow) {
        this.needShow = needShow;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComboCouponIdList() {
        return comboCouponIdList;
    }

    public void setComboCouponIdList(String comboCouponIdList) {
        this.comboCouponIdList = comboCouponIdList;
    }

    public String getComboCouponTitleList() {
        return comboCouponTitleList;
    }

    public void setComboCouponTitleList(String comboCouponTitleList) {
        this.comboCouponTitleList = comboCouponTitleList;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getShopIds() {
        return shopIds;
    }

    public void setShopIds(String shopIds) {
        this.shopIds = shopIds;
    }

    public String getShopNames() {
        return shopNames;
    }

    public void setShopNames(String shopNames) {
        this.shopNames = shopNames;
    }

    @Override
    public String toString() {
        return "CouponConfig{" +
                "id=" + id +
                ", couponTitle='" + couponTitle + '\'' +
                ", couponContent='" + couponContent + '\'' +
                ", amount=" + amount +
                ", type=" + type +
                ", status=" + status +
                ", needShow=" + needShow +
                ", claimLimitPerMember=" + claimLimitPerMember +
                ", totalAmount=" + totalAmount +
                ", claimAmount=" + claimAmount +
                ", idType=" + idType +
                ", ruleContent='" + ruleContent + '\'' +
                ", merchantIds='" + merchantIds + '\'' +
                ", brandIds='" + brandIds + '\'' +
                ", barIds='" + barIds + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", expireDays=" + expireDays +
                ", expireTime=" + expireTime +
                ", oldPrice=" + oldPrice +
                ", currentPrice=" + currentPrice +
                ", updateTime=" + updateTime +
                ", comboCouponIdList='" + comboCouponIdList + '\'' +
                ", comboCouponTitleList='" + comboCouponTitleList + '\'' +
                ", available=" + available +
                ", comboCouponList=" + comboCouponList +
                ", title='" + title + '\'' +
                '}';
    }
}

