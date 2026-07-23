package ch.supertext.aem.translation.connector.core.impl.pojos;

import com.google.gson.annotations.SerializedName;

public class Feedback {
    @SerializedName("OrderId")
    private long orderId;
    @SerializedName("PostEditing")
    private boolean postEditing;
    @SerializedName("Rating")
    private long rating;
    @SerializedName("Comments")
    private FeedbackComment[] comments;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public boolean getPostEditing() {
        return postEditing;
    }

    public void setPostEditing(boolean postEditing) {
        this.postEditing = postEditing;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public FeedbackComment[] getComments() {
        return comments;
    }

    public void setComments(FeedbackComment[] comments) {
        this.comments = comments;
    }
}
