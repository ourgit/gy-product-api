package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 系统规格表
 */
@Entity
@Table(name = "v1_category_attr")
public class CategoryAttribute extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "category_id")
    public long categoryId;

    @Column(name = "sys_cate_type_id")
    public long sysCateTypeId;

    public static Finder<Long, CategoryAttribute> find = new Finder<>(CategoryAttribute.class);

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

    public long getSysCateTypeId() {
        return sysCateTypeId;
    }

    public void setSysCateTypeId(long sysCateTypeId) {
        this.sysCateTypeId = sysCateTypeId;
    }
}
