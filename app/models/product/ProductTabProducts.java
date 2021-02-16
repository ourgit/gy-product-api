package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 产品TAB
 */
@Entity
@Table(name = "v1_product_tab_products")
public class ProductTabProducts extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_tab_id")
    public long productTabId;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "sort")
    public int sort;

    public static Finder<Long, ProductTabProducts> find = new Finder<>(ProductTabProducts.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductTabId() {
        return productTabId;
    }

    public void setProductTabId(long productTabId) {
        this.productTabId = productTabId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "ProductTabProducts{" +
                "id=" + id +
                ", productTabId=" + productTabId +
                ", productId=" + productId +
                ", sort=" + sort +
                '}';
    }
}
