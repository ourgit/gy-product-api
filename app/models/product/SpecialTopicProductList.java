package models.product;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

/**
 * 专题商品列表
 */
@Entity
@Table(name = "v1_product_st_list")
public class SpecialTopicProductList extends Model {


    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "topic_id")
    public long topicId;

    public static Finder<Long, SpecialTopicProductList> find = new Finder<>(SpecialTopicProductList.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }
}
