package com.delgo.reward.dto.user;

import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class SignUpDTO {
    @NotBlank private String userName;
    @NotBlank private String email;
    @NotBlank private String password;
    @NotBlank private String phoneNo;
    @NotBlank private String geoCode;
    @NotBlank private String pGeoCode;
    @NotBlank private String petName;
    @NotNull private String breed;
    @NotNull private LocalDate birthday;

    public User makeUser(String password, String address){
        return User.builder()
                .name(userName)
                .email(email)
                .password(password)
                .phoneNo(phoneNo.replaceAll("[^0-9]", ""))
                .userSocial(UserSocial.D)
                .address(address)
                .geoCode(geoCode)
                .pGeoCode(pGeoCode)
                .build();
    }

    public Pet makePet(int userId){
        return Pet.builder()
                .name(petName)
                .breed(breed)
                .birthday(birthday)
                .userId(userId)
                .build();
    }

}
