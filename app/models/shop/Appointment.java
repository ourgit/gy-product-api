package models.shop;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * org
 */
@Entity
@Table(name = "v1_appointment")
public class Appointment extends Model {

    //状态，1为正常，2为被锁定，3为待审核 4审核不通过 5审核驳回
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_LOCK = 2;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "status")
    public int status;

    @Column(name = "uid")
    public long uid;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "staff_id")
    public long staffId;

    @Column(name = "org_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String orgName;

    @Column(name = "staff_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String staffName;

    @Column(name = "begin_time")
    public long beginTime;

    @Column(name = "end_time")
    public long endTime;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, Appointment> find = new Finder<>(Appointment.class);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public long getStaffId() {
        return staffId;
    }

    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", status=" + status +
                ", uid=" + uid +
                ", orgId=" + orgId +
                ", staffId=" + staffId +
                ", orgName='" + orgName + '\'' +
                ", staffName='" + staffName + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                '}';
    }
}
