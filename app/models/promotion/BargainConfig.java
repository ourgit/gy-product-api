package models.promotion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

@Entity
@Table(name = "v1_bargain_config")
public class BargainConfig extends Model {
    public static final int STATUS_NOT_START = 1;
    public static final int STATUS_PROCESSING = 2;
    public static final int STATUS_END = 3;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String title;//标题

    @Column(name = "content")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String content;//内容

    @Column(name = "rule_content")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String ruleContent;

    @Column(name = "require_invites")
    public int requireInvites;

    @Column(name = "img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String imgUrl;//图片

    @Column(name = "begin_time")
    public long beginTime;//生效时间

    @Column(name = "end_time")
    public long endTime;//失效时间

    @Column(name = "product_id")
    public long productId;

    @Column(name = "sku_id")
    public long skuId;

    @Column(name = "expire_hours")
    public long expireHours;

    @Column(name = "need_pay_money")
    public long needPayMoney;

    @Column(name = "status")
    public int status;

    @Column(name = "need_show")
    public boolean needShow;

    @Column(name = "need_uni_time")
    public boolean needUniTime;

    @Column(name = "max_count")
    public long maxCount;

    @Column(name = "already_succeed_count")
    public long alreadySucceedAssistAmount;

    @Column(name = "use_system_rules")
    public boolean useSystemRules;

    @Column(name = "need_address")
    public boolean needAddress;

    @Column(name = "limit_location")
    public String limitLocation = "";

    @Transient
    public boolean available;

    @Transient
    public Bargain bargain;

    @Transient
    public String couponProductCoverImgUrl = "";

    @Transient
    public long couponProductPrice;

    public static Finder<Long, BargainConfig> find = new Finder<>(BargainConfig.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRuleContent() {
        return ruleContent;
    }

    public void setRuleContent(String ruleContent) {
        this.ruleContent = ruleContent;
    }

    public int getRequireInvites() {
        return requireInvites;
    }

    public void setRequireInvites(int requireInvites) {
        this.requireInvites = requireInvites;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public long getExpireHours() {
        return expireHours;
    }

    public void setExpireHours(long expireHours) {
        this.expireHours = expireHours;
    }

    public long getNeedPayMoney() {
        return needPayMoney;
    }

    public void setNeedPayMoney(long needPayMoney) {
        this.needPayMoney = needPayMoney;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isNeedShow() {
        return needShow;
    }

    public void setNeedShow(boolean needShow) {
        this.needShow = needShow;
    }

    public boolean isNeedUniTime() {
        return needUniTime;
    }

    public void setNeedUniTime(boolean needUniTime) {
        this.needUniTime = needUniTime;
    }

    public long getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(long maxCount) {
        this.maxCount = maxCount;
    }

    public long getAlreadySucceedAssistAmount() {
        return alreadySucceedAssistAmount;
    }

    public void setAlreadySucceedAssistAmount(long alreadySucceedAssistAmount) {
        this.alreadySucceedAssistAmount = alreadySucceedAssistAmount;
    }

    public boolean isUseSystemRules() {
        return useSystemRules;
    }

    public void setUseSystemRules(boolean useSystemRules) {
        this.useSystemRules = useSystemRules;
    }

    public boolean isNeedAddress() {
        return needAddress;
    }

    public void setNeedAddress(boolean needAddress) {
        this.needAddress = needAddress;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Bargain getBargain() {
        return bargain;
    }

    public void setBargain(Bargain bargain) {
        this.bargain = bargain;
    }

    public String getCouponProductCoverImgUrl() {
        return couponProductCoverImgUrl;
    }

    public void setCouponProductCoverImgUrl(String couponProductCoverImgUrl) {
        this.couponProductCoverImgUrl = couponProductCoverImgUrl;
    }

    public long getCouponProductPrice() {
        return couponProductPrice;
    }

    public void setCouponProductPrice(long couponProductPrice) {
        this.couponProductPrice = couponProductPrice;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }

    public String getLimitLocation() {
        return limitLocation;
    }

    public void setLimitLocation(String limitLocation) {
        this.limitLocation = limitLocation;
    }
}

