package models.product;

import io.ebean.Finder;
import io.ebean.Model;
import models.shop.Shop;

import javax.persistence.*;


@Entity
@Table(name = "v1_shop_fav")
public class ShopFav extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "shop_id")
    private long shopId;

    @Column(name = "uid")
    private long uid;

    @Column(name = "enable")
    private boolean enable;

    @Column(name = "create_time")
    private long createTime;

    @Transient
    public Shop shop;

    public static Finder<Long, ShopFav> find = new Finder<>(ShopFav.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ShopFav{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", uid=" + uid +
                ", enable=" + enable +
                ", createTime=" + createTime +
                ", shop=" + shop +
                '}';
    }
}
