package models.user;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 用户点赞
 */
@Entity
@Table(name = "v1_member_like")
public class MemberLike extends Model {

    public static int TYPE_CROWD_FUND = 1;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "member_id")
    public long memberId;

    @Column(name = "type")
    public int type;

    @Column(name = "target_id")
    public long targetId;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, MemberLike> find = new Finder<>(MemberLike.class);

    public void setId(long id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "MemberLike{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", type=" + type +
                ", targetId=" + targetId +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
