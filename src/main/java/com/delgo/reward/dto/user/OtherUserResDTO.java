package com.delgo.reward.dto.user;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.cert.UserVisitMungpleCountDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OtherUserResDTO {
    @Schema(description = "유저 고유 아이디")
    private int userId;
    @Schema(description = "유저 이름")
    private String nickname;
    @Schema(description = "유저 사진 URL")
    private String profile;
    @Schema(description = "프로필 조회 수")
    private int viewCount;
    @Schema(description = "펫 고유 아이디")
    private int petId;
    @Schema(description = "펫 이름")
    private String petName;
    @Schema(description = "품종 코드")
    private String breed;
    @Schema(description = "품종 명")
    private String breedName;
    @Schema(description = "펫 나이(년)")
    private int yearOfPetAge;
    @Schema(description = "펫 나이(월)")
    private int monthOfPetAge;
    @Schema(description = "카테고리별 활동 값")
    private Map<CategoryCode, Integer> activityMapByCategoryCode;
    private List<UserVisitMungpleCountDTO> top3VisitedMungpleList;
  
    public OtherUserResDTO(User user, Map<CategoryCode, Integer> activityMapByCategoryCode, List<UserVisitMungpleCountDTO> top3VisitedMungpleList){
        // User
        userId = user.getUserId();
        nickname = user.getName();
        profile = user.getProfile();
        viewCount = user.getViewCount();
        petId = user.getPet().getPetId();
        petName = user.getPet().getName();
        breed = user.getPet().getBreed();
        breedName = user.getPet().getBreedName();

        Period period = Period.between(user.getPet().getBirthday(), LocalDate.now());
        yearOfPetAge = period.getYears();
        monthOfPetAge = period.getMonths();

        this.activityMapByCategoryCode = activityMapByCategoryCode;
        this.top3VisitedMungpleList = top3VisitedMungpleList;
    }
}