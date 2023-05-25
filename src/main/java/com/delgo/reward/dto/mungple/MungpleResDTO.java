package com.delgo.reward.dto.mungple;


import com.delgo.reward.domain.Mungple;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
public class MungpleResDTO {
    private int mungpleId;
    private String categoryCode; // 카테고리 코드 ( ex. 카페, 음식점 .. )

    private String placeName;
    private String placeNameEn;
    private String roadAddress; // 도로명 주소
    private String jibunAddress; // 지번 주소

    private String geoCode; // 지역 코드
    private String pGeoCode; // 부모 지역 코드
    private String latitude; // 위도
    private String longitude; // 경도

    private String photoUrl; // 사진 url
    private String detailUrl; // 상세 페이지 url


    public MungpleResDTO(Mungple mungple) {
        mungpleId = mungple.getMungpleId();
        categoryCode = mungple.getCategoryCode();

        placeName = mungple.getPlaceName();
        placeNameEn = mungple.getPlaceNameEn();
        roadAddress = mungple.getRoadAddress();
        jibunAddress = mungple.getJibunAddress();

        geoCode = mungple.getGeoCode();
        pGeoCode = mungple.getPGeoCode();
        latitude = mungple.getLatitude();
        longitude = mungple.getLongitude();

        photoUrl = mungple.getPhotoUrl();
        detailUrl = mungple.getDetailUrl();
    }
}