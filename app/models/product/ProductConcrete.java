package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 产品聚合
 */
@Entity
@Table(name = "v1_product_concrete")
public class ProductConcrete extends Model {

    public static int TYPE_AT_HOME = 1;
    public static int TYPE_NEW_ARRIVAL = 2;
    public static int TYPE_ON_PROMOTION = 3;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "concrete_type")
    public int concreteType;

    public static Finder<Long, ProductConcrete> find = new Finder<>(ProductConcrete.class);

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

    public int getConcreteType() {
        return concreteType;
    }

    public void setConcreteType(int concreteType) {
        this.concreteType = concreteType;
    }

    @Override
    public String toString() {
        return "ProductConcrete{" +
                "id=" + id +
                ", productId=" + productId +
                ", concreteType=" + concreteType +
                '}';
    }
}
