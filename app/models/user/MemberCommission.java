package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 用户类
 * Created by win7 on 2016/6/7.
 */
@Entity
@Table(name = "v1_commission")
public class MemberCommission extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "from")
    public long from;//额度区间结起点

    @Column(name = "to")
    public long to;//额度区间结点

    @Column(name = "days")
    public int days;//时间数

    @Column(name = "month")
    public int month;//时间数

    @Column(name = "commission_ratio")
    public double commissionRatio;

    @Column(name = "uid")
    public long uid;

    @Column(name = "user_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String userName;


    public static Finder<Long, MemberCommission> find = new Finder<>(MemberCommission.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getCommissionRatio() {
        return commissionRatio;
    }

    public void setCommissionRatio(double commissionRatio) {
        this.commissionRatio = commissionRatio;
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
}
