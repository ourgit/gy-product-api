package models.enroll;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 *  提交记录
 */
@Entity
@Table(name = "v1_enroll_content_poll")
public class EnrollContentPoll extends Model {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "name")
    public String name;

    @Column(name = "uid")
    public long uid;//问卷ID

    @Column(name = "config_id")
    public long configId;//问卷ID

    @Column(name = "order_id")
    public long orderId;

    @Column(name = "group_id")
    public long groupId;

    @Column(name = "item_id")
    public long itemId;//用户选择的项目

    @Column(name = "item_content")
    public String itemContent;//用户输入的内容

    @Column(name = "update_time")
    public long updateTime;

    public static Finder<Long, EnrollContentPoll> find = new Finder<>(EnrollContentPoll.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getConfigId() {
        return configId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
