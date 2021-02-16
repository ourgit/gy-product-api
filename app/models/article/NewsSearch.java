package models.article;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 文章搜索
 */
@Entity
@Table(name = "v1_news_search")
public class NewsSearch extends Model {
    //搜索类型 1为推荐（默认） 2为动态 3酒吧 4酒库 5活动 6用户
    public static final int TYPE_RECOMMENT = 1;
    public static final int TYPE_TIMELINE = 2;
    public static final int TYPE_BAR = 3;
    public static final int TYPE_WINE = 4;
    public static final int TYPE_ACTIVITY = 5;
    public static final int TYPE_MEMBER = 6;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "keyword")
    public String keyword;

    @Column(name = "views")
    public long views;

    public static Finder<Long, NewsSearch> find = new Finder<>(NewsSearch.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
