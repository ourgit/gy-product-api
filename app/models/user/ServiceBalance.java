package models.user;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 服务类余额
 */
@Entity
@Table(name = "v1_service_balance")
public class ServiceBalance extends Model {

    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_EXPIRE = 2;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    public long id;

    @Column(name = "uid")
    public long uid;//用户id

    @Column(name = "order_id")
    public long orderId;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "sku_id")
    public long skuId;

    @Column(name = "service_img")
    public String serviceImg;

    @Column(name = "service_name")
    public String serviceName;

    @Column(name = "status")
    public int status;

    @Column(name = "left_balance")
    public int leftBalance;

    @Column(name = "used_balance")
    public int usedBalance;

    @Column(name = "total_balance")
    public int totalBalance;

    @Column(name = "begin_time")
    public long beginTime;

    @Column(name = "end_time")
    public long endTime;

    @Column(name = "update_time")
    public long updateTime;//更新时间

    @Column(name = "create_time")
    public long createdTime;//创建时间

    public static Finder<Long, ServiceBalance> find = new Finder<>(ServiceBalance.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getLeftBalance() {
        return leftBalance;
    }

    public void setLeftBalance(int leftBalance) {
        this.leftBalance = leftBalance;
    }

    public int getUsedBalance() {
        return usedBalance;
    }

    public void setUsedBalance(int usedBalance) {
        this.usedBalance = usedBalance;
    }

    public int getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(int totalBalance) {
        this.totalBalance = totalBalance;
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

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getServiceImg() {
        return serviceImg;
    }

    public void setServiceImg(String serviceImg) {
        this.serviceImg = serviceImg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "ServiceBalance{" +
                "id=" + id +
                ", uid=" + uid +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", skuId=" + skuId +
                ", serviceImg='" + serviceImg + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", status=" + status +
                ", leftBalance=" + leftBalance +
                ", usedBalance=" + usedBalance +
                ", totalBalance=" + totalBalance +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", updateTime=" + updateTime +
                ", createdTime=" + createdTime +
                '}';
    }
}
