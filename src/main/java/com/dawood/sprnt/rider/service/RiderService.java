package com.dawood.sprnt.rider.service;

import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.repository.UserRepository;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.rider.api.dto.ProfileRequestDTO;
import com.dawood.sprnt.rider.api.dto.ProfileResponseDTO;
import com.dawood.sprnt.rider.mapper.RiderMapper;
import com.dawood.sprnt.rider.model.Rider;
import com.dawood.sprnt.rider.model.RiderStatus;
import com.dawood.sprnt.rider.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderRepository riderRepository;
    private final IdentityService identityService;
    private final UserRepository userRepository;


    public ProfileResponseDTO completeProfileDTO(ProfileRequestDTO profileRequest){

        User currentUser = identityService.getCurrentLoggedInUser();

        Rider rider = currentUser.getRider();
        rider.setStatus(RiderStatus.ACTIVE);
        rider.setProfileImage(profileRequest.getImageUrl());
        rider.setDefaultPickupLocation(profileRequest.getDefaultPickupLocation());
        rider.setDisplayName(profileRequest.getDisplayName());

        userRepository.save(currentUser);
        riderRepository.save(rider);

        return RiderMapper.toProfileResponse(rider);
    }

}
