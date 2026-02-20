package com.dawood.sprnt.ride.scheduler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideCancelledTask;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.ride.model.TaskStatus;
import com.dawood.sprnt.ride.repository.RideCancelledTaskRepository;
import com.dawood.sprnt.ride.repository.RideRepository;
import com.dawood.sprnt.ride.service.RideMatchingService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RideTimeoutScheduler {

    private RideMatchingService rideMatchingService;
    private RideRepository rideRepository;
    private RideCancelledTaskRepository rideCancelledTaskRepository;

    public RideTimeoutScheduler(
            RideCancelledTaskRepository rideCancelledTaskRepository,
            RideRepository rideRepository,
            @Lazy RideMatchingService rideMatchingService) {
        this.rideCancelledTaskRepository = rideCancelledTaskRepository;
        this.rideRepository = rideRepository;
        this.rideMatchingService = rideMatchingService;
    }

    public RideCancelledTask scheduleTimeout(UUID rideId, UUID driverId) {
        RideCancelledTask task = new RideCancelledTask();
        task.setRideId(rideId);
        task.setDriverId(driverId);
        task.setStatus(TaskStatus.PENDING);
        task.setProcessAt(LocalDateTime.now().plusSeconds(15));

        return rideCancelledTaskRepository.save(task);
    }

    @Scheduled(fixedDelay = 1000)
    public void processTimeouts() {

        List<RideCancelledTask> tasks = rideCancelledTaskRepository.findAndLockCancelledAndDueTasks(
                LocalDateTime.now(),
                50);

        if (tasks.isEmpty())
            return;

        for (RideCancelledTask task : tasks) {

            Ride ride = rideRepository.findById(task.getRideId()).orElse(null);

            try {

                if (ride == null ||
                        task.getStatus().equals(TaskStatus.PROCESSED) ||
                        task.getStatus().equals(TaskStatus.CANCELLED)) {
                    task.setStatus(TaskStatus.PROCESSED);
                    continue;
                }

                rideMatchingService.handleDriverRejectOrTimeout(task.getRideId(), task.getDriverId());
                task.setStatus(TaskStatus.PROCESSED);
            } catch (Exception e) {
                log.error("Error processing timeout task {}", task.getId(), e);
                ride.setRideStatus(RideStatus.FAILED);
                rideRepository.save(ride);
            }

        }

        rideCancelledTaskRepository.saveAll(tasks);
    }

}
