package com.delgo.reward.comm.ncp.greeneye;


import lombok.*;

import java.util.List;


@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GreenEyeReqDTO {
    private String version;
    private String requestId;
    private long timestamp;
    private List<Image> images;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Image {
        private String name;
        private String url;
    }
}