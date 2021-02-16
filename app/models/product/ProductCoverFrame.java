package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 邮费配置
 */
@Entity
@Table(name = "v1_product_cover_frame")
public class ProductCoverFrame extends Model {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String imgUrl;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    public static Finder<Integer, ProductCoverFrame> find = new Finder<>(ProductCoverFrame.class);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProductCoverFrame{" +
                "id=" + id +
                ", imgUrl='" + imgUrl + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
