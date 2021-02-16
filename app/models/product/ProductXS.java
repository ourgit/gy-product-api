package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * sku
 */
@Entity
@Table(name = "v1_xs_product")
public class ProductXS extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "prId")
    public long prId;

    @Column(name = "acId")
    public long acId;

    @Column(name = "preId")
    public long preId;

    @Column(name = "sku")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String sku;

    @Column(name = "dailySaleTime")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String dailySaleTime;

    @Column(name = "tmBuyStart")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String tmBuyStart;

    @Column(name = "tmBuyEnd")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String tmBuyEnd;

    @Column(name = "tmShowStart")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String tmShowStart;

    @Column(name = "tmShowEnd")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String tmShowEnd;

    @Column(name = "adUrl")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String adUrl;

    @Column(name = "prName")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String prName;//sku名称

    @Column(name = "tmPickUp")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String tmPickUp;//sku名称

    @Column(name = "limitQty")
    public long limitQty;

    @Column(name = "ulimitQty")
    public long ulimitQty;

    @Column(name = "marketAmt")
    public double marketAmt;

    @Column(name = "saleAmt")
    public double saleAmt;

    @Column(name = "prType")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String prType;//sku名称

    @Column(name = "areaId")
    public long areaId;

    @Column(name = "shelfLife")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String shelfLife;//sku名称

    @Column(name = "vesName")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String vesName;//sku名称

    @Column(name = "attrs")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String attrs;//sku名称

    @Column(name = "specialSale")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String specialSale;//sku名称

    @Column(name = "saleStatus")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String saleStatus;//sku名称

    @Column(name = "videoUrl")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String videoUrl;

    @Column(name = "windowId")
    public long windowId;

    @Column(name = "sortSeq")
    public long sortSeq;

    @Column(name = "existsInAct")
    public boolean existsInAct;

    @Column(name = "verificationCode")
    public boolean verificationCode;

    @Column(name = "spuSn")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String spuSn;

    @Column(name = "skuSn")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String skuSn;

    @Column(name = "isNextDayPickUp")
    public boolean isNextDayPickUp;

    @Column(name = "assignType")
    public int assignType;

    @Column(name = "imgUrl")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String imgUrl;

    @Column(name = "eskuSn")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String eskuSn;

    @Column(name = "consumers")
    public long consumers;

    @Column(name = "daySaleQty")
    public long daySaleQty;

    @Column(name = "folQty")
    public long folQty;

    @Column(name = "likeNum")
    public long likeNum;

    @Column(name = "saleQty")
    public long saleQty;

    @Column(name = "viewNum")
    public long viewNum;

    @Column(name = "wantBuyQty")
    public long wantBuyQty;

    @Column(name = "filter")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filter;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, ProductXS> find = new Finder<>(ProductXS.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPrId() {
        return prId;
    }

    public void setPrId(long prId) {
        this.prId = prId;
    }

    public long getAcId() {
        return acId;
    }

    public void setAcId(long acId) {
        this.acId = acId;
    }

    public long getPreId() {
        return preId;
    }

    public void setPreId(long preId) {
        this.preId = preId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDailySaleTime() {
        return dailySaleTime;
    }

    public void setDailySaleTime(String dailySaleTime) {
        this.dailySaleTime = dailySaleTime;
    }

    public String getTmBuyStart() {
        return tmBuyStart;
    }

    public void setTmBuyStart(String tmBuyStart) {
        this.tmBuyStart = tmBuyStart;
    }

    public String getTmBuyEnd() {
        return tmBuyEnd;
    }

    public void setTmBuyEnd(String tmBuyEnd) {
        this.tmBuyEnd = tmBuyEnd;
    }

    public String getTmShowStart() {
        return tmShowStart;
    }

    public void setTmShowStart(String tmShowStart) {
        this.tmShowStart = tmShowStart;
    }

    public String getTmShowEnd() {
        return tmShowEnd;
    }

    public void setTmShowEnd(String tmShowEnd) {
        this.tmShowEnd = tmShowEnd;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getPrName() {
        return prName;
    }

    public void setPrName(String prName) {
        this.prName = prName;
    }

    public String getTmPickUp() {
        return tmPickUp;
    }

    public void setTmPickUp(String tmPickUp) {
        this.tmPickUp = tmPickUp;
    }

    public long getLimitQty() {
        return limitQty;
    }

    public void setLimitQty(long limitQty) {
        this.limitQty = limitQty;
    }

    public long getUlimitQty() {
        return ulimitQty;
    }

    public void setUlimitQty(long ulimitQty) {
        this.ulimitQty = ulimitQty;
    }

    public double getMarketAmt() {
        return marketAmt;
    }

    public void setMarketAmt(double marketAmt) {
        this.marketAmt = marketAmt;
    }

    public double getSaleAmt() {
        return saleAmt;
    }

    public void setSaleAmt(double saleAmt) {
        this.saleAmt = saleAmt;
    }

    public String getPrType() {
        return prType;
    }

    public void setPrType(String prType) {
        this.prType = prType;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getVesName() {
        return vesName;
    }

    public void setVesName(String vesName) {
        this.vesName = vesName;
    }

    public String getAttrs() {
        return attrs;
    }

    public void setAttrs(String attrs) {
        this.attrs = attrs;
    }

    public String getSpecialSale() {
        return specialSale;
    }

    public void setSpecialSale(String specialSale) {
        this.specialSale = specialSale;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getWindowId() {
        return windowId;
    }

    public void setWindowId(long windowId) {
        this.windowId = windowId;
    }

    public long getSortSeq() {
        return sortSeq;
    }

    public void setSortSeq(long sortSeq) {
        this.sortSeq = sortSeq;
    }

    public boolean isExistsInAct() {
        return existsInAct;
    }

    public void setExistsInAct(boolean existsInAct) {
        this.existsInAct = existsInAct;
    }

    public boolean isVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(boolean verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getSpuSn() {
        return spuSn;
    }

    public void setSpuSn(String spuSn) {
        this.spuSn = spuSn;
    }

    public String getSkuSn() {
        return skuSn;
    }

    public void setSkuSn(String skuSn) {
        this.skuSn = skuSn;
    }

    public boolean isNextDayPickUp() {
        return isNextDayPickUp;
    }

    public void setNextDayPickUp(boolean nextDayPickUp) {
        isNextDayPickUp = nextDayPickUp;
    }

    public int getAssignType() {
        return assignType;
    }

    public void setAssignType(int assignType) {
        this.assignType = assignType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEskuSn() {
        return eskuSn;
    }

    public void setEskuSn(String eskuSn) {
        this.eskuSn = eskuSn;
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

    public long getConsumers() {
        return consumers;
    }

    public void setConsumers(long consumers) {
        this.consumers = consumers;
    }

    public long getDaySaleQty() {
        return daySaleQty;
    }

    public void setDaySaleQty(long daySaleQty) {
        this.daySaleQty = daySaleQty;
    }

    public long getFolQty() {
        return folQty;
    }

    public void setFolQty(long folQty) {
        this.folQty = folQty;
    }

    public long getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(long likeNum) {
        this.likeNum = likeNum;
    }

    public long getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(long saleQty) {
        this.saleQty = saleQty;
    }

    public long getViewNum() {
        return viewNum;
    }

    public void setViewNum(long viewNum) {
        this.viewNum = viewNum;
    }

    public long getWantBuyQty() {
        return wantBuyQty;
    }

    public void setWantBuyQty(long wantBuyQty) {
        this.wantBuyQty = wantBuyQty;
    }

    @Override
    public String toString() {
        return "ProductXS{" +
                "id=" + id +
                ", prId=" + prId +
                ", acId=" + acId +
                ", preId=" + preId +
                ", sku='" + sku + '\'' +
                ", dailySaleTime='" + dailySaleTime + '\'' +
                ", tmBuyStart='" + tmBuyStart + '\'' +
                ", tmBuyEnd='" + tmBuyEnd + '\'' +
                ", tmShowStart='" + tmShowStart + '\'' +
                ", tmShowEnd='" + tmShowEnd + '\'' +
                ", adUrl='" + adUrl + '\'' +
                ", prName='" + prName + '\'' +
                ", tmPickUp='" + tmPickUp + '\'' +
                ", limitQty=" + limitQty +
                ", ulimitQty=" + ulimitQty +
                ", marketAmt=" + marketAmt +
                ", saleAmt=" + saleAmt +
                ", prType='" + prType + '\'' +
                ", areaId=" + areaId +
                ", shelfLife='" + shelfLife + '\'' +
                ", vesName='" + vesName + '\'' +
                ", attrs='" + attrs + '\'' +
                ", specialSale='" + specialSale + '\'' +
                ", saleStatus='" + saleStatus + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", windowId=" + windowId +
                ", sortSeq=" + sortSeq +
                ", existsInAct=" + existsInAct +
                ", verificationCode=" + verificationCode +
                ", spuSn='" + spuSn + '\'' +
                ", skuSn='" + skuSn + '\'' +
                ", isNextDayPickUp=" + isNextDayPickUp +
                ", assignType=" + assignType +
                ", imgUrl='" + imgUrl + '\'' +
                ", eskuSn='" + eskuSn + '\'' +
                ", consumers=" + consumers +
                ", daySaleQty=" + daySaleQty +
                ", folQty=" + folQty +
                ", likeNum=" + likeNum +
                ", saleQty=" + saleQty +
                ", viewNum=" + viewNum +
                ", wantBuyQty=" + wantBuyQty +
                ", filter='" + filter + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
