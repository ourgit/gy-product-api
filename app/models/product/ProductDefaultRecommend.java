package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 推荐商品
 */
@Entity
@Table(name = "v1_product_default_recommend")
public class ProductDefaultRecommend extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_id")
    public long productId;

    @Transient
    public Product product;

    public static Finder<Long, ProductDefaultRecommend> find = new Finder<>(ProductDefaultRecommend.class);

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

    @Override
    public String toString() {
        return "ProductDefaultRecommend{" +
                "id=" + id +
                ", productId=" + productId +
                '}';
    }
}
