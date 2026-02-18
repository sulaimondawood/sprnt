package com.dawood.sprnt.driver.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.driver.model.DriverAvailabilityStatus;
import com.dawood.sprnt.driver.repository.DriverRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DriverScheduler {

  private final DriverRepository driverRepository;

  @Scheduled(fixedRate = 60000)
  public void autoOfflineInactiveDrivers() {

    LocalDateTime cutoff = LocalDateTime.now().minusMinutes(5);

    List<Driver> inactiveDrivers = driverRepository.findOnlineBefore(cutoff);

    inactiveDrivers.forEach((driver) -> {
      driver.setAvailabilityStatus(DriverAvailabilityStatus.OFFLINE);
    });

    driverRepository.saveAll(inactiveDrivers);

  }

}
