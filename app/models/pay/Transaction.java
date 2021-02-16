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
@Table(name = "v1_transaction")
public class Transaction extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "order_tx_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String orderTxId;

    @Column(name = "uid")
    public long uid;

    @Column(name = "amount")
    public long amount;//交易金额

    @Column(name = "usedScore")
    public long usedScore;//使用的积分

    @Column(name = "pay_method")
    public int payMethod;//支付渠道 0余额 1微信 2支付宝

    @Column(name = "pay_source")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String paySource;//支付来源 wx app web wap

    @Column(name = "status")
    public int status;//支付状态 -1：取消 0 未完成 1已完成 -2:异常

    @Column(name = "complete_time")
    public long completeTime;//完成时间

    @Column(name = "note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String note;//备注

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, Transaction> find = new Finder<>(Transaction.class);


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

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getUsedScore() {
        return usedScore;
    }

    public void setUsedScore(long usedScore) {
        this.usedScore = usedScore;
    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public String getPaySource() {
        return paySource;
    }

    public void setPaySource(String paySource) {
        this.paySource = paySource;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        return "Transaction{" +
                "id=" + id +
                ", orderTxId='" + orderTxId + '\'' +
                ", uid=" + uid +
                ", amount=" + amount +
                ", usedScore=" + usedScore +
                ", payMethod=" + payMethod +
                ", paySource='" + paySource + '\'' +
                ", status=" + status +
                ", completeTime=" + completeTime +
                ", note='" + note + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
