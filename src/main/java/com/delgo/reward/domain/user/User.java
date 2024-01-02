package com.delgo.reward.domain.user;

import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.comm.encoder.CustomPasswordEncoder;
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

    @Column(name = "social")
    @Enumerated(EnumType.STRING)
    private UserSocial userSocial;

    @Setter
    @OneToOne(mappedBy = "user")
    private Pet pet;

    @ToString.Exclude
    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    private List<Bookmark> bookmarkList;

    public static User from(SignUpRecord signUpRecord, CustomPasswordEncoder passwordEncoder, String address, String version){
        return User.builder()
                .name(signUpRecord.userName())
                .email(signUpRecord.email())
                .password(passwordEncoder.encode(signUpRecord.password()))
                .phoneNo(signUpRecord.phoneNo().replaceAll("[^0-9]", ""))
                .userSocial(UserSocial.D)
                .address(address)
                .geoCode(signUpRecord.geoCode())
                .pGeoCode(signUpRecord.pGeoCode())
                .isNotify(true)
                .version(version)
                .roles("ROLE_USER")
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
                .roles("ROLE_USER")
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

    public static String encodePassword(CustomPasswordEncoder passwordEncoder, String password){
        return passwordEncoder.encode(password);
    }
}
