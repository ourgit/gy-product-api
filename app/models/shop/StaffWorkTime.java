package models.shop;


import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = "v1_staff_work_time")
public class StaffWorkTime extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "staff_id")
    public long staffId;

    @Column(name = "day")
    public int day;

    @Column(name = "hour")
    public int hour;

    public static Finder<Long, StaffWorkTime> find = new Finder<>(StaffWorkTime.class);

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

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
