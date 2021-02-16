package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 邮费配置
 */
@Entity
@Table(name = "v1_mail_fee_config")
public class MailFee extends Model {
    public static final int METHOD_INSTANT_SEND = 2;
    public static final int METHOD_MAIL = 3;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "region_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String regionCode;

    @Column(name = "region_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String regionName;

    @Column(name = "first_weight_fee")
    public long firstWeightFee;

    @Column(name = "next_weight_fee")
    public long nextWeightFee;

    @Column(name = "upto_money_free")
    public long uptoMoneyFree;

    @Column(name = "upto_amount_free")
    public long uptoAmountFree;

    @Column(name = "title")
    public String title;

    @Column(name = "method")
    public int method;

    @Column(name = "update_time")
    public long updateTime;

    public static Finder<Integer, MailFee> find = new Finder<>(MailFee.class);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public double getFirstWeightFee() {
        return firstWeightFee;
    }

    public void setFirstWeightFee(long firstWeightFee) {
        this.firstWeightFee = firstWeightFee;
    }

    public double getNextWeightFee() {
        return nextWeightFee;
    }

    public void setNextWeightFee(long nextWeightFee) {
        this.nextWeightFee = nextWeightFee;
    }

    public double getUptoMoneyFree() {
        return uptoMoneyFree;
    }

    public void setUptoMoneyFree(long uptoMoneyFree) {
        this.uptoMoneyFree = uptoMoneyFree;
    }

    public double getUptoAmountFree() {
        return uptoAmountFree;
    }

    public void setUptoAmountFree(long uptoAmountFree) {
        this.uptoAmountFree = uptoAmountFree;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "MailFee{" +
                "id=" + id +
                ", regionCode='" + regionCode + '\'' +
                ", regionName='" + regionName + '\'' +
                ", firstWeightFee=" + firstWeightFee +
                ", nextWeightFee=" + nextWeightFee +
                ", updateTime=" + updateTime +
                '}';
    }
}
