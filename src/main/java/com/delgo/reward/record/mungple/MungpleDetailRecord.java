package com.delgo.reward.record.mungple;


import com.delgo.reward.comm.code.BusinessHourCode;
import com.delgo.reward.comm.code.DetailCode;
import com.delgo.reward.mongoDomain.MungpleDetail;

import java.util.List;
import java.util.Map;

public record MungpleDetailRecord(
        int mungpleId, // 멍플 ID
        String enterDesc, // 강아지 동반 안내 매장 설명문
        String residentDogName, // 상주견 이름
        String residentDogPhoto, // 상주견 사진
        Map<BusinessHourCode, String> businessHour, // 운영 시간 ( 요일별로 표시 )
        Map<String, DetailCode> acceptSize, // 허용 크기 ( S, M , L )
        String instaId, // 인스타 ID
        List<String> photoUrls, // 매장 사진들
        String representMenuTitle, // 대표 메뉴 제목
        List<String> representMenuPhotoUrls, // 대표 메뉴 사진 URL // ※무조건 3개 이상이어야 함.
        Boolean isParking, // 주차 가능 여부
        String parkingInfo, // 주차 정보
        String copyLink
) {
    public MungpleDetail makeDetailData(){
        return MungpleDetail.builder()
                .mungpleId(mungpleId)
                .enterDesc(enterDesc)
                .residentDogName(residentDogName)
                .residentDogPhoto(residentDogPhoto)
                .businessHour(businessHour)
                .acceptSize(acceptSize)
                .instaId(instaId)
                .photoUrls(photoUrls)
                .representMenuTitle(representMenuTitle)
                .representMenuPhotoUrls(representMenuPhotoUrls)
                .isParking(isParking)
                .parkingInfo(parkingInfo)
                .copyLink(copyLink)
                .build();
    }
}
