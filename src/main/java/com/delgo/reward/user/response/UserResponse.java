package com.delgo.reward.user.response;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.cert.repository.dto.UserVisitMungpleCountDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {
    @Schema(description = "유저 고유 아이디", required = true)
    private int userId;
    @Schema(description = "이메일", required = true)
    private String email;
    @Schema(description = "닉네임", required = true)
    private String nickname;
    @Schema(description = "전화번호", required = true)
    private String phoneNo;
    @Schema(description = "지역코드", required = true)
    private String geoCode;
    @Schema(description = "주소")
    private String address;
    @Schema(description = "유저 프로필 이미지 URL")
    private String profile;
    @Schema(description = "가입 소셜 정보 (D: Delgo, A: Apple, K: Kakao,  N:Naver)")
    private UserSocial userSocial;
    @Schema(description = "알림 수신 여부")
    private Boolean isNotify;
    @Schema(description = "프로필 조회 수")
    private int viewCount;
    @Schema(description = "펫 고유 아이디", required = true)
    private int petId;
    @Schema(description = "펫 이름", required = true)
    private String petName;
    @Schema(description = "품종 코드")
    private String breed;
    @Schema(description = "품종 명")
    private String breedName;
    @Schema(description = "펫의 생일")
    private LocalDate birthday;
    @Schema(description = "펫 나이(년)")
    private int yearOfPetAge;
    @Schema(description = "펫 나이(월)")
    private int monthOfPetAge;
    @Schema(description = "등록 일자")
    @JsonFormat(pattern="yyyy.MM.dd/HH:mm/E")
    private LocalDateTime registDt;

    // Account
    @Schema(description = "활동 비율 표시 [Key: CategoryCode(ENUM)]")
    private Map<CategoryCode, Integer> activityMapByCategoryCode;
    @Schema(description = "가장 많이 방문 한 멍플(최대 3개)")
    private List<UserVisitMungpleCountDTO> top3VisitedMungpleList;

    public static UserResponse from(User user) {
        Period period = Period.between(user.getPet().getBirthday(), LocalDate.now());
        return UserResponse.builder()
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
                .registDt(user.getRegistDt())
                 // Pet
                .petId(user.getPet().getPetId())
                .petName(user.getPet().getName())
                .breed(user.getPet().getBreed())
                .breedName(user.getPet().getBreedName())
                .birthday(user.getPet().getBirthday())
                .yearOfPetAge(period.getYears())
                .monthOfPetAge(period.getMonths())
                .build();
    }

    public static UserResponse fromAccount(User user, Map<CategoryCode, Integer> activityMapByCategoryCode, List<UserVisitMungpleCountDTO> top3VisitedMungpleList) {
        Period period = Period.between(user.getPet().getBirthday(), LocalDate.now());
        return UserResponse.builder()
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
                .registDt(user.getRegistDt())
                // Pet
                .petId(user.getPet().getPetId())
                .petName(user.getPet().getName())
                .breed(user.getPet().getBreed())
                .breedName(user.getPet().getBreedName())
                .birthday(user.getPet().getBirthday())
                .yearOfPetAge(period.getYears())
                .monthOfPetAge(period.getMonths())

                .activityMapByCategoryCode(activityMapByCategoryCode)
                .top3VisitedMungpleList(top3VisitedMungpleList)
                .build();
    }

    public static UserResponse fromOther(User user, Map<CategoryCode, Integer> activityMapByCategoryCode, List<UserVisitMungpleCountDTO> top3VisitedMungpleList) {
        Period period = Period.between(user.getPet().getBirthday(), LocalDate.now());
        return UserResponse.builder()
                .userId(user.getUserId())
                .nickname(user.getName())
                .profile(user.getProfile())
                .viewCount(user.getViewCount())
                // Pet
                .petId(user.getPet().getPetId())
                .petName(user.getPet().getName())
                .breed(user.getPet().getBreed())
                .breedName(user.getPet().getBreedName())
                .birthday(user.getPet().getBirthday())
                .yearOfPetAge(period.getYears())
                .monthOfPetAge(period.getMonths())

                .activityMapByCategoryCode(activityMapByCategoryCode)
                .top3VisitedMungpleList(top3VisitedMungpleList)
                .build();
    }

    public static UserResponse fromSearch(User user) {
        Period period = Period.between(user.getPet().getBirthday(), LocalDate.now());
        return UserResponse.builder()
                .userId(user.getUserId())
                .nickname(user.getName())
                .profile(user.getProfile())
                // Pet
                .petId(user.getPet().getPetId())
                .petName(user.getPet().getName())
                .breed(user.getPet().getBreed())
                .breedName(user.getPet().getBreedName())
                .birthday(user.getPet().getBirthday())
                .yearOfPetAge(period.getYears())
                .monthOfPetAge(period.getMonths())
                .build();
    }

    public static List<UserResponse> fromSearchList(List<User> userList) {
        return userList.stream().map(user -> UserResponse.fromSearch(user)).toList();
    }
}