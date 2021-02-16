package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品TAB
 */
@Entity
@Table(name = "v1_product_tab")
public class ProductTab extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "tab_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String tabName;

    @Column(name = "head_pic")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String headPic;

    @Column(name = "bg_color")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String bgColor;

    @Column(name = "sort")
    public int sort;

    @Column(name = "enable")
    public boolean enable;

    @Transient
    public List<ProductTabClassify> tabClassifyList = new ArrayList();

    @Transient
    public List<Product> productList = new ArrayList();

    public static Finder<Long, ProductTab> find = new Finder<>(ProductTab.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "ProductTab{" +
                "id=" + id +
                ", tabName='" + tabName + '\'' +
                ", headPic='" + headPic + '\'' +
                ", bgColor='" + bgColor + '\'' +
                ", sort=" + sort +
                ", enable=" + enable +
                ", tabClassifyList=" + tabClassifyList +
                ", productList=" + productList +
                '}';
    }
}
