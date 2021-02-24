package models.promotion;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "v1_bargain")
public class Bargain extends Model {
    public static final int STATUS_FAILED = -1;
    public static final int STATUS_PROCESSING = 1;
    public static final int STATUS_SUCCEED = 2;
    public static final int STATUS_REFUND = 4;
    public static final int STATUS_DELIVERED = 6;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "uid")
    public long uid;//用户ID

    @Column(name = "user_name")
    public String userName;

    @Column(name = "user_avatar")
    public String userAvatar;

    @Column(name = "bargain_id")
    public long bargainId;

    @Column(name = "bargain_title")
    public String bargainTitle;

    @Column(name = "begin_time")
    public long beginTime;//生效时间

    @Column(name = "end_time")
    public long endTime;//失效时间

    @Column(name = "status")
    public long status;//状态

    @Column(name = "address_id")
    public long addressId;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "use_time")
    public long useTime;

    @Column(name = "invites_amount")
    public int inviteAmount;

    @Column(name = "price")
    public long price;

    @Column(name = "min_price")
    public long minPrice;

    @Column(name = "already_bargain_money")
    public long alreadyBargainMoney;

    @Column(name = "require_invites_amount")
    public int requireInvitesAmount;

    @Column(name = "order_id")
    public long orderId;

    @Column(name = "order_no")
    public String orderNo;

    @Column(name = "pay_no")
    public String payNo;

    @Column(name = "refund_no")
    public String refundNo;

    @Column(name = "prompt_yet")
    public boolean promptYet;

    @Transient
    public BargainConfig config;

    @Transient
    public List<BargainMember> bargainMemberList = new ArrayList<>();

    public static Finder<Long, Bargain> find = new Finder<>(Bargain.class);

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getBargainId() {
        return bargainId;
    }

    public void setBargainId(long bargainId) {
        this.bargainId = bargainId;
    }

    public String getBargainTitle() {
        return bargainTitle;
    }

    public void setBargainTitle(String bargainTitle) {
        this.bargainTitle = bargainTitle;
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

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public long getAlreadyBargainMoney() {
        return alreadyBargainMoney;
    }

    public void setAlreadyBargainMoney(long alreadyBargainMoney) {
        this.alreadyBargainMoney = alreadyBargainMoney;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public int getInviteAmount() {
        return inviteAmount;
    }

    public void setInviteAmount(int inviteAmount) {
        this.inviteAmount = inviteAmount;
    }

    public int getRequireInvitesAmount() {
        return requireInvitesAmount;
    }

    public void setRequireInvitesAmount(int requireInvitesAmount) {
        this.requireInvitesAmount = requireInvitesAmount;
    }

    public BargainConfig getConfig() {
        return config;
    }

    public void setConfig(BargainConfig config) {
        this.config = config;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public boolean isPromptYet() {
        return promptYet;
    }

    public void setPromptYet(boolean promptYet) {
        this.promptYet = promptYet;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(long minPrice) {
        this.minPrice = minPrice;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}

