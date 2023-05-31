package com.delgo.reward.dto.mungple;

import com.delgo.reward.comm.code.DetailCode;
import com.delgo.reward.domain.Mungple;
import com.delgo.reward.mongoDomain.MungpleDetailData;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@ToString
public class MungpleDetailResDTO {
    private int mungpleId;
    private String phoneNo; // 매장 번호
    private String categoryCode; // 카테고리 코드 ( ex. 카페, 음식점 .. )
    private String placeName;
    private String placeNameEn;
    private String address; // 주소

    private String enterDesc; // 강아지 동반 안내 매장 설명문
    private String residentDogName; // 상주견 이름
    private Map<String, String> businessHour; // 운영 시간 ( 요일별로 표시 )
    private Map<String, DetailCode> acceptSize; // 허용 크기 ( S, M , L )

    private String instaId; // 인스타 ID

    private List<String> photoUrls; // 매장 사진 URL List

    private String representMenuTitle; // 대표 메뉴 제목
    private List<String> representMenuPhotoUrls; // 대표 메뉴 사진 URL // ※무조건 3개 이상이어야 함.

    private Boolean isParking; // 주차 가능 여부
    private String parkingInfo; // 주차 정보

    private String editorNoteUrl; //
    private String copyLink;

    private Integer certCount; // 인증 개수
    private Integer recommendCount = 0; // 추천 개수


    public MungpleDetailResDTO(Mungple mungple, MungpleDetailData mungpleDetailData, int certCount){
        mungpleId = mungple.getMungpleId();
        phoneNo = mungple.getPhoneNo();
        categoryCode = mungple.getCategoryCode();
        placeName = mungple.getPlaceName();
        placeNameEn = mungple.getPlaceNameEn();
        address = mungple.getJibunAddress();

        enterDesc = mungpleDetailData.getEnterDesc();
        residentDogName = mungpleDetailData.getResidentDogName();
        businessHour = mungpleDetailData.getBusinessHour();
        acceptSize = mungpleDetailData.getAcceptSize();

        photoUrls = mungpleDetailData.getPhotoUrls();
        instaId = mungpleDetailData.getInstaId();

        representMenuTitle = mungpleDetailData.getRepresentMenuTitle();
        representMenuPhotoUrls = mungpleDetailData.getRepresentMenuPhotoUrls();

        isParking = mungpleDetailData.getIsParking();
        parkingInfo = mungpleDetailData.getParkingInfo();

        editorNoteUrl = mungple.getDetailUrl();
        copyLink = mungpleDetailData.getCopyLink();

        this.certCount = certCount;
    }
}
