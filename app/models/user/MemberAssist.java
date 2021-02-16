package models.user;

import io.ebean.Finder;
import io.ebean.Model;
import models.promotion.AssistConfig;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "v1_member_assist")
public class MemberAssist extends Model {
    public static final int STATUS_FAILED = -1;
    public static final int STATUS_PROCESSING = 1;
    public static final int STATUS_SUCCEED = 2;

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

    @Column(name = "assist_id")
    public long assistId;

    @Column(name = "assist_title")
    public String assistTitle;

    @Column(name = "begin_time")
    public long beginTime;//生效时间

    @Column(name = "end_time")
    public long endTime;//失效时间

    @Column(name = "status")
    public long status;//状态

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "use_time")
    public long useTime;

    @Column(name = "invites_amount")
    public int inviteAmount;

    @Column(name = "require_invites_amount")
    public int requireInvitesAmount;

    @Transient
    public AssistConfig config;

    @Transient
    public List<AssistMember> assistMemberList = new ArrayList<>();

    @Transient
    public MemberCardCoupon memberCardCoupon;

    public static Finder<Long, MemberAssist> find = new Finder<>(MemberAssist.class);

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

    public long getAssistId() {
        return assistId;
    }

    public void setAssistId(long assistId) {
        this.assistId = assistId;
    }

    public String getAssistTitle() {
        return assistTitle;
    }

    public void setAssistTitle(String assistTitle) {
        this.assistTitle = assistTitle;
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

    public AssistConfig getConfig() {
        return config;
    }

    public void setConfig(AssistConfig config) {
        this.config = config;
    }

    public int getRequireInvitesAmount() {
        return requireInvitesAmount;
    }

    public void setRequireInvitesAmount(int requireInvitesAmount) {
        this.requireInvitesAmount = requireInvitesAmount;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    @Override
    public String toString() {
        return "MemberAssist{" +
                "id=" + id +
                ", uid=" + uid +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", assistId=" + assistId +
                ", assistTitle='" + assistTitle + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", updateTime=" + updateTime +
                ", useTime=" + useTime +
                ", inviteAmount=" + inviteAmount +
                ", requireInvitesAmount=" + requireInvitesAmount +
                ", config=" + config +
                ", assistMemberList=" + assistMemberList +
                '}';
    }
}

