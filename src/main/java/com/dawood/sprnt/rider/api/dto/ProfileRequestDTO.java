package com.dawood.sprnt.rider.api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileRequestDTO {

    private String imageUrl;

    private String displayName;

    private String defaultPickupLocation;
    
}
