package multi_tenant.DTO;

import java.time.LocalDateTime;

public class NotificationResponse {

    private Long id;
    private String type;
    private String channel;
    private String subject;
    private String message;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;

    public NotificationResponse(
            Long id,
            String type,
            String channel,
            String subject,
            String message,
            String status,
            LocalDateTime createdAt,
            LocalDateTime sentAt) {

        this.id = id;
        this.type = type;
        this.channel = channel;
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getChannel() {
        return channel;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

}
