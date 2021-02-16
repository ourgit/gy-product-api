package models.postservice;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.DateSerializer;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 退货收货人信息表
 */
@Entity
@Table(name = "v1_return_contact_detail")
public class ReturnContactDetail extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "org_id")
    public long orgId;

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

    @Column(name = "update_time")
    @JsonSerialize(using = DateSerializer.class)
    public long updateTime;

    @Column(name = "create_time")
    @JsonSerialize(using = DateSerializer.class)
    public long createTime;

    public static Finder<Long, ReturnContactDetail> find = new Finder<>(ReturnContactDetail.class);

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

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
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
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
