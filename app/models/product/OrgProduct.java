package models.product;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;
import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * org
 */
@Entity
@Table(name = "v1_org_product")
public class OrgProduct extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "name")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "product_name")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String productName;

    @Column(name = "lat")
    public double lat;//latitude

    @Column(name = "lon")
    public double lon;//longtitude

    @Column(name = "status")
    public int status;

    @Column(name = "sort")
    public long sort;

    @Transient
    public Product product;
    public static Finder<Long, OrgProduct> find = new Finder<>(OrgProduct.class);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
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

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "OrgProduct{" +
                "id=" + id +
                ", productId=" + productId +
                ", orgId=" + orgId +
                ", name='" + name + '\'' +
                ", productName='" + productName + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", status=" + status +
                ", sort=" + sort +
                '}';
    }
}
