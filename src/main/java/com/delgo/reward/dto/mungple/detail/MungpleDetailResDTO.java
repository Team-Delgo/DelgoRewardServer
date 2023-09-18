package com.delgo.reward.dto.mungple.detail;

import com.delgo.reward.comm.code.BusinessHourCode;
import com.delgo.reward.comm.code.DetailCode;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
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

    private final String editorNoteUrl;

    private final Boolean isParking; // 주차 가능
    private final String parkingInfo; // 주차 정보

    private final Integer certCount; // 인증 개수
    private final Integer recommendCount = 0; // 추천 개수

    private final Boolean isBookmarked; // 유저가 북마크를 했는가?

    public MungpleDetailResDTO(MongoMungple mongoMungple, int certCount, boolean isBookmarked){
        mungpleId = mongoMungple.getMungpleId();
        phoneNo = mongoMungple.getPhoneNo();
        categoryCode = mongoMungple.getCategoryCode();
        placeName = mongoMungple.getPlaceName();
        placeNameEn = mongoMungple.getPlaceNameEn();
        address = mongoMungple.getJibunAddress();

        enterDesc = mongoMungple.getEnterDesc();
        acceptSize = mongoMungple.getAcceptSize();
        businessHour = mongoMungple.getBusinessHour();

        instaId = mongoMungple.getInstaId();
        photoUrls = mongoMungple.getPhotoUrls();

        editorNoteUrl = mongoMungple.getDetailUrl();

        isParking = mongoMungple.getIsParking();
        parkingInfo = mongoMungple.getParkingInfo();

        this.isBookmarked = isBookmarked;

        this.certCount = certCount;
    }
}
