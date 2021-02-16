package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品聚合
 */
@Entity
@Table(name = "v1_product_classify")
public class ProductClassify extends Model {

    public static final int STATUS_SHOW = 1;
    public static final int STATUS_HIDE = 2;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "classify_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String classifyCode;

    @Column(name = "sub_title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String subTitle;

    @Column(name = "head_pic")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String headPic;

    @Column(name = "icon")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String icon;

    @Column(name = "bg_color")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String bgColor;

    @Column(name = "font_color")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String fontColor;

    @Column(name = "link_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String linkUrl;

    @Column(name = "status")
    public int status;

    @Column(name = "classify_type")
    public int classifyType;

    @Column(name = "height")
    public int height;

    @Column(name = "sort")
    public long sort;

    @Column(name = "product_count")
    public int productCount;

    @Column(name = "parent_id")
    public long parentId;

    @Column(name = "is_parent")
    public boolean isParent;

    @Column(name = "activity_type")
    public int activityType;

    @Transient
    public List<Product> productList = new ArrayList();

    public static Finder<Long, ProductClassify> find = new Finder<>(ProductClassify.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClassifyCode() {
        return classifyCode;
    }

    public void setClassifyCode(String classifyCode) {
        this.classifyCode = classifyCode;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getClassifyType() {
        return classifyType;
    }

    public void setClassifyType(int classifyType) {
        this.classifyType = classifyType;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Override
    public String toString() {
        return "ProductClassify{" +
                "id=" + id +
                ", classifyCode='" + classifyCode + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", headPic='" + headPic + '\'' +
                ", icon='" + icon + '\'' +
                ", bgColor='" + bgColor + '\'' +
                ", fontColor='" + fontColor + '\'' +
                ", status=" + status +
                ", classifyType=" + classifyType +
                ", height=" + height +
                ", sort=" + sort +
                ", productCount=" + productCount +
                ", parentId=" + parentId +
                ", isParent=" + isParent +
                ", activityType=" + activityType +
                ", productList=" + productList +
                '}';
    }
}
