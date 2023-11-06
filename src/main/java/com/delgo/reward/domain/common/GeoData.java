package com.delgo.reward.domain.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GeoData {
    private String latitude; // 위도
    private String longitude; // 경도
    private String roadAddress; // 도로명 주소
    private String jibunAddress; // 지번 주소
}
