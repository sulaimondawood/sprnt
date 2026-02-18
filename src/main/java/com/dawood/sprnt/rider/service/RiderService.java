package com.dawood.sprnt.rider.service;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawood.sprnt.common.utils.GeometryUtils;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.ride.api.dto.CreateRideRequest;
import com.dawood.sprnt.ride.api.dto.CreateRideResponse;
import com.dawood.sprnt.ride.api.dto.RideResponseDTO;
import com.dawood.sprnt.ride.mapper.RideMapper;
import com.dawood.sprnt.ride.model.Location;
import com.dawood.sprnt.ride.model.Ride;
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

        if (user.getRider() == null) {
            throw new RiderNotFoundException();
        }

        List<Ride> recentRides = rideRepository.findTop5ByRiderOrderByCreatedAtDesc(user.getRider());

        List<RideResponseDTO> rides = recentRides.stream()
                .map(RideMapper::toDTO).toList();

        return rides;

    }
}
