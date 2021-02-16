package models.log;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.DateSerializer;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 用户积分变动记录表
 */
@Entity
@Table(name = "v1_member_score_log")
public class MemberScoreLog extends Model {

    //1增加 2扣除
    public static final int TYPE_ADD = 1;
    public static final int TYPE_SUBTRACT = 2;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "member_id")
    public long memberId;//

    @Column(name = "score")
    public long score;//本次变动的积分

    @Column(name = "type")
    public int type;//1增加 2扣除

    @Column(name = "reason_type")
    public int reasonType;//变动原因 参考MemberScoreConfig中的类型 1注册，2认证手机3认证邮箱4首次充值5每日登录6充值7交易

    @Column(name = "description")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String description;//描述

    @Column(name = "create_time")
    @JsonSerialize(using = DateSerializer.class)
    public long createTime;

    public static Finder<Long, MemberScoreLog> find = new Finder<>(MemberScoreLog.class);

    public void setId(long id) {
        this.id = id;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setReasonType(int reasonType) {
        this.reasonType = reasonType;
    }

    @Override
    public String toString() {
        return "MemberScoreLog{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", score=" + score +
                ", type=" + type +
                ", reasonType=" + reasonType +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
