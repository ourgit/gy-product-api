package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 推荐商品
 */
@Entity
@Table(name = "v1_product_relate")
public class ProductRelate extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "combo_product_id")
    public long comboProductId;

    @Column(name = "relate_product_id")
    public long relateProductId;

    @Transient
    public Product product;

    public static Finder<Long, ProductRelate> find = new Finder<>(ProductRelate.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getComboProductId() {
        return comboProductId;
    }

    public void setComboProductId(long comboProductId) {
        this.comboProductId = comboProductId;
    }

    public long getRelateProductId() {
        return relateProductId;
    }

    public void setRelateProductId(long relateProductId) {
        this.relateProductId = relateProductId;
    }

    @Override
    public String toString() {
        return "ProductRelate{" +
                "id=" + id +
                ", comboProductId=" + comboProductId +
                ", relateProductId=" + relateProductId +
                ", product=" + product +
                '}';
    }
}
