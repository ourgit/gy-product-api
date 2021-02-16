package models.enroll;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 答题配置选项
 */
@Entity
@Table(name = "v1_enroll_config")
public class EnrollConfig extends Model {
    //1为未开始，2为进行中，3为已结束
    public static final int STATUS_NOT_START = 1;
    public static final int STATUS_PROCESSING = 2;
    public static final int STATUS_FINISHED = 3;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "begin_time")
    public long beginTime;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "sku_id")
    public long skuId;

    @Column(name = "end_time")
    public long endTime;

    @Column(name = "status")
    public int status;//1为未开始，2为进行中，3为已结束

    @Transient
    public String statusName;//状态名字

    @Column(name = "title")
    public String title;

    @Column(name = "content")
    public String content;//活动规则内容与说明


    @Column(name = "update_time")
    public long updateTime;

    @Transient
    public String productName;

    @Transient
    public String skuName;

    public static Finder<Long, EnrollConfig> find = new Finder<>(EnrollConfig.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
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

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
