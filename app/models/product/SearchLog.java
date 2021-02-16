package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 个人搜索记录
 */
@Entity
@Table(name = "v1_search_log")
public class SearchLog extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "uid")
    public long uid;

    @Column(name = "keyword")
    public String keyword;

    @Column(name = "views")
    public long views;

    public static Finder<Long, SearchLog> find = new Finder<>(SearchLog.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "SearchLog{" +
                "id=" + id +
                ", uid=" + uid +
                ", keyword='" + keyword + '\'' +
                ", views=" + views +
                '}';
    }
}
