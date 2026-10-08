package multi_tenant.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import multi_tenant.Repository.PlanRepository;
import multi_tenant.entity.Plan;
import multi_tenant.entity.PlanTYpe;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializePlans(
            PlanRepository planRepository) {

        return args -> {

            if (planRepository.count() == 0) {

                planRepository.save(
                        new Plan(
                                PlanTYpe.FREE,
                                3,
                                2,
                                50,
                                0
                        )
                );

                planRepository.save(
                        new Plan(
                                PlanTYpe.BASIC,
                                10,
                                10,
                                500,
                                499
                        )
                );

                planRepository.save(
                        new Plan(
                                PlanTYpe.PREMIUM,
                                100,
                                100,
                                10000,
                                1499
                        )
                );
            }
        };
    }
}
