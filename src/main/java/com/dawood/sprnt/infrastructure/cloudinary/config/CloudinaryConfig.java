package com.dawood.sprnt.infrastructure.cloudinary.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CloudinaryConfig {

    @Value("${app.cloudinary.api-key}")
    private String apiKey;

    @Value("${app.cloudinary.secret-key}")
    private String secretKey;

    @Value("${app.cloudinary.cloud-name}")
    private String cloudName;

    @Bean
    public Cloudinary cloudinary(){

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
        "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", secretKey,
                "secure",true)
        );

        return cloudinary;
    }

}
