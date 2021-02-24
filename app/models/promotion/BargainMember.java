package models.promotion;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name = "v1_bargain_member")
public class BargainMember extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "dealer_id")
    public long dealerId;

    @Column(name = "dealer_name")
    public String dealerName;

    @Column(name = "uid")
    public long uid;//用户id

    @Column(name = "user_name")
    public String userName;

    @Column(name = "dealer_avatar")
    public String dealerAvatar;

    @Column(name = "user_avatar")
    public String userAvatar;

    @Column(name = "user_bargain_id")
    public long userBargainId;

    @Column(name = "bargain_config_id")
    public long bargainConfigId;

    @Column(name = "bargain_title")
    public String bargainTitle;

    @Column(name = "create_time")
    public long createdTime;

    @Column(name = "bargain_amount")
    public long bargainAmount;

    @Column(name = "location")
    public String location;

    public static Finder<Long, BargainMember> find = new Finder<>(BargainMember.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
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

    public String getDealerAvatar() {
        return dealerAvatar;
    }

    public void setDealerAvatar(String dealerAvatar) {
        this.dealerAvatar = dealerAvatar;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public long getUserBargainId() {
        return userBargainId;
    }

    public void setUserBargainId(long userBargainId) {
        this.userBargainId = userBargainId;
    }

    public long getBargainConfigId() {
        return bargainConfigId;
    }

    public void setBargainConfigId(long bargainConfigId) {
        this.bargainConfigId = bargainConfigId;
    }

    public String getBargainTitle() {
        return bargainTitle;
    }

    public void setBargainTitle(String bargainTitle) {
        this.bargainTitle = bargainTitle;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getBargainAmount() {
        return bargainAmount;
    }

    public void setBargainAmount(long bargainAmount) {
        this.bargainAmount = bargainAmount;
    }
}
