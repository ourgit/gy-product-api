package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 优惠方案详情
 */
@Entity
@Table(name = "v1_product_favor_detail")
public class ProductFavorDetail extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "favor_id")
    public long favorId;

    @Column(name = "sku_favor_id")
    public long skuFavorId;

    @Column(name = "require_type")
    public int requireType;//1满数量立减 2满金额立减

    @Column(name = "upto_money")
    public long uptoMoney;

    @Column(name = "upto_amount")
    public long uptoAmount;

    @Column(name = "sub_amount")
    public long subAmount;//扣减金额

    @Column(name = "discount")
    public double discount;//折扣

    @Column(name = "seq")
    public int seq;//顺序，第几件商品

    @Column(name = "operation")
    public String operation = "";

    @Column(name = "gift_product_ids")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String giftProductIds = "";

    public static Finder<Long, ProductFavorDetail> find = new Finder<>(ProductFavorDetail.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFavorId() {
        return favorId;
    }

    public void setFavorId(long favorId) {
        this.favorId = favorId;
    }

    public int getRequireType() {
        return requireType;
    }

    public void setRequireType(int requireType) {
        this.requireType = requireType;
    }

    public double getUptoMoney() {
        return uptoMoney;
    }

    public void setUptoMoney(long uptoMoney) {
        this.uptoMoney = uptoMoney;
    }

    public double getUptoAmount() {
        return uptoAmount;
    }

    public void setUptoAmount(long uptoAmount) {
        this.uptoAmount = uptoAmount;
    }

    public double getSubAmount() {
        return subAmount;
    }

    public void setSubAmount(long subAmount) {
        this.subAmount = subAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public long getSkuFavorId() {
        return skuFavorId;
    }

    public void setSkuFavorId(long skuFavorId) {
        this.skuFavorId = skuFavorId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getGiftProductIds() {
        return giftProductIds;
    }

    public void setGiftProductIds(String giftProductIds) {
        this.giftProductIds = giftProductIds;
    }

    @Override
    public String toString() {
        return "ProductFavorDetail{" +
                "id=" + id +
                ", favorId=" + favorId +
                ", skuFavorId=" + skuFavorId +
                ", requireType=" + requireType +
                ", uptoMoney=" + uptoMoney +
                ", uptoAmount=" + uptoAmount +
                ", subAmount=" + subAmount +
                ", discount=" + discount +
                ", seq=" + seq +
                ", operation='" + operation + '\'' +
                ", giftProductIds='" + giftProductIds + '\'' +
                '}';
    }
}
