package com.delgo.reward.dto.mungple;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.user.Bookmark;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.Map;


@Getter
@Builder
@AllArgsConstructor
public class MungpleResponse {
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
    @Schema(description = "유저가 저장했는지 여부")
    protected Boolean isBookmarked;

    public static MungpleResponse from(Mungple mungple){
        return MungpleResponse.builder()
                .mungpleId(mungple.getMungpleId())
                .categoryCode(mungple.getCategoryCode())
                .placeName(mungple.getPlaceName())
                .placeNameEn(mungple.getPlaceNameEn())
                .address(mungple.getJibunAddress())
                .latitude(mungple.getLatitude())
                .longitude(mungple.getLongitude())
                .photoUrl(mungple.getPhotoUrls().get(0))
                .detailUrl(mungple.getDetailUrl())
                .build();
    }

    public static List<MungpleResponse> fromList(List<Mungple> mungpleList) {
        return mungpleList.stream().map(mungple -> MungpleResponse.from(mungple)).toList();
    }

    public static MungpleResponse from(Mungple mungple, int certCount, int bookmarkCount) {
        return MungpleResponse.builder()
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
                .build();
    }

    public static List<MungpleResponse> fromList(List<Mungple> mungpleList, Map<Integer, Integer> certCountMap, Map<Integer, Integer> bookmarkCountMap) {
        return mungpleList.stream().map(mungple -> {
            int certCount = certCountMap.getOrDefault(mungple.getMungpleId(),0);
            int bookmarkCount = bookmarkCountMap.getOrDefault(mungple.getMungpleId(), 0);

            return MungpleResponse.from(mungple, certCount, bookmarkCount);
        }).toList();
    }

    public static MungpleResponse from(Mungple mungple, int certCount, int bookmarkCount, boolean isBookmarked) {
        return MungpleResponse.builder()
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
                .build();
    }

    public static List<MungpleResponse> fromList(List<Mungple> mungpleList, Map<Integer, Integer> certCountMap, Map<Integer, Integer> bookmarkCountMap, List<Bookmark> bookmarkList) {
        List<Integer> bookmarkedMungpleIdList = Bookmark.getMungpleIdList(bookmarkList);
        return mungpleList.stream().map(mungple -> {
            int certCount = certCountMap.getOrDefault(mungple.getMungpleId(),0);
            int bookmarkCount = bookmarkCountMap.getOrDefault(mungple.getMungpleId(), 0);
            boolean isBookmarked = bookmarkedMungpleIdList.contains(mungple.getMungpleId());

            return MungpleResponse.from(mungple, certCount, bookmarkCount, isBookmarked);
        }).toList();
    }
}