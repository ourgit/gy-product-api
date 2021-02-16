package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 用户持有的优惠券
 */
@Entity
@Table(name = "v1_member_coupon")
public class MemberCoupon extends Model {
    //1为未使用，2为已使用,3为已失效
    public static final int STATUS_NOT_USE = 1;
    public static final int STATUS_USED = 2;
    public static final int STATUS_EXPIRED = 3;
    public static final int STATUS_UNPAY = 4;
    public static final int STATUS_PAIED = 5;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "uid")
    public long uid;//用户ID

    @Column(name = "coupon_id")
    public long couponId;//优惠券配置ID

    @Column(name = "begin_time")
    public long beginTime;//生效时间

    @Column(name = "end_time")
    public long endTime;//失效时间

    @Column(name = "status")
    public long status;//状态

    @Column(name = "code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String code;//券码

    @Column(name = "tx_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String transactionId;//流水ID

    @Column(name = "sub_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String subId;//第三方支付返回的事务ID

    @Column(name = "pay_type")
    public int payType;//支付方式

    @Column(name = "real_pay")
    public int realPay;//实付

    @Column(name = "update_time")
    public long updateTime;

    public static Finder<Long, MemberCoupon> find = new Finder<>(MemberCoupon.class);

    public void setId(long id) {
        this.id = id;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public void setRealPay(int realPay) {
        this.realPay = realPay;
    }

    @Override
    public String toString() {
        return "MemberCoupon{" +
                "id=" + id +
                ", uid=" + uid +
                ", couponId=" + couponId +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", code='" + code + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", subId='" + subId + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}

