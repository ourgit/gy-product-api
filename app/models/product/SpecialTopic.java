package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;
import myannotation.EscapeHtmlSerializerForKeepSomeHtml;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 专题
 */
@Entity
@Table(name = "v1_product_special_topic")
public class SpecialTopic extends Model {

    public static final int STATUS_DRAFT = 1;
    public static final int STATUS_ENABLE = 2;
    public static final int STATUS_DISABLE = -1;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "cover_img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String coverImgUrl;//''封面图''

    @Column(name = "title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String title;

    @Column(name = "details")
    @JsonDeserialize(using = EscapeHtmlSerializerForKeepSomeHtml.class)
    public String details;//商品描述

    @Column(name = "product_count")
    public int productCount;

    @Column(name = "status")
    public int status;

    @Column(name = "create_time")
    public long createTime;

    @Transient
    public List<Product> productList = new ArrayList();

    public static Finder<Long, SpecialTopic> find = new Finder<>(SpecialTopic.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SpecialTopic{" +
                "id=" + id +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", productCount=" + productCount +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
