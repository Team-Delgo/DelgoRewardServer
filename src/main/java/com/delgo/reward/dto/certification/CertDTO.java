package com.delgo.reward.dto.certification;


import com.delgo.reward.domain.Mungple;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.common.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CertDTO {
    @NotNull private Integer userId;
    @NotBlank private String categoryCode;

    @NotNull private Integer mungpleId; // mungpleId == 0이면 mungple 장소 아님.
    @NotBlank private String placeName; // 장소 명
    @NotBlank private String description; // 내용
    private String latitude; // 위도
    private String longitude; // 경도

    public Certification toEntity(Location location) {
        return Certification.builder()
                .userId(this.userId)
                .categoryCode(this.categoryCode)
                .mungpleId(this.mungpleId)
                .placeName(this.placeName)
                .description(this.description)
                .address(location.getSIDO() + " " + location.getSIGUGUN())
                .geoCode(location.getGeoCode()) // 사용자 기준 geoCode
                .pGeoCode(location.getPGeoCode()) // 사용자 기준 geoCode
                .latitude(this.latitude) // 사용자한테 입력받은 위도
                .longitude(this.longitude) // 사용자한테 입력받은 경도
                .isCorrectPhoto(true)
                .isAchievements(false)
                .isExpose(false)
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
                .isCorrectPhoto(true)
                .isAchievements(false)
                .isExpose(false)
                .build();
    }
}