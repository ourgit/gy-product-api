package models.promotion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;
import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * 推广记录
 */
@Entity
@Table(name = "v1_promot")
public class Promotion extends Model {
    //1待审核 2 已通过 3已拒绝
    public static final int PROMOTION_STATUS_UNDER_AUDIT = 1;
    public static final int PROMOTION_STATUS_PASS = 2;
    public static final int PROMOTION_STATUS_REFUSED = 3;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public long id;

    @Column(name = "uid")
    public long uid;//用户id

    @Column(name = "invite_count")
    public int inviteCount;//邀请人数

    @Column(name = "income")
    public long income;//邀请收益

    @JsonIgnore
    @Column(name = "status")
    public int status;//1待审核 2 已通过 3已拒绝

    @Column(name = "invite_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    @Constraints.MaxLength(10)
    public String inviteCode;//邀请码

    @JsonIgnore
    @Column(name = "update_time")
    public long updateTime;

    @JsonIgnore
    @Column(name = "create_time")
    public long createdTime;

    public static Finder<Long, Promotion> find = new Finder<>(Promotion.class);

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

    public int getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(int inviteCount) {
        this.inviteCount = inviteCount;
    }

    public long getIncome() {
        return income;
    }

    public void setIncome(long income) {
        this.income = income;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
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

    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", uid=" + uid +
                ", inviteCount=" + inviteCount +
                ", income=" + income +
                ", status=" + status +
                ", inviteCode='" + inviteCode + '\'' +
                ", updateTime=" + updateTime +
                ", createdTime=" + createdTime +
                '}';
    }
}

