package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 评论
 */
@Entity
@Table(name = "v1_product_comment_image")
public class CommentImage extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "comment_id")
    public long commentId;

    @Column(name = "img_url")
    public String imgUrl;

    public static Finder<Long, CommentImage> find = new Finder<>(CommentImage.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "CommentImage{" +
                "id=" + id +
                ", commentId=" + commentId +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
