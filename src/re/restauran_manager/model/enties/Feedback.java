package re.restauran_manager.model.enties;

import java.time.LocalDateTime;

public class Feedback {
    private int feedbackId;
    private int userId;
    private int orderId;
    private int rate;
    private String comment;
    private LocalDateTime createdAt;


    public Feedback() {}

    public Feedback(int feedbackId, int userId, int orderId, int rate, String comment, LocalDateTime createdAt) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.orderId = orderId;
        this.rate = rate;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void displayData() {
        System.out.printf("ID: %-5d | UserID: %-5d | Rate: %-2d sao | Comment: %s\n",
                feedbackId, userId, rate, comment);
    }
}