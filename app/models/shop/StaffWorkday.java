package models.shop;


import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = "v1_staff_work_day")
public class StaffWorkday extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "staff_id")
    public long staffId;

    @Column(name = "day")
    public int day;

    public static Finder<Long, StaffWorkday> find = new Finder<>(StaffWorkday.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStaffId() {
        return staffId;
    }

    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "StaffWorkday{" +
                "id=" + id +
                ", staffId=" + staffId +
                ", day=" + day +
                '}';
    }
}
