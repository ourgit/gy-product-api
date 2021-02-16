package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;


@Entity
@Table(name = "v1_membership_log")
public class MembershipLog extends Model {


    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "uid")
    public long uid;

    @Column(name = "user_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String userName;

    @Column(name = "membership_title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String membershipTitle;

    @Column(name = "level")
    public int level;

    @Column(name = "money")
    public long money;

    @Column(name = "first_dealer_id")
    public long firstDealerId;

    @Column(name = "first_dealer_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String firstDealerName;

    @Column(name = "first_dealer_award")
    public long firstDealerAward;

    @Column(name = "second_dealer_id")
    public long secondDealerId;

    @Column(name = "second_dealer_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String secondDealerName;

    @Column(name = "second_dealer_award")
    public long secondDealerAward;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, MembershipLog> find = new Finder<>(MembershipLog.class);

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

    public String getMembershipTitle() {
        return membershipTitle;
    }

    public void setMembershipTitle(String membershipTitle) {
        this.membershipTitle = membershipTitle;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getFirstDealerId() {
        return firstDealerId;
    }

    public void setFirstDealerId(long firstDealerId) {
        this.firstDealerId = firstDealerId;
    }

    public String getFirstDealerName() {
        return firstDealerName;
    }

    public void setFirstDealerName(String firstDealerName) {
        this.firstDealerName = firstDealerName;
    }

    public long getFirstDealerAward() {
        return firstDealerAward;
    }

    public void setFirstDealerAward(long firstDealerAward) {
        this.firstDealerAward = firstDealerAward;
    }

    public long getSecondDealerId() {
        return secondDealerId;
    }

    public void setSecondDealerId(long secondDealerId) {
        this.secondDealerId = secondDealerId;
    }

    public String getSecondDealerName() {
        return secondDealerName;
    }

    public void setSecondDealerName(String secondDealerName) {
        this.secondDealerName = secondDealerName;
    }

    public long getSecondDealerAward() {
        return secondDealerAward;
    }

    public void setSecondDealerAward(long secondDealerAward) {
        this.secondDealerAward = secondDealerAward;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MembershipLog{" +
                "id=" + id +
                ", uid=" + uid +
                ", userName='" + userName + '\'' +
                ", membershipTitle='" + membershipTitle + '\'' +
                ", level=" + level +
                ", money=" + money +
                ", firstDealerId=" + firstDealerId +
                ", firstDealerName='" + firstDealerName + '\'' +
                ", firstDealerAward=" + firstDealerAward +
                ", secondDealerId=" + secondDealerId +
                ", secondDealerName='" + secondDealerName + '\'' +
                ", secondDealerAward=" + secondDealerAward +
                ", createTime=" + createTime +
                '}';
    }
}
