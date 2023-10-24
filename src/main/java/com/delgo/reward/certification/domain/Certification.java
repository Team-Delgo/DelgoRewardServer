package com.delgo.reward.certification.domain;


import com.delgo.reward.certification.domain.request.CertCreate;
import com.delgo.reward.certification.domain.request.CertUpdate;
import com.delgo.reward.certification.service.port.GeoDataPort;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoService.MongoMungpleService;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Builder
@ToString
@AllArgsConstructor
public class Certification {
    private Integer certificationId;
    private String placeName; // 장소 명
    private String description; // 내용

    private Integer mungpleId; // mungpleId == 0이면 mungple 장소 아님.
    private CategoryCode categoryCode;

    private String address; // 주소
    private Boolean isHideAddress; // 주소 숨기기 여부 (true: 숨김 , false: 노출) 기본값 : false
    private String geoCode; // 지역 코드
    private String pGeoCode; // 부모 지역 코드
    private String latitude; // 위도
    private String longitude; // 경도

    private Boolean isCorrect; // 올바른 사진 여부 ( NCP GreenEye로 체크 )
    private int commentCount; // 댓글 개수

    private User user;

    private LocalDateTime registDt;

    public static Certification from(CertCreate certCreate, GeoDataPort geoDataPort, User user) {
        Location location = geoDataPort.getReverseGeoData(certCreate.latitude(), certCreate.longitude());
            return Certification.builder()
                    .user(user)
                    .categoryCode(certCreate.categoryCode())
                    .mungpleId(certCreate.mungpleId())
                    .placeName(certCreate.placeName())
                    .description(certCreate.description())
                    .address(location.getSIDO() + " " + location.getSIGUGUN() + " " + location.getDONG())
                    .geoCode(location.getGeoCode())
                    .pGeoCode(location.getPGeoCode())
                    .latitude(certCreate.latitude())
                    .longitude(certCreate.longitude())
                    .isCorrect(true)
                    .isHideAddress(certCreate.isHideAddress())
                    .commentCount(0)
                    .build();
        }

    public static Certification from(CertCreate certCreate, MongoMungpleService mongoMungpleService, User user) {
        MongoMungple mongoMungple = mongoMungpleService.getMungpleByMungpleId(certCreate.mungpleId());
        String[] arr = mongoMungple.getJibunAddress().split(" ");
        String address = arr[0] + " " + arr[1] + " " + arr[2];
        return Certification.builder()
                .user(user)
                .categoryCode(mongoMungple.getCategoryCode())
                .mungpleId(certCreate.mungpleId())
                .placeName(mongoMungple.getPlaceName())
                .description(certCreate.description())
                .address(address)
                .geoCode(mongoMungple.getGeoCode())
                .pGeoCode(mongoMungple.getPGeoCode())
                .latitude(mongoMungple.getLatitude())
                .longitude(mongoMungple.getLongitude())
                .isCorrect(true)
                .isHideAddress(false)
                .commentCount(0)
                .build();
    }

    public Certification update(CertUpdate certUpdate) {
        return Certification.builder()
                .certificationId(this.certificationId)
                .user(user)
                .categoryCode(categoryCode)
                .mungpleId(mungpleId)
                .placeName(placeName)
                .address(address)
                .geoCode(geoCode)
                .pGeoCode(pGeoCode)
                .latitude(latitude)
                .longitude(longitude)
                .isCorrect(isCorrect)
                .commentCount(commentCount)
                // update
                .description(certUpdate.description())
                .isHideAddress(certUpdate.isHideAddress())
                .build();
    }

    public void setIsCorrect(boolean isCorrect){
        this.isCorrect = isCorrect;

    }

    public void setCommentCount(Integer commentCount){
        this.commentCount = commentCount;
    }
}