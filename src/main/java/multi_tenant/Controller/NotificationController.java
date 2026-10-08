package multi_tenant.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import multi_tenant.DTO.NotificationResponse;
import multi_tenant.Service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService =
                notificationService;
    }

    @PostMapping("/test/{userId}")
    public ResponseEntity<NotificationResponse> testEmail(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.sendEmailNotification(
                        userId,
                        multi_tenant.Enum.NotificationType.USER_CREATED,
                        "Welcome to Multi Tenant SaaS project dost",
                        "Hello,\n\n"
                        + "Your account has been created successfully and your application will work correct "
                        + " but this mail show that project will work in real time not AI generated  mail ."
                        + "\n\n"
                        + "Welcome to our project now you use the service like "
                        + "1 . register yourself first"
                        + "2 login with your register credential "
                        + "3 create your task , project  and description"
                        + "4 when task submit you got a mail "
                        + ""
                        + "BEST REGARDS"
                        + "from java dev."
                )
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<NotificationResponse>>
            getMyNotifications() {

        return ResponseEntity.ok(
                notificationService
                        .getMyNotifications());
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>>
            getTenantNotifications() {

        return ResponseEntity.ok(
                notificationService
                        .getTenantNotifications());
    }
}
