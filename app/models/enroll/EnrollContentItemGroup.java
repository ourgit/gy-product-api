package models.enroll;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "v1_enroll_content_item_group")
public class EnrollContentItemGroup extends Model {
    public static final int TYPE_RADIO_BTN = 1;
    public static final int TYPE_MULTI_BTN = 2;
    public static final int TYPE_INPUT_BTN = 3;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "config_id")
    public long configId;//问卷ID

    @Column(name = "title")
    public String title;

    @Column(name = "sort")
    public int sort;

    @Column(name = "is_require")
    public boolean require;

    @Column(name = "item_type")
    public int itemType;//1为单选2为多选3为手动输入

    @Column(name = "item_type_name")
    public String typeName;

    @Column(name = "update_time")
    public long updateTime;

    @Transient
    public List<EnrollContentItem> items = new ArrayList<>();

    public static Finder<Long, EnrollContentItemGroup> find = new Finder<>(EnrollContentItemGroup.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getConfigId() {
        return configId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isRequire() {
        return require;
    }

    public void setRequire(boolean require) {
        this.require = require;
    }
}
