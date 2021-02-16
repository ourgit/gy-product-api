package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 商品分类
 */
@Entity
@Table(name = "v1_category_classify")
public class CategoryClassify extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "category_id")
    public long categoryId;

    @Column(name = "classify_id")
    public long classifyId;

    @Column(name = "classify_name")
    public String classifyName;

    @Column(name = "cover_img")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String coverImg;

    @Column(name = "sort")
    public long sort;

    public static Finder<Long, CategoryClassify> find = new Finder<>(CategoryClassify.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(long classifyId) {
        this.classifyId = classifyId;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    @Override
    public String toString() {
        return "CategoryClassify{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", classifyId=" + classifyId +
                ", classifyName='" + classifyName + '\'' +
                ", coverImg='" + coverImg + '\'' +
                ", sort=" + sort +
                '}';
    }
}
