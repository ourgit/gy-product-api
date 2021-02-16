package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 赠品
 */
@Entity
@Table(name = "v1_gift_product")
public class ProductGift extends Model {

    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_NOT_AVAILABLE = 2;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "gift_id")
    public long giftId;

    @Column(name = "product_id")
    public long productId;

    public static Finder<Long, ProductGift> find = new Finder<>(ProductGift.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGiftId() {
        return giftId;
    }

    public void setGiftId(long giftId) {
        this.giftId = giftId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "ProductGift{" +
                "id=" + id +
                ", giftId=" + giftId +
                ", productId=" + productId +
                '}';
    }
}
