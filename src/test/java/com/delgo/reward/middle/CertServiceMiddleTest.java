package com.delgo.reward.middle;

import com.delgo.reward.certification.controller.port.CertService;
import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.domain.request.CertCreate;
import com.delgo.reward.certification.domain.request.CertUpdate;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.dto.comm.PageCustom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CertServiceMiddleTest {

    @Autowired
    private CertService certService;

    public String testPlaceName = "Test Place Name";
    public String testDescription = "Test Description";

    @Test
    @Transactional
    public void create를_이용하여_인증을_등록할_수_있다() {
        //given
        int userId = 434;
        String latitude = "37.5101562";
        String longitude = "127.1091707";

        CertCreate certCreate = CertCreate.builder()
                .userId(userId)
                .placeName(testPlaceName)
                .description(testDescription)
                .mungpleId(0)
                .categoryCode(CategoryCode.CA0002)
                .latitude(latitude)
                .longitude(longitude)
                .isHideAddress(false)
                .build();

        //when
        Certification savedCertification = certService.create(certCreate);

        //then
        assertThat(savedCertification.getUser().getUserId()).isEqualTo(434);
        assertThat(savedCertification.getPlaceName()).isEqualTo(testPlaceName);
        assertThat(savedCertification.getDescription()).isEqualTo(testDescription);
        assertThat(savedCertification.getUser().getUserId()).isEqualTo(434);
    }

    @Test
    @Transactional
    public void createByMungple를_이용하여_인증을_등록할_수_있다() {
        //given
        int userId = 434;
        int mungpleId = 20;
        CertCreate certCreate = CertCreate.builder()
                .userId(userId)
                .placeName(testPlaceName)
                .description(testDescription)
                .mungpleId(mungpleId)
                .categoryCode(CategoryCode.CA0002)
                .latitude("")
                .longitude("")
                .isHideAddress(false)
                .build();

        //when
        Certification savedCertification = certService.createByMungple(certCreate);

        //then
        assertThat(savedCertification.getMungpleId()).isEqualTo(mungpleId);
        assertThat(savedCertification.getDescription()).isEqualTo(testDescription);
        assertThat(savedCertification.getUser().getUserId()).isEqualTo(434);
    }


    @Test
    public void getById는_certId로_인증을_조회한다() {
        //given
        int certId = 1438;

        //when
        Certification certification = certService.getById(certId);

        //then
        assertThat(certification).isNotNull();
        assertThat(certification.getCertificationId()).isEqualTo(certId);
    }

    @Test
    public void from_조건으로_인증_리스트를_조회한다_1() {
        //given
        Boolean isCorrect = null;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.from(isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getTotalCount()).isEqualTo(page.getContent().size());
    }

    @Test
    public void  from_조건으로_Correct_인증_리스트를_조회한다_2() {
        //given
        Boolean isCorrect = true;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.from(isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getTotalCount()).isEqualTo(page.getContent().size());
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void  from_조건으로_인증_리스트를_Paging_조회한다_3() {
        //given
        Boolean isCorrect = null;
        Pageable pageable = PageRequest.of(0,3);
        CertCondition condition = CertCondition.from(isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getTotalCount()).isGreaterThan(0);
        assertThat(page.getContent().size()).isEqualTo(3);
    }

    @Test
    public void  from_조건으로_Correct_인증_리스트를_Paging_조회한다_4() {
        //given
        Boolean isCorrect = true;
        Pageable pageable = PageRequest.of(0,3);
        CertCondition condition = CertCondition.from(isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getTotalCount()).isGreaterThan(0);
        assertThat(page.getContent().size()).isEqualTo(3);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }


    @Test
    public void byUser_조건으로_특정유저가_등록한_인증_리스트를_조회한다_1() {
        //given
        int userId = 434;
        Boolean isCorrect = null;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.byUser(userId, isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getTotalCount()).isEqualTo(page.getContent().size());
        assertThat(page.getContent()).extracting(c -> c.getUser().getUserId()).containsOnly(userId);
    }

    @Test
    public void byUser_조건으로_특정유저가_등록한_Correct_인증_리스트를_조회한다_2() {
        //given
        int userId = 434;
        Boolean isCorrect = true;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.byUser(userId, isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getTotalCount()).isEqualTo(page.getContent().size());
        assertThat(page.getContent()).extracting(c -> c.getUser().getUserId()).containsOnly(userId);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void byUser_조건으로_특정유저가_등록한_인증_리스트를_Paging_조회한다_3() {
        //given
        int userId = 434;
        Boolean isCorrect = null;
        int size = 3;
        Pageable pageable = PageRequest.of(0, size);
        CertCondition condition = CertCondition.byUser(userId, isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getTotalCount()).isGreaterThan(0);
        assertThat(page.getContent().size()).isEqualTo(size);
        assertThat(page.getContent()).extracting(c -> c.getUser().getUserId()).containsOnly(userId);
    }


    @Test
    public void byUser_조건으로_특정유저가_등록한_Correct_인증_리스트를_Paging_조회한다_4() {
        //given
        int userId = 434;
        Boolean isCorrect = true;
        int size = 3;
        Pageable pageable = PageRequest.of(0, size);
        CertCondition condition = CertCondition.byUser(userId, isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getTotalCount()).isGreaterThan(0);
        assertThat(page.getContent().size()).isEqualTo(size);
        assertThat(page.getContent()).extracting(c -> c.getUser().getUserId()).containsOnly(userId);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void byMungple_조건으로_특정멍플로_등록된_인증_리스트를_조회한다_1() {
        //given
        int mungpleId = 20;
        Boolean isCorrect = null;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.byMungple(mungpleId, isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getTotalCount()).isEqualTo(page.getContent().size());
        assertThat(page.getContent()).extracting(Certification::getMungpleId).containsOnly(mungpleId);
    }

    @Test
    public void byMungple_조건으로_특정멍플로_등록된_Correct_인증_리스트를_조회한다_2() {
        //given
        int mungpleId = 20;
        Boolean isCorrect = true;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.byMungple(mungpleId, isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getTotalCount()).isEqualTo(page.getContent().size());
        assertThat(page.getContent()).extracting(Certification::getMungpleId).containsOnly(mungpleId);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void byMungple_조건으로_특정멍플로_등록된_인증_리스트를_Paging_조회한다_3() {
        // given
        int mungpleId = 20;
        Boolean isCorrect = null;
        int size = 3;
        Pageable pageable = PageRequest.of(0, size);
        CertCondition condition = CertCondition.byMungple(mungpleId, isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getContent().size()).isEqualTo(size);
        assertThat(page.getContent()).extracting(Certification::getMungpleId).containsOnly(mungpleId);
    }


    @Test
    public void byMungple_조건으로_특정멍플로_등록된_Correct_인증_리스트를_Paging_조회한다_4() {
        // given
        int mungpleId = 20;
        int size = 3;
        Boolean isCorrect = true;
        Pageable pageable = PageRequest.of(0, size);
        CertCondition condition = CertCondition.byMungple(mungpleId, isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getContent().size()).isEqualTo(size);
        assertThat(page.getContent()).extracting(Certification::getMungpleId).containsOnly(mungpleId);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void byDateAndUser은_특정유저가_특정날짜에_등록한_인증_리스트를_조회한다_1() {
        // given
        int userId = 434;
        LocalDate date = LocalDate.of(2023, 10, 19);
        Boolean isCorrect = null;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.byDateAndUser(date, userId, isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getTotalCount()).isEqualTo(page.getContent().size());
        assertThat(page.getContent()).extracting(c -> c.getUser().getUserId()).containsOnly(userId);
        assertThat(page.getContent()).extracting(c -> c.getRegistDt().toLocalDate()).containsOnly(date);
    }

    @Test
    public void byDateAndUser은_특정유저가_특정날짜에_등록한_Correct_인증_리스트를_조회한다_2() {
        // given
        int userId = 434;
        LocalDate date = LocalDate.of(2023,10,19);
        Boolean isCorrect = true;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.byDateAndUser(date, userId, isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getTotalCount()).isEqualTo(page.getContent().size());
        assertThat(page.getContent()).extracting(c -> c.getUser().getUserId()).containsOnly(userId);
        assertThat(page.getContent()).extracting(c -> c.getRegistDt().toLocalDate()).containsOnly(date);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void byDateAndUser은_특정유저가_특정날짜에_등록한_인증_리스트를_Paging_조회한다_3() {
        // given
        int userId = 434;
        LocalDate date = LocalDate.of(2023,10,19);
        Boolean isCorrect = null;
        int size = 3;
        Pageable pageable = PageRequest.of(0, 3);
        CertCondition condition = CertCondition.byDateAndUser(date, userId, isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getContent().size()).isEqualTo(size);
        assertThat(page.getContent()).extracting(c -> c.getUser().getUserId()).containsOnly(userId);
        assertThat(page.getContent()).extracting(c -> c.getRegistDt().toLocalDate()).containsOnly(date);
    }


    @Test
    public void byDateAndUser은_특정유저가_특정날짜에_등록한_Correct_인증_리스트를_Paging_조회한다_4() {
        // given
        int userId = 434;
        LocalDate date = LocalDate.of(2023,10,19);
        Boolean isCorrect = true;
        int size = 3;
        Pageable pageable = PageRequest.of(0, 3);
        CertCondition condition = CertCondition.byDateAndUser(date, userId, isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getContent().size()).isEqualTo(size);
        assertThat(page.getContent()).extracting(c -> c.getUser().getUserId()).containsOnly(userId);
        assertThat(page.getContent()).extracting(c -> c.getRegistDt().toLocalDate()).containsOnly(date);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }



    @Test
    @Transactional
    public void update를_사용하여_인증을_수정할_수_있다() {
        //given
        int userId = 343;
        int certificationId = 1438;
        Boolean isHideAddress = true;

        CertUpdate certUpdate = CertUpdate.builder()
                .userId(userId)
                .certificationId(certificationId)
                .description(testDescription)
                .isHideAddress(isHideAddress)
                .build();
        //when

        Certification updatedCertification = certService.update(certUpdate);

        //then
        assertThat(updatedCertification.getIsHideAddress()).isEqualTo(isHideAddress);
        assertThat(updatedCertification.getDescription()).isEqualTo(testDescription);
    }

    @Test
    @Transactional
    public void delete를_사용하여_인증을_삭제할_수_있다() {
        //given
        int certificationId = 1438;

        //when
        certService.delete(certificationId);

        //then
        assertThatThrownBy(() -> certService.getById(certificationId))
                .isInstanceOf(NullPointerException.class);
    }
}
