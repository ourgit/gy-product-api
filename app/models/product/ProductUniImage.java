package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 商品统一图片
 */
@Entity
@Table(name = "v1_product_uni_img")
public class ProductUniImage extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "prepend_img1")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String prependImg1;

    @Column(name = "prepend_img2")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String prependImg2;

    @Column(name = "prepend_img3")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String prependImg3;

    @Column(name = "append_img1")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String appendImg1;

    @Column(name = "append_img2")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String appendImg2;

    @Column(name = "append_img3")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String appendImg3;

    @Column(name = "prepend_img1_category_id")
    public String prependImg1CategoryId = "";

    @Column(name = "prepend_img2_category_id")
    public String prependImg2CategoryId = "";

    @Column(name = "prepend_img3_category_id")
    public String prependImg3CategoryId = "";

    @Column(name = "append_img1_category_id")
    public String appendImg1CategoryId = "";

    @Column(name = "append_img2_category_id")
    public String appendImg2CategoryId = "";

    @Column(name = "append_img3_category_id")
    public String appendImg3CategoryId = "";

    public static Finder<Long, ProductUniImage> find = new Finder<>(ProductUniImage.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPrependImg1() {
        return prependImg1;
    }

    public void setPrependImg1(String prependImg1) {
        this.prependImg1 = prependImg1;
    }

    public String getPrependImg2() {
        return prependImg2;
    }

    public void setPrependImg2(String prependImg2) {
        this.prependImg2 = prependImg2;
    }

    public String getPrependImg3() {
        return prependImg3;
    }

    public void setPrependImg3(String prependImg3) {
        this.prependImg3 = prependImg3;
    }

    public String getAppendImg1() {
        return appendImg1;
    }

    public void setAppendImg1(String appendImg1) {
        this.appendImg1 = appendImg1;
    }

    public String getAppendImg2() {
        return appendImg2;
    }

    public void setAppendImg2(String appendImg2) {
        this.appendImg2 = appendImg2;
    }

    public String getAppendImg3() {
        return appendImg3;
    }

    public void setAppendImg3(String appendImg3) {
        this.appendImg3 = appendImg3;
    }

    public String getPrependImg1CategoryId() {
        return prependImg1CategoryId;
    }

    public void setPrependImg1CategoryId(String prependImg1CategoryId) {
        this.prependImg1CategoryId = prependImg1CategoryId;
    }

    public String getPrependImg2CategoryId() {
        return prependImg2CategoryId;
    }

    public void setPrependImg2CategoryId(String prependImg2CategoryId) {
        this.prependImg2CategoryId = prependImg2CategoryId;
    }

    public String getPrependImg3CategoryId() {
        return prependImg3CategoryId;
    }

    public void setPrependImg3CategoryId(String prependImg3CategoryId) {
        this.prependImg3CategoryId = prependImg3CategoryId;
    }

    public String getAppendImg1CategoryId() {
        return appendImg1CategoryId;
    }

    public void setAppendImg1CategoryId(String appendImg1CategoryId) {
        this.appendImg1CategoryId = appendImg1CategoryId;
    }

    public String getAppendImg2CategoryId() {
        return appendImg2CategoryId;
    }

    public void setAppendImg2CategoryId(String appendImg2CategoryId) {
        this.appendImg2CategoryId = appendImg2CategoryId;
    }

    public String getAppendImg3CategoryId() {
        return appendImg3CategoryId;
    }

    public void setAppendImg3CategoryId(String appendImg3CategoryId) {
        this.appendImg3CategoryId = appendImg3CategoryId;
    }

    @Override
    public String toString() {
        return "ProductUniImage{" +
                "id=" + id +
                ", prependImg1='" + prependImg1 + '\'' +
                ", prependImg2='" + prependImg2 + '\'' +
                ", prependImg3='" + prependImg3 + '\'' +
                ", appendImg1='" + appendImg1 + '\'' +
                ", appendImg2='" + appendImg2 + '\'' +
                ", appendImg3='" + appendImg3 + '\'' +
                '}';
    }
}
