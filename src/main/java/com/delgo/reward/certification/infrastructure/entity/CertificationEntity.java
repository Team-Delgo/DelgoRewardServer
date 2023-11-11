package com.delgo.reward.certification.infrastructure.entity;


import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.common.BaseTimeEntity;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.user.infrastructure.entity.UserEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "certification")
public class CertificationEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer certificationId;
    private String placeName; // 장소 명
    private String description; // 내용

    private Integer mungpleId; // mungpleId == 0이면 mungple 장소 아님.
    @Enumerated(EnumType.STRING)
    private CategoryCode categoryCode;

    private String address; // 주소
    private Boolean isHideAddress; // 주소 숨기기 여부 (true: 숨김 , false: 노출) 기본값 : false
    private String geoCode; // 지역 코드
    private String pGeoCode; // 부모 지역 코드
    private String latitude; // 위도
    private String longitude; // 경도

    private Boolean isCorrect; // 올바른 사진 여부 ( NCP GreenEye로 체크 )
    private int commentCount; // 댓글 개수

    @ManyToOne
    @JoinColumn(name = "userId", updatable = false)
    private UserEntity userEntity;


    public Certification toModel() {
        return Certification.builder()
                .certificationId(certificationId)
                .placeName(placeName)
                .description(description)
                .mungpleId(mungpleId)
                .categoryCode(categoryCode)
                .address(address)
                .isHideAddress(isHideAddress)
                .geoCode(geoCode)
                .pGeoCode(pGeoCode)
                .latitude(latitude)
                .longitude(longitude)
                .isCorrect(isCorrect)
                .commentCount(commentCount)
                .user(userEntity.toModel())
                .registDt(super.getRegistDt())
                .build();
    }

    public static CertificationEntity from(Certification certification) {
        return CertificationEntity.builder()
                .placeName(certification.getPlaceName())
                .description(certification.getDescription())
                .mungpleId(certification.getMungpleId())
                .categoryCode(certification.getCategoryCode())
                .address(certification.getAddress())
                .isHideAddress(certification.getIsHideAddress())
                .geoCode(certification.getGeoCode())
                .pGeoCode(certification.getPGeoCode())
                .latitude(certification.getLatitude())
                .longitude(certification.getLongitude())
                .isCorrect(certification.getIsCorrect())
                .commentCount(certification.getCommentCount())
                .userEntity(UserEntity.from(certification.getUser()))
                .build();
    }
}