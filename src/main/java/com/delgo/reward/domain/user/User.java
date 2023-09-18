package com.delgo.reward.domain.user;

import com.delgo.reward.domain.common.BaseTimeEntity;
import com.delgo.reward.domain.pet.Pet;
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

    //    // 권한
//    @JsonIgnore
//    private String roles;
//
//    // ENUM으로 안하고 ,로 해서 구분해서 ROLE을 입력된 -> 그걸 파싱!!
//    @JsonIgnore
//    public List<String> getRoleList() {
//        if (this.roles.length() > 0) {
//            return Arrays.asList(this.roles.split(","));
//        }
//        return new ArrayList<>();
//    }
}
