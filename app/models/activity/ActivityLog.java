package models.activity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "v1_activity_log")
public class ActivityLog extends Model implements Serializable {
    public static final int STATUS_AVAILABLE = 1;
    public static final int STATUS_NOT_AVAILABLE = -1;
    private static final long serialVersionUID = 2577034218067124977L;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "uid")
    public long uid;

    @Column(name = "config_id")
    public long configId;

    @Column(name = "user_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String userName;

    @Column(name = "config_title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String configTitle;

    @Column(name = "avatar")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String avatar;

    @Column(name = "shop_avatar")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String shopAvatar;

    @Column(name = "phone_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String phoneNumber;

    @Column(name = "place")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String place;

    @Column(name = "order_id")
    public long orderId;

    @Column(name = "order_no")
    public String orderNo;

    @Column(name = "shop_id")
    public long shopId;

    @Column(name = "shop_name")
    public String shopName;

    @Column(name = "lead_shop_id")
    public long leadShopId;

    @Column(name = "lead_shop_name")
    public String leadShopName;

    @Column(name = "create_time")
    public long createdTime;

    @Column(name = "status")
    public int status;

    @Column(name = "filter")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filter;

    public static Finder<Long, ActivityLog> find = new Finder<>(ActivityLog.class);

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

    public long getConfigId() {
        return configId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getConfigTitle() {
        return configTitle;
    }

    public void setConfigTitle(String configTitle) {
        this.configTitle = configTitle;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    public long getLeadShopId() {
        return leadShopId;
    }

    public void setLeadShopId(long leadShopId) {
        this.leadShopId = leadShopId;
    }

    public String getLeadShopName() {
        return leadShopName;
    }

    public void setLeadShopName(String leadShopName) {
        this.leadShopName = leadShopName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
