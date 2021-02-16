package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.DateSerializer;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 收货人信息表
 */
@Entity
@Table(name = "v1_contact_detail")
public class ContactDetail extends Model {
    //1为默认,2为非默认
    public static final int IS_DEFAULT = 1;
    public static final int NOT_DEFAULT = 2;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "uid")
    public long uid;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "province")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String province;
    @Column(name = "province_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String provinceCode;

    @Column(name = "city")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String city;

    @Column(name = "city_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String cityCode;

    @Column(name = "area")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String area;

    @Column(name = "area_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String areaCode;

    @Column(name = "details")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String details;

    @Column(name = "postcode")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String postcode;

    @Column(name = "telephone")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String telephone;

    @Column(name = "is_default")
    public int isDefault;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "create_time")
    @JsonSerialize(using = DateSerializer.class)
    public long createTime;

    public static Finder<Long, ContactDetail> find = new Finder<>(ContactDetail.class);

    public void setId(long id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public long getId() {
        return id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public String getCity() {
        return city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getArea() {
        return area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getDetails() {
        return details;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    @Override
    public String toString() {
        return "ContactDetail{" +
                "id=" + id +
                ", uid=" + uid +
                ", name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", city='" + city + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", area='" + area + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", details='" + details + '\'' +
                ", postcode='" + postcode + '\'' +
                ", telephone='" + telephone + '\'' +
                ", isDefault=" + isDefault +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
