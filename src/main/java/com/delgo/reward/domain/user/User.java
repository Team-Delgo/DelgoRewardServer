package com.delgo.reward.domain.user;

import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.domain.common.BaseTimeEntity;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.record.signup.OAuthSignUpRecord;
import com.delgo.reward.record.signup.SignUpRecord;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

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

    private int viewCount; // 내 지도 View Count
    private String version; // 현재 User의 버전

    private String roles;  // 권한

    @Column(name = "social")
    @Enumerated(EnumType.STRING)
    private UserSocial userSocial;

    @OneToOne(mappedBy = "user")
    private Pet pet;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="userId")
//    private Point point;

    @ToString.Exclude
    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    private List<Bookmark> bookmarkList;

    public User setPet(Pet pet) {
        this.pet = pet;
        return this;
    }

    public Boolean setNotify(){
        this.isNotify = !isNotify;
        return this.isNotify;
    }

    public User setProfile(String profile) {
        this.profile = profile;
        return this;
    }

    public User setPassword(String password){
        this.password = password;
        return this;
    }

    public User setName(String name){
        this.name = name;
        return this;
    }

    public User setGeoCode(String geoCode){
        this.geoCode = geoCode;
        return this;
    }

    public User setPGeoCode(String pGeoCode){
        this.pGeoCode = pGeoCode;
        return this;
    }

    public User setAddress(String address){
        this.address = address;
        return this;
    }

    public void setVersion(String version){
        this.version = version;
    }


    public static User from(SignUpRecord signUpRecord, String password, String address, String version){
        return User.builder()
                .name(signUpRecord.userName())
                .email(signUpRecord.email())
                .password(password)
                .phoneNo(signUpRecord.phoneNo().replaceAll("[^0-9]", ""))
                .userSocial(UserSocial.D)
                .address(address)
                .geoCode(signUpRecord.geoCode())
                .pGeoCode(signUpRecord.pGeoCode())
                .isNotify(true)
                .version(version)
                .build();
    }

    public static User from(OAuthSignUpRecord oAuthSignUpRecord, String address, String version) {
        User.UserBuilder userBuilder = User.builder()
                .name(oAuthSignUpRecord.userName())
                .phoneNo(oAuthSignUpRecord.phoneNo().replaceAll("[^0-9]", ""))
                .userSocial(oAuthSignUpRecord.userSocial())
                .address(address)
                .geoCode(oAuthSignUpRecord.geoCode())
                .pGeoCode(oAuthSignUpRecord.pGeoCode())
                .isNotify(true)
                .version(version);

        // 각 사례별로 다른 속성 설정
        switch (oAuthSignUpRecord.userSocial()) {
            case A -> userBuilder.appleUniqueNo(oAuthSignUpRecord.appleUniqueNo());
            case K -> userBuilder.email(oAuthSignUpRecord.email())
                    .kakaoId(oAuthSignUpRecord.socialId())
                    .age(oAuthSignUpRecord.age())
                    .gender(oAuthSignUpRecord.gender());
            case N -> userBuilder.email(oAuthSignUpRecord.email())
                    .age(oAuthSignUpRecord.age())
                    .gender(oAuthSignUpRecord.gender());
        }
        return userBuilder.build();
    }
}
