package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.DateSerializer;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 商品图片
 */
@Entity
@Table(name = "v1_product_img")
public class ProductImage extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String imgUrl;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "tips")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String tips;

    @Column(name = "sort")
    public int sort;//显示顺序

    @Column(name = "update_time")
    @JsonSerialize(using = DateSerializer.class)
    public long updateTime;

    @Column(name = "create_time")
    @JsonSerialize(using = DateSerializer.class)
    public long createTime;


    public static Finder<Long, ProductImage> find = new Finder<>(ProductImage.class);

    public void setId(long id) {
        this.id = id;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }


    @Override
    public String toString() {
        return "ProductImage{" +
                "id=" + id +
                ", imgUrl='" + imgUrl + '\'' +
                ", productId=" + productId +
                ", tips='" + tips + '\'' +
                ", sort=" + sort +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
