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
@Table(name = "v1_xs_product_price_log")
public class ProductXSPriceLog extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "prId")
    public long prId;

    @Column(name = "sku")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String sku;

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

    @Column(name = "limitQty")
    public long limitQty;

    @Column(name = "ulimitQty")
    public long ulimitQty;

    @Column(name = "marketAmt")
    public double marketAmt;

    @Column(name = "saleAmt")
    public double saleAmt;

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

    @Column(name = "spuSn")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String spuSn;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, ProductXSPriceLog> find = new Finder<>(ProductXSPriceLog.class);

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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public String getSpuSn() {
        return spuSn;
    }

    public void setSpuSn(String spuSn) {
        this.spuSn = spuSn;
    }

    @Override
    public String toString() {
        return "ProductXSPriceLog{" +
                "id=" + id +
                ", productId=" + productId +
                ", prId=" + prId +
                ", sku='" + sku + '\'' +
                ", tmBuyStart='" + tmBuyStart + '\'' +
                ", tmBuyEnd='" + tmBuyEnd + '\'' +
                ", tmShowStart='" + tmShowStart + '\'' +
                ", tmShowEnd='" + tmShowEnd + '\'' +
                ", limitQty=" + limitQty +
                ", ulimitQty=" + ulimitQty +
                ", marketAmt=" + marketAmt +
                ", saleAmt=" + saleAmt +
                ", consumers=" + consumers +
                ", daySaleQty=" + daySaleQty +
                ", folQty=" + folQty +
                ", likeNum=" + likeNum +
                ", saleQty=" + saleQty +
                ", viewNum=" + viewNum +
                ", wantBuyQty=" + wantBuyQty +
                ", spuSn='" + spuSn + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
