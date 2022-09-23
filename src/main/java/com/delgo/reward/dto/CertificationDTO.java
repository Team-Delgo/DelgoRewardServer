package com.delgo.reward.dto;


import com.delgo.reward.domain.Certification;
import com.delgo.reward.domain.Code;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificationDTO {
    @NotNull private Integer userId;
    @NotBlank private String categoryCode;

    @NotNull private Integer mungpleId; // mungpleId == 0이면 mungple 장소 아님.
    @NotBlank private String placeName; // 장소 명
    @NotBlank private String description; // 내용
    @NotBlank private String photo; // 인코딩 된 사진 파일

    @NotBlank private String latitude; // 위도
    @NotBlank private String longitude; // 경도

    public Certification makeCertification(Code code) {
        return Certification.builder()
                .userId(this.userId)
                .categoryCode(this.categoryCode)
                .mungpleId(this.mungpleId)
                .placeName(this.placeName)
                .description(this.description)
                .geoCode(code.getCode()) // 사용자 기준 geoCode
                .p_geoCode(code.getPCode()) // 사용자 기준 geoCode
                .latitude(this.latitude) // 사용자한테 입력받은 위도
                .longitude(this.longitude) // 사용자한테 입력받은 경도
                .isPhotoChecked(0)
                .isAchievements(0)
                .build();
    }
}