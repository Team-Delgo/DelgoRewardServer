package com.delgo.reward.ncp.domain;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;


@Getter
@Builder
@AllArgsConstructor
public class GreenEyeResponse {
    private double adult;
    private double normal;
    private double porn;
    private double sexy;
    
    public static GreenEyeResponse fromJson(JsonNode jsonNode){
        return GreenEyeResponse.builder()
                .adult(jsonNode.at("/images/0/result/adult/confidence").asDouble())
                .normal(jsonNode.at("/images/0/result/normal/confidence").asDouble())
                .porn(jsonNode.at("/images/0/result/porn/confidence").asDouble())
                .sexy(jsonNode.at("/images/0/result/sexy/confidence").asDouble())
                .build();
    }
}