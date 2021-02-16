package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 产品TAB
 */
@Entity
@Table(name = "v1_product_favor_products")
public class ProductFavorProducts extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "favor_id")
    public long favorId;

    @Column(name = "favor_name")
    public String favorName;//优惠方案名字

    @Column(name = "product_id")
    public long productId;

    @Column(name = "sku_id")
    public long skuId;

    @Column(name = "sku_name")
    public String skuName;

    @Column(name = "begin_time")
    public long beginTime;

    @Column(name = "end_time")
    public long endTime;

    @Column(name = "require_type")
    public int requireType;

    @Column(name = "status")
    public int status;

    @Column(name = "limit_amount")
    public long limitAmount;//限购数量

    @Column(name = "sold_amount")
    public long soldAmount;//已售数量

    public static Finder<Long, ProductFavorProducts> find = new Finder<>(ProductFavorProducts.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFavorId() {
        return favorId;
    }

    public void setFavorId(long favorId) {
        this.favorId = favorId;
    }

    public String getFavorName() {
        return favorName;
    }

    public void setFavorName(String favorName) {
        this.favorName = favorName;
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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
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

    public int getRequireType() {
        return requireType;
    }

    public void setRequireType(int requireType) {
        this.requireType = requireType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(long limitAmount) {
        this.limitAmount = limitAmount;
    }

    public long getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(long soldAmount) {
        this.soldAmount = soldAmount;
    }

    @Override
    public String toString() {
        return "ProductFavorProducts{" +
                "id=" + id +
                ", favorId=" + favorId +
                ", favorName='" + favorName + '\'' +
                ", productId=" + productId +
                ", skuId=" + skuId +
                ", skuName='" + skuName + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", requireType=" + requireType +
                ", status=" + status +
                ", limitAmount=" + limitAmount +
                ", soldAmount=" + soldAmount +
                '}';
    }
}
