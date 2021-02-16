package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;


@Entity
@Table(name = "v1_membership_right")
public class MembershipRight extends Model {


    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "membership_id")
    public long membershipId;

    @Column(name = "title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String title;

    @Column(name = "icon")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String icon;

    @Column(name = "detail")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String detail;

    @Column(name = "sort")
    public int sort;


    public static Finder<Long, MembershipRight> find = new Finder<>(MembershipRight.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(long membershipId) {
        this.membershipId = membershipId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "MembershipRight{" +
                "id=" + id +
                ", membershipId=" + membershipId +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", detail='" + detail + '\'' +
                ", sort=" + sort +
                '}';
    }
}
