package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义规格表
 */
@Entity
@Table(name = "v1_product_attr")
public class ProductAttr extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_id")
    public long productId;//商品编码

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "sort")
    public int sort;

    @Transient
    public List<ProductAttrOption> options = new ArrayList<>();

    public static Finder<Long, ProductAttr> find = new Finder<>(ProductAttr.class);

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "ProductAttr{" +
                "id=" + id +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                '}';
    }
}
