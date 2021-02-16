package models.article;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 文章收藏
 */
@Entity
@Table(name = "v1_article_fav")
public class ArticleFav extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "article_id")
    private long articleId;

    @Column(name = "uid")
    private long uid;

    @Column(name = "head_pic")
    private String headPic;

    @Column(name = "author")
    private String author;

    @Column(name = "title")
    private String title;

    @Column(name = "enable")
    private boolean enable;

    @Column(name = "article_time")
    private long articleTime;

    @Column(name = "create_time")
    private long createdTime;

    public static Finder<Long, ArticleFav> find = new Finder<>(ArticleFav.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getArticleTime() {
        return articleTime;
    }

    public void setArticleTime(long articleTime) {
        this.articleTime = articleTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "ArticleFav{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", uid=" + uid +
                ", headPic='" + headPic + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", enable=" + enable +
                ", articleTime=" + articleTime +
                ", createdTime=" + createdTime +
                '}';
    }
}
