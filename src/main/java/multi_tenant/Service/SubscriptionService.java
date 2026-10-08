package multi_tenant.Service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import multi_tenant.DTO.ChangePlanRequest;
import multi_tenant.DTO.SubscriptionResponse;
import multi_tenant.Repository.PlanRepository;
import multi_tenant.Repository.SubscriptionRepository;
import multi_tenant.Security.TenantContext;
import multi_tenant.entity.Plan;
import multi_tenant.entity.PlanTYpe;

import multi_tenant.entity.Subscription;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;

    public SubscriptionService(
            SubscriptionRepository subscriptionRepository,
            PlanRepository planRepository) {

        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
    }

    // Get current tenant subscription
    public SubscriptionResponse getMySubscription() {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            throw new RuntimeException("Tenant not found");
        }

        Subscription subscription =
                subscriptionRepository
                        .findByTenantId(tenantId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Subscription not found"));

        return convert(subscription);
    }

    // Change subscription plan
    public SubscriptionResponse changePlan(
            ChangePlanRequest request) {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            throw new RuntimeException("Tenant not found");
        }

        Subscription subscription =
                subscriptionRepository
                        .findByTenantId(tenantId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Subscription not found"));

        PlanTYpe planType;

        try {

            planType = PlanTYpe.valueOf(
                    request.getPlan().toUpperCase()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid plan. Use FREE, BASIC or PREMIUM"
            );
        }

        Plan plan =
                planRepository
                        .findByName(planType)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Plan not found"));

        subscription.setPlan(plan);

        subscription.setStartDate(
                LocalDate.now()
        );

        subscription.setEndDate(
                LocalDate.now().plusMonths(1)
        );

        subscription.setActive(true);

        Subscription savedSubscription =
                subscriptionRepository.save(subscription);

        return convert(savedSubscription);
    }

    // Convert Entity to DTO
    private SubscriptionResponse convert(
            Subscription subscription) {

        Plan plan = subscription.getPlan();

        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getTenant().getId(),
                plan.getName().name(),
                plan.getMaxUsers(),
                plan.getMaxProjects(),
                plan.getMaxCustomers(),
                plan.getPrice(),
                subscription.isActive()
        );
    }
}