package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品参数
 */
@Entity
@Table(name = "v1_product_param")
public class ProductParam extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_id")
    public long productId;//商品编码

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "value")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String value;

    @Column(name = "need_show")
    public boolean needShow;

    @Column(name = "sort")
    public int sort;

    @Transient
    public String method = "";

    @Transient
    public List<String> list = new ArrayList();

    public static Finder<Long, ProductParam> find = new Finder<>(ProductParam.class);

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isNeedShow() {
        return needShow;
    }

    public void setNeedShow(boolean needShow) {
        this.needShow = needShow;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "ProductParam{" +
                "id=" + id +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", needShow=" + needShow +
                ", sort=" + sort +
                ", method='" + method + '\'' +
                ", list=" + list +
                '}';
    }
}
