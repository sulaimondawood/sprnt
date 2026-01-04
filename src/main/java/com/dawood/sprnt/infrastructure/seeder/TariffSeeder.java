package com.dawood.sprnt.infrastructure.seeder;

import com.dawood.sprnt.pricing.model.Tariff;
import com.dawood.sprnt.pricing.repository.TariffRepository;
import com.dawood.sprnt.ride.model.RideType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class TariffSeeder implements CommandLineRunner {

    private final TariffRepository tariffRepository;

    @Override
    public void run(String... args) throws Exception {

        if (tariffRepository.count() == 0) {
            log.info("Seeding Default Tariffs...");
            seedLagos();
        }

    }

    private void seedLagos() {

        Tariff lagStandard = Tariff.builder()
                .city("LAGOS")
                .rideType(RideType.STANDARD)
                .baseFare(BigDecimal.valueOf(400))
                .perKmRate(BigDecimal.valueOf(130))
                .perMinuteRate(BigDecimal.valueOf(35))
                .minimumFare(BigDecimal.valueOf(1000))
                .build();

        Tariff lagPremium = Tariff.builder()
                .city("LAGOS")
                .rideType(RideType.PREMIUM)
                .baseFare(BigDecimal.valueOf(800))
                .perKmRate(BigDecimal.valueOf(200))
                .perMinuteRate(BigDecimal.valueOf(50))
                .minimumFare(BigDecimal.valueOf(2000))
                .build();

        tariffRepository.saveAll(List.of(lagStandard, lagPremium));
    }

}
