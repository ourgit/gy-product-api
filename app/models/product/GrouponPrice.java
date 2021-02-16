package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "v1_groupon_price")
public class GrouponPrice extends Model implements Serializable {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "price")
    public long price;

    @Column(name = "upto")
    public int upto;

    public static Finder<Long, GrouponPrice> find = new Finder<>(GrouponPrice.class);

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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getUpto() {
        return upto;
    }

    public void setUpto(int upto) {
        this.upto = upto;
    }
}
