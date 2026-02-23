package com.dawood.sprnt.rider.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.dawood.sprnt.common.dto.Meta;
import com.dawood.sprnt.common.utils.GeometryUtils;
import com.dawood.sprnt.driver.exception.DriverNotFoundException;
import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.driver.model.DriverAvailabilityStatus;
import com.dawood.sprnt.driver.repository.DriverRepository;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.ride.api.dto.CreateRideRequest;
import com.dawood.sprnt.ride.api.dto.CreateRideResponse;
import com.dawood.sprnt.ride.api.dto.RideResponseDTO;
import com.dawood.sprnt.ride.api.dto.RideResponseMetaDTO;
import com.dawood.sprnt.ride.exception.RideException;
import com.dawood.sprnt.ride.exception.RideNotFoundException;
import com.dawood.sprnt.ride.mapper.RideMapper;
import com.dawood.sprnt.ride.model.Location;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.ride.repository.RideRepository;
import com.dawood.sprnt.ride.service.RideService;
import com.dawood.sprnt.rider.api.dto.ProfileRequestDTO;
import com.dawood.sprnt.rider.api.dto.ProfileResponseDTO;
import com.dawood.sprnt.rider.exception.RiderNotFoundException;
import com.dawood.sprnt.rider.mapper.RiderMapper;
import com.dawood.sprnt.rider.model.Rider;
import com.dawood.sprnt.rider.repository.RiderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderRepository riderRepository;
    private final IdentityService identityService;
    private final RideRepository rideRepository;
    private final RideService rideService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final DriverRepository driverRepository;

    @Transactional
    public ProfileResponseDTO completeProfileDTO(ProfileRequestDTO profileRequest) {

        User currentUser = identityService.getCurrentLoggedInUser();

        double lng = profileRequest.getDefaultPickupLocation().getLng();
        double lat = profileRequest.getDefaultPickupLocation().getLat();

        Rider rider = currentUser.getRider();

        if (rider == null) {
            rider = new Rider();
            rider.setUser(currentUser);
        }

        Location location = new Location();

        location.setAddress(profileRequest.getDefaultPickupLocation().getAddress());
        Point coords = GeometryUtils.createPoint(lng, lat);
        location.setCoords(coords);

        rider.completeProfile(
                profileRequest.getImageUrl(),
                location,
                profileRequest.getDisplayName());

        Rider savedRider = riderRepository.save(rider);

        return RiderMapper.toProfileResponse(savedRider);
    }

    public CreateRideResponse createRideQuest(CreateRideRequest request) {
        return rideService.createRideRequest(request);
    }

    public List<RideResponseDTO> getRecentRides() {

        User user = identityService.getCurrentLoggedInUser();

        Rider rider = user.getRider();
        if (rider == null) {
            throw new RiderNotFoundException();
        }

        List<Ride> recentRides = rideRepository.findTop5ByRiderAndRideStatusInOrderByCreatedAtDesc(rider,
                List.of(RideStatus.COMPLETED, RideStatus.RIDER_CANCELLED, RideStatus.DRIVER_CANCELLED));

        List<RideResponseDTO> rides = recentRides.stream()
                .map(RideMapper::toDTO).toList();

        return rides;

    }

    public RideResponseDTO currentRide() {

        User user = identityService.getCurrentLoggedInUser();

        Rider rider = user.getRider();
        if (rider == null) {
            throw new RiderNotFoundException();
        }

        Ride response = rideRepository.findByRiderAndRideStatusIn(rider,
                List.of(RideStatus.ON_TRIP, RideStatus.DRIVER_ACCEPTED,
                        RideStatus.DRIVER_ARRIVED,
                        RideStatus.DRIVER_EN_ROUTE))
                .orElseThrow(() -> new RideNotFoundException());

        return RideMapper.toDTO(response);

    }

    @Transactional
    public void cancelRideRequest() {

        Rider rider = identityService.getCurrentLoggedInUser().getRider();

        if (rider == null) {
            throw new RiderNotFoundException();
        }

        Ride ride = rideRepository.findByRiderAndRideStatusIn(rider,
                List.of(RideStatus.ON_TRIP, RideStatus.DRIVER_ACCEPTED,
                        RideStatus.DRIVER_ARRIVED,
                        RideStatus.DRIVER_EN_ROUTE,
                        RideStatus.REQUESTED,
                        RideStatus.SEARCHING))
                .orElseThrow(() -> new RideNotFoundException());

        if (ride.getRideStatus() == RideStatus.ON_TRIP) {
            throw new RideException("You cannot cancel an ongoing ride");
        }

        ride.setRideStatus(RideStatus.RIDER_CANCELLED);
        ride.setRejectedAt(LocalDateTime.now());
        rideRepository.save(ride);

        Driver driver = ride.getDriver();
        if (driver != null) {
            driver.setAvailabilityStatus(DriverAvailabilityStatus.ONLINE);
            driverRepository.save(driver);

            Map<String, String> res = new HashMap<>();
            res.put("riderName", ride.getRider().getDisplayName());
            res.put("message", ride.getRider().getDisplayName() + " has cancelled the ride request");

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    simpMessagingTemplate.convertAndSendToUser(
                            driver.getUser().getEmail(),
                            "/queue/rider-reject",
                            res);
                }
            });
        }

    }

    public RideResponseMetaDTO getRideHistory(int pageNo, int pageSize, String keyword, LocalDateTime from,
            LocalDateTime to, RideStatus status) {

        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("From date cannot be after To date");
        }

        User user = identityService.getCurrentLoggedInUser();

        if (user.getRider() == null) {
            throw new RiderNotFoundException();
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("created_at").descending());

        String statusStr = status != null ? status.name() : null;

        Page<Ride> pageRides = rideRepository.findByRiderAndRideStatus(user.getRider().getId(),
                statusStr,
                keyword, from,
                to,
                pageable);

        List<RideResponseDTO> rides = pageRides.getContent().stream()
                .map(RideMapper::toDTO).toList();

        Meta meta = Meta.builder()
                .currentPage(pageRides.getNumber())
                .totalPages(pageRides.getTotalPages())
                .pageSize(pageRides.getSize())
                .hasNext(pageRides.hasNext())
                .hasPrev(pageRides.hasPrevious())
                .build();

        RideResponseMetaDTO response = new RideResponseMetaDTO();
        response.setMeta(meta);
        response.setData(rides);

        return response;

    }

}
