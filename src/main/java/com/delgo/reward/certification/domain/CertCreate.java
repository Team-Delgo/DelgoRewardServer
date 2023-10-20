package com.delgo.reward.certification.domain;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public record CertCreate(
        @Schema(description = "유저 고유 아이디")
        @NotNull
        Integer userId,

        @Schema(description = "인증 카테고리 코드")
        @NotNull
        CategoryCode categoryCode,

        @Schema(description = "멍플 고유 아이디 (안 멍플일 경우 = 0 )")
        @NotNull
        Integer mungpleId,

        @Schema(description = "장소 명")
        @NotNull
        String placeName,

        @Schema(description = "인증 내용")
        @NotBlank
        String description,

        @Schema(description = "위도")
        String latitude,

        @Schema(description = "경도")
        String longitude,

        @Schema(description = "주소 숨김 여부")
        @NotNull
        Boolean isHideAddress) {

    public Certification toEntity(Location location, User user) {
        return Certification.builder()
                .user(user)
                .categoryCode(categoryCode)
                .mungpleId(mungpleId)
                .placeName(placeName)
                .description(description)
                .address(location.getSIDO() + " " + location.getSIGUGUN() + " " + location.getDONG())
                .geoCode(location.getGeoCode())
                .pGeoCode(location.getPGeoCode())
                .latitude(latitude)
                .longitude(longitude)
                .isCorrect(true)
                .isAchievements(false)
                .isExpose(true)
                .isHideAddress(isHideAddress)
                .commentCount(0)
                .build();
    }

    public Certification toEntity(MongoMungple mongoMungple, User user){
        String[] arr = mongoMungple.getJibunAddress().split(" ");
        String address = arr[0] + " " + arr[1] + " " + arr[2];
        return Certification.builder()
                .user(user)
                .categoryCode(mongoMungple.getCategoryCode())
                .mungpleId(mungpleId)
                .placeName(mongoMungple.getPlaceName())
                .description(description)
                .address(address)
                .geoCode(mongoMungple.getGeoCode())
                .pGeoCode(mongoMungple.getPGeoCode())
                .latitude(mongoMungple.getLatitude())
                .longitude(mongoMungple.getLongitude())
                .isCorrect(true)
                .isAchievements(false)
                .isExpose(true)
                .isHideAddress(false)
                .commentCount(0)
                .build();
    }
}