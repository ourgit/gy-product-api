package models.promotion;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 推广表成员表
 */
@Entity
@Table(name = "v1_promot_member")
public class PromotionMember extends Model {
    //1否2已奖
    public static final int TYPE_NOT_AWARD_YET = 1;
    public static final int TYPE_AWARDED = 2;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "promotion_id")
    public long promotionId;//promot表id

    @Column(name = "uid")
    public long uid;//用户id

    @Column(name = "master_id")
    public long masterId;//所属推广人的用户id

    @Column(name = "is_award")
    public int isAward;//1否2已奖

    @Column(name = "create_time")
    public long createdTime;

    public static Finder<Long, PromotionMember> find = new Finder<>(PromotionMember.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(long promotionId) {
        this.promotionId = promotionId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getMasterId() {
        return masterId;
    }

    public void setMasterId(long masterId) {
        this.masterId = masterId;
    }

    public int getIsAward() {
        return isAward;
    }

    public void setIsAward(int isAward) {
        this.isAward = isAward;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "PromotionMember{" +
                "id=" + id +
                ", promotionId=" + promotionId +
                ", uid=" + uid +
                ", masterId=" + masterId +
                ", isAward=" + isAward +
                ", createdTime=" + createdTime +
                '}';
    }
}
