package re.restauran_manager.model.enties;

import java.time.LocalDateTime;

public class Reviews {
    private int feedbackId;
    private int accountId;
    private int orderId;
    private int rate;
    private String comment;
    private LocalDateTime createdAt;

    private String accountName;

    public Reviews() {
    }

    public Reviews(int feedbackId, int accountId, int orderId, int rate, String comment, LocalDateTime createdAt) {
        this.feedbackId = feedbackId;
        this.accountId = accountId;
        this.orderId = orderId;
        this.rate = rate;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Getter và Setter
    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void displayFeedback() {
        String stars = "⭐".repeat(rate);
        System.out.printf("Khách hàng: %-15s | Đánh giá: %-5s | Nội dung: %s\n",
                accountName != null ? accountName : "ID: " + accountId,
                stars,
                comment);
    }
}