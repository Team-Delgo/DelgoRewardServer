package com.delgo.reward.dto.user;


import com.delgo.reward.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Period;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserResDTO {
    @Schema(description = "유저 고유 아이디", required = true)
    private int userId;
    @Schema(description = "유저 이름", required = true)
    private String nickname;
    @Schema(description = "유저 사진 URL")
    private String profile;
    @Schema(description = "펫 고유 아이디", required = true)
    private int petId;
    @Schema(description = "펫 이름", required = true)
    private String petName;
    @Schema(description = "품종 코드")
    private String breed;
    @Schema(description = "품종 명")
    private String breedName;
    @Schema(description = "펫 나이(년)")
    private int yearOfPetAge;
    @Schema(description = "펫 나이(월)")
    private int monthOfPetAge;

    public SearchUserResDTO(User user){
        userId = user.getUserId();
        nickname = user.getName();
        profile = user.getProfile();

        petId = user.getPet().getPetId();
        petName = user.getPet().getName();
        breed = user.getPet().getBreed();
        breedName = user.getPet().getBreedName();

        Period period = Period.between(user.getPet().getBirthday(), LocalDate.now());
        yearOfPetAge = period.getYears();
        monthOfPetAge = period.getMonths();
    }
}