package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 浏览记录
 */
@Entity
@Table(name = "v1_browse_log")
public class BrowseLog extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "uid")
    private long uid;

    @Column(name = "create_time")
    private long createTime;

    @Transient
    public Product product;

    public static Finder<Long, BrowseLog> find = new Finder<>(BrowseLog.class);

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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "BrowseLog{" +
                "id=" + id +
                ", productId=" + productId +
                ", uid=" + uid +
                ", createTime=" + createTime +
                ", product=" + product +
                '}';
    }
}
