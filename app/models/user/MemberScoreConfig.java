package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 用户积分配置表
 */
@Entity
@Table(name = "v1_member_score_config")
public class MemberScoreConfig extends Model {

    //类型，1注册，2认证手机3认证邮箱4首次充值5每日登录6充值7交易
    public static final int TYPE_REG = 1;
    public static final int TYPE_AUTHENTICATE_PHONENUMBER = 2;
    public static final int TYPE_AUTHENTICATE_EMAIL = 3;
    public static final int TYPE_FIRST_CHARGE = 4;
    public static final int TYPE_LOGIN_EVERYDAY = 5;
    public static final int TYPE_CHARGE = 6;
    public static final int TYPE_TRANSACTION = 7;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "type")
    public int type;//类型，1注册，2认证手机3认证邮箱4首次充值5每日登录6充值7交易

    @Column(name = "description")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String description;

    @Column(name = "score")
    public long score;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, MemberScoreConfig> find = new Finder<>(MemberScoreConfig.class);

    public void setId(long id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MemberScoreConfig{" +
                "id=" + id +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", score=" + score +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
