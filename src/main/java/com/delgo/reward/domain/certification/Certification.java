package com.delgo.reward.domain.certification;


import com.delgo.reward.domain.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Certification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer certificationId;
    private Integer userId;
    private String categoryCode;

    private Integer mungpleId; // mungpleId == 0이면 mungple 장소 아님.
    private String placeName; // 장소 명
    private String description; // 내용

    private String address; // 주소
    private String geoCode; // 지역 코드
    private String pGeoCode; // 부모 지역 코드
    private String latitude; // 위도
    private String longitude; // 경도

    private String photoUrl; // 사진 URL
    private Boolean isCorrectPhoto; // 올바른 사진 여부 ( 파이썬 모듈로 체크 )
    private Boolean isAchievements; // 업적 영향 여부 ( 해당 인증이 등록되었을 때 가지게 된 업적이 있는가?)

    private int commentCount; // 댓글 개수
    private Boolean isExpose; // Map에 노출 시키는 인증 구분. ( 초기엔 운영진이 직접 추가 예정 )

    public Certification setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;

        return this;
    }

    public void setIsCorrectPhoto(boolean isCorrectPhoto){
        this.isCorrectPhoto = isCorrectPhoto;

    }

    public Certification modify(String description){
        this.description = description;

        return this;
    }
}