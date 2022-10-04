package com.delgo.reward.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
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

    @Column(nullable = false, name = "accumulated_point")
    private int accumulatedPoint;

    @Column(nullable = false, name = "weekly_point")
    private int weeklyPoint;

    @Column(nullable = false, name = "geo_code")
    private String geoCode;

    @Column(nullable = false, name = "p_geo_code")
    private String p_geoCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, name = "social")
    private UserSocial userSocial;

    @CreationTimestamp
    @Column(name = "regist_dt")
    private LocalDateTime registDt;

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
}
