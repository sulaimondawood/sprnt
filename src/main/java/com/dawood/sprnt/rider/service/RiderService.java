package com.dawood.sprnt.rider.service;

import com.dawood.sprnt.common.utils.GeometryUtils;
import com.dawood.sprnt.identity.model.Role;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.repository.UserRepository;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.ride.api.dto.CreateRideRequest;
import com.dawood.sprnt.ride.model.Location;
import com.dawood.sprnt.ride.service.RideMatchingService;
import com.dawood.sprnt.ride.service.RideService;
import com.dawood.sprnt.rider.api.dto.ProfileRequestDTO;
import com.dawood.sprnt.rider.api.dto.ProfileResponseDTO;
import com.dawood.sprnt.rider.mapper.RiderMapper;
import com.dawood.sprnt.rider.model.Rider;
import com.dawood.sprnt.rider.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderRepository riderRepository;
    private final IdentityService identityService;
    private final UserRepository userRepository;
    private final RideService rideService


    @Transactional
    public ProfileResponseDTO completeProfileDTO(ProfileRequestDTO profileRequest) {

        User currentUser = identityService.getCurrentLoggedInUser();

        double lng = profileRequest.getDefaultPickupLocation().getLng();
        double lat = profileRequest.getDefaultPickupLocation().getLat();

//        if(currentUser.getRole() != Role.RIDER){
//            throw new AccessDeniedException();
//        }

        Rider rider = currentUser.getRider();

        if (rider == null) {
            rider = new Rider();
            rider.setUser(currentUser);
        }

        Location location = new Location();

        location.setAddress(profileRequest.getDefaultPickupLocation().getAddress());
        Point coords = GeometryUtils.createPoint(lng,lat);
        location.setCoords(coords);

        rider.completeProfile(
                profileRequest.getImageUrl(),
                location,
                profileRequest.getDisplayName());

        Rider savedRider = riderRepository.save(rider);

        return RiderMapper.toProfileResponse(savedRider);
    }

    public void createRideQuest(CreateRideRequest request) {
        rideService.createRideRequest(request);
    }


}
