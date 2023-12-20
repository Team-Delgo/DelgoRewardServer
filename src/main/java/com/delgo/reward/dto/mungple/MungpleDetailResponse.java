package com.delgo.reward.dto.mungple;

import com.delgo.reward.comm.code.BusinessHourCode;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.DetailCode;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class MungpleDetailResponse {
    @Schema(description = "멍플 고유 아이디")
    private int mungpleId;
    @Schema(description = "카테고리 코드", enumAsRef = true)
    private CategoryCode categoryCode;
    @Schema(description = "장소 명")
    private String placeName;
    @Schema(description = "영문 장소 명")
    private String placeNameEn;
    @Schema(description = "지번 주소")
    private String address;
    @Schema(description = "위도")
    private String latitude;
    @Schema(description = "경도")
    private String longitude;
    @Schema(description = "사진 url")
    private String photoUrl;
    @Schema(description = "상세 페이지 url")
    private String detailUrl;
    @Schema(description = "인증 개수")
    private int certCount;
    @Schema(description = "저장 개수")
    private int bookmarkCount;
    @Schema(description = "유저가 저장했는지 여부")
    private Boolean isBookmarked;

    @Schema(description = "매장 번호")
    private String phoneNo;
    @Schema(description = "강아지 동반 안내 매장 설명문")
    private String enterDesc;
    @Schema(description = "허용 크기 설정 | key:[S, M, L] | value: DetailCode ", implementation = DetailCode.class)
    private Map<String, DetailCode> acceptSize;
    @Schema(description = "운영 시간 | key: BusinessHourCode | value: 입력 값", implementation = BusinessHourCode.class)
    private Map<BusinessHourCode, String> businessHour;
    @Schema(description = "Insta Id")
    private String instaId;
    @Schema(description = "매장 사진 URL List")
    private List<String> photoUrls;
    @Schema(description = "에디터 메모 URL")
    private String editorNoteUrl;
    @Schema(description = "주차 가능 여부")
    private Boolean isParking;
    @Schema(description = "주차 정보")
    private String parkingInfo;

    // CA0002, CA0003
    @Schema(description = "상주견 이름")
    private String residentDogName; // 상주견 이름
    @Schema(description = "상주견 프로필")
    private String residentDogPhoto; // 상주견 프로필

    @Schema(description = "대표 메뉴 제목")
    private String representMenuTitle; // 대표 메뉴 제목
    @Schema(description = "대표 메뉴 사진 URL")
    private List<String> representMenuPhotoUrls; // 대표 메뉴 사진 URL // ※무조건 3개 이상이어야 함.

    // 그 외 카테고리
    @Schema(description = "가격표 존재 여부")
    private Boolean isPriceTag;
    @Schema(description = "가격표 사진")
    private List<String> priceTagPhotoUrls;


    public static MungpleDetailResponse from(Mungple mungple, int certCount, int bookmarkCount, boolean isBookmarked) {
        List<String> representMenuBoardPhotoUrls = mungple.getRepresentMenuBoardPhotoUrls();
        representMenuBoardPhotoUrls.addAll(mungple.getRepresentMenuPhotoUrls());

        return MungpleDetailResponse.builder()
                .mungpleId(mungple.getMungpleId())
                .categoryCode(mungple.getCategoryCode())
                .placeName(mungple.getPlaceName())
                .placeNameEn(mungple.getPlaceNameEn())
                .address(mungple.getJibunAddress())
                .latitude(mungple.getLatitude())
                .longitude(mungple.getLongitude())
                .photoUrl(mungple.getPhotoUrls().get(0))
                .detailUrl(mungple.getDetailUrl())
                .certCount(certCount)
                .bookmarkCount(bookmarkCount)
                .isBookmarked(isBookmarked)

                .phoneNo(mungple.getPhoneNo())
                .enterDesc(mungple.getEnterDesc())
                .acceptSize(mungple.getAcceptSize())
                .businessHour(mungple.getBusinessHour())
                .instaId(mungple.getInstaId())
                .photoUrls(mungple.getPhotoUrls())
                .editorNoteUrl(mungple.getDetailUrl())
                .isParking(mungple.getIsParking())
                .parkingInfo(mungple.getParkingInfo())

                .residentDogName(mungple.getResidentDogName())
                .residentDogPhoto(mungple.getResidentDogPhoto())
                .representMenuTitle(mungple.getRepresentMenuTitle())
                .representMenuPhotoUrls(representMenuBoardPhotoUrls)

                .isPriceTag(!mungple.getPriceTagPhotoUrls().isEmpty())
                .priceTagPhotoUrls(mungple.getPriceTagPhotoUrls())
                .build();
    }
}
