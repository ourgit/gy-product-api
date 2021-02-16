package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 字典
 */
@Entity
@Table(name = "v1_dict")
public class Dict extends Model {

    public static final int TYPE_SIZE = 1;
    public static final int TYPE_COLOR = 2;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "type")
    public int type;//1为尺寸,2为颜色

    @Column(name = "value")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String value;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, Dict> find = new Finder<>(Dict.class);

    public void setId(long id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Dict{" +
                "id=" + id +
                ", type=" + type +
                ", value='" + value + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
