package models.activity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 奖品配置表
 */
@Entity
@Table(name = "v1_activity_config")
public class ActivityConfig extends Model implements Serializable {
    public static final int STATUS_NOT_START = 1;
    public static final int STATUS_PROCESSING = 2;
    public static final int STATUS_END = 3;
    private static final long serialVersionUID = 897490437582777897L;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String title;

    @Column(name = "note")
    public String note;

    @Column(name = "images")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String images;

    @Column(name = "open_time")
    public long openTime;

    @Column(name = "begin_time")
    public long beginTime;

    @Column(name = "attenders")
    public long attenders;

    @Column(name = "create_time")
    public long createdTime;//创建时间

    @Column(name = "update_time")
    public long updateTime;//创建时间

    @Column(name = "status")
    public int status;

    @Column(name = "need_pay")
    public boolean needPay;

    public static Finder<Long, ActivityConfig> find = new Finder<>(ActivityConfig.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    public long getAttenders() {
        return attenders;
    }

    public void setAttenders(long attenders) {
        this.attenders = attenders;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public boolean isNeedPay() {
        return needPay;
    }

    public void setNeedPay(boolean needPay) {
        this.needPay = needPay;
    }

    @Override
    public String toString() {
        return "LuckyConfig{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", note='" + note + '\'' +
                ", images='" + images + '\'' +
                ", openTime=" + openTime +
                ", beginTime=" + beginTime +
                ", attenders=" + attenders +
                ", createdTime=" + createdTime +
                ", updateTime=" + updateTime +
                ", status=" + status +
                '}';
    }
}
