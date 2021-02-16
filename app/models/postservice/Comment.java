package models.postservice;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.DateSerializer;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 评论
 */
@Entity
@Table(name = "v1_comment")
public class Comment extends Model {

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_REPLY = 2;
    public static final int TYPE_APPEND = 3;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "order_id")
    public long orderId;

    @Column(name = "content")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String content;

    @Column(name = "level")
    public int level;//' '级别 -1差评 0中评 1好评'

    @Column(name = "desc_star")
    public int descStar;//描述相符 1-5

    @Column(name = "logistics_star")
    public int logisticsStar;//物流服务 1-5

    @Column(name = "service_star")
    public int serviceStar;//服务态度 1-5

    @Column(name = "uid")
    public long uid;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "type")
    public int type;

    @Column(name = "reply_id")
    public long replyId;

    @Column(name = "has_append")
    public boolean hasAppend;

    @Column(name = "update_time")
    @JsonSerialize(using = DateSerializer.class)
    public long updateTime;//'物流更新时间'

    @Column(name = "create_time")
    @JsonSerialize(using = DateSerializer.class)
    public long createTime;//'发货时间'

    public static Finder<Long, Comment> find = new Finder<>(Comment.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDescStar() {
        return descStar;
    }

    public void setDescStar(int descStar) {
        this.descStar = descStar;
    }

    public int getLogisticsStar() {
        return logisticsStar;
    }

    public void setLogisticsStar(int logisticsStar) {
        this.logisticsStar = logisticsStar;
    }

    public int getServiceStar() {
        return serviceStar;
    }

    public void setServiceStar(int serviceStar) {
        this.serviceStar = serviceStar;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getReplyId() {
        return replyId;
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
    }

    public boolean isHasAppend() {
        return hasAppend;
    }

    public void setHasAppend(boolean hasAppend) {
        this.hasAppend = hasAppend;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", productId=" + productId +
                ", orderId=" + orderId +
                ", content='" + content + '\'' +
                ", level=" + level +
                ", descStar=" + descStar +
                ", logisticsStar=" + logisticsStar +
                ", serviceStar=" + serviceStar +
                ", uid=" + uid +
                ", replyId=" + replyId +
                ", hasAppend=" + hasAppend +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
