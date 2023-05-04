package com.delgo.reward.dto.user;


import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
public class UserResDTO {
    // User Data
    private int userId;
    private String email;
    private String userName;
    private String phoneNo; // 보안 필요함.
    private String address;
    private String profile;
    private UserSocial userSocial;
    private boolean isNotify;

    // Pet Data
    private int petId;
    private String petName;
    private String breedName;
    private LocalDate birthday;

    @JsonFormat(pattern="yyyy.MM.dd/HH:mm/E")
    private LocalDateTime createdDate;

    public UserResDTO(User user){
        // User
        userId = user.getUserId();
        email = user.getEmail();
        userName = user.getName();
        phoneNo = user.getPhoneNo();
        address = user.getPhoneNo();
        profile = user.getProfile();
        userSocial = user.getUserSocial();
        isNotify = user.isNotify();

        // Pet
        petId = user.getPet().getPetId();
        petName = user.getPet().getName();
        breedName = user.getPet().getBreedName();
        birthday = user.getPet().getBirthday();
        createdDate = user.getCreatedDate();
    }
}