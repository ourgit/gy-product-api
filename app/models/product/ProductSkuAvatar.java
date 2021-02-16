package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * sku
 */
@Entity
@Table(name = "v1_sku_avatar")
public class ProductSkuAvatar extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "sku_id")
    public long skuId;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "uid")
    public long uid;

    @Column(name = "amount")
    public long amount;

    @Column(name = "avatar")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String avatar;

    @Column(name = "user_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String userName;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, ProductSkuAvatar> find = new Finder<>(ProductSkuAvatar.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "ProductSkuAvatar{" +
                "id=" + id +
                ", skuId=" + skuId +
                ", uid=" + uid +
                ", amount=" + amount +
                ", avatar='" + avatar + '\'' +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
