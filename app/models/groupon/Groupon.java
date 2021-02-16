package models.groupon;

import io.ebean.Finder;
import io.ebean.Model;
import models.product.Product;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "v1_groupon")
public class Groupon extends Model implements Serializable {

    public static final int STATUS_PROCESSING = 1;
    public static final int STATUS_SUCCEED = 2;
    public static final int STATUS_FAILED = 3;
    public static final int STATUS_FAILED_REFUND = 4;
    public static final int STATUS_LEVEL_GROUPON_SUCCEED = 5;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "launcher_uid")
    public long launcherUid;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "sku_id")
    public long skuId;

    @Column(name = "expire_time")
    public long expireTime;

    @Column(name = "orders")
    public long orders;

    @Column(name = "avatars")
    public String avatars;

    @Column(name = "users")
    public String users;

    @Column(name = "uid_list")
    public String uidList;

    @Column(name = "status")
    public int status;

    @Column(name = "groupon_type")
    public int grouponType;

    @Column(name = "create_time")
    public long createTime;//创建时间

    @Transient
    public long needOrdersCount;

    @Transient
    public GrouponOrder grouponOrder;

    @Transient
    public Product product;

    public static Finder<Long, Groupon> find = new Finder<>(Groupon.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLauncherUid() {
        return launcherUid;
    }

    public void setLauncherUid(long launcherUid) {
        this.launcherUid = launcherUid;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getOrders() {
        return orders;
    }

    public void setOrders(long orders) {
        this.orders = orders;
    }

    public String getAvatars() {
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public int getGrouponType() {
        return grouponType;
    }

    public void setGrouponType(int grouponType) {
        this.grouponType = grouponType;
    }

    public String getUidList() {
        return uidList;
    }

    public void setUidList(String uidList) {
        this.uidList = uidList;
    }

    public long getNeedOrdersCount() {
        return needOrdersCount;
    }

    public void setNeedOrdersCount(long needOrdersCount) {
        this.needOrdersCount = needOrdersCount;
    }
}
