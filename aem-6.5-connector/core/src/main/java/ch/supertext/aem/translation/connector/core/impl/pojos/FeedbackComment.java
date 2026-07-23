package ch.supertext.aem.translation.connector.core.impl.pojos;

import com.google.gson.annotations.SerializedName;

public class FeedbackComment {
    @SerializedName("OrderId")
    private long orderId;
    @SerializedName("Title")
    private String title;
    @SerializedName("Comment")
    private String comment;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
