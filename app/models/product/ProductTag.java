package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 商品参数
 */
@Entity
@Table(name = "v1_product_tag")
public class ProductTag extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_id")
    public long productId;//商品编码

    @Column(name = "tag")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String tag;

    @Transient
    public Product product;

    public static Finder<Long, ProductTag> find = new Finder<>(ProductTag.class);

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "ProductTag{" +
                "id=" + id +
                ", productId=" + productId +
                ", tag='" + tag + '\'' +
                '}';
    }
}
