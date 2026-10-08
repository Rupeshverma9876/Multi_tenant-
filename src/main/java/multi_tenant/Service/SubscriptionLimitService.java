package multi_tenant.Service;

import org.springframework.stereotype.Service;

import multi_tenant.Repository.UserRepository;

@Service
public class SubscriptionLimitService {

    private final UserRepository userRepository;

    public SubscriptionLimitService(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public void checkUserLimit(Long tenantId) {

        long currentUserCount =
                userRepository.countByTenantId(tenantId);

        long userLimit = 5;

        if (currentUserCount >= userLimit) {

            throw new RuntimeException(
                    "User limit reached. Maximum allowed users: "
                    + userLimit
            );
        }
    }
}