package com.delgo.reward.ncp.domain;


import lombok.*;

import java.util.List;
import java.util.UUID;


@Getter
@Builder
public class GreenEyeRequest {
    private String version;
    private String requestId;
    private long timestamp;
    private List<Image> images;

    public static GreenEyeRequest create(List<Image> images){
        return GreenEyeRequest.builder()
                .version("V1")
                .requestId(String.valueOf(UUID.randomUUID()))
                .images(images)
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class Image {
        private String name;
        private String url;
    }
}