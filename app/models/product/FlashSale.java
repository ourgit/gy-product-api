package models.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 抢购
 */
@Entity
@Table(name = "v1_flash_sale")
public class FlashSale extends Model {

    public static final int STATUS_NOT_START = 1;
    public static final int STATUS_PROCESSING = 2;
    public static final int STATUS_PROCESSED = 3;
    public static final int STATUS_END = -1;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "display_time")
    public String displayTime;

    @Column(name = "beginTime")
    public long beginTime;

    @Column(name = "endTime")
    public long endTime;

    @Column(name = "status")
    public int status;

    @Column(name = "cover_frame_img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String coverFrameImgUrl;

    @Transient
    public List<FlashSaleProduct> productList = new ArrayList();

    public static Finder<Long, FlashSale> find = new Finder<>(FlashSale.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    public String getCoverFrameImgUrl() {
        return coverFrameImgUrl;
    }

    public void setCoverFrameImgUrl(String coverFrameImgUrl) {
        this.coverFrameImgUrl = coverFrameImgUrl;
    }

    @Override
    public String toString() {
        return "FlashSale{" +
                "id=" + id +
                ", displayTime='" + displayTime + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", coverFrameImgUrl='" + coverFrameImgUrl + '\'' +
                ", productList=" + productList +
                '}';
    }
}
