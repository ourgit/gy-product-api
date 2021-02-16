package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import models.user.ServiceBalance;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * sku
 */
@Entity
@Table(name = "v1_product_sku")
public class ProductSku extends Model {

    public static final int SOURCE_FROM_SELF = 1;
    public static final int SOURCE_FROM_THIRD = 2;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;//sku名称

    @Column(name = "product_id")
    public long productId;//商品编码

    @Column(name = "stock")
    public long stock;//库存量

    @Column(name = "presale_stock")
    public long presaleStock;

    @Column(name = "flash_total_amount")
    public long flashTotalAmount;

    @Column(name = "flash_left_amount")
    public long flashLeftAmount;

    @Column(name = "weight")
    public double weight;//毛重

    @Column(name = "warning_stock")
    public long warningStock;//'库存警告'

    @Column(name = "img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String imgUrl;//主图

    @Column(name = "price")
    public long price;

    @Column(name = "old_price")
    public long oldPrice;

    @Column(name = "dist_price")
    public long distPrice;//分销价

    @Column(name = "groupon_price")
    public long grouponPrice;//团购价

    @Column(name = "flash_price")
    public long flashPrice;//秒杀

    @Column(name = "award")
    public long award;//业务员返点

    @Column(name = "member_favor")
    public long memberFavor;//会员优惠

    @Column(name = "indirect_award")
    public long indirectAward;

    @Column(name = "recommend_price")
    public long recommendPrice;//建议零售价

    @Column(name = "min_order_amount")
    public int minOrderAmount;//起订量

    @Column(name = "sold_amount")
    public long soldAmount;//已售数量

    @Column(name = "virtual_amount")
    public long virtualAmount;//虚拟销量

    @Column(name = "service_amount")
    public long serviceAmount;

    @Column(name = "service_days")
    public long serviceDays;

    @Column(name = "require_score")
    public long requireScore;

    @Column(name = "sort")
    public int sort;

    @Column(name = "code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String code;//商品SKU编码，系统编码，唯一性，productId:optionId1:optionId2::option3:option5

    @Column(name = "barcode")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String barcode;//商品条形码，唯一性，对应商品厂家的条形码
    /**
     * {
     * attr:"attr1:attr2:attr3:attr4",
     * value:"value1:value2:value3:value4"
     * }
     */
    @Column(name = "data")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String data;//sku串，用于商品选项选择时定位

    @Column(name = "begin_time")
    public long beginTime;

    @Column(name = "begin_hour")
    public int beginHour;

    @Column(name = "end_time")
    public long endTime;

    @Column(name = "require_type")
    public int requireType;

    @Column(name = "status")
    public int status;

    @Column(name = "limit_amount")
    public long limitAmount;//限购数量

    @Column(name = "favor_name")
    public String favorName;

    @Column(name = "cover_frame_id")
    public long coverFrameId;

    @Column(name = "deliver_hours")
    public long deliverHours;

    @Column(name = "cover_frame_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String coverFrameUrl = "";

    @Column(name = "enable")
    public boolean enable;

    @Column(name = "source")
    public int source;

    @Column(name = "bid_price")
    public long bidPrice;

    @Column(name = "require_invites")
    public long requireInvites;

    @Column(name = "card_coupons")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String cardCoupons;

    @Transient
    public String giftProductIds = "";

    @Transient
    public ProductFavorProducts favor;

    @Transient
    public ServiceBalance serviceBalance;

    @Transient
    public List<ProductFavorDetail> detailList = new ArrayList();

    public static Finder<Long, ProductSku> find = new Finder<>(ProductSku.class);

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

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public long getWarningStock() {
        return warningStock;
    }

    public void setWarningStock(long warningStock) {
        this.warningStock = warningStock;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public double getDistPrice() {
        return distPrice;
    }

    public void setDistPrice(long distPrice) {
        this.distPrice = distPrice;
    }

    public double getAward() {
        return award;
    }

    public void setAward(long award) {
        this.award = award;
    }

    public double getRecommendPrice() {
        return recommendPrice;
    }

    public void setRecommendPrice(long recommendPrice) {
        this.recommendPrice = recommendPrice;
    }

    public int getMinOrderAmount() {
        return minOrderAmount;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setMinOrderAmount(int minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public long getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(long soldAmount) {
        this.soldAmount = soldAmount;
    }

    public long getVirtualAmount() {
        return virtualAmount;
    }

    public void setVirtualAmount(long virtualAmount) {
        this.virtualAmount = virtualAmount;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getRequireType() {
        return requireType;
    }

    public void setRequireType(int requireType) {
        this.requireType = requireType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(long limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getFavorName() {
        return favorName;
    }

    public void setFavorName(String favorName) {
        this.favorName = favorName;
    }

    public long getCoverFrameId() {
        return coverFrameId;
    }

    public void setCoverFrameId(long coverFrameId) {
        this.coverFrameId = coverFrameId;
    }

    public String getCoverFrameUrl() {
        return coverFrameUrl;
    }

    public void setCoverFrameUrl(String coverFrameUrl) {
        this.coverFrameUrl = coverFrameUrl;
    }

    public long getGrouponPrice() {
        return grouponPrice;
    }

    public void setGrouponPrice(long grouponPrice) {
        this.grouponPrice = grouponPrice;
    }

    public long getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(long serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public long getServiceDays() {
        return serviceDays;
    }

    public void setServiceDays(long serviceDays) {
        this.serviceDays = serviceDays;
    }

    public String getGiftProductIds() {
        return giftProductIds;
    }

    public void setGiftProductIds(String giftProductIds) {
        this.giftProductIds = giftProductIds;
    }

    public long getRequireScore() {
        return requireScore;
    }

    public void setRequireScore(long requireScore) {
        this.requireScore = requireScore;
    }

    public long getFlashPrice() {
        return flashPrice;
    }

    public void setFlashPrice(long flashPrice) {
        this.flashPrice = flashPrice;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public long getDeliverHours() {
        return deliverHours;
    }

    public void setDeliverHours(long deliverHours) {
        this.deliverHours = deliverHours;
    }

    public long getPresaleStock() {
        return presaleStock;
    }

    public void setPresaleStock(long presaleStock) {
        this.presaleStock = presaleStock;
    }

    public long getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(long oldPrice) {
        this.oldPrice = oldPrice;
    }

    public long getIndirectAward() {
        return indirectAward;
    }

    public void setIndirectAward(long indirectAward) {
        this.indirectAward = indirectAward;
    }

    public long getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(long bidPrice) {
        this.bidPrice = bidPrice;
    }

    public long getRequireInvites() {
        return requireInvites;
    }

    public void setRequireInvites(long requireInvites) {
        this.requireInvites = requireInvites;
    }

    public long getMemberFavor() {
        return memberFavor;
    }

    public void setMemberFavor(long memberFavor) {
        this.memberFavor = memberFavor;
    }

    public long getFlashTotalAmount() {
        return flashTotalAmount;
    }

    public void setFlashTotalAmount(long flashTotalAmount) {
        this.flashTotalAmount = flashTotalAmount;
    }

    public long getFlashLeftAmount() {
        return flashLeftAmount;
    }

    public void setFlashLeftAmount(long flashLeftAmount) {
        this.flashLeftAmount = flashLeftAmount;
    }

    public int getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    public String getCardCoupons() {
        return cardCoupons;
    }

    public void setCardCoupons(String cardCoupons) {
        this.cardCoupons = cardCoupons;
    }

    @Override
    public String toString() {
        return "ProductSku{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productId=" + productId +
                ", stock=" + stock +
                ", presaleStock=" + presaleStock +
                ", flashTotalAmount=" + flashTotalAmount +
                ", flashLeftAmount=" + flashLeftAmount +
                ", weight=" + weight +
                ", warningStock=" + warningStock +
                ", imgUrl='" + imgUrl + '\'' +
                ", price=" + price +
                ", oldPrice=" + oldPrice +
                ", distPrice=" + distPrice +
                ", grouponPrice=" + grouponPrice +
                ", flashPrice=" + flashPrice +
                ", award=" + award +
                ", memberFavor=" + memberFavor +
                ", indirectAward=" + indirectAward +
                ", recommendPrice=" + recommendPrice +
                ", minOrderAmount=" + minOrderAmount +
                ", soldAmount=" + soldAmount +
                ", virtualAmount=" + virtualAmount +
                ", serviceAmount=" + serviceAmount +
                ", serviceDays=" + serviceDays +
                ", requireScore=" + requireScore +
                ", sort=" + sort +
                ", code='" + code + '\'' +
                ", barcode='" + barcode + '\'' +
                ", data='" + data + '\'' +
                ", beginTime=" + beginTime +
                ", beginHour=" + beginHour +
                ", endTime=" + endTime +
                ", requireType=" + requireType +
                ", status=" + status +
                ", limitAmount=" + limitAmount +
                ", favorName='" + favorName + '\'' +
                ", coverFrameId=" + coverFrameId +
                ", deliverHours=" + deliverHours +
                ", coverFrameUrl='" + coverFrameUrl + '\'' +
                ", enable=" + enable +
                ", source=" + source +
                ", bidPrice=" + bidPrice +
                ", requireInvites=" + requireInvites +
                ", cardCoupons='" + cardCoupons + '\'' +
                ", giftProductIds='" + giftProductIds + '\'' +
                ", favor=" + favor +
                ", serviceBalance=" + serviceBalance +
                ", detailList=" + detailList +
                '}';
    }
}
