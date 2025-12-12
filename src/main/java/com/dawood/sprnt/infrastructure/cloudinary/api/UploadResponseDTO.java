package com.dawood.sprnt.infrastructure.cloudinary.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadResponseDTO {

    private String signature;

    private String timestamp;

    private String apiKey;

    private String cloudName;

    private String folder;

}
