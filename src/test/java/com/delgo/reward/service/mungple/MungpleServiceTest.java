package com.delgo.reward.service.mungple;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.MungpleSort;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.mungple.service.MungpleService;
import com.delgo.reward.mungple.service.strategy.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class MungpleServiceTest {

    @Autowired
    MungpleService mungpleService;

    @Test
    public void save() {
        // given
        Mungple mungple = Mungple.builder()
                .categoryCode(CategoryCode.CA0002) // 가정된 열거형 값
                .phoneNo("010-1234-5678") // 예시 전화번호
                .placeName("테스트 카페") // 예시 장소 이름
                .placeNameEn("Test Cafe") // 예시 장소 영문 이름
                .roadAddress("서울시 강남구 테헤란로 123") // 예시 도로명 주소
                .jibunAddress("서울시 강남구 역삼동 456-78") // 예시 지번 주소
                .geoCode("12345") // 예시 지역 코드
                .pGeoCode("123") // 예시 부모 지역 코드
                .latitude("37.12345") // 예시 위도
                .longitude("127.12345") // 예시 경도
                .detailUrl("http://example.com/detail") // 예시 상세 페이지 URL
                .isActive(true) // 활성화 상태
                .photoUrls(Arrays.asList("http://example.com/photo1.jpg", "http://example.com/photo2.jpg")) // 예시 사진 URL 리스트
                .enterDesc("강아지 동반 가능") // 예시 매장 설명문
                .acceptSize(new HashMap<>()) // 예시 허용 크기 맵
                .businessHour(new HashMap<>()) // 예시 운영 시간 맵
                .instaId("testcafe") // 예시 인스타그램 ID
                .isParking(true) // 주차 가능 여부
                .parkingInfo("주차 가능") // 예시 주차 정보
                .residentDogName("초코") // 예시 상주견 이름
                .residentDogPhoto("http://example.com/dog.jpg") // 예시 상주견 사진
                .representMenuTitle("시그니처 커피") // 예시 대표 메뉴 제목
                .representMenuPhotoUrls(Arrays.asList("http://example.com/menu1.jpg", "http://example.com/menu2.jpg")) // 예시 대표 메뉴 사진 URL 리스트
                .representMenuBoardPhotoUrls(Arrays.asList("http://example.com/board1.jpg", "http://example.com/board2.jpg")) // 예시 대표 메뉴 보드 사진 URL 리스트
                .isPriceTag(true) // 가격표 존재 여부
                .priceTagPhotoUrls(Arrays.asList("http://example.com/price1.jpg", "http://example.com/price2.jpg")) // 예시 가격표 사진 URL 리스트
                .build();
        // when
        Mungple savedMungple = mungpleService.save(mungple);

        // then
        assertThat(savedMungple.getCategoryCode()).isEqualTo(mungple.getCategoryCode());
        assertThat(savedMungple.getPhoneNo()).isEqualTo(mungple.getPhoneNo());
        assertThat(savedMungple.getPlaceName()).isEqualTo(mungple.getPlaceName());
        assertThat(savedMungple.getPlaceNameEn()).isEqualTo(mungple.getPlaceNameEn());
        assertThat(savedMungple.getRoadAddress()).isEqualTo(mungple.getRoadAddress());
        assertThat(savedMungple.getJibunAddress()).isEqualTo(mungple.getJibunAddress());
        assertThat(savedMungple.getGeoCode()).isEqualTo(mungple.getGeoCode());
        assertThat(savedMungple.getPGeoCode()).isEqualTo(mungple.getPGeoCode());
        assertThat(savedMungple.getLatitude()).isEqualTo(mungple.getLatitude());
        assertThat(savedMungple.getLongitude()).isEqualTo(mungple.getLongitude());
        assertThat(savedMungple.getDetailUrl()).isEqualTo(mungple.getDetailUrl());
        assertThat(savedMungple.isActive()).isEqualTo(mungple.isActive());
        assertThat(savedMungple.getPhotoUrls()).isEqualTo(mungple.getPhotoUrls());
        assertThat(savedMungple.getEnterDesc()).isEqualTo(mungple.getEnterDesc());
        assertThat(savedMungple.getAcceptSize()).isEqualTo(mungple.getAcceptSize());
        assertThat(savedMungple.getBusinessHour()).isEqualTo(mungple.getBusinessHour());
        assertThat(savedMungple.getInstaId()).isEqualTo(mungple.getInstaId());
        assertThat(savedMungple.getIsParking()).isEqualTo(mungple.getIsParking());
        assertThat(savedMungple.getParkingInfo()).isEqualTo(mungple.getParkingInfo());
        assertThat(savedMungple.getResidentDogName()).isEqualTo(mungple.getResidentDogName());
        assertThat(savedMungple.getResidentDogPhoto()).isEqualTo(mungple.getResidentDogPhoto());
        assertThat(savedMungple.getRepresentMenuTitle()).isEqualTo(mungple.getRepresentMenuTitle());
        assertThat(savedMungple.getRepresentMenuPhotoUrls()).isEqualTo(mungple.getRepresentMenuPhotoUrls());
        assertThat(savedMungple.getRepresentMenuBoardPhotoUrls()).isEqualTo(mungple.getRepresentMenuBoardPhotoUrls());
        assertThat(savedMungple.getIsPriceTag()).isEqualTo(mungple.getIsPriceTag());
        assertThat(savedMungple.getPriceTagPhotoUrls()).isEqualTo(mungple.getPriceTagPhotoUrls());

        mungpleService.delete(savedMungple.getMungpleId());
    }

    @Test
    public void getAll() {
        // given

        // when
        List<Mungple> mungpleList = mungpleService.getAll();

        // then
        assertThat(mungpleList.size()).isGreaterThan(0);
        assertThat(mungpleList).extracting(Mungple::isActive).containsOnly(true);
    }

    @Test
    public void getOneByMungpleId() {
        // given
        int mungpleId = 1;

        // when
        Mungple mungple = mungpleService.getOneByMungpleId(1);

        // then
        assertThat(mungple.getMungpleId()).isEqualTo(mungpleId);
    }

    @Test
    public void getOneByMungpleId_예외처리() {
        // given
        int mungpleId = 0;

        // when

        // then
        assertThatThrownBy(() -> mungpleService.getOneByMungpleId(mungpleId))
                .isInstanceOf(NotFoundDataException.class);
    }

    @Test
    public void getOneByPlaceName() {
        // given
        String placeName = "니드스윗";

        // when
        Mungple mungple = mungpleService.getOneByPlaceName(placeName);

        // then
        assertThat(mungple.getPlaceName()).isEqualTo(placeName);
    }

    @Test
    public void getOneByPlaceName_예외처리() {
        // given
        String placeName = "에베베베베베";

        // when

        // then
        assertThatThrownBy(() -> mungpleService.getOneByPlaceName(placeName))
                .isInstanceOf(NotFoundDataException.class);
    }


    @Test
    public void getListByCategoryCode() {
        // given
        CategoryCode categoryCode = CategoryCode.CA0002;

        // when
        List<Mungple> mungpleList = mungpleService.getListByCategoryCode(categoryCode);

        // then
        assertThat(mungpleList).extracting(Mungple::getCategoryCode).containsOnly(categoryCode);
    }

    @Test
    public void getListByIds() {
        // given
        List<Integer> mungpleIdList = Arrays.asList(2, 3);

        // when
        List<Mungple> mungpleList = mungpleService.getListByIds(mungpleIdList);
        System.out.println("mungpleList = " + mungpleList);

        // then
        assertThat(mungpleList.get(0).getMungpleId()).isEqualTo(mungpleIdList.get(0));
        assertThat(mungpleList.get(1).getMungpleId()).isEqualTo(mungpleIdList.get(1));
    }

    @Test
    public void isMungpleExisting() {
        // given
        String placeName = "니드스윗";
        String address = "서울특별시 송파구 송파동 54-13";

        // when
        boolean result = mungpleService.isMungpleExisting(address, placeName);

        // then
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void delete() {
        // given
        int mungpleId = 1;

        // when
        Mungple mungple = mungpleService.getOneByMungpleId(mungpleId);
        assertThat(mungple.getMungpleId()).isEqualTo(mungpleId);

        mungpleService.delete(mungpleId);
        // then
        assertThatThrownBy(() -> mungpleService.getOneByMungpleId(mungpleId))
                .isInstanceOf(NotFoundDataException.class);

        mungpleService.save(mungple);
    }

    @Test
    public void getSortingStrategy() {
        // given
        int userId = 1;
        String latitude = "37.5101562";
        String longitude = "127.1091707";

        // when
       MungpleSortingStrategy distance = mungpleService.getSortingStrategy(MungpleSort.DISTANCE, latitude, longitude, userId);
       MungpleSortingStrategy certCount = mungpleService.getSortingStrategy(MungpleSort.CERT, latitude, longitude, userId);
       MungpleSortingStrategy bookmarkCount = mungpleService.getSortingStrategy(MungpleSort.BOOKMARK, latitude, longitude, userId);
       MungpleSortingStrategy oldest = mungpleService.getSortingStrategy(MungpleSort.OLDEST, latitude, longitude, userId);
       MungpleSortingStrategy newest = mungpleService.getSortingStrategy(MungpleSort.NEWEST, latitude, longitude, userId);
       MungpleSortingStrategy not = mungpleService.getSortingStrategy(MungpleSort.NOT, latitude, longitude, userId);

        // then
        assertThat(distance).isInstanceOf(DistanceSorting.class);
        assertThat(certCount).isInstanceOf(CertCountSorting.class);
        assertThat(bookmarkCount).isInstanceOf(BookmarkCountSorting.class);
        assertThat(oldest).isInstanceOf(OldestSorting.class);
        assertThat(newest).isInstanceOf(NewestSorting.class);
        assertThat(not).isInstanceOf(NotSorting.class);
    }
}