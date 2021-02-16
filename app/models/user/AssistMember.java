package models.user;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name = "v1_assist_member")
public class AssistMember extends Model {

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

    @Column(name = "user_avatar")
    public String avatar;

    @Column(name = "user_assist_id")
    public long userAssistId;

    @Column(name = "assist_title")
    public String assistTitle;

    @Column(name = "create_time")
    public long createdTime;

    public static Finder<Long, AssistMember> find = new Finder<>(AssistMember.class);

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getUserAssistId() {
        return userAssistId;
    }

    public void setUserAssistId(long userAssistId) {
        this.userAssistId = userAssistId;
    }

    public String getAssistTitle() {
        return assistTitle;
    }

    public void setAssistTitle(String assistTitle) {
        this.assistTitle = assistTitle;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "AssistMember{" +
                "id=" + id +
                ", dealerId=" + dealerId +
                ", dealerName='" + dealerName + '\'' +
                ", uid=" + uid +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", userAssistId=" + userAssistId +
                ", assistTitle='" + assistTitle + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }

}
