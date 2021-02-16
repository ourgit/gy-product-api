package models.groupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "v1_groupon_order")
public class GrouponOrder extends Model implements Serializable {
    public static int STATUS_HIT = 1;
    public static int STATUS_NOT_HIT = 2;
    public static int RETURN_STATUS_NOT_NEED = 0;
    public static int RETURN_STATUS_NEED = 1;
    public static int RETURN_STATUS_FAILED_REFUND_ALL = 2;
    public static int RETURN_STATUS_DONE = 3;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "groupon_id")
    public long grouponId;

    @Column(name = "order_id")
    public long orderId;

    @Column(name = "hit_status")
    public int hitStatus;

    @Column(name = "return_status")
    public int returnStatus;

    @Column(name = "return_money")
    public long returnMoney;

    @Column(name = "amount")
    public long amount;

    @Column(name = "price")
    public long price;

    @Column(name = "groupon_price")
    public long grouponPrice;

    @Column(name = "pay_method")
    public int payMethod;

    @Column(name = "return_tx_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String returnTxId = "";

    public static Finder<Long, GrouponOrder> find = new Finder<>(GrouponOrder.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGrouponId() {
        return grouponId;
    }

    public void setGrouponId(long grouponId) {
        this.grouponId = grouponId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getHitStatus() {
        return hitStatus;
    }

    public void setHitStatus(int hitStatus) {
        this.hitStatus = hitStatus;
    }

    public int getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(int returnStatus) {
        this.returnStatus = returnStatus;
    }

    public long getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(long returnMoney) {
        this.returnMoney = returnMoney;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getGrouponPrice() {
        return grouponPrice;
    }

    public void setGrouponPrice(long grouponPrice) {
        this.grouponPrice = grouponPrice;
    }

    public String getReturnTxId() {
        return returnTxId;
    }

    public void setReturnTxId(String returnTxId) {
        this.returnTxId = returnTxId;
    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    @Override
    public String toString() {
        return "GrouponOrder{" +
                "id=" + id +
                ", grouponId=" + grouponId +
                ", orderId=" + orderId +
                ", hitStatus=" + hitStatus +
                ", returnStatus=" + returnStatus +
                ", returnMoney=" + returnMoney +
                ", amount=" + amount +
                ", price=" + price +
                ", grouponPrice=" + grouponPrice +
                ", payMethod=" + payMethod +
                ", returnTxId='" + returnTxId + '\'' +
                '}';
    }
}
