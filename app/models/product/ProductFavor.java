package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠方案
 */
@Entity
@Table(name = "v1_product_favor")
public class ProductFavor extends Model {

    public static final int STATUS_NOT_START = 1;
    public static final int STATUS_ENABLE = 2;
    public static final int STATUS_DISABLE = 3;
    public static final int REQUIRE_TYPE_UPTO_AMOUNT_TO_SUB = 1;//满量立减
    public static final int REQUIRE_TYPE_UPTO_MONEY_TO_SUB = 2;//满额立减
    public static final int REQUIRE_TYPE_UPTO_AMOUNT_TO_DISCOUNT = 3;//满量立折
    public static final int REQUIRE_TYPE_UPTO_MONEY_TO_DISCOUNT = 4;//满额立折
    public static final int REQUIRE_TYPE_TIME_LIMIT_TO_DISCOUNT = 5;//限时打折
    public static final int REQUIRE_TYPE_SEQ_DISCOUNT = 6;//第几件几折
    public static final int REQUIRE_TYPE_FIRST_DISCOUNT = 7;//订单首件打折
    public static final int REQUIRE_TYPE_APP_USE_DISCOUNT = 8;//APP专享打折
    public static final int REQUIRE_TYPE_SKU_DISCOUNT = 9;//SKU打折
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "require_type")
    public int requireType;//1满数量立减 2满金额立减

    @Column(name = "name")
    public String name;

    @Column(name = "limit_amount")
    public long limitAmount;//限购数量

    @Column(name = "begin_time")
    public long beginTime;

    @Column(name = "end_time")
    public long endTime;

    @Column(name = "status")
    public int status;

    @Column(name = "cover_frame_id")
    public long coverFrameId;

    @Column(name = "cover_frame_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String coverFrameUrl = "";

    @Column(name = "create_time")
    public long createTime;

    @Transient
    public List<ProductFavorProducts> favorProducts = new ArrayList();

    @Transient
    public List<ProductFavorDetail> detailList = new ArrayList();

    @Transient
    public List<Product> productList = new ArrayList();

    public static Finder<Long, ProductFavor> find = new Finder<>(ProductFavor.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRequireType() {
        return requireType;
    }

    public void setRequireType(int requireType) {
        this.requireType = requireType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(long limitAmount) {
        this.limitAmount = limitAmount;
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

    public String getCoverFrameUrl() {
        return coverFrameUrl;
    }

    public void setCoverFrameUrl(String coverFrameUrl) {
        this.coverFrameUrl = coverFrameUrl;
    }

    public long getCoverFrameId() {
        return coverFrameId;
    }

    public void setCoverFrameId(long coverFrameId) {
        this.coverFrameId = coverFrameId;
    }

    @Override
    public String toString() {
        return "ProductFavor{" +
                "id=" + id +
                ", requireType=" + requireType +
                ", name='" + name + '\'' +
                ", limitAmount=" + limitAmount +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", coverFrameId=" + coverFrameId +
                ", coverFrameUrl='" + coverFrameUrl + '\'' +
                ", createTime=" + createTime +
                ", favorProducts=" + favorProducts +
                ", detailList=" + detailList +
                ", productList=" + productList +
                '}';
    }
}
