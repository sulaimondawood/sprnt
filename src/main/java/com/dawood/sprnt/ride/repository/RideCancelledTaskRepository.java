package com.dawood.sprnt.ride.repository;

import com.dawood.sprnt.ride.model.RideCancelledTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RideCancelledTaskRepository extends JpaRepository<RideCancelledTask, UUID> {

    @Query(value = """
    SELECT * FROM ride_cancelled_timeouts
    WHERE status = 'PENDING'
    AND process_at <= :now
    ORDER BY process_at ASC
    LIMIT :batchSize
    FOR UPDATE SKIP LOCKED
""", nativeQuery = true)
    List<RideCancelledTask> findAndLockCancelledAndDueTasks(@Param("now")LocalDateTime now,
                                                            @Param("batchSize") int batchSize);
}
