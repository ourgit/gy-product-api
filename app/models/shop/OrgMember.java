package models.shop;


import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = "cp_org_member")
public class OrgMember extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "member_id")
    public long memberId;

    public static Finder<Long, OrgMember> find = new Finder<>(OrgMember.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "OrgMember{" +
                "id=" + id +
                ", orgId=" + orgId +
                ", memberId=" + memberId +
                '}';
    }
}
