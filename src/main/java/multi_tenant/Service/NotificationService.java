package multi_tenant.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import multi_tenant.DTO.NotificationResponse;
import multi_tenant.Enum.NotificationChannel;
import multi_tenant.Enum.NotificationStatus;
import multi_tenant.Enum.NotificationType;
import multi_tenant.Repository.NotificationRepository;
import multi_tenant.Repository.UserRepository;
import multi_tenant.Security.TenantContext;
import multi_tenant.entity.NotificationEntity;
import multi_tenant.entity.Tenant;
import multi_tenant.entity.User;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            EmailService emailService) {

        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public NotificationResponse sendEmailNotification(
            Long userId,
            NotificationType type,
            String subject,
            String message) {

        Long tenantId = getTenantId();

        User user = userRepository
                .findByIdAndTenantId(userId, tenantId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Tenant tenant = user.getTenant();

        NotificationEntity notification = new NotificationEntity();

        notification.setTenant(tenant);
        notification.setUser(user);
        notification.setType(type);
        notification.setChannel(
                NotificationChannel.EMAIL
        );
        notification.setSubject(subject);
        notification.setMessage(message);
        notification.setStatus(
                NotificationStatus.PENDING
        );
        notification.setCreatedAt(
                LocalDateTime.now()
        );

        NotificationEntity saved =
                notificationRepository.save(notification);

        try {

            emailService.sendEmail(
                    user.getEmail(),
                    subject,
                    message
            );

            saved.setStatus(
                    NotificationStatus.SENT
            );

            saved.setSentAt(
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            System.out.println("Email sending failed!");
            System.out.println("Error: " + e.getMessage());

            e.printStackTrace();

            saved.setStatus(
                    NotificationStatus.FAILED
            );
        }

        NotificationEntity finalNotification =
                notificationRepository.save(saved);

        return convert(finalNotification);
    }

    public List<NotificationResponse> getMyNotifications() {

        Long tenantId = getTenantId();

        String email =
                org.springframework.security.core.context
                        .SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return notificationRepository
                .findByUserIdAndTenantIdOrderByCreatedAtDesc(
                        user.getId(),
                        tenantId
                )
                .stream()
                .map(this::convert)
                .toList();
    }

    public List<NotificationResponse> getTenantNotifications() {

        Long tenantId = getTenantId();

        return notificationRepository
                .findByTenantIdOrderByCreatedAtDesc(
                        tenantId
                )
                .stream()
                .map(this::convert)
                .toList();
    }

    private NotificationResponse convert(
            NotificationEntity notification) {

        return new NotificationResponse(
                notification.getId(),
                notification.getType().name(),
                notification.getChannel().name(),
                notification.getSubject(),
                notification.getMessage(),
                notification.getStatus().name(),
                notification.getCreatedAt(),
                notification.getSentAt()
        );
    }

    private Long getTenantId() {

        Long tenantId =
                TenantContext.getTenantId();

        if (tenantId == null) {
            throw new RuntimeException(
                    "Tenant not found"
            );
        }

        return tenantId;
    }

}
