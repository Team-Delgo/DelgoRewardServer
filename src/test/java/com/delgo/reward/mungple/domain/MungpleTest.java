package com.delgo.reward.mungple.domain;

import com.delgo.reward.comm.code.BusinessHourCode;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.exception.GoogleSheetException;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MungpleTest {

    @Test
    void getThumbnailUrl() {
        // given
        Mungple mungple = Mungple.builder()
                .photoUrls(List.of("photo1", "photo2"))
                .build();

        // when
        String url = mungple.getThumbnailUrl();

        // then
        assertThat(url).isEqualTo(mungple.getPhotoUrls().get(0));
    }

    @Test
    void setAcceptSize() {
        // given
        Mungple mungple = Mungple.builder().mungpleId(1).build();
        String input = "S: ALLOW, \n" +
                "M: DENY, \n" +
                "L: OUTDOOR";

        // when
        mungple.setAcceptSize(input);

        // then
        assertThat(mungple.getAcceptSize().get("S")).isEqualTo(EntryPolicy.ALLOW);
        assertThat(mungple.getAcceptSize().get("M")).isEqualTo(EntryPolicy.DENY);
        assertThat(mungple.getAcceptSize().get("L")).isEqualTo(EntryPolicy.OUTDOOR);
    }

    @Test
    void setAcceptSize_예외처리() {
        // given
        Mungple mungple = Mungple.builder().mungpleId(1).build();
        String invalidInput = "S: INVALID_CODE";

        // when

        // then
        assertThatThrownBy(() -> {
            mungple.setAcceptSize(invalidInput);
        }).isInstanceOf(GoogleSheetException.class);
    }

    @Test
    void setBusinessHour() {
        // given
        Mungple mungple = Mungple.builder().mungpleId(1).build();
        String input = "MON: 06:00 - 24:00\n" +
                "TUE: 06:00 - 24:00\n" +
                "WED: 06:00 - 24:00\n" +
                "THU: 06:00 - 24:00\n" +
                "FRI: 06:00 - 24:00\n" +
                "SAT: 06:00 - 24:00\n" +
                "SUN: 06:00 - 24:00\n" +
                "HOLIDAY: 연중무휴";

        // when
        mungple.setBusinessHour(input);

        // then
        assertThat(mungple.getBusinessHour().get(BusinessHourCode.MON)).isEqualTo("06:00 - 24:00");
        assertThat(mungple.getBusinessHour().get(BusinessHourCode.TUE)).isEqualTo("06:00 - 24:00");
        assertThat(mungple.getBusinessHour().get(BusinessHourCode.WED)).isEqualTo("06:00 - 24:00");
        assertThat(mungple.getBusinessHour().get(BusinessHourCode.THU)).isEqualTo("06:00 - 24:00");
        assertThat(mungple.getBusinessHour().get(BusinessHourCode.FRI)).isEqualTo("06:00 - 24:00");
        assertThat(mungple.getBusinessHour().get(BusinessHourCode.SAT)).isEqualTo("06:00 - 24:00");
        assertThat(mungple.getBusinessHour().get(BusinessHourCode.SUN)).isEqualTo("06:00 - 24:00");
        assertThat(mungple.getBusinessHour().get(BusinessHourCode.HOLIDAY)).isEqualTo("연중무휴");
        assertThat(mungple.getBusinessHour().get(BusinessHourCode.BREAK_TIME)).isEqualTo("");
        assertThat(mungple.getBusinessHour().get(BusinessHourCode.LAST_ORDER)).isEqualTo("");
    }

    @Test
    void setBusinessHour_예외처리() {
        // given
        Mungple mungple = Mungple.builder().mungpleId(1).build();
        String invalidInput = "INVALID_CODE: 09:00-18:00";

        // when

        // then
        assertThatThrownBy(() -> {
            mungple.setBusinessHour(invalidInput);
        }).isInstanceOf(GoogleSheetException.class);
    }

    @Test
    void sortPhotoList() {
        // given
        List<String> photoList = List.of("test_3.webp", "test_1.webp", "test_2.webp");

        // when
        List<String> sortedPhotoList = Mungple.sortPhotoList(photoList);

        // then
        List<String> expected = List.of("test_1.webp", "test_2.webp", "test_3.webp");
        assertThat(sortedPhotoList).isEqualTo(expected);
    }

    @Test
    void getBaseNameForFigma() {
        // given
        String placeName = "TestPlaceName";
        CategoryCode categoryCode = CategoryCode.CA0002;
        Mungple mungple = Mungple.builder()
                .placeName(placeName)
                .categoryCode(categoryCode)
                .jibunAddress("경기도 성남시 54-13")
                .build();

        // when
        String baseName = mungple.getBaseNameForFigma();

        // then
        String expected = "성남시_" + CategoryCode.CA0002.getName() + "_" + placeName;
        assertThat(baseName).isEqualTo(expected);
    }

    @Test
    void getLocalAreaName() {
        // given
        Mungple mungple = Mungple.builder()
                .categoryCode(CategoryCode.CA0002)
                .jibunAddress("경기도 성남시 분당구 54-13")
                .build();

        // when
        String address = mungple.getLocalAreaName();

        // then
        String expected = "분당구";
        assertThat(address).isEqualTo(expected);
    }

    @Test
    void getLocalAreaName_예외처리() {
        // given
        Mungple mungple = Mungple.builder()
                .categoryCode(CategoryCode.CA0002)
                .jibunAddress("경기도 54-123")
                .build();

        // when
        String address = mungple.getLocalAreaName();

        // then
        String expected = "";
        assertThat(address).isEqualTo(expected);
    }

    @Test
    void formattedAddress() {
        // given
        Mungple mungple = Mungple.builder()
                .categoryCode(CategoryCode.CA0002)
                .jibunAddress("경기도 성남시 분당구 54-13")
                .build();

        // when
        String address = mungple.formattedAddress();

        // then
        String expected = "경기도 성남시 분당구";
        assertThat(address).isEqualTo(expected);
    }

    @Test
    void listToMap() {
        // given
        List<Mungple> mungpleList = List.of(
                Mungple.builder().mungpleId(1).build(),
                Mungple.builder().mungpleId(2).build(),
                Mungple.builder().mungpleId(3).build());

        // when
        Map<Integer, Mungple> map = Mungple.listToMap(mungpleList);

        // then
        assertThat(map.get(1)).isEqualTo(mungpleList.get(0));
        assertThat(map.get(2)).isEqualTo(mungpleList.get(1));
        assertThat(map.get(3)).isEqualTo(mungpleList.get(2));
    }

    @Test
    void setId() {
        // given
        Mungple mungple = new Mungple();
        String id = "123456";

        // when
        mungple.setId(id);

        // then
        assertThat(mungple.getId()).isEqualTo(id);
    }

    @Test
    void setMungpleId() {
        // given
        Mungple mungple = new Mungple();
        int mungpleId = 123;

        // when
        mungple.setMungpleId(mungpleId);

        // then
        assertThat(mungple.getMungpleId()).isEqualTo(mungpleId);
    }

    @Test
    void setPhotoUrls() {
        // given
        Mungple mungple = new Mungple();
        List<String> photoList = List.of("test_3.webp", "test_1.webp", "test_2.webp");

        // when
        mungple.setPhotoUrls(photoList);

        // then
        assertThat(mungple.getPhotoUrls()).isEqualTo(photoList);
    }

    @Test
    void setResidentDogPhoto() {
        // given
        Mungple mungple = new Mungple();
        String photo = "photo1";

        // when
        mungple.setResidentDogPhoto(photo);

        // then
        assertThat(mungple.getResidentDogPhoto()).isEqualTo(photo);
    }

    @Test
    void setRepresentMenuTitle() {
        // given
        Mungple mungple = new Mungple();
        String menuTitle = "title";

        // when
        mungple.setRepresentMenuTitle(menuTitle);

        // then
        assertThat(mungple.getRepresentMenuTitle()).isEqualTo(menuTitle);
    }

    @Test
    void setRepresentMenuPhotoUrls() {
        // given
        Mungple mungple = new Mungple();
        List<String> photoList = List.of("test_3.webp", "test_1.webp", "test_2.webp");

        // when
        mungple.setRepresentMenuPhotoUrls(photoList);

        // then
        assertThat(mungple.getRepresentMenuPhotoUrls()).isEqualTo(photoList);
    }

    @Test
    void setRepresentMenuBoardPhotoUrls() {
        // given
        Mungple mungple = new Mungple();
        List<String> photoList = List.of("test_3.webp", "test_1.webp", "test_2.webp");

        // when
        mungple.setRepresentMenuBoardPhotoUrls(photoList);

        // then
        assertThat(mungple.getRepresentMenuBoardPhotoUrls()).isEqualTo(photoList);
    }

    @Test
    void setPriceTagPhotoUrls() {
        // given
        Mungple mungple = new Mungple();
        List<String> photoList = List.of("test_3.webp", "test_1.webp", "test_2.webp");

        // when
        mungple.setPriceTagPhotoUrls(photoList);

        // then
        assertThat(mungple.getPriceTagPhotoUrls()).isEqualTo(photoList);
    }

    @Test
    void getter() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Mungple mungple = Mungple.builder()
                .id("someId")
                .mungpleId(123)
                .categoryCode(CategoryCode.CA0002)
                .phoneNo("010-1234-5678")
                .placeName("테스트 장소")
                .placeNameEn("Test Place")
                .roadAddress("서울시 강남구 테헤란로")
                .jibunAddress("서울시 강남구 역삼동 123-45")
                .geoCode("G123")
                .pGeoCode("PG123")
                .latitude("37.12345")
                .longitude("127.12345")
                .detailUrl("http://example.com/detail")
                .isActive(true)
                .createdAt(now)
                .location(new GeoJsonPoint(127.12345, 37.12345))
                .photoUrls(Arrays.asList("http://example.com/photo1.jpg", "http://example.com/photo2.jpg"))
                .enterDesc("강아지 동반 가능")
                .acceptSize(Map.of("S", EntryPolicy.ALLOW, "M", EntryPolicy.ALLOW))
                .businessHour(Map.of(BusinessHourCode.MON, "09:00-18:00"))
                .instaId("insta123")
                .isParking(true)
                .parkingInfo("주차 가능")
                .residentDogName("멍멍이")
                .residentDogPhoto("http://example.com/dog.jpg")
                .representMenuTitle("대표 메뉴")
                .representMenuPhotoUrls(Arrays.asList("http://example.com/menu1.jpg", "http://example.com/menu2.jpg"))
                .representMenuBoardPhotoUrls(Arrays.asList("http://example.com/menuBoard1.jpg", "http://example.com/menuBoard2.jpg"))
                .isPriceTag(true)
                .priceTagPhotoUrls(Arrays.asList("http://example.com/priceTag1.jpg", "http://example.com/priceTag2.jpg"))
                .build();

        // then
        assertThat(mungple.getId()).isEqualTo("someId");
        assertThat(mungple.getMungpleId()).isEqualTo(123);
        assertThat(mungple.getCategoryCode()).isEqualTo(CategoryCode.CA0002);
        assertThat(mungple.getPhoneNo()).isEqualTo("010-1234-5678");
        assertThat(mungple.getPlaceName()).isEqualTo("테스트 장소");
        assertThat(mungple.getPlaceNameEn()).isEqualTo("Test Place");
        assertThat(mungple.getRoadAddress()).isEqualTo("서울시 강남구 테헤란로");
        assertThat(mungple.getJibunAddress()).isEqualTo("서울시 강남구 역삼동 123-45");
        assertThat(mungple.getGeoCode()).isEqualTo("G123");
        assertThat(mungple.getPGeoCode()).isEqualTo("PG123");
        assertThat(mungple.getLatitude()).isEqualTo("37.12345");
        assertThat(mungple.getLongitude()).isEqualTo("127.12345");
        assertThat(mungple.getDetailUrl()).isEqualTo("http://example.com/detail");
        assertThat(mungple.isActive()).isTrue();
        assertThat(mungple.getCreatedAt()).isEqualTo(now);
        assertThat(mungple.getLocation()).isEqualTo(new GeoJsonPoint(127.12345, 37.12345));
        assertThat(mungple.getPhotoUrls()).containsExactlyElementsOf(Arrays.asList("http://example.com/photo1.jpg", "http://example.com/photo2.jpg"));
        assertThat(mungple.getEnterDesc()).isEqualTo("강아지 동반 가능");
        assertThat(mungple.getAcceptSize()).containsAllEntriesOf(Map.of("S", EntryPolicy.ALLOW, "M", EntryPolicy.ALLOW));
        assertThat(mungple.getBusinessHour()).containsAllEntriesOf(Map.of(BusinessHourCode.MON, "09:00-18:00"));
        assertThat(mungple.getInstaId()).isEqualTo("insta123");
        assertThat(mungple.getIsParking()).isTrue();
        assertThat(mungple.getParkingInfo()).isEqualTo("주차 가능");
        assertThat(mungple.getResidentDogName()).isEqualTo("멍멍이");
        assertThat(mungple.getResidentDogPhoto()).isEqualTo("http://example.com/dog.jpg");
        assertThat(mungple.getRepresentMenuTitle()).isEqualTo("대표 메뉴");
        assertThat(mungple.getRepresentMenuPhotoUrls()).containsExactlyElementsOf(Arrays.asList("http://example.com/menu1.jpg", "http://example.com/menu2.jpg"));
        assertThat(mungple.getRepresentMenuBoardPhotoUrls()).containsExactlyElementsOf(Arrays.asList("http://example.com/menuBoard1.jpg", "http://example.com/menuBoard2.jpg"));
        assertThat(mungple.getIsPriceTag()).isTrue();
        assertThat(mungple.getPriceTagPhotoUrls()).containsExactlyElementsOf(Arrays.asList("http://example.com/priceTag1.jpg", "http://example.com/priceTag2.jpg"));
    }
}