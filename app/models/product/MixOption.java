package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 混搭选项
 */
@Entity
@Table(name = "v1_mix_option")
public class MixOption extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "amount")
    public int amount;

    @Column(name = "mix_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String mixCode;


    public static Finder<Integer, MixOption> find = new Finder<>(MixOption.class);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMixCode() {
        return mixCode;
    }

    public void setMixCode(String mixCode) {
        this.mixCode = mixCode;
    }

    @Override
    public String toString() {
        return "MixOption{" +
                "id=" + id +
                ", amount=" + amount +
                ", mixCode='" + mixCode + '\'' +
                '}';
    }
}
