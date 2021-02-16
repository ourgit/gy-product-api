package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 推荐商品
 */
@Entity
@Table(name = "v1_product_recommend")
public class ProductRecommend extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "recommend_product_id")
    public long recommendProductId;

    @Transient
    public Product product;

    public static Finder<Long, ProductRecommend> find = new Finder<>(ProductRecommend.class);

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

    public long getRecommendProductId() {
        return recommendProductId;
    }

    public void setRecommendProductId(long recommendProductId) {
        this.recommendProductId = recommendProductId;
    }

    @Override
    public String toString() {
        return "ProductRecommend{" +
                "id=" + id +
                ", productId=" + productId +
                ", recommendProductId=" + recommendProductId +
                '}';
    }
}
