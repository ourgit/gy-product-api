package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;
import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * 用户类
 * Created by win7 on 2016/6/7.
 */
@Entity
@Table(name = "v1_member")
public class Member extends Model {

    public static final String DEFAULT_AVATAR_URL = "https://img.maiyacang.com/avatar.png";
    /**
     * 用户的状态：正常
     */
    public static final int MEMBER_STATUS_NORMAL = 1;
    /**
     * 用户的状态：被锁定
     */
    public static final int MEMBER_STATUS_LOCK = 2;
    //1普通会员，2高级会员，3钻石会员，4至尊会员
    public static final int LEVEL_0 = 0;
    public static final int LEVEL_1 = 1;
    public static final int LEVEL_2 = 2;
    public static final int LEVEL_3 = 3;
    public static final int LEVEL_4 = 4;

    public static final int AUTH_STATUS_DENY = -1;
    public static final int AUTH_STATUS_NOT_AUTH = 0;
    public static final int AUTH_STATUS_PROCESSING = 1;
    public static final int AUTH_STATUS_PRE_AUTH = 2;
    public static final int AUTH_STATUS_AUTHED = 3;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Constraints.Required
    @Constraints.MinLength(6)
    @Constraints.MaxLength(30)
    @com.fasterxml.jackson.annotation.JsonIgnore
    @Column(name = "login_password")
    public String loginPassword;//登录密码

    @Column(name = "pay_password")
    @com.fasterxml.jackson.annotation.JsonIgnore
    public String payPassword;//支付密码

    @Column(name = "status")
    public int status;//用户状态 1正常2锁定

    @Column(name = "real_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String realName;//真实姓名

    @Column(name = "nick_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String nickName;//昵称

    @Column(name = "phone_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String phoneNumber;//手机号

    @Column(name = "contact_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String contactNumber;

    @Column(name = "create_time")
    public long createdTime;//创建时间

    @Column(name = "dealer_id")
    public long dealerId;

    @Column(name = "dealer_code")
    public String dealerCode;

    @Column(name = "dealer_type")
    public long dealerType;

    @Column(name = "favs_count")
    public long favsCount;

    @Column(name = "auth_status")
    public int authStatus;

    @Column(name = "level")
    public int level;

    @Column(name = "avatar")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String avatar;//头像

    @Column(name = "continuation_sign_days")
    public long continuationSignDays;

    @Transient
    public long leftBalance;

    @Transient
    public long score;

    @Transient
    public String dealerName;

    @Transient
    public double totalOrderMoney;


    public static Finder<Long, Member> find = new Finder<>(Member.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getLeftBalance() {
        return leftBalance;
    }

    public void setLeftBalance(long leftBalance) {
        this.leftBalance = leftBalance;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getFavsCount() {
        return favsCount;
    }

    public void setFavsCount(long favsCount) {
        this.favsCount = favsCount;
    }

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public long getDealerType() {
        return dealerType;
    }

    public void setDealerType(long dealerType) {
        this.dealerType = dealerType;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public double getTotalOrderMoney() {
        return totalOrderMoney;
    }

    public void setTotalOrderMoney(double totalOrderMoney) {
        this.totalOrderMoney = totalOrderMoney;
    }

    public long getContinuationSignDays() {
        return continuationSignDays;
    }

    public void setContinuationSignDays(long continuationSignDays) {
        this.continuationSignDays = continuationSignDays;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", loginPassword='" + loginPassword + '\'' +
                ", payPassword='" + payPassword + '\'' +
                ", status=" + status +
                ", realName='" + realName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", createdTime=" + createdTime +
                ", dealerId=" + dealerId +
                ", dealerCode='" + dealerCode + '\'' +
                ", dealerType=" + dealerType +
                ", favsCount=" + favsCount +
                ", authStatus=" + authStatus +
                ", level=" + level +
                ", avatar='" + avatar + '\'' +
                ", continuationSignDays=" + continuationSignDays +
                ", leftBalance=" + leftBalance +
                ", score=" + score +
                ", dealerName='" + dealerName + '\'' +
                ", totalOrderMoney=" + totalOrderMoney +
                '}';
    }
}
