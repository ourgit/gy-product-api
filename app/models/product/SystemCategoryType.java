package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 预定义商品分类表
 */
@Entity
@Table(name = "v1_system_category_type")
public class SystemCategoryType extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "attr_count")
    public int attrCount;

    @Column(name = "param_count")
    public int paramCount;

    @Column(name = "sort")
    public int sort;

    public static Finder<Long, SystemCategoryType> find = new Finder<>(SystemCategoryType.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttrCount() {
        return attrCount;
    }

    public void setAttrCount(int attrCount) {
        this.attrCount = attrCount;
    }

    public int getParamCount() {
        return paramCount;
    }

    public void setParamCount(int paramCount) {
        this.paramCount = paramCount;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
