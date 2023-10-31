package com.delgo.reward.certification.domain;

import com.delgo.reward.certification.domain.request.CertCreate;
import com.delgo.reward.certification.domain.request.CertUpdate;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.code.Code;
import com.delgo.reward.domain.user.User;

import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CertificationTest {
    @Test
    public void CertCreate_으로_일반_인증을_만들_수_있다() {
        // given
        int userId = 1;
        String placeName = "Test Place";
        String description = "Test Description";
        String latitude = "37.123456";
        String longitude = "127.123456";
        String address = "서울특별시 중구 태평로1가 31";
        Code geoCode = Code.builder().code("101600").pCode("101000").build();
        CertCreate certCreate = CertCreate.builder()
                .userId(userId)
                .placeName(placeName)
                .description(description)
                .mungpleId(0)
                .categoryCode(CategoryCode.CA0002)
                .latitude(latitude)
                .longitude(longitude)
                .isHideAddress(false)
                .build();

        User user = User.builder()
                .userId(userId)
                .name("Test User")
                .build();

        // when
        Certification certification = Certification.from(certCreate, address, geoCode, user);

        // then
        assertThat(certification.getCertificationId()).isNull(); // Jpa에서 자동생성.
        assertThat(certification.getRegistDt()).isNull(); // Jpa에서 자동생성.
        assertThat(certification.getUser().getUserId()).isEqualTo(userId);
        assertThat(certification.getPlaceName()).isEqualTo(placeName);
        assertThat(certification.getDescription()).isEqualTo(description);
        assertThat(certification.getMungpleId()).isEqualTo(0);
        assertThat(certification.getCategoryCode()).isEqualTo(CategoryCode.CA0002);
        assertThat(certification.getAddress()).isEqualTo(address);
        assertThat(certification.getGeoCode()).isEqualTo(geoCode.getCode());
        assertThat(certification.getPGeoCode()).isEqualTo(geoCode.getPCode());
        assertThat(certification.getLatitude()).isEqualTo(latitude);
        assertThat(certification.getLongitude()).isEqualTo(longitude);
        assertThat(certification.getIsHideAddress()).isEqualTo(false);

        assertThat(certification.getIsCorrect()).isEqualTo(true); // true 가 기본 값
        assertThat(certification.getCommentCount()).isEqualTo(0); // 0이 기본 값

    }

    @Test
    public void CertCreate_으로_멍플_인증을_만들_수_있다() {
        // given
        int userId = 1;
        int mungpleId = 20 ;
        String description = "Test Description";
        CertCreate certCreate = CertCreate.builder()
                .userId(userId)
                .description(description)
                .mungpleId(mungpleId)
                .isHideAddress(false)
                .build();

        User user = User.builder()
                .userId(userId)
                .name("Test User")
                .build();

        MongoMungple mungple = MongoMungple.builder()
                .mungpleId(mungpleId)
                .categoryCode(CategoryCode.CA0002)
                .phoneNo("02-8484-7846")
                .placeName("니드스윗")
                .placeNameEn("need sweet")
                .roadAddress("서울특별시 송파구 백제고분로45길 22-1")
                .jibunAddress("서울특별시 송파구 송파동 54-13")
                .geoCode("101180")
                .pGeoCode("101000")
                .latitude("37.5101562")
                .longitude("127.1091707")
                .build();

        // when
        Certification certification = Certification.from(certCreate, mungple, user);

        // then
        assertThat(certification.getCertificationId()).isNull(); // Jpa에서 자동생성.
        assertThat(certification.getRegistDt()).isNull(); // Jpa에서 자동생성.
        assertThat(certification.getUser().getUserId()).isEqualTo(userId);
        assertThat(certification.getPlaceName()).isEqualTo(mungple.getPlaceName());
        assertThat(certification.getDescription()).isEqualTo(description);
        assertThat(certification.getMungpleId()).isEqualTo(mungpleId);
        assertThat(certification.getCategoryCode()).isEqualTo(mungple.getCategoryCode());
        assertThat(certification.getAddress()).isEqualTo(mungple.formattedAddress());
        assertThat(certification.getGeoCode()).isEqualTo(mungple.getGeoCode());
        assertThat(certification.getPGeoCode()).isEqualTo(mungple.getPGeoCode());
        assertThat(certification.getLatitude()).isEqualTo(mungple.getLatitude());
        assertThat(certification.getLongitude()).isEqualTo(mungple.getLongitude());
        assertThat(certification.getIsHideAddress()).isEqualTo(false);

        assertThat(certification.getIsCorrect()).isEqualTo(true); // true 가 기본 값
        assertThat(certification.getCommentCount()).isEqualTo(0); // 0이 기본 값
    }

    @Test
    public void CertUpdate_으로_멍플_인증을_수정할_수_있다() {
        // given
        int userId = 343;
        int certificationId = 1438;
        Boolean isHideAddress = true;

        CertUpdate certUpdate = CertUpdate.builder()
                .userId(userId)
                .certificationId(certificationId)
                .description("test description")
                .isHideAddress(isHideAddress)
                .build();

        Certification certification = Certification.builder()
                .certificationId(certificationId)
                .description("test description2")
                .isHideAddress(false)
                .build();

        // when
        Certification post = certification.update(certUpdate);

        // then
        assertThat(post.getDescription()).isEqualTo("test description");
        assertThat(post.getIsHideAddress()).isEqualTo(isHideAddress);
    }

    @Test
    public void setIsCorrect로_isCorrect_값을_변경할_수_있다(){
        // given
        int certificationId = 1438;
        Certification certification = Certification.builder()
                .certificationId(certificationId)
                .isCorrect(true)
                .build();

        // when
        certification.setIsCorrect(false);

        // then
        assertThat(certification.getIsCorrect()).isEqualTo(false);
    }

    @Test
    public void setCommentCount로_commentCount_값을_변경할_수_있다(){
        // given
        int certificationId = 1438;
        Certification certification = Certification.builder()
                .certificationId(certificationId)
                .commentCount(0)
                .build();

        // when
        certification.setCommentCount(10);

        // then
        assertThat(certification.getCommentCount()).isEqualTo(10);
    }
}
