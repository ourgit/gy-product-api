package models.wine;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import models.product.Product;
import myannotation.EscapeHtmlSerializer;
import myannotation.EscapeHtmlSerializerForKeepSomeHtml;

import javax.persistence.*;

/**
 * 酒库
 */
@Entity
@Table(name = "v1_wine")
public class Wine extends Model {

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

    @Column(name = "name_en")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String englishName;

    @Column(name = "category_id")
    public String categoryId;

    @Column(name = "details")
    @JsonDeserialize(using = EscapeHtmlSerializerForKeepSomeHtml.class)
    public String details;

    @Column(name = "img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String imgUrl;

    @Column(name = "style")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String style;

    @Column(name = "style_id")
    public long styleId;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "alcohol_percent")
    public double alcoholPercent;

    @Column(name = "bitter_percent")
    public double bitterPercent;

    @Column(name = "brand_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String brandName;

    @Column(name = "production_place")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String productionPlace;

    @Column(name = "status")
    public int status;

    @Column(name = "wanna_count")
    public long wannaCount;

    @Column(name = "drunk_count")
    public long drunkCount;

    @Column(name = "comments")
    public long comments;

    @Column(name = "smell_rate")
    public int smellRate;

    @Column(name = "taste_rate")
    public int tasteRate;

    @Column(name = "shape_rate")
    public int shapeRate;

    @Column(name = "feel_rate")
    public int feelRate;

    @Column(name = "sort")
    public int sort;

    @Column(name = "tag")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String tag;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "create_time")
    public long createTime;

    @Transient
    public Product product;

    public static Finder<Long, Wine> find = new Finder<>(Wine.class);

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

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public long getStyleId() {
        return styleId;
    }

    public void setStyleId(long styleId) {
        this.styleId = styleId;
    }

    public double getAlcoholPercent() {
        return alcoholPercent;
    }

    public void setAlcoholPercent(double alcoholPercent) {
        this.alcoholPercent = alcoholPercent;
    }

    public double getBitterPercent() {
        return bitterPercent;
    }

    public void setBitterPercent(double bitterPercent) {
        this.bitterPercent = bitterPercent;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductionPlace() {
        return productionPlace;
    }

    public void setProductionPlace(String productionPlace) {
        this.productionPlace = productionPlace;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getWannaCount() {
        return wannaCount;
    }

    public void setWannaCount(long wannaCount) {
        this.wannaCount = wannaCount;
    }

    public long getDrunkCount() {
        return drunkCount;
    }

    public void setDrunkCount(long drunkCount) {
        this.drunkCount = drunkCount;
    }

    public int getSmellRate() {
        return smellRate;
    }

    public void setSmellRate(int smellRate) {
        this.smellRate = smellRate;
    }

    public int getTasteRate() {
        return tasteRate;
    }

    public void setTasteRate(int tasteRate) {
        this.tasteRate = tasteRate;
    }

    public int getShapeRate() {
        return shapeRate;
    }

    public void setShapeRate(int shapeRate) {
        this.shapeRate = shapeRate;
    }

    public int getFeelRate() {
        return feelRate;
    }

    public void setFeelRate(int feelRate) {
        this.feelRate = feelRate;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Wine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", englishName='" + englishName + '\'' +
                ", categoryId=" + categoryId +
                ", details='" + details + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", style='" + style + '\'' +
                ", styleId=" + styleId +
                ", productId=" + productId +
                ", alcoholPercent=" + alcoholPercent +
                ", bitterPercent=" + bitterPercent +
                ", brandName='" + brandName + '\'' +
                ", productionPlace='" + productionPlace + '\'' +
                ", status=" + status +
                ", wannaCount=" + wannaCount +
                ", drunkCount=" + drunkCount +
                ", smellRate=" + smellRate +
                ", tasteRate=" + tasteRate +
                ", shapeRate=" + shapeRate +
                ", feelRate=" + feelRate +
                ", sort=" + sort +
                ", tag='" + tag + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
