package com.delgo.reward.dto.mungple;

import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
public class MungpleResDTO {
    // mongo
    private int mungpleId;
    private String categoryCode; // 카테고리 코드 ( ex. 카페, 음식점 .. )

    private String placeName;
    private String placeNameEn;
    private String address; // 지번 주소

    private String latitude; // 위도
    private String longitude; // 경도

    private String photoUrl; // 사진 url
    private String detailUrl; // 상세 페이지 url

    public MungpleResDTO(MongoMungple mungple) {
        mungpleId = mungple.getMungpleId();
        categoryCode = mungple.getCategoryCode();

        placeName = mungple.getPlaceName();
        placeNameEn = mungple.getPlaceNameEn();
        address = mungple.getJibunAddress();

        latitude = mungple.getLatitude();
        longitude = mungple.getLongitude();

        photoUrl = mungple.getPhotoUrls().get(0);
        detailUrl = mungple.getDetailUrl();
    }
}