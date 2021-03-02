package models.shop;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import models.product.Product;
import myannotation.EscapeHtmlSerializer;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "v1_shop")
public class Shop extends Model {

    public static final int SELF_RUN = 1;
    public static final int THIRD_RUN = 2;

    //状态，1为正常，2为被锁定，3为待审核 4审核不通过 5审核驳回
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_LOCK = 2;
    public static final int STATUS_NEED_APPROVE = 3;
    public static final int STATUS_DENY = 4;
    public static final int STATUS_RETURN = 5;
    public static final int STATUS_DELETED = 6;
    public static final int STATUS_NOT_SHOW = 7;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "status")
    public int status;

    @Column(name = "run_type")
    public int runType;

    @Column(name = "shop_level")
    public int shopLevel;

    @Column(name = "name")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "digest")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String digest;

    @Column(name = "contact_number")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String contactNumber;//联系电话

    @Column(name = "contact_name")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String contactName;//联系人

    @Column(name = "contact_address")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String contactAddress;//联系地址

    @Column(name = "license_number")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String licenseNumber;//营业执照号

    @Column(name = "license_img")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String licenseImg;//营业执照图片

    @Column(name = "description")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String description;//备注

    @Column(name = "approve_note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String approveNote;//审核说明

    @Column(name = "log")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String log;//审请记录

    @Column(name = "creator_id")
    public long creatorId;//创建者uid

    @Column(name = "approver_id")
    public long approverId;//审核人员uid

    @Column(name = "lat")
    public double lat;//latitude

    @Column(name = "lon")
    public double lon;//longtitude

    @Column(name = "open_time")
    public int openTime;

    @Column(name = "close_time")
    public int closeTime;

    @Column(name = "update_time")
    public long updateTime;//更新时间

    @Column(name = "create_time")
    public long createTime;//创建时间

    @Column(name = "filter")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filter;//审请记录

    @Column(name = "business_time")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String businessTime;

    @Column(name = "avatar")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String avatar;

    @Column(name = "rect_logo")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String rectLogo;

    @Column(name = "product_counts")
    public long productCounts;

    @Column(name = "views")
    public long views;

    @Column(name = "tags")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String tags;

    @Column(name = "images")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String images;

    @Column(name = "discount_str")
    public String discountStr;

    @Column(name = "branches")
    public String branches;

    @Column(name = "discount")
    public int discount;

    @Column(name = "bid_discount")
    public int bidDiscount;

    @Column(name = "average_consumption")
    public int averageConsumption;

    @Column(name = "order_count")
    public long orderCount;

    @Column(name = "sort")
    public int sort;

    @Column(name = "place_top")
    public boolean placeTop;

    @Column(name = "dada_shop_id")
    public String dadaShopId;

    @Column(name = "dada_user_no")
    public String dadaUserNo;

    @Column(name = "bulletin")
    public String bulletin;

    @Column(name = "env_images")
    public String envImages;

    @Transient
    public List<Staff> staffList = new ArrayList<>();

    @Transient
    public int staffCount;

    @Transient
    public String customerName = "";

    @Transient
    public String openId = "";

    @Transient
    public List<Product> homepageShops = new ArrayList<>();


    public static Finder<Long, Shop> find = new Finder<>(Shop.class);

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setLicenseImg(String licenseImg) {
        this.licenseImg = licenseImg;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public void setApproveNote(String approveNote) {
        this.approveNote = approveNote;
    }

    public void setApproverId(long approverId) {
        this.approverId = approverId;
    }

    public void setLog(String log) {
        this.log = log;
    }


    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public int getRunType() {
        return runType;
    }

    public void setRunType(int runType) {
        this.runType = runType;
    }

    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getLicenseImg() {
        return licenseImg;
    }

    public String getDescription() {
        return description;
    }

    public String getApproveNote() {
        return approveNote;
    }

    public String getLog() {
        return log;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public long getApproverId() {
        return approverId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getOpenTime() {
        return openTime;
    }

    public void setOpenTime(int openTime) {
        this.openTime = openTime;
    }

    public int getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(int closeTime) {
        this.closeTime = closeTime;
    }

    public String getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(String businessTime) {
        this.businessTime = businessTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getDiscountStr() {
        return discountStr;
    }

    public void setDiscountStr(String discountStr) {
        this.discountStr = discountStr;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getAverageConsumption() {
        return averageConsumption;
    }

    public void setAverageConsumption(int averageConsumption) {
        this.averageConsumption = averageConsumption;
    }

    public long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(long orderCount) {
        this.orderCount = orderCount;
    }

    public String getBranches() {
        return branches;
    }

    public void setBranches(String branches) {
        this.branches = branches;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getBidDiscount() {
        return bidDiscount;
    }

    public void setBidDiscount(int bidDiscount) {
        this.bidDiscount = bidDiscount;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public boolean isPlaceTop() {
        return placeTop;
    }

    public void setPlaceTop(boolean placeTop) {
        this.placeTop = placeTop;
    }

    public String getDadaShopId() {
        return dadaShopId;
    }

    public void setDadaShopId(String dadaShopId) {
        this.dadaShopId = dadaShopId;
    }

    public String getDadaUserNo() {
        return dadaUserNo;
    }

    public void setDadaUserNo(String dadaUserNo) {
        this.dadaUserNo = dadaUserNo;
    }

    public int getShopLevel() {
        return shopLevel;
    }

    public void setShopLevel(int shopLevel) {
        this.shopLevel = shopLevel;
    }

    public String getRectLogo() {
        return rectLogo;
    }

    public void setRectLogo(String rectLogo) {
        this.rectLogo = rectLogo;
    }

    public long getProductCounts() {
        return productCounts;
    }

    public void setProductCounts(long productCounts) {
        this.productCounts = productCounts;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBulletin() {
        return bulletin;
    }

    public void setBulletin(String bulletin) {
        this.bulletin = bulletin;
    }

    public String getEnvImages() {
        return envImages;
    }

    public void setEnvImages(String envImages) {
        this.envImages = envImages;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", status=" + status +
                ", runType=" + runType +
                ", name='" + name + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactAddress='" + contactAddress + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", licenseImg='" + licenseImg + '\'' +
                ", description='" + description + '\'' +
                ", approveNote='" + approveNote + '\'' +
                ", log='" + log + '\'' +
                ", creatorId=" + creatorId +
                ", approverId=" + approverId +
                ", lat=" + lat +
                ", lon=" + lon +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", filter='" + filter + '\'' +
                ", businessTime='" + businessTime + '\'' +
                ", avatar='" + avatar + '\'' +
                ", images='" + images + '\'' +
                ", discountStr='" + discountStr + '\'' +
                ", branches='" + branches + '\'' +
                ", discount=" + discount +
                ", averageConsumption=" + averageConsumption +
                ", orderCount=" + orderCount +
                ", sort=" + sort +
                ", staffList=" + staffList +
                '}';
    }
}
