package com.delgo.reward.cert.service;

import com.delgo.reward.cert.controller.request.CertCreate;
import com.delgo.reward.cert.controller.request.CertUpdate;
import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.exception.NotFoundDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CertCommandServiceTest {
    @Autowired
    CertCommandService certCommandService;
    @Autowired
    CertQueryService certQueryService;

    @Test
    @Transactional
    void save() {
        // given
        Certification certification = Certification.builder()
                .certificationId(1)
                .build();

        // when
        Certification savedCertification = certCommandService.save(certification);

        // then
        assertThat(savedCertification.getCertificationId()).isEqualTo(certification.getCertificationId());
    }

    @Test
    @Transactional
    void create() {
        // given
        CertCreate certCreate = CertCreate.builder()
                .userId(332)
                .placeName("test place name")
                .mungpleId(0)
                .categoryCode(CategoryCode.CA0002)
                .description("test description")
                .latitude("37.5101562")
                .longitude("127.1091707")
                .isHideAddress(false)
                .build();

        // when
        Certification certification = certCommandService.create(certCreate);

        // then
        assertThat(certification.getUser().getUserId()).isEqualTo(certCreate.userId());
        assertThat(certification.getAddress()).isEqualTo("서울특별시 송파구 송파동");
        assertThat(certification.getGeoCode()).isEqualTo("101180");
        assertThat(certification.getPGeoCode()).isEqualTo("101000");
    }

    @Test
    @Transactional
    void createByMungple() {
        // given
        CertCreate certCreate = CertCreate.builder()
                .userId(332)
                .mungpleId(1)
                .description("test description")
                .isHideAddress(false)
                .build();

        // when
        Certification certification = certCommandService.createByMungple(certCreate);

        // then
        assertThat(certification.getUser().getUserId()).isEqualTo(certCreate.userId());
        assertThat(certification.getMungpleId()).isEqualTo(certCreate.mungpleId());
    }

    @Test
    @Transactional
    void update() {
        // given
        CertUpdate certUpdate = CertUpdate.builder()
                .userId(1)
                .certificationId(1)
                .description("test update")
                .isHideAddress(true)
                .build();

        // when
        Certification certification = certCommandService.update(certUpdate);

        // then
        assertThat(certification.getDescription()).isEqualTo(certUpdate.description());
        assertThat(certification.getIsHideAddress()).isEqualTo(certUpdate.isHideAddress());
    }

    @Test
    @Transactional
    void changeIsCorrect() {
        // given
        int certificationId = 1;
        boolean isCorrect = false;
        Certification certification = certQueryService.getOneById(certificationId);

        // when
        certCommandService.changeIsCorrect(certificationId, isCorrect);

        // then
        assertThat(certification.getIsCorrect()).isEqualTo(isCorrect);
    }

    @Test
    @Transactional
    void increaseCommentCount() {
        // given
        int certificationId = 1;
        Certification certification = certQueryService.getOneById(certificationId);
        int commentCount = certification.getCommentCount();

        // when
        certCommandService.increaseCommentCount(certificationId);

        // then
        assertThat(certification.getCommentCount()).isEqualTo(commentCount + 1);
    }

    @Test
    @Transactional
    void setPhotos() {
        // then
        int certificationId = 1;
        List<String> photos = List.of("photo1", "photo2");

        // when
        certCommandService.setPhotos(certificationId, photos);

        // then
        Certification certification = certQueryService.getOneById(certificationId);
        assertThat(certification.getPhotos()).isEqualTo(photos);
    }

    @Test
    @Transactional
    void delete() {
        // given
        int certificationId = 1;

        // when
        certCommandService.delete(certificationId);

        // then
        assertThatThrownBy(() -> {
            certQueryService.getOneById(certificationId);
        }).isInstanceOf(NotFoundDataException.class);
    }

    @Test
    @Transactional
    void deleteByUserId() {
        // given
        int userId = 1;

        // when
        certCommandService.deleteByUserId(userId);

        // then
        Page<Certification> certificationList = certQueryService.getPagingListByUserId(userId, Pageable.unpaged());
        assertThat(certificationList.getContent().size()).isEqualTo(0);
    }
}