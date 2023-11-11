package com.delgo.reward.user.domain;

import com.delgo.reward.comm.code.UserSocial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class User {
    private Integer userId;

    private String email;
    private String password;

    private String name;
    private Integer age;
    private String gender;
    private String phoneNo;
    private String address;
    private String profile;

    private String geoCode;
    private String pGeoCode;

    private Boolean isNotify;
    private String appleUniqueNo; // Apple 연동 시에만 필요.
    private String kakaoId; // Kakao 연동 시에만 필요.

    private Integer viewCount;
    private String version;

    private UserSocial userSocial;
    private Pet pet;

    private List<Bookmark> bookmarkList;

    private LocalDateTime registDt;

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void setNotify(){
        this.isNotify = !isNotify;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setGeoCode(String geoCode){
        this.geoCode = geoCode;
    }

    public void setPGeoCode(String pGeoCode){
        this.pGeoCode = pGeoCode;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setVersion(String version){
        this.version = version;
    }
}
