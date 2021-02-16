package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;


@Entity
@Table(name = "v1_member_card_coupon")
public class MemberCardCoupon extends Model {
    //1为未使用，2为已使用,3为已失效
    public static final int STATUS_CANCELED = -1;
    public static final int STATUS_NOT_USE = 1;
    public static final int STATUS_USED = 2;
    public static final int STATUS_EXPIRED = 3;
    public static final int STATUS_UNPAY = 4;
    public static final int STATUS_PAIED = 5;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "uid")
    public long uid;//用户ID

    @Column(name = "user_name")
    public String userName;

    @Column(name = "card_coupon_id")
    public long cardCouponId;

    @Column(name = "shop_id")
    public long shopId;

    @Column(name = "order_id")
    public long orderId;

    @Column(name = "title")
    public String title;

    @Column(name = "content")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String content;

    @Column(name = "digest")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String digest;

    @Column(name = "shop_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String shopName;

    @Column(name = "shop_address")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String shopAddress;

    @Column(name = "shop_avatar")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String shopAvatar;

    @Column(name = "end_time")
    public long endTime;//失效时间

    @Column(name = "status")
    public long status;//状态

    @Column(name = "code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String code;//券码

    @Column(name = "tx_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String transactionId;//流水ID

    @Column(name = "sub_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String subId;//第三方支付返回的事务ID

    @Column(name = "pay_type")
    public int payType;//支付方式

    @Column(name = "real_pay")
    public int realPay;//实付

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "use_time")
    public long useTime;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "sku_id")
    public long skuId;

    @Column(name = "no_limit_count")
    public boolean noLimitCount;

    @Column(name = "use_count")
    public long useCount;

    @Transient
    public String couponProductCoverImgUrl = "";

    @Transient
    public long couponProductPrice;

    @Transient
    public double lat;

    @Transient
    public double lng;
    public static Finder<Long, MemberCardCoupon> find = new Finder<>(MemberCardCoupon.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getCardCouponId() {
        return cardCouponId;
    }

    public void setCardCouponId(long cardCouponId) {
        this.cardCouponId = cardCouponId;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getRealPay() {
        return realPay;
    }

    public void setRealPay(int realPay) {
        this.realPay = realPay;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
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

    public long getUseCount() {
        return useCount;
    }

    public void setUseCount(long useCount) {
        this.useCount = useCount;
    }

    @Override
    public String toString() {
        return "MemberCardCoupon{" +
                "id=" + id +
                ", uid=" + uid +
                ", userName='" + userName + '\'' +
                ", cardCouponId=" + cardCouponId +
                ", shopId=" + shopId +
                ", orderId=" + orderId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", digest='" + digest + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", shopAvatar='" + shopAvatar + '\'' +
                ", endTime=" + endTime +
                ", status=" + status +
                ", code='" + code + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", subId='" + subId + '\'' +
                ", payType=" + payType +
                ", realPay=" + realPay +
                ", updateTime=" + updateTime +
                ", useTime=" + useTime +
                ", productId=" + productId +
                ", skuId=" + skuId +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}

