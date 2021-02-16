package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 产品聚合
 */
@Entity
@Table(name = "v1_product_mix")
public class ProductMix extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "mix_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String mixCode;

    public static Finder<Long, ProductMix> find = new Finder<>(ProductMix.class);

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

    public String getMixCode() {
        return mixCode;
    }

    public void setMixCode(String mixCode) {
        this.mixCode = mixCode;
    }

    @Override
    public String toString() {
        return "ProductMix{" +
                "id=" + id +
                ", productId=" + productId +
                ", mixCode='" + mixCode + '\'' +
                '}';
    }
}
