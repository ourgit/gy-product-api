package models.postservice;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ebean.Finder;
import io.ebean.Model;
import models.order.Order;
import myannotation.DateSerializer;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 退货订单
 */
@Entity
@Table(name = "v1_order_returns")
public class OrderReturns extends Model {
    public static final int STATUS_REJECT = -2;//申请退货被驳回
    public static final int STATUS_CANCEL_APPLY = -1;//取消退货
    public static final int STATUS_APPLY = 1;//申请售后
    public static final int STATUS_AUDIT_PASS = 2;//审核通过
    public static final int STATUS_DELIEVERYING_BACK = 3;//寄出
    public static final int STATUS_DELIEVERED = 4;//已收货
    public static final int STATUS_REFUND = 5;//已退款
    public static final int STATUS_FINISHED = 200;//处理完毕

    public static final int STATE_REFUND = 1;//仅退款
    public static final int STATE_RETURN_REFUND = 2;//退货退款

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "returns_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String returnsNo;


    @Column(name = "order_id")
    public long orderId;

    @Column(name = "order_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String orderNo;

    @Column(name = "order_detail_id")
    public long orderDetailId;//子订单编码

    @Column(name = "express_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String expressNo;

    @Column(name = "consignee_realname")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String consigneeRealname;//收货人姓名

    @Column(name = "consignee_phone_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String consigneePhoneNumber;//收货人电话

    @Column(name = "consignee_address")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String consigneeAddress;//收货地址

    @Column(name = "consignee_postcode")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String consigneePostcode;

    @Column(name = "logis_name")
    public String logisName;

    @Column(name = "state")
    public int state;//0 仅退款 1退货退款

    @Column(name = "pre_status")
    public int preStatus;

    @Column(name = "status")
    public int status;

    @Column(name = "uid")
    public long uid;

    @Column(name = "operator_id")
    public long operatorId;

    @Column(name = "operator_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String operatorName;

    @Column(name = "reason")
    public String reason;//审核拒绝的原因

    @Column(name = "logistics_last_desc")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String logisticsLastDesc;//'物流最后状态描述'

    @Column(name = "logistics_desc")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String logistics_desc;//'物流描述'

    @Column(name = "return_type")
    public int returnType;//'0全部退单 1部分退单' DEFAULT '0'

    @Column(name = "handling_way")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String handlingWay;//'PUPAWAY:退货入库;REDELIVERY:重新发货;RECLAIM-REDELIVERY:不要求归还并重新发货; REFUND:退款; COMPENSATION:不退货并赔偿',

    @Column(name = "return_money")
    public long returnMoney;//'退款金额'

    @Column(name = "return_submit_time")
    public long returnSubmitTime;//'退货申请时间'

    @Column(name = "handling_return_time")
    public long handlingReturnTime;//'退货处理时间'

    @Column(name = "remark")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String remark;//'退货原因'

    @Column(name = "update_time")
    @JsonSerialize(using = DateSerializer.class)
    public long updateTime;//'物流更新时间'

    @Column(name = "create_time")
    @JsonSerialize(using = DateSerializer.class)
    public long createTime;//'发货时间'

    @Transient
    public Order order;

    public static Finder<Long, OrderReturns> find = new Finder<>(OrderReturns.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReturnsNo() {
        return returnsNo;
    }

    public void setReturnsNo(String returnsNo) {
        this.returnsNo = returnsNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getLogisName() {
        return logisName;
    }

    public void setLogisName(String logisName) {
        this.logisName = logisName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(int preStatus) {
        this.preStatus = preStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLogisticsLastDesc() {
        return logisticsLastDesc;
    }

    public void setLogisticsLastDesc(String logisticsLastDesc) {
        this.logisticsLastDesc = logisticsLastDesc;
    }

    public String getLogistics_desc() {
        return logistics_desc;
    }

    public void setLogistics_desc(String logistics_desc) {
        this.logistics_desc = logistics_desc;
    }

    public int getReturnType() {
        return returnType;
    }

    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }

    public String getHandlingWay() {
        return handlingWay;
    }

    public void setHandlingWay(String handlingWay) {
        this.handlingWay = handlingWay;
    }

    public double getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(long returnMoney) {
        this.returnMoney = returnMoney;
    }

    public long getReturnSubmitTime() {
        return returnSubmitTime;
    }

    public void setReturnSubmitTime(long returnSubmitTime) {
        this.returnSubmitTime = returnSubmitTime;
    }

    public long getHandlingReturnTime() {
        return handlingReturnTime;
    }

    public void setHandlingReturnTime(long handlingReturnTime) {
        this.handlingReturnTime = handlingReturnTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getConsigneeRealname() {
        return consigneeRealname;
    }

    public void setConsigneeRealname(String consigneeRealname) {
        this.consigneeRealname = consigneeRealname;
    }

    public String getConsigneePhoneNumber() {
        return consigneePhoneNumber;
    }

    public void setConsigneePhoneNumber(String consigneePhoneNumber) {
        this.consigneePhoneNumber = consigneePhoneNumber;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getConsigneePostcode() {
        return consigneePostcode;
    }

    public void setConsigneePostcode(String consigneePostcode) {
        this.consigneePostcode = consigneePostcode;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "OrderReturns{" +
                "id=" + id +
                ", returnsNo='" + returnsNo + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderDetailId=" + orderDetailId +
                ", expressNo='" + expressNo + '\'' +
                ", consigneeRealname='" + consigneeRealname + '\'' +
                ", consigneePhoneNumber='" + consigneePhoneNumber + '\'' +
                ", consigneeAddress='" + consigneeAddress + '\'' +
                ", consigneePostcode='" + consigneePostcode + '\'' +
                ", logisName='" + logisName + '\'' +
                ", state=" + state +
                ", preStatus=" + preStatus +
                ", status=" + status +
                ", uid=" + uid +
                ", operatorId=" + operatorId +
                ", operatorName='" + operatorName + '\'' +
                ", reason='" + reason + '\'' +
                ", logisticsLastDesc='" + logisticsLastDesc + '\'' +
                ", logistics_desc='" + logistics_desc + '\'' +
                ", returnType=" + returnType +
                ", handlingWay='" + handlingWay + '\'' +
                ", returnMoney=" + returnMoney +
                ", returnSubmitTime=" + returnSubmitTime +
                ", handlingReturnTime=" + handlingReturnTime +
                ", remark='" + remark + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", order=" + order +
                '}';
    }
}
