package myLib;

public interface Notifiable {
    void sendNotification();
    void updateNotificationPreference(notificationType type);
}
