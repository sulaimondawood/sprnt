package com.dawood.sprnt.infrastructure.cloudinary;

import com.cloudinary.Cloudinary;
import com.dawood.sprnt.infrastructure.cloudinary.api.UploadResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${app.cloudinary.secret-key}")
    private String apiSecret;

    @Value("${app.cloudinary.api-key}")
    private String apiKey;

    @Value("${app.cloudinary.cloud-name}")
    private String cloudName;

    private String generateApiSignRequest(Map<String,Object> params){
        return cloudinary.apiSignRequest(params, apiSecret,1);
    }

    public UploadResponseDTO getUploadSignature(String folder){

        Map<String, Object> params = new HashMap<>();
        params.put("timestamp", System.currentTimeMillis()/1000);

        if(folder != null){
            params.put("folder", folder);
        }else{
            params.put("folder","sprnt");
        }

        String signature = generateApiSignRequest(params);

        UploadResponseDTO response = new UploadResponseDTO();
        response.setSignature(signature);
        response.setTimestamp(params.get("timestamp").toString());
        response.setApiKey(apiKey);
        response.setCloudName(cloudName);

        return  response;

    }

}
