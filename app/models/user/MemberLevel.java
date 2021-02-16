package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 用户等级类
 */
@Entity
@Table(name = "v1_member_level")
public class MemberLevel extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "need_score")
    public long needScore;//该等级所需积分

    @Column(name = "level")
    public int level;//用户等级

    @Column(name = "level_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String levelName;//用户等级名字

    @Column(name="order_discount")
    public int orderDiscount;//订单折扣

    @Column(name = "update_time")
    public long updateTime;//更新时间

    @Column(name = "create_time")
    public long createdTime;//创建时间

    public static Finder<Long, MemberLevel> find = new Finder<>(MemberLevel.class);

    public void setId(int id) {
        this.id = id;
    }

    public void setNeedScore(long needScore) {
        this.needScore = needScore;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public void setOrderDiscount(int orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "MemberLevel{" +
                "id=" + id +
                ", needScore=" + needScore +
                ", level=" + level +
                ", levelName='" + levelName + '\'' +
                ", orderDiscount=" + orderDiscount +
                ", updateTime=" + updateTime +
                ", createdTime=" + createdTime +
                '}';
    }
}
