package models.shop;


import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Entity
@Table(name = "v1_staff")
public class Staff extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "name")
    public String name;

    @Column(name = "role")
    public String role;

    @Column(name = "avatar")
    public String avatar;

    @Column(name = "introduce")
    public String introduce;

    @Column(name = "create_time")
    public long createTime;

    @Transient
    public List<StaffWorkday> workdayList = new ArrayList<>();

    @Transient
    public List<StaffWorkTime> workTimeList = new ArrayList<>();

    public static Finder<Long, Staff> find = new Finder<>(Staff.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
