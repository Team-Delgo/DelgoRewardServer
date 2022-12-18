package com.delgo.reward.domain.user;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "phone_no")
    private String phoneNo;

    @Column(nullable = false, name = "address")
    private String address;

    @Column(nullable = false, name = "geo_code")
    private String geoCode;

    @Column(nullable = false, name = "p_geo_code")
    private String pGeoCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, name = "social")
    private UserSocial userSocial;

    private String appleUniqueNo; // Apple 연동시에만 필요.

    @CreationTimestamp
    @Column(name = "regist_dt")
    private LocalDateTime registDt;

    @Column(nullable = false, name="is_notify")
    private boolean isNotify;

    private String profile;

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

    public Boolean setNotify(){
        this.isNotify = !isNotify;
        return this.isNotify;
    }

    public User setProfile(String profile) {
        this.profile = profile;
        return this;
    }

}
