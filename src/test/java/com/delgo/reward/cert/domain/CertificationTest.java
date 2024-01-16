package com.delgo.reward.cert.domain;

import com.delgo.reward.cert.controller.request.CertCreate;
import com.delgo.reward.cert.controller.request.CertUpdate;
import com.delgo.reward.code.domain.Code;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.user.domain.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CertificationTest {

    @Test
    void from() {
        // given
        CertCreate certCreate = CertCreate.builder()
                .placeName("test place name")
                .mungpleId(0)
                .categoryCode(CategoryCode.CA0002)
                .description("test description")
                .latitude("123.123")
                .longitude("124.124")
                .isHideAddress(false)
                .build();
        String address = "test address";
        Code code = Code.builder()
                .code("101000")
                .pCode("101000")
                .build();
        User user = User.builder()
                .userId(1)
                .roles("ROLE_USER")
                .build();

        // when
        Certification certification = Certification.from(certCreate, address, code, user);

        // then
        assertThat(certification.getPlaceName()).isEqualTo(certCreate.placeName());
        assertThat(certification.getCategoryCode()).isEqualTo(certCreate.categoryCode());
        assertThat(certification.getDescription()).isEqualTo(certCreate.description());
        assertThat(certification.getLatitude()).isEqualTo(certCreate.latitude());
        assertThat(certification.getLongitude()).isEqualTo(certCreate.longitude());
        assertThat(certification.getIsHideAddress()).isEqualTo(certCreate.isHideAddress());
        assertThat(certification.getAddress()).isEqualTo(address);
        assertThat(certification.getGeoCode()).isEqualTo(code.getCode());
        assertThat(certification.getPGeoCode()).isEqualTo(code.getPCode());
        assertThat(certification.getUser()).isEqualTo(user);
        assertThat(certification.getIsExpose()).isEqualTo(true);
        assertThat(certification.getIsCorrect()).isEqualTo(true);
        assertThat(certification.getCommentCount()).isEqualTo(0);
        assertThat(certification.getMungpleId()).isEqualTo(certCreate.mungpleId());
    }

    @Test
    void fromByMungple() {
        // given
        CertCreate certCreate = CertCreate.builder()
                .description("test description")
                .isHideAddress(false)
                .build();
        Mungple mungple = Mungple.builder()
                .placeName("test place name")
                .categoryCode(CategoryCode.CA0002)
                .jibunAddress("경기도 성남시 분당구")
                .geoCode("101000")
                .pGeoCode("102000")
                .latitude("123.123")
                .longitude("124.124")
                .build();
        User user = User.builder()
                .userId(1)
                .roles("ROLE_USER")
                .build();

        // when
        Certification certification = Certification.from(certCreate, mungple, user);

        // then
        assertThat(certification.getMungpleId()).isEqualTo(mungple.getMungpleId());
        assertThat(certification.getPlaceName()).isEqualTo(mungple.getPlaceName());
        assertThat(certification.getCategoryCode()).isEqualTo(mungple.getCategoryCode());
        assertThat(certification.getDescription()).isEqualTo(certCreate.description());
        assertThat(certification.getLatitude()).isEqualTo(mungple.getLatitude());
        assertThat(certification.getLongitude()).isEqualTo(mungple.getLongitude());
        assertThat(certification.getIsHideAddress()).isEqualTo(certCreate.isHideAddress());
        assertThat(certification.getAddress()).isEqualTo(mungple.formattedAddress());
        assertThat(certification.getGeoCode()).isEqualTo(mungple.getGeoCode());
        assertThat(certification.getPGeoCode()).isEqualTo(mungple.getPGeoCode());
        assertThat(certification.getUser()).isEqualTo(user);
        assertThat(certification.getIsExpose()).isEqualTo(true);
        assertThat(certification.getIsCorrect()).isEqualTo(true);
        assertThat(certification.getCommentCount()).isEqualTo(0);
    }

    @Test
    void update() {
        // given
        CertUpdate certUpdate = CertUpdate.builder()
                .userId(1)
                .certificationId(1)
                .description("test update")
                .isHideAddress(true)
                .build();

        Certification certification = Certification.builder()
                .user(new User()) // 임시 User 객체, 필요에 따라 세부 속성 설정
                .categoryCode(CategoryCode.CA0002)
                .mungpleId(1) // 예시 ID
                .placeName("임시 장소 이름")
                .description("임시 설명")
                .address("임시 주소")
                .geoCode("GEO123") // 예시 지오코드
                .pGeoCode("PGEO456") // 예시 부모 지오코드
                .latitude("37.5665") // 예시 위도
                .longitude("126.9780") // 예시 경도
                .isCorrect(true)
                .isHideAddress(false)
                .commentCount(0)
                .isExpose(true) // 또는 false, 상황에 따라 설정
                .build();

        // when
        Certification updatedCertification = certification.update(certUpdate);

        // then
        assertThat(updatedCertification.getDescription()).isEqualTo(certUpdate.description());
        assertThat(updatedCertification.getIsHideAddress()).isEqualTo(certUpdate.isHideAddress());
    }

    @Test
    void getThumbnailUrl() {
        // given
        Certification certification = Certification.builder()
                .photos(List.of("test photo url1", "test photo url2"))
                .build();
        // when

        String photo = certification.getThumbnailUrl();

        // then
        assertThat(photo).isEqualTo(certification.getPhotos().get(0));
    }

    @Test
    void getUserId() {
        // given
        Certification certification = Certification.builder()
                .user(User.builder().userId(2).build())
                .build();

        // when
        int userId = certification.getUserId();

        // then
        assertThat(userId).isEqualTo(certification.getUser().getUserId());
    }

    @Test
    void setCategoryCode() {
        // given
        CategoryCode categoryCode = CategoryCode.CA0002;
        Certification certification = Certification.builder()
                .categoryCode(CategoryCode.CA0003)
                .build();

        // when
        certification.setCategoryCode(categoryCode);

        // then
        assertThat(certification.getCategoryCode()).isEqualTo(categoryCode);
    }

    @Test
    void setMungpleId() {
        // given
        int mungpleId = 2;
        Certification certification = Certification.builder()
                .mungpleId(1)
                .build();

        // when
        certification.setMungpleId(mungpleId);

        // then
        assertThat(certification.getMungpleId()).isEqualTo(mungpleId);
    }

    @Test
    void setIsCorrect() {
        // given
        boolean isCorrect = false;
        Certification certification = Certification.builder()
                .isCorrect(true)
                .build();

        // when
        certification.setIsCorrect(isCorrect);

        // then
        assertThat(certification.getIsCorrect()).isEqualTo(isCorrect);
    }

    @Test
    void setCommentCount() {
        // given
        int commentCount = 4;
        Certification certification = Certification.builder()
                .commentCount(6)
                .build();

        // when
        certification.setCommentCount(commentCount);

        // then
        assertThat(certification.getCommentCount()).isEqualTo(commentCount);
    }

    @Test
    void setPhotos() {
        // given
        List<String> photos = List.of("photo1", "photo2");
        Certification certification = Certification.builder()
                .photos(new ArrayList<>())
                .build();

        // when
        certification.setPhotos(photos);

        // then
        assertThat(certification.getPhotos().size()).isEqualTo(2);
    }


    @Test
    void NoArgsConstructor(){
        // given

        // when
        Certification certification = new Certification();

        // then
        assertThat(certification).isNotNull();
    }
}