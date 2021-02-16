package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 复购商品
 */
@Entity
@Table(name = "v1_regular_buy_product")
public class RegularBuyProduct extends Model {

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

    @Column(name = "total_count")
    private long totalCount;

    @Transient
    public Product product;

    public static Finder<Long, RegularBuyProduct> find = new Finder<>(RegularBuyProduct.class);

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

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "RegularBuyProduct{" +
                "id=" + id +
                ", productId=" + productId +
                ", skuId=" + skuId +
                ", uid=" + uid +
                ", totalCount=" + totalCount +
                ", product=" + product +
                '}';
    }
}
