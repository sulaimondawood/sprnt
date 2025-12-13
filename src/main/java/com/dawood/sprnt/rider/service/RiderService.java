package com.dawood.sprnt.rider.service;

import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.repository.UserRepository;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.rider.api.dto.ProfileRequestDTO;
import com.dawood.sprnt.rider.api.dto.ProfileResponseDTO;
import com.dawood.sprnt.rider.mapper.RiderMapper;
import com.dawood.sprnt.rider.model.Rider;
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

        if(rider == null){
            rider = new Rider();
        }

        rider.completeProfile(
                profileRequest.getImageUrl(),
                profileRequest.getDefaultPickupLocation(),
                profileRequest.getDisplayName());

        userRepository.save(currentUser);

        return RiderMapper.toProfileResponse(rider);
    }

    public void bookRide(){

    }


}
