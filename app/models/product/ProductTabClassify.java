package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 产品TAB
 */
@Entity
@Table(name = "v1_product_tab_classify")
public class ProductTabClassify extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_tab_id")
    public long productTabId;

    @Column(name = "classify_id")
    public long classifyId;

    @Column(name = "classify_cover_img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String classifyCoverImgUrl;

    @Column(name = "classify_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String classifyName;

    @Column(name = "sort")
    public int sort;

    public static Finder<Long, ProductTabClassify> find = new Finder<>(ProductTabClassify.class);

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

    public long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(long classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyCoverImgUrl() {
        return classifyCoverImgUrl;
    }

    public void setClassifyCoverImgUrl(String classifyCoverImgUrl) {
        this.classifyCoverImgUrl = classifyCoverImgUrl;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "ProductTabClassify{" +
                "id=" + id +
                ", productTabId=" + productTabId +
                ", classifyId=" + classifyId +
                ", classifyCoverImgUrl='" + classifyCoverImgUrl + '\'' +
                ", classifyName='" + classifyName + '\'' +
                ", sort=" + sort +
                '}';
    }
}
