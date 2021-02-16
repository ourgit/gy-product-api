package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户类
 * Created by win7 on 2016/6/7.
 */
@Entity
@Table(name = "v1_member_profile")
public class MemberProfile extends Model {
    //1普通会员，2高级会员，3钻石会员，4至尊会员
    public static final int LEVEL_0 = 0;
    public static final int LEVEL_1 = 1;
    public static final int LEVEL_2 = 2;
    public static final int LEVEL_3 = 3;
    public static final int LEVEL_4 = 4;
    public static final int LEVEL_PROXY = 8;

    public static final int LEVEL_COOPERATOR = 20;
    public static final int LEVEL_COOPERATOR_2 = 24;
    public static final int LEVEL_COOPERATOR_3 = 28;
    public static final int LEVEL_COOPERATOR_SHOP_1 = 32;
    public static final int LEVEL_COOPERATOR_SHOP_2 = 36;
    public static final int LEVEL_COOPERATOR_SHOP_3 = 40;

    public static final int PARTNER_DENY = -2;
    public static final int PARTNER_NOT_APPLY = -1;
    public static final int PARTNER_APPLY = 1;
    public static final int PARTNER_APPROVE = 2;


    public static final int DEALER_TYPE_SUPPLIER_CANCELED = -2;
    public static final int DEALER_TYPE_SALESMAN_CANCELED = -1;
    public static final int DEALER_TYPE_SALESMAN = 1;//业务员
    public static final int DEALER_TYPE_DIST_MAN = 2;//分销商
    public static final int DEALER_TYPE_WHOLE_SALE_MAN = 3;//批发商
    public static final int DEALER_TYPE_SUPPLIER = 4;//供货商

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "uid")
    public long uid;

    @Column(name = "description")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String description;//备注

    @Column(name = "birthday")
    public long birthday;//生日

    @Column(name = "update_time")
    public long updateTime;//更新时间

    @Column(name = "level")
    public int level;//1普通会员，2高级会员，3钻石会员，4至尊会员

    @Column(name = "id_card_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String idCardNo;//身份证号码

    @Column(name = "license_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String licenseNo;

    @Column(name = "gender")
    public int gender;//0：未知、1：男、2：女

    @Column(name = "city")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String city;

    @Column(name = "province")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String province;

    @Column(name = "country")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String country;

    @Column(name = "country_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String countryCode;

    @Column(name = "shop_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String shopName;

    @Column(name = "shop_link")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String shopLink;

    @Column(name = "contact_phone_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String contactPhoneNumber;

    @Column(name = "contact_address")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String contactAddress;

    @Column(name = "business_items")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String businessItems;

    @Column(name = "images")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String images;

    @Column(name = "auth_note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String authNote;

    @Column(name = "month_sales_amount")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String monthSalesAmount;

    @Column(name = "auth_status")
    public int authStatus;

    @Column(name = "dealer_type")
    public long dealerType;

    @Column(name = "dealer_id")
    public long dealerId;

    @Column(name = "second_dealer_id")
    public long secondDealerId;

    @Column(name = "third_dealer_id")
    public long thirdDealerId;

    @Column(name = "fourth_dealer_id")
    public long fourthDealerId;

    @Column(name = "agent_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String agentCode;

    @Column(name = "self_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String selfCode;

    @Column(name = "apply_time")
    public long applyTime;

    @Column(name = "membership")
    public int membership;

    @Column(name = "membership_expire_time")
    public long membershipExpireTime;

    @Column(name = "open_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String openId;//微信openId，作为帐号标识

    @Column(name = "session_key")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String sessionKey;

    @Column(name = "union_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String unionId;

    @Column(name = "barcode_img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String barcodeImgUrl;

    @Column(name = "approve_time")
    public long approveTime;

    @Column(name = "create_time")
    public long createTime;

    @Column(name = "root_dealer_id")
    public long rootDealerId;

    @Column(name = "is_pickup_place")
    public boolean isPickupPlace;

    @Column(name = "is_driver")
    public boolean isDriver;

    @Column(name = "partner_status")
    public int partnerStatus;

    @Column(name = "root_dealer_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String rootDealerName = "";

    @Column(name = "relationship")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String relationship = "";

    @Column(name = "real_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String realName;//真实姓名

    @Column(name = "nick_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String nickName;//昵称

    @Column(name = "nick_name2")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String nickNameText;

    @Column(name = "invite_expire_time")
    public long inviteExpireTime;

    @Column(name = "avatar")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String avatar;//昵称

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "org_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String orgName;

    @Transient
    public Member member;

    @Transient
    public String dealerName;

    @Transient
    public String userName;

    @Transient
    public String membershipTitle;

    @Transient
    public long totalOrderMoney;

    @Transient
    public long totalOrders;

    @Transient
    public List<MemberBalance> balances = new ArrayList();

    @Transient
    public List<MemberCommission> commissionList = new ArrayList();

    public static Finder<Long, MemberProfile> find = new Finder<>(MemberProfile.class);

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getBusinessItems() {
        return businessItems;
    }

    public void setBusinessItems(String businessItems) {
        this.businessItems = businessItems;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getAuthNote() {
        return authNote;
    }

    public void setAuthNote(String authNote) {
        this.authNote = authNote;
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

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public long getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(long approveTime) {
        this.approveTime = approveTime;
    }

    public long getDealerType() {
        return dealerType;
    }

    public void setDealerType(long dealerType) {
        this.dealerType = dealerType;
    }

    public String getShopLink() {
        return shopLink;
    }

    public void setShopLink(String shopLink) {
        this.shopLink = shopLink;
    }

    public String getMonthSalesAmount() {
        return monthSalesAmount;
    }

    public void setMonthSalesAmount(String monthSalesAmount) {
        this.monthSalesAmount = monthSalesAmount;
    }

    public int getMembership() {
        return membership;
    }

    public void setMembership(int membership) {
        this.membership = membership;
    }

    public long getMembershipExpireTime() {
        return membershipExpireTime;
    }

    public void setMembershipExpireTime(long membershipExpireTime) {
        this.membershipExpireTime = membershipExpireTime;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getBarcodeImgUrl() {
        return barcodeImgUrl;
    }

    public void setBarcodeImgUrl(String barcodeImgUrl) {
        this.barcodeImgUrl = barcodeImgUrl;
    }

    public long getRootDealerId() {
        return rootDealerId;
    }

    public void setRootDealerId(long rootDealerId) {
        this.rootDealerId = rootDealerId;
    }

    public String getRootDealerName() {
        return rootDealerName;
    }

    public void setRootDealerName(String rootDealerName) {
        this.rootDealerName = rootDealerName;
    }

    public boolean isPickupPlace() {
        return isPickupPlace;
    }

    public void setPickupPlace(boolean pickupPlace) {
        isPickupPlace = pickupPlace;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public int getPartnerStatus() {
        return partnerStatus;
    }

    public void setPartnerStatus(int partnerStatus) {
        this.partnerStatus = partnerStatus;
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

    public String getSelfCode() {
        return selfCode;
    }

    public void setSelfCode(String selfCode) {
        this.selfCode = selfCode;
    }

    public long getSecondDealerId() {
        return secondDealerId;
    }

    public void setSecondDealerId(long secondDealerId) {
        this.secondDealerId = secondDealerId;
    }

    public long getThirdDealerId() {
        return thirdDealerId;
    }

    public void setThirdDealerId(long thirdDealerId) {
        this.thirdDealerId = thirdDealerId;
    }

    public long getFourthDealerId() {
        return fourthDealerId;
    }

    public void setFourthDealerId(long fourthDealerId) {
        this.fourthDealerId = fourthDealerId;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public void setDriver(boolean driver) {
        isDriver = driver;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getNickNameText() {
        return nickNameText;
    }

    public void setNickNameText(String nickNameText) {
        this.nickNameText = nickNameText;
    }

    public long getInviteExpireTime() {
        return inviteExpireTime;
    }

    public void setInviteExpireTime(long inviteExpireTime) {
        this.inviteExpireTime = inviteExpireTime;
    }

    @Override
    public String toString() {
        return "MemberProfile{" +
                "id=" + id +
                ", uid=" + uid +
                ", description='" + description + '\'' +
                ", birthday=" + birthday +
                ", updateTime=" + updateTime +
                ", level=" + level +
                ", idCardNo='" + idCardNo + '\'' +
                ", licenseNo='" + licenseNo + '\'' +
                ", gender=" + gender +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopLink='" + shopLink + '\'' +
                ", contactPhoneNumber='" + contactPhoneNumber + '\'' +
                ", contactAddress='" + contactAddress + '\'' +
                ", businessItems='" + businessItems + '\'' +
                ", images='" + images + '\'' +
                ", authNote='" + authNote + '\'' +
                ", monthSalesAmount='" + monthSalesAmount + '\'' +
                ", authStatus=" + authStatus +
                ", dealerType=" + dealerType +
                ", dealerId=" + dealerId +
                ", secondDealerId=" + secondDealerId +
                ", thirdDealerId=" + thirdDealerId +
                ", fourthDealerId=" + fourthDealerId +
                ", agentCode='" + agentCode + '\'' +
                ", selfCode='" + selfCode + '\'' +
                ", applyTime=" + applyTime +
                ", membership=" + membership +
                ", membershipExpireTime=" + membershipExpireTime +
                ", openId='" + openId + '\'' +
                ", sessionKey='" + sessionKey + '\'' +
                ", unionId='" + unionId + '\'' +
                ", barcodeImgUrl='" + barcodeImgUrl + '\'' +
                ", approveTime=" + approveTime +
                ", createTime=" + createTime +
                ", rootDealerId=" + rootDealerId +
                ", isPickupPlace=" + isPickupPlace +
                ", isDriver=" + isDriver +
                ", partnerStatus=" + partnerStatus +
                ", rootDealerName='" + rootDealerName + '\'' +
                ", relationship='" + relationship + '\'' +
                ", realName='" + realName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", orgId=" + orgId +
                ", orgName='" + orgName + '\'' +
                ", member=" + member +
                ", dealerName='" + dealerName + '\'' +
                ", userName='" + userName + '\'' +
                ", totalOrderMoney=" + totalOrderMoney +
                ", totalOrders=" + totalOrders +
                '}';
    }
}
