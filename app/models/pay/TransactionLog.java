package models.pay;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.DateSerializer;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 订单
 */
@Entity
@Table(name = "v1_transaction_log")
public class TransactionLog extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "order_tx_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String orderTxId;

    @Column(name = "events")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String events;//事件详情

    @Column(name = "result")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String result;//结果详情

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, TransactionLog> find = new Finder<>(TransactionLog.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderTxId() {
        return orderTxId;
    }

    public void setOrderTxId(String orderTxId) {
        this.orderTxId = orderTxId;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    @Override
    public String toString() {
        return "TransactionLog{" +
                "id=" + id +
                ", orderTxId='" + orderTxId + '\'' +
                ", events='" + events + '\'' +
                ", result='" + result + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
