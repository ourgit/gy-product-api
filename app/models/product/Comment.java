package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.DateSerializer;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论
 */
@Entity
@Table(name = "v1_product_comment")
public class Comment extends Model {

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_REPLY = 2;
    public static final int TYPE_APPEND = 3;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "uid")
    public long uid;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "order_id")
    public long orderId;

    @Column(name = "sku_id")
    public long skuId;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "content")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String content;

    @Column(name = "score")
    public int score;

    @Column(name = "type")
    public int type;

    @Column(name = "reply_id")
    public long replyId;

    @Column(name = "sku_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String skuName;

    @Column(name = "has_append")
    public boolean hasAppend;

    @Column(name = "update_time")
    @JsonSerialize(using = DateSerializer.class)
    public long updateTime;//'物流更新时间'

    @Transient
    public List<CommentImage> imgList = new ArrayList<>();

    public static Finder<Long, Comment> find = new Finder<>(Comment.class);


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getReplyId() {
        return replyId;
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
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

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", uid=" + uid +
                ", productId=" + productId +
                ", orderId=" + orderId +
                ", skuId=" + skuId +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", score=" + score +
                ", type=" + type +
                ", replyId=" + replyId +
                ", skuName='" + skuName + '\'' +
                ", hasAppend=" + hasAppend +
                ", updateTime=" + updateTime +
                '}';
    }
}
