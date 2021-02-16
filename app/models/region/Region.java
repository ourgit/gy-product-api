package models.region;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 地区信息
 */
@Entity
@Table(name = "v1_region")
public class Region extends Model {
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

    @Column(name = "parent_id")
    public int parentId;

    @Column(name = "region_level")
    public int regionLevel;

    @Column(name = "region_order")
    public int regionOrder;

    @Column(name = "region_name_en")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String regionNameEn;

    @Column(name = "region_short_name_en")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String regionShortNameEn;

    public static Finder<Integer, Region> find = new Finder<>(Region.class);

    public void setId(int id) {
        this.id = id;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setRegionLevel(int regionLevel) {
        this.regionLevel = regionLevel;
    }

    public void setRegionOrder(int regionOrder) {
        this.regionOrder = regionOrder;
    }

    public void setRegionNameEn(String regionNameEn) {
        this.regionNameEn = regionNameEn;
    }

    public void setRegionShortNameEn(String regionShortNameEn) {
        this.regionShortNameEn = regionShortNameEn;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", regionCode='" + regionCode + '\'' +
                ", regionName='" + regionName + '\'' +
                ", parentId=" + parentId +
                ", regionLevel=" + regionLevel +
                ", regionOrder=" + regionOrder +
                ", regionNameEn='" + regionNameEn + '\'' +
                ", regionShortNameEn='" + regionShortNameEn + '\'' +
                '}';
    }
}
