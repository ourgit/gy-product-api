package models.log;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "v1_balance_log")
public class BalanceLog extends Model implements Serializable {
    private static final long serialVersionUID = -1885841224604019263L;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "uid")
    public long uid;//用户id

    @Column(name = "item_id")
    public int itemId;

    @Column(name = "left_balance")
    public long leftBalance;

    @Column(name = "freeze_balance")
    public long freezeBalance;

    @Column(name = "total_balance")
    public long totalBalance;

    @Column(name = "change_amount")
    public long changeAmount;

    @Column(name = "biz_type")
    public int bizType;

    @Column(name = "note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String note;

    @Column(name = "create_time")
    public long createTime;//创建时间

    @Transient
    public String changeAmountStr;

    @Transient
    public String freezeBalanceStr;

    public static Finder<Long, BalanceLog> find = new Finder<>(BalanceLog.class);

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

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public long getLeftBalance() {
        return leftBalance;
    }

    public void setLeftBalance(long leftBalance) {
        this.leftBalance = leftBalance;
    }

    public long getFreezeBalance() {
        return freezeBalance;
    }

    public void setFreezeBalance(long freezeBalance) {
        this.freezeBalance = freezeBalance;
    }

    public long getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(long totalBalance) {
        this.totalBalance = totalBalance;
    }

    public long getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(long changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public String getChangeAmountStr() {
        return changeAmountStr;
    }

    public void setChangeAmountStr(String changeAmountStr) {
        this.changeAmountStr = changeAmountStr;
    }

    public String getFreezeBalanceStr() {
        return freezeBalanceStr;
    }

    public void setFreezeBalanceStr(String freezeBalanceStr) {
        this.freezeBalanceStr = freezeBalanceStr;
    }

    @Override
    public String toString() {
        return "BalanceLog{" +
                "id=" + id +
                ", uid=" + uid +
                ", itemId=" + itemId +
                ", leftBalance=" + leftBalance +
                ", freezeBalance=" + freezeBalance +
                ", totalBalance=" + totalBalance +
                ", changeAmount=" + changeAmount +
                ", bizType=" + bizType +
                ", note='" + note + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
