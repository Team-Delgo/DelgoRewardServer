package com.delgo.reward.dto.mungple;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
public class MungpleResDTO {
    @Schema(description = "멍플 고유 아이디")
    protected int mungpleId;
    @Schema(description = "카테고리 코드", enumAsRef = true)
    protected CategoryCode categoryCode;
    @Schema(description = "장소 명")
    protected String placeName;
    @Schema(description = "영문 장소 명")
    protected String placeNameEn;
    @Schema(description = "지번 주소")
    protected String address;
    @Schema(description = "위도")
    protected String latitude;
    @Schema(description = "경도")
    protected String longitude;
    @Schema(description = "사진 url")
    protected String photoUrl;
    @Schema(description = "상세 페이지 url")
    protected String detailUrl;
    @Schema(description = "인증 개수")
    protected int certCount;
    @Schema(description = "저장 개수")
    protected int bookmarkCount;

    // 지도 생성자
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

    // 목록 생성자
    public MungpleResDTO(MongoMungple mungple, int certCount, int bookmarkCount) {
        mungpleId = mungple.getMungpleId();
        categoryCode = mungple.getCategoryCode();

        placeName = mungple.getPlaceName();
        placeNameEn = mungple.getPlaceNameEn();
        address = mungple.getJibunAddress();

        latitude = mungple.getLatitude();
        longitude = mungple.getLongitude();

        photoUrl = mungple.getPhotoUrls().get(0);
        detailUrl = mungple.getDetailUrl();

        this.certCount = certCount;
        this.bookmarkCount = bookmarkCount;
    }
}