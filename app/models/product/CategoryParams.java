package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 系统规格表
 */
@Entity
@Table(name = "v1_category_attr")
public class CategoryParams extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "name")
    public String name;

    @Column(name = "sort")
    public int sort;

    public static Finder<Long, CategoryParams> find = new Finder<>(CategoryParams.class);

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

    @Override
    public String toString() {
        return "SystemAttribute{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                '}';
    }
}
