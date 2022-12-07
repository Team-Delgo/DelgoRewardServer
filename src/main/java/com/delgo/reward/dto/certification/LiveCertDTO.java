package com.delgo.reward.dto.certification;


import com.delgo.reward.domain.Certification;
import com.delgo.reward.domain.Code;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
public class LiveCertDTO {
    @NotNull private Integer userId;
    @NotBlank private String categoryCode;

    @NotNull private Integer mungpleId; // mungpleId == 0이면 mungple 장소 아님.
    @NotBlank private String placeName; // 장소 명
    @NotBlank private String description; // 내용
    @NotBlank private String photo; // 인코딩 된 사진 파일

    @NotBlank private String latitude; // 위도
    @NotBlank private String longitude; // 경도

    public Certification toEntity(Code code, String address) {
        return Certification.builder()
                .userId(this.userId)
                .categoryCode(this.categoryCode)
                .mungpleId(this.mungpleId)
                .placeName(this.placeName)
                .description(this.description)
                .address(address)
                .geoCode(code.getCode()) // 사용자 기준 geoCode
                .pGeoCode(code.getPCode()) // 사용자 기준 geoCode
                .latitude(this.latitude) // 사용자한테 입력받은 위도
                .longitude(this.longitude) // 사용자한테 입력받은 경도
                .isPhotoChecked(false)
                .isAchievements(false)
                .isLive(true)
                .build();
    }

    public LiveCertDTO toLog(){
        this.photo = "";

        return this;
    }
}