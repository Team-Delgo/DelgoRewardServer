package com.delgo.reward.dto.mungple.detail;

import com.delgo.reward.comm.code.BusinessHourCode;
import com.delgo.reward.comm.code.DetailCode;
import com.delgo.reward.domain.mungple.Mungple;
import com.delgo.reward.mongoDomain.MungpleDetail;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@ToString
public class MungpleDetailResDTO {
    private final int mungpleId;
    private final String phoneNo; // 매장 번호
    private final String categoryCode; // 카테고리 코드 ( ex. 카페, 음식점 .. )
    private final String placeName;
    private final String placeNameEn;
    private final String address; // 주소

    private final String enterDesc; // 강아지 동반 안내 매장 설명문
    private final Map<String, DetailCode> acceptSize; // 허용 크기 ( S, M , L )
    private final Map<BusinessHourCode, String> businessHour; // 운영 시간 ( 요일 별로 표시 )

    private final String instaId; // 인스타 ID
    private final List<String> photoUrls; // 매장 사진 URL List

    private final String editorNoteUrl; //
    private final String copyLink;

    private final Boolean isParking; // 주차 가능
    private final String parkingInfo; // 주차 정보

    private final Integer certCount; // 인증 개수
    private final Integer recommendCount = 0; // 추천 개수

    public MungpleDetailResDTO(Mungple mungple, MungpleDetail mungpleDetail, int certCount){
        mungpleId = mungple.getMungpleId();
        phoneNo = mungple.getPhoneNo();
        categoryCode = mungple.getCategoryCode();
        placeName = mungple.getPlaceName();
        placeNameEn = mungple.getPlaceNameEn();
        address = mungple.getJibunAddress();

        enterDesc = mungpleDetail.getEnterDesc();
        acceptSize = mungpleDetail.getAcceptSize();
        businessHour = mungpleDetail.getBusinessHour();

        instaId = mungpleDetail.getInstaId();
        photoUrls = mungpleDetail.getPhotoUrls();

        editorNoteUrl = mungple.getDetailUrl();
        copyLink = mungpleDetail.getCopyLink();

        isParking = mungpleDetail.getIsParking();
        parkingInfo = mungpleDetail.getParkingInfo();

        this.certCount = certCount;
    }
}
