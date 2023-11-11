package com.delgo.reward.user.infrastructure.entity;

import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.domain.common.BaseTimeEntity;
import com.delgo.reward.user.domain.Bookmark;
import com.delgo.reward.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity extends BaseTimeEntity {
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

    @Column(name = "social")
    @Enumerated(EnumType.STRING)
    private UserSocial userSocial;

    @OneToOne(mappedBy = "user")
    private PetEntity petEntity;

    @ToString.Exclude
    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    private List<BookmarkEntity> bookmarkEntityList;

    public User toModel(){
        List<Bookmark> bookmarkList =
                bookmarkEntityList
                    .stream()
                    .map(BookmarkEntity::toModel)
                    .toList();

        return User
                .builder()
                .userId(userId)
                .email(email)
                .password(password)
                .name(name)
                .age(age)
                .gender(gender)
                .phoneNo(phoneNo)
                .address(address)
                .profile(profile)
                .geoCode(geoCode)
                .pGeoCode(pGeoCode)
                .isNotify(isNotify)
                .appleUniqueNo(appleUniqueNo)
                .kakaoId(kakaoId)
                .viewCount(viewCount)
                .version(version)
                .userSocial(userSocial)
                .pet(petEntity.toModel())
                .bookmarkList(bookmarkList)
                .registDt(super.getRegistDt())
                .build();
    }

    public static UserEntity from(User user){
        List<BookmarkEntity> bookmarkEntityList =
                user.getBookmarkList()
                        .stream()
                        .map((BookmarkEntity::from))
                        .toList();

        return UserEntity
                .builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .phoneNo(user.getPhoneNo())
                .address(user.getAddress())
                .profile(user.getProfile())
                .geoCode(user.getGeoCode())
                .pGeoCode(user.getPGeoCode())
                .isNotify(user.getIsNotify())
                .appleUniqueNo(user.getAppleUniqueNo())
                .kakaoId(user.getKakaoId())
                .viewCount(user.getViewCount())
                .version(user.getVersion())
                .userSocial(user.getUserSocial())
                .petEntity(PetEntity.from(user.getPet()))
                .bookmarkEntityList(bookmarkEntityList)
                .build();
    }
}
