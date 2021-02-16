package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 规格属性绑定表
 */
@Entity
@Table(name = "v1_product_attr_option")
public class ProductAttrOption extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "option_id")
    public long optionId;//属性选项编码

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;//选项名称

    @Column(name = "attr_id")
    public long attrId;//属性编号

    @Column(name = "supplier_option_id")
    public long supplierOptionId;

    @Column(name = "sort")
    public int sort;

    public static Finder<Long, ProductAttrOption> find = new Finder<>(ProductAttrOption.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOptionId() {
        return optionId;
    }

    public void setOptionId(long optionId) {
        this.optionId = optionId;
    }

    public long getAttrId() {
        return attrId;
    }

    public void setAttrId(long attrId) {
        this.attrId = attrId;
    }

    public long getSupplierOptionId() {
        return supplierOptionId;
    }

    public void setSupplierOptionId(long supplierOptionId) {
        this.supplierOptionId = supplierOptionId;
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
}
