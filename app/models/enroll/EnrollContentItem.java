package models.enroll;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 答题项
 */
@Entity
@Table(name = "v1_enroll_content_item")
public class EnrollContentItem extends Model {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "content")
    public String content;

    @Column(name = "sort")
    public int sort;

    @Column(name = "group_id")
    public long groupId;

    @Column(name = "update_time")
    public long updateTime;

    public static Finder<Long, EnrollContentItem> find = new Finder<>(EnrollContentItem.class);

    public void setId(long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "QuestionareItem{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", sort=" + sort +
                ", groupId=" + groupId +
                ", updateTime=" + updateTime +
                '}';
    }
}
