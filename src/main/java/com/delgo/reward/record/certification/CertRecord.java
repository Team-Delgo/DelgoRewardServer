package com.delgo.reward.record.certification;

import com.delgo.reward.domain.Mungple;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public record CertRecord(
        @NotNull Integer userId,
        @NotBlank String categoryCode,
        @NotNull Integer mungpleId,
        @NotBlank String placeName,
        @NotBlank String description,
        String latitude,
        String longitude) {

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
                .isCorrectPhoto(false)
                .isAchievements(false)
                .isExpose(true)
                .build();
    }

    public Certification toEntity(Mungple mungple, User user) {
        String[] arr = mungple.getJibunAddress().split(" ");
        String address = arr[0] + " " + arr[1];
        return Certification.builder()
                .user(user)
                .categoryCode(mungple.getCategoryCode())
                .mungpleId(mungpleId)
                .placeName(mungple.getPlaceName())
                .description(description)
                .address(address)
                .geoCode(mungple.getGeoCode())
                .pGeoCode(mungple.getPGeoCode())
                .latitude(mungple.getLatitude())
                .longitude(mungple.getLongitude())
                .isCorrectPhoto(false)
                .isAchievements(false)
                .isExpose(true)
                .build();
    }

}