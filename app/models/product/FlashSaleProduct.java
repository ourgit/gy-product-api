package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;

/**
 * 抢购产品集合
 */
@Entity
@Table(name = "v1_flash_sale_product")
public class FlashSaleProduct extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "flash_sale_id")
    public long flashSaleId;

    @Column(name = "product_head_pic")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String headPic;

    @Column(name = "title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String title;

    @Column(name = "price")
    public long price;

    @Column(name = "market_price")
    public long marketPrice;

    @Column(name = "bid_price")
    public long bidPrice;

    @Column(name = "stock")
    public long stock;

    @Column(name = "total_count")
    public long totalCount;

    @Column(name = "sold_count")
    public long soldCount;

    @Column(name = "max_count_by_one")
    public long maxCountByOne;

    @Column(name = "begin_hour")
    public int beginHour;

    @Column(name = "begin_time")
    public long beginTime;

    @Column(name = "end_time")
    public long endTime;

    @Column(name = "duration")
    public long duration;

    @Column(name = "status")
    public int status;

    @Column(name = "sort")
    public long sort;

    @Column(name = "cover_frame_img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String coverFrameImgUrl;

    public static Finder<Long, FlashSaleProduct> find = new Finder<>(FlashSaleProduct.class);

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

    public long getFlashSaleId() {
        return flashSaleId;
    }

    public void setFlashSaleId(long flashSaleId) {
        this.flashSaleId = flashSaleId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(long soldCount) {
        this.soldCount = soldCount;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
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

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(long marketPrice) {
        this.marketPrice = marketPrice;
    }

    public long getMaxCountByOne() {
        return maxCountByOne;
    }

    public void setMaxCountByOne(long maxCountByOne) {
        this.maxCountByOne = maxCountByOne;
    }

    public String getCoverFrameImgUrl() {
        return coverFrameImgUrl;
    }

    public void setCoverFrameImgUrl(String coverFrameImgUrl) {
        this.coverFrameImgUrl = coverFrameImgUrl;
    }

    public int getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    public long getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(long bidPrice) {
        this.bidPrice = bidPrice;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "FlashSaleProduct{" +
                "id=" + id +
                ", productId=" + productId +
                ", flashSaleId=" + flashSaleId +
                ", headPic='" + headPic + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", marketPrice=" + marketPrice +
                ", totalCount=" + totalCount +
                ", soldCount=" + soldCount +
                ", maxCountByOne=" + maxCountByOne +
                ", beginHour=" + beginHour +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", status=" + status +
                ", sort=" + sort +
                ", coverFrameImgUrl='" + coverFrameImgUrl + '\'' +
                '}';
    }
}
