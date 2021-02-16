package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 缺货登记
 */
@Entity
@Table(name = "v1_out_stock_reg")
public class OutStockReg extends Model {

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

    @Column(name = "create_time")
    private long createTime;

    @Transient
    public Product product;

    @Transient
    public String productName;

    public static Finder<Long, OutStockReg> find = new Finder<>(OutStockReg.class);

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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "OutStockReg{" +
                "id=" + id +
                ", productId=" + productId +
                ", skuId=" + skuId +
                ", uid=" + uid +
                ", createTime=" + createTime +
                ", product=" + product +
                '}';
    }
}
