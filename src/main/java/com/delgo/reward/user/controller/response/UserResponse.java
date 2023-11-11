package com.delgo.reward.user.controller.response;

import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    @Schema(description = "유저 고유 아이디", required = true)
    protected Integer userId;
    @Schema(description = "이메일", required = true)
    protected String email;
    @Schema(description = "닉네임", required = true)
    protected String nickname;
    @Schema(description = "전화번호", required = true)
    protected String phoneNo;
    @Schema(description = "지역코드", required = true)
    protected String geoCode;
    @Schema(description = "주소")
    protected String address;
    @Schema(description = "유저 프로필 이미지 URL")
    protected String profile;
    @Schema(description = "가입 소셜 정보 (D: Delgo, A: Apple, K: Kakao,  N:Naver)")
    protected UserSocial userSocial;
    @Schema(description = "알림 수신 여부")
    protected Boolean isNotify;
    @Schema(description = "프로필 조회 수")
    protected Integer viewCount;
    @Schema(description = "펫 고유 아이디", required = true)
    protected Integer petId;
    @Schema(description = "펫 이름", required = true)
    protected String petName;
    @Schema(description = "품종 코드")
    protected String breed;
    @Schema(description = "품종 명")
    protected String breedName;
    @Schema(description = "펫의 생일")
    protected LocalDate birthday;
    @Schema(description = "펫 나이(년)")
    protected Integer yearOfPetAge;
    @Schema(description = "펫 나이(월)")
    protected Integer monthOfPetAge;
    @Schema(description = "등록 일자")
    @JsonFormat(pattern="yyyy.MM.dd/HH:mm/E")
    protected LocalDateTime registDt;


    public static UserResponse from(User user){
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
                .petId(user.getPet().getPetId())
                .petName(user.getPet().getName())
                .breed(user.getPet().getBreed())
                .breedName(user.getPet().getBreedName())
                .birthday(user.getPet().getBirthday())
                .registDt(user.getRegistDt())
                .yearOfPetAge(period.getYears())
                .monthOfPetAge(period.getMonths())
                .build();
    }

    public static UserResponse fromToSearch(User user){
        Period period = Period.between(user.getPet().getBirthday(), LocalDate.now());

        return UserResponse.builder()
                .userId(user.getUserId())
                .nickname(user.getName())
                .profile(user.getProfile())
                .petId(user.getPet().getPetId())
                .petName(user.getPet().getName())
                .breed(user.getPet().getBreed())
                .breedName(user.getPet().getBreedName())
                .yearOfPetAge(period.getYears())
                .monthOfPetAge(period.getMonths())
                .build();
    }
}