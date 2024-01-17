package com.delgo.reward.user.domain;

import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.comm.encoder.CustomPasswordEncoder;
import com.delgo.reward.common.domain.BaseTimeEntity;
import com.delgo.reward.token.domain.Token;
import com.delgo.reward.user.controller.request.OAuthCreate;
import com.delgo.reward.user.controller.request.UserCreate;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String email;
    @Setter
    private String password;

    @Setter
    private String name;
    private Integer age;
    private String gender;
    private String phoneNo;
    @Setter
    private String address;
    @Setter
    private String profile;

    @Setter
    private String geoCode;
    @Setter
    private String pGeoCode;

    @Setter
    private Boolean isNotify;
    private String appleUniqueNo; // Apple 연동 시에만 필요.
    private String kakaoId; // Kakao 연동 시에만 필요.

    private int viewCount; // 내 지도 View Count
    @Setter
    private String version; // 현재 User의 버전

    private String roles;  // 권한

    @Setter
    private LocalDateTime lastAccessDt; // 마지막 접속 시간

    @Column(name = "social")
    @Enumerated(EnumType.STRING)
    private UserSocial userSocial;

    @Setter
    @OneToOne(mappedBy = "user")
    private Pet pet;

    @Setter
    @OneToOne
    @JoinColumn(name = "userId")
    private Token token;

        public static User from(UserCreate userCreate, String encodedPassword, String phoneNo, String address, String version) {
        return User.builder()
                .name(userCreate.userName())
                .email(userCreate.email())
                .password(encodedPassword)
                .phoneNo(phoneNo)
                .userSocial(UserSocial.D)
                .address(address)
                .geoCode(userCreate.geoCode())
                .pGeoCode(userCreate.pGeoCode())
                .isNotify(true)
                .version(version)
                .roles("ROLE_USER")
                .build();
    }

    public static User from(OAuthCreate oAuthCreate, String phoneNo, String address, String version) {
        User.UserBuilder userBuilder = User.builder()
                .name(oAuthCreate.userName())
                .phoneNo(phoneNo)
                .userSocial(oAuthCreate.userSocial())
                .address(address)
                .geoCode(oAuthCreate.geoCode())
                .pGeoCode(oAuthCreate.pGeoCode())
                .isNotify(true)
                .roles("ROLE_USER")
                .version(version);

        // 각 사례별로 다른 속성 설정
        switch (oAuthCreate.userSocial()) {
            case A -> userBuilder.appleUniqueNo(oAuthCreate.appleUniqueNo());
            case K -> userBuilder.email(oAuthCreate.email())
                    .kakaoId(oAuthCreate.socialId())
                    .age(oAuthCreate.age())
                    .gender(oAuthCreate.gender());
            case N -> userBuilder.email(oAuthCreate.email())
                    .age(oAuthCreate.age())
                    .gender(oAuthCreate.gender());
        }
        return userBuilder.build();
    }

    public static String encodePassword(CustomPasswordEncoder passwordEncoder, String password){
        return passwordEncoder.encode(password);
    }

    public static String formattedPhoneNo(String phoneNo) {
        return phoneNo.replaceAll("[^0-9]", "");
    }

    public String getFcmToken() {
        return (token != null) ? token.getFcmToken() : "";
    }

    public String getPetName() {
        return (pet != null) ? pet.getName() : "";
    }
}
