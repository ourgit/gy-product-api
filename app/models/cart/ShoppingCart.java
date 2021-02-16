package models.cart;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 购物车
 */
@Entity
@Table(name = "v1_shopping_cart")
public class ShoppingCart extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "uid")
    public long uid;//用户id

    @Column(name = "product_id")
    public long productId;//商品id

    @Column(name = "sku_id")
    public long skuId;

    @Column(name = "amount")
    public long amount;//购买的数量

    @Column(name = "enable")
    public boolean enable;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, ShoppingCart> find = new Finder<>(ShoppingCart.class);

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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", uid=" + uid +
                ", productId=" + productId +
                ", skuId=" + skuId +
                ", amount=" + amount +
                ", enable=" + enable +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
