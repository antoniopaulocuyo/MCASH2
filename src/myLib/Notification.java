package myLib;

import java.time.LocalDateTime;

public class Notification implements Notifiable {
    private String notificationId;
    private String userId;
    private String message;
    private LocalDateTime timestamp;
    private notificationType type;
    private String investmentId;
    private String accountId;

    public Notification(String notificationId, String userId, String message, notificationType type) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.investmentId = null;
        this.accountId = null;
    }

    public Notification(String notificationId, String userId, String message, notificationType type, String investmentId, String accountId) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.investmentId = investmentId;
        this.accountId = accountId;
    }

    public notificationType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getMessage() {
        return message;
    }

    public String getInvestmentId() {
        return investmentId;
    }

    public String getAccountId() {
        return accountId;
    }

    @Override
    public void sendNotification() {
        switch (type) {
            case SMS_SENT:
            case SMS_INVESTMENT:
                sendSMS();
                break;
            case EMAIL_SENT:
            case EMAIL_INVESTMENT:
                sendEmail();
                break;
            case IN_APP_SENT:
            case IN_APP_INVESTMENT:
                sendInApp();
                break;
            case WEB_SENT:
            case WEB_INVESTMENT:
                sendWeb();
                break;
            default:
                System.out.println("Unknown notification type: " + type);
        }
    }

    @Override
    public void updateNotificationPreference(notificationType type) {
        this.type = type;
        System.out.println("Notification preference updated to: " + type + " for user: " + userId);
    }

    private void sendSMS() {
        String enhancedMessage = buildEnhancedMessage();
        System.out.println("Sending SMS to user " + userId + ": " + enhancedMessage);
    }

    private void sendEmail() {
        String enhancedMessage = buildEnhancedMessage();
        System.out.println("Sending Email to user " + userId + ": " + enhancedMessage);
    }

    private void sendInApp() {
        String enhancedMessage = buildEnhancedMessage();
        System.out.println("Sending In-App notification to user " + userId + ": " + enhancedMessage);
    }

    private void sendWeb() {
        String enhancedMessage = buildEnhancedMessage();
        System.out.println("Sending Web notification to user " + userId + ": " + enhancedMessage);
    }

    private String buildEnhancedMessage() {
        StringBuilder enhancedMessage = new StringBuilder(message);

        if (type.name().contains("INVESTMENT") && investmentId != null) {
            enhancedMessage.append(" | Investment ID: ").append(investmentId);
        }

        if (type.name().contains("SENT") && accountId != null) {
            enhancedMessage.append(" | Account ID: ").append(accountId);
        }

        return enhancedMessage.toString();
    }

    // Static factory method to create notification from transaction
    public static Notification createFromTransaction(String notificationId, Transaction transaction, notificationType type) {
        String message = String.format("Transaction %s: %s of $%.2f",
                transaction.getType(),
                transaction.getType().toString().toLowerCase(),
                transaction.getAmount());

        return new Notification(notificationId, transaction.getAccountId1(), message, type,
                null, transaction.getAccountId1());
    }
}
