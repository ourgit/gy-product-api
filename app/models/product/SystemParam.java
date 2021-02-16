package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;
import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * 商品参数
 */
@Entity
@Table(name = "v1_system_param")
public class SystemParam extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "sys_cate_type_id")
    public long sysCateTypeId;

    @Column(name = "method")
    @Constraints.MaxLength(10)
    public String method;//input,select

    @Column(name = "value")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String value;

    @Column(name = "sort")
    public int sort;

    public static Finder<Long, SystemParam> find = new Finder<>(SystemParam.class);

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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "SystemParam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", method='" + method + '\'' +
                ", value='" + value + '\'' +
                ", sort=" + sort +
                '}';
    }
}
