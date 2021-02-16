package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 商品收藏
 */
@Entity
@Table(name = "v1_product_fav")
public class ProductFav extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "sku_id")
    private long skuId;

    @Column(name = "uid")
    private long uid;

    @Column(name = "enable")
    private boolean enable;

    @Column(name = "create_time")
    private long createTime;

    @Transient
    public Product product;

    public static Finder<Long, ProductFav> find = new Finder<>(ProductFav.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }

    @Override
    public String toString() {
        return "ProductFav{" +
                "id=" + id +
                ", productId=" + productId +
                ", skuId=" + skuId +
                ", uid=" + uid +
                ", enable=" + enable +
                ", createTime=" + createTime +
                ", product=" + product +
                '}';
    }
}
