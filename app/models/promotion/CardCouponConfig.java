package models.promotion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import models.shop.Shop;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;


@Entity
@Table(name = "v1_card_coupon_config")
public class CardCouponConfig extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "shop_id")
    public long shopId;

    @Column(name = "title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String title;

    @Column(name = "content")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String content;

    @Column(name = "digest")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String digest;

    @Column(name = "img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String imgUrl;//图片

    @Column(name = "filter")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filter;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "days")
    public long days;

    @Column(name = "no_limit_count")
    public boolean noLimitCount;

    @Column(name = "create_time")
    public long createTime;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "sku_id")
    public long skuId;

    @Column(name = "give_count")
    public int giveCount;

    @Transient
    public Shop shop;

    @Transient
    public String productName;

    @Transient
    public String skuName;


    public static Finder<Long, CardCouponConfig> find = new Finder<>(CardCouponConfig.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
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

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
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

    public boolean isNoLimitCount() {
        return noLimitCount;
    }

    public void setNoLimitCount(boolean noLimitCount) {
        this.noLimitCount = noLimitCount;
    }

    public int getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(int giveCount) {
        this.giveCount = giveCount;
    }

    @Override
    public String toString() {
        return "CardCouponConfig{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", digest='" + digest + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", filter='" + filter + '\'' +
                ", updateTime=" + updateTime +
                ", days=" + days +
                ", noLimitCount=" + noLimitCount +
                ", createTime=" + createTime +
                ", productId=" + productId +
                ", skuId=" + skuId +
                ", giveCount=" + giveCount +
                ", shop=" + shop +
                ", productName='" + productName + '\'' +
                ", skuName='" + skuName + '\'' +
                '}';
    }
}

