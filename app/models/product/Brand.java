package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 品牌
 */
@Entity
@Table(name = "v1_brand")
public class Brand extends Model {

    //1正常，2暂停使用3下架
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_NORMAL_PAUSE = 2;
    public static final int STATUS_NORMAL_OFFSHELF = 3;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String url;

    @Column(name = "logo")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String logo;

    @Column(name = "poster")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String poster;//宣传图

    @Column(name = "content")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String content;

    @Column(name = "sort")
    public int sort;

    @Column(name = "status")
    public int status;//1正常，2暂停使用3下架

    @Column(name = "seo_title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String seoTitle;

    @Column(name = "seo_keywords")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String seoKeywords;

    @Column(name = "seo_description")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String seoDescription;

    @Column(name = "show_at_home")
    public boolean showAtHome;//是否在主页显示

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, Brand> find = new Finder<>(Brand.class);

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public void setShowAtHome(boolean showAtHome) {
        this.showAtHome = showAtHome;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", logo='" + logo + '\'' +
                ", poster='" + poster + '\'' +
                ", content='" + content + '\'' +
                ", sort=" + sort +
                ", status=" + status +
                ", seoTitle='" + seoTitle + '\'' +
                ", seoKeywords='" + seoKeywords + '\'' +
                ", seoDescription='" + seoDescription + '\'' +
                ", showAtHome=" + showAtHome +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
