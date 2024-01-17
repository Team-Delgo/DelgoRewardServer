package com.delgo.reward.mungple.response;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.mungple.domain.Mungple;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MungpleResponseTest {

    @Test
    void from() {
        // given
        int certCount = 3;
        int bookmarkCount = 5;
        boolean isBookmarked = true;
        Mungple mungple = Mungple.builder()
                .id("someId")
                .mungpleId(123)
                .categoryCode(CategoryCode.CA0002)
                .placeName("테스트 장소")
                .placeNameEn("Test Place")
                .jibunAddress("서울시 강남구 역삼동 123-45")
                .latitude("37.12345")
                .longitude("127.12345")
                .detailUrl("http://example.com/detail")
                .photoUrls(Arrays.asList("http://example.com/photo1.jpg", "http://example.com/photo2.jpg"))
                .build();

        // when
        MungpleResponse mungpleResponse = MungpleResponse.from(mungple,certCount, bookmarkCount, isBookmarked);

        // then
        assertThat(mungpleResponse.getMungpleId()).isEqualTo(mungple.getMungpleId());
        assertThat(mungpleResponse.getCategoryCode()).isEqualTo(mungple.getCategoryCode());
        assertThat(mungpleResponse.getPlaceName()).isEqualTo(mungple.getPlaceName());
        assertThat(mungpleResponse.getPlaceNameEn()).isEqualTo(mungple.getPlaceNameEn());
        assertThat(mungpleResponse.getAddress()).isEqualTo(mungple.getJibunAddress());
        assertThat(mungpleResponse.getLatitude()).isEqualTo(mungple.getLatitude());
        assertThat(mungpleResponse.getLongitude()).isEqualTo(mungple.getLongitude());
        assertThat(mungpleResponse.getPhotoUrl()).isEqualTo(mungple.getPhotoUrls().get(0));
        assertThat(mungpleResponse.getDetailUrl()).isEqualTo(mungple.getDetailUrl());
        assertThat(mungpleResponse.getCertCount()).isEqualTo(certCount);
        assertThat(mungpleResponse.getBookmarkCount()).isEqualTo(bookmarkCount);
        assertThat(mungpleResponse.getIsBookmarked()).isEqualTo(isBookmarked);
    }

    @Test
    void fromList() {
        // given
        Mungple mungple1 = Mungple.builder().mungpleId(123).photoUrls(List.of("photo1")).build();
        Mungple mungple2 = Mungple.builder().mungpleId(124).photoUrls(List.of("photo2")).build();
        Mungple mungple3 = Mungple.builder().mungpleId(125).photoUrls(List.of("photo3")).build();
        List<Mungple> mungpleList = List.of(mungple1, mungple2, mungple3);
        Map<Integer, Integer> certCountMap = Map.of(
                123, 2,
                124, 3,
                125, 4);
        Map<Integer, Integer> bookmarkCountMap = Map.of(
                123, 8,
                124, 9,
                125, 10);
        List<Integer> bookmarkedMungpleIdList = List.of(123, 124, 125);

        // when
        List<MungpleResponse> mungpleResponseList = MungpleResponse.fromList(mungpleList, certCountMap, bookmarkCountMap, bookmarkedMungpleIdList);

        // then
        assertThat(mungpleResponseList.get(0).getMungpleId()).isEqualTo(mungpleList.get(0).getMungpleId());
        assertThat(mungpleResponseList.get(1).getMungpleId()).isEqualTo(mungpleList.get(1).getMungpleId());
        assertThat(mungpleResponseList.get(2).getMungpleId()).isEqualTo(mungpleList.get(2).getMungpleId());
    }
}