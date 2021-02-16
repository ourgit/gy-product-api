package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统规格表
 */
@Entity
@Table(name = "v1_system_attr")
public class SystemAttribute extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "sys_cate_type_id")
    public long sysCateTypeId;

    @Column(name = "sort")
    public int sort;

    @Transient
    public List<SystemAttributeOption> optionList = new ArrayList<>();

    public static Finder<Long, SystemAttribute> find = new Finder<>(SystemAttribute.class);

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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public long getSysCateTypeId() {
        return sysCateTypeId;
    }

    public void setSysCateTypeId(long sysCateTypeId) {
        this.sysCateTypeId = sysCateTypeId;
    }

    @Override
    public String toString() {
        return "SystemAttribute{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                '}';
    }
}
