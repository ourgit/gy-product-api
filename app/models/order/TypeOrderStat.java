package models.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 订单
 */
@Entity
@Table(name = "v1_order_type_stat")
public class TypeOrderStat extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "amount")
    public long amount;

    @Column(name = "activity_type")
    public int activityType;

    @Column(name = "date")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String date;

    public static Finder<Long, TypeOrderStat> find = new Finder<>(TypeOrderStat.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
