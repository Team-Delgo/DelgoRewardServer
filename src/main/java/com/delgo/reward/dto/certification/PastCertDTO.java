package com.delgo.reward.dto.certification;


import com.delgo.reward.domain.Certification;
import com.delgo.reward.domain.Mungple;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PastCertDTO {
    @NotNull private Integer userId;
    @NotBlank private String categoryCode;

    @NotNull private Integer mungpleId; // mungpleId == 0이면 mungple 장소 아님.
    @NotBlank private String placeName; // 장소 명
    @NotBlank private String description; // 내용

    public Certification toEntity() {
        return Certification.builder()
                .userId(this.userId)
                .categoryCode(this.categoryCode)
                .mungpleId(this.mungpleId)
                .placeName(this.placeName)
                .description(this.description)
                .isPhotoChecked(false)
                .isAchievements(false)
                .isLive(false)
                .build();
    }

    public Certification toEntity(Mungple mungple) {
        String[] arr = mungple.getJibunAddress().split(" ");
        String address = arr[0] + " " + arr[1];
        return Certification.builder()
                .userId(this.userId)
                .categoryCode(mungple.getCategoryCode())
                .mungpleId(this.mungpleId)
                .placeName(mungple.getPlaceName())
                .description(this.description)
                .address(address)
                .geoCode(mungple.getGeoCode()) // 멍플 geoCode
                .pGeoCode(mungple.getPGeoCode()) // 멍플 geoCode
                .latitude(mungple.getLatitude()) // 멍플 위도
                .longitude(mungple.getLongitude()) // 멍플 경도
                .isPhotoChecked(false)
                .isAchievements(false)
                .isLive(false)
                .build();
    }
}