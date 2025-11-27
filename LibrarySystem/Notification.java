package Project.LibrarySystem;

import java.time.LocalDateTime;

public class Notification {
    public enum NotificationType {
        ASSIGNED,
        SEATED,
        AWAY,
        CANCELED,
        EXPIRED
    }

    private String notificationId;
    private String userId;
    private String message;
    private NotificationType type;
    private LocalDateTime timestamp;
    private boolean isRead;

    public Notification(String notificationId, String userId, String message, NotificationType type) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }

    public String getMessage() {
        return message;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
