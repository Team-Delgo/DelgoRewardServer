package com.delgo.reward.record.certification;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public record CertRecord(
        @NotNull Integer userId,
        @NotNull CategoryCode categoryCode,
        @NotNull Integer mungpleId,
        @NotNull String placeName,
        @NotBlank String description,
        String latitude,
        String longitude,
        @NotNull Boolean isHideAddress) {

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