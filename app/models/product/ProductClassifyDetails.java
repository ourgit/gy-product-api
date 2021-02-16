package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 产品聚合
 */
@Entity
@Table(name = "v1_product_classify_detail")
public class ProductClassifyDetails extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "classify_id")
    public long classifyId;

    @Column(name = "sort")
    public long sort;

    public static Finder<Long, ProductClassifyDetails> find = new Finder<>(ProductClassifyDetails.class);

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

    public long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(long classifyId) {
        this.classifyId = classifyId;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "ProductClassifyDetails{" +
                "id=" + id +
                ", productId=" + productId +
                ", classifyId=" + classifyId +
                ", sort=" + sort +
                '}';
    }
}
