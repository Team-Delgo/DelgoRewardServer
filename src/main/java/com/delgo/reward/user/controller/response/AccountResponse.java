package com.delgo.reward.user.controller.response;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;


@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse extends UserResponse {
    @Schema(description = "활동 비율 표시 [Key: CategoryCode(ENUM)]")
    protected Map<CategoryCode, Integer> activityMapByCategoryCode;
    @Schema(description = "가장 많이 방문 한 멍플(최대 3개)")
    protected List<UserVisitMungpleCountResponse> top3VisitedMungpleList;

    public static AccountResponse from(User user, Map<CategoryCode, Integer> activityMapByCategoryCode, List<UserVisitMungpleCountResponse> top3VisitedMungpleList) {
        Period period = Period.between(user.getPet().getBirthday(), LocalDate.now());

        return AccountResponse
                .builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getName())
                .phoneNo(user.getPhoneNo())
                .geoCode(user.getGeoCode())
                .address(user.getAddress())
                .profile(user.getProfile())
                .userSocial(user.getUserSocial())
                .isNotify(user.getIsNotify())
                .viewCount(user.getViewCount())
                .petId(user.getPet().getPetId())
                .petName(user.getPet().getName())
                .breed(user.getPet().getBreed())
                .breedName(user.getPet().getBreedName())
                .birthday(user.getPet().getBirthday())
                .registDt(user.getRegistDt())
                .yearOfPetAge(period.getYears())
                .monthOfPetAge(period.getMonths())
                .activityMapByCategoryCode(activityMapByCategoryCode)
                .top3VisitedMungpleList(top3VisitedMungpleList)
                .build();
    }

    public static AccountResponse fromToOther(User user, Map<CategoryCode, Integer> activityMapByCategoryCode, List<UserVisitMungpleCountResponse> top3VisitedMungpleList) {
        Period period = Period.between(user.getPet().getBirthday(), LocalDate.now());

        return AccountResponse
                .builder()
                .userId(user.getUserId())
                .nickname(user.getName())
                .profile(user.getProfile())
                .viewCount(user.getViewCount())
                .petId(user.getPet().getPetId())
                .petName(user.getPet().getName())
                .breed(user.getPet().getBreed())
                .breedName(user.getPet().getBreedName())
                .yearOfPetAge(period.getYears())
                .monthOfPetAge(period.getMonths())
                .activityMapByCategoryCode(activityMapByCategoryCode)
                .top3VisitedMungpleList(top3VisitedMungpleList)
                .build();
    }
}