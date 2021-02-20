package models.enroll;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 *  提交记录
 */
@Entity
@Table(name = "v1_enroll_user_info")
public class EnrollContentUserInfo extends Model {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "user_name")
    public String userName;

    @Column(name = "uid")
    public long uid;

    @Column(name = "config_id")
    public long configId;

    @Column(name = "order_id")
    public long orderId;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "phone_number")
    public String phoneNumber;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, EnrollContentUserInfo> find = new Finder<>(EnrollContentUserInfo.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getConfigId() {
        return configId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
