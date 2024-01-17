package com.delgo.reward.mungple.response;

import com.delgo.reward.comm.code.BusinessHourCode;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.mungple.domain.EntryPolicy;
import com.delgo.reward.mungple.domain.Mungple;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MungpleDetailResponseTest {

    @Test
    void from() {
        // given
        int certCount = 3;
        int bookmarkCount = 6;
        boolean isBookmarked = true;
        Mungple mungple = Mungple.builder()
                .mungpleId(123)
                .categoryCode(CategoryCode.CA0002)
                .placeName("테스트 장소")
                .placeNameEn("Test Place")
                .jibunAddress("서울시 강남구 역삼동 123-45")
                .latitude("37.12345")
                .longitude("127.12345")
                .detailUrl("http://example.com/detail")
                .photoUrls(new ArrayList<>(Arrays.asList("http://example.com/photo1.jpg", "http://example.com/photo2.jpg")))
                .acceptSize(Map.of("S", EntryPolicy.ALLOW, "M", EntryPolicy.ALLOW))
                .businessHour(Map.of(BusinessHourCode.MON, "09:00-18:00"))
                .instaId("insta123")
                .isParking(true)
                .parkingInfo("주차 가능")
                .residentDogName("멍멍이")
                .residentDogPhoto("http://example.com/dog.jpg")
                .representMenuTitle("대표 메뉴")
                .representMenuPhotoUrls(new ArrayList<>(Arrays.asList("http://example.com/menu1.jpg", "http://example.com/menu2.jpg")))
                .representMenuBoardPhotoUrls(new ArrayList<>(Arrays.asList("http://example.com/menuBoard1.jpg", "http://example.com/menuBoard2.jpg")))
                .isPriceTag(true)
                .priceTagPhotoUrls(new ArrayList<>(Arrays.asList("http://example.com/priceTag1.jpg", "http://example.com/priceTag2.jpg")))
                .build();

        // when
        MungpleDetailResponse mungpleDetailResponse = MungpleDetailResponse.from(mungple, certCount, bookmarkCount, isBookmarked);

        // then
        mungple.getRepresentMenuBoardPhotoUrls().addAll(mungple.getRepresentMenuPhotoUrls());
        assertThat(mungpleDetailResponse.getMungpleId()).isEqualTo(mungple.getMungpleId());
        assertThat(mungpleDetailResponse.getCategoryCode()).isEqualTo(mungple.getCategoryCode());
        assertThat(mungpleDetailResponse.getPlaceName()).isEqualTo(mungple.getPlaceName());
        assertThat(mungpleDetailResponse.getPlaceNameEn()).isEqualTo(mungple.getPlaceNameEn());
        assertThat(mungpleDetailResponse.getAddress()).isEqualTo(mungple.getJibunAddress());
        assertThat(mungpleDetailResponse.getLatitude()).isEqualTo(mungple.getLatitude());
        assertThat(mungpleDetailResponse.getLongitude()).isEqualTo(mungple.getLongitude());
        assertThat(mungpleDetailResponse.getDetailUrl()).isEqualTo(mungple.getDetailUrl());
        assertThat(mungpleDetailResponse.getPhotoUrl()).isEqualTo(mungple.getPhotoUrls().get(0));
        assertThat(mungpleDetailResponse.getCertCount()).isEqualTo(certCount);
        assertThat(mungpleDetailResponse.getBookmarkCount()).isEqualTo(bookmarkCount);
        assertThat(mungpleDetailResponse.getIsBookmarked()).isEqualTo(isBookmarked);
        assertThat(mungpleDetailResponse.getPhoneNo()).isEqualTo(mungple.getPhoneNo());
        assertThat(mungpleDetailResponse.getEnterDesc()).isEqualTo(mungple.getEnterDesc());
        assertThat(mungpleDetailResponse.getAcceptSize()).isEqualTo(mungple.getAcceptSize());
        assertThat(mungpleDetailResponse.getBusinessHour()).isEqualTo(mungple.getBusinessHour());
        assertThat(mungpleDetailResponse.getInstaId()).isEqualTo(mungple.getInstaId());
        assertThat(mungpleDetailResponse.getPhotoUrls()).isEqualTo(mungple.getPhotoUrls());
        assertThat(mungpleDetailResponse.getEditorNoteUrl()).isEqualTo(mungple.getDetailUrl());
        assertThat(mungpleDetailResponse.getIsParking()).isEqualTo(mungple.getIsParking());
        assertThat(mungpleDetailResponse.getParkingInfo()).isEqualTo(mungple.getParkingInfo());
        assertThat(mungpleDetailResponse.getResidentDogName()).isEqualTo(mungple.getResidentDogName());
        assertThat(mungpleDetailResponse.getResidentDogPhoto()).isEqualTo(mungple.getResidentDogPhoto());
        assertThat(mungpleDetailResponse.getRepresentMenuTitle()).isEqualTo(mungple.getRepresentMenuTitle());
        assertThat(mungpleDetailResponse.getRepresentMenuPhotoUrls()).isEqualTo(mungple.getRepresentMenuBoardPhotoUrls());
        assertThat(mungpleDetailResponse.getIsPriceTag()).isEqualTo(!mungple.getPriceTagPhotoUrls().isEmpty());
        assertThat(mungpleDetailResponse.getPriceTagPhotoUrls()).isEqualTo(mungple.getPriceTagPhotoUrls());
    }
}