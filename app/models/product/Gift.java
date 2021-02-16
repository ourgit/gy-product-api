package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;
import myannotation.EscapeHtmlSerializerForKeepSomeHtml;

import javax.persistence.*;

/**
 * 赠品
 */
@Entity
@Table(name = "v1_gift")
public class Gift extends Model {

    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_NOT_AVAILABLE = 2;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "sketch")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String sketch = "";//简述

    @Column(name = "details")
    @JsonDeserialize(using = EscapeHtmlSerializerForKeepSomeHtml.class)
    public String details = "";//商品描述

    @Column(name = "price")
    public double price;

    @Column(name = "give_amount")
    public long giveAmount;

    @Column(name = "gift_product_id")
    public long giftProductId;

    @Column(name = "total_amount")
    public long totalAmount;

    @Column(name = "cover_img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String coverImgUrl = "";//''封面图''

    @Column(name = "product_ids")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String productIds = "";//''封面图''

    @Column(name = "status")
    public int status;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, Gift> find = new Finder<>(Gift.class);

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getGiveAmount() {
        return giveAmount;
    }

    public void setGiveAmount(long giveAmount) {
        this.giveAmount = giveAmount;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public long getGiftProductId() {
        return giftProductId;
    }

    public void setGiftProductId(long giftProductId) {
        this.giftProductId = giftProductId;
    }

    @Override
    public String toString() {
        return "Gift{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sketch='" + sketch + '\'' +
                ", details='" + details + '\'' +
                ", price=" + price +
                ", giveAmount=" + giveAmount +
                ", totalAmount=" + totalAmount +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", productIds='" + productIds + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
