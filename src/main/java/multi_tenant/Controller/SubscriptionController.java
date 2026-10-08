package multi_tenant.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import multi_tenant.DTO.ChangePlanRequest;
import multi_tenant.DTO.SubscriptionResponse;
import multi_tenant.Service.SubscriptionService;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionService
            subscriptionService;

    public SubscriptionController(
            SubscriptionService subscriptionService) {

        this.subscriptionService =
                subscriptionService;
    }

    @GetMapping
    public ResponseEntity<SubscriptionResponse>
    getSubscription() {

        return ResponseEntity.ok(
                subscriptionService
                        .getMySubscription()
        );
    }

    @PutMapping("/plan")
    public ResponseEntity<SubscriptionResponse>
    changePlan(
            @RequestBody ChangePlanRequest request) {

        return ResponseEntity.ok(
                subscriptionService
                        .changePlan(request)
        );
    }

}
