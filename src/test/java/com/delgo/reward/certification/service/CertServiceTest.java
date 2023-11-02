package com.delgo.reward.certification.service;

import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.domain.request.CertUpdate;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.comm.PageCustom;
import com.delgo.reward.fake.FakeCertRepositoryImpl;
import com.delgo.reward.certification.domain.CertCondition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


public class CertServiceTest {
    CertService certService;


    // SetUp에서 사용
    private Certification createCertification(int certificationId, User user, int mungpleId, boolean isCorrect, LocalDate date) {
        return Certification.builder()
                .certificationId(certificationId)
                .description("Test Description")
                .user(user)
                .mungpleId(mungpleId)
                .isCorrect(isCorrect)
                .isHideAddress(false)
                .registDt(date.atStartOfDay())
                .build();
    }

    @Before
    public void setUp() {
        User user1 = User.builder()
                .userId(1)
                .name("Test User")
                .pet(Pet.builder().petId(1).build())
                .build();

        User user2 = User.builder()
                .userId(2)
                .name("Test User")
                .pet(Pet.builder().petId(2).build())
                .build();

        List<Certification> initCertList = new ArrayList<>();
        initCertList.add(createCertification(1, user1, 0, true, LocalDate.now()));
        initCertList.add(createCertification(2, user1, 0, false, LocalDate.now()));
        initCertList.add(createCertification(3, user1, 2, true, LocalDate.now()));
        initCertList.add(createCertification(4, user1, 2, false, LocalDate.now()));
        initCertList.add(createCertification(5, user2, 0, true, LocalDate.now()));
        initCertList.add(createCertification(6, user2, 0, false, LocalDate.now()));
        initCertList.add(createCertification(7, user2, 2, true, LocalDate.now().plusDays(1)));
        initCertList.add(createCertification(8, user2, 2, false, LocalDate.now().plusDays(1)));

        certService = CertService.builder()
                .certRepository(new FakeCertRepositoryImpl(initCertList))
                .objectStorageService(Mockito.mock(ObjectStorageService.class))
//        .UserService()
//        .CodeService()
//        .MongoMungpleService()
//        .GeoDataPort()
                .build();
    }

    @Test
    public void create를_이용하여_인증을_등록할_수_있다() {
//        //given
//        int userId = 434;
//        String latitude = "37.5101562";
//        String longitude = "127.1091707";
//
//        CertCreate certCreate = CertCreate.builder()
//                .userId(userId)
//                .placeName(testPlaceName)
//                .description(testDescription)
//                .mungpleId(0)
//                .categoryCode(CategoryCode.CA0002)
//                .latitude(latitude)
//                .longitude(longitude)
//                .isHideAddress(false)
//                .build();
//
//        //when
//        Certification savedCertification = certService.create(certCreate);
//
//        //then
//        assertThat(savedCertification.getUser().getUserId()).isEqualTo(434);
//        assertThat(savedCertification.getPlaceName()).isEqualTo(testPlaceName);
//        assertThat(savedCertification.getDescription()).isEqualTo(testDescription);
//        assertThat(savedCertification.getUser().getUserId()).isEqualTo(434);
    }

    @Test
    public void createByMungple를_이용하여_인증을_등록할_수_있다() {
//        //given
//        int userId = 434;
//        int mungpleId = 20;
//        CertCreate certCreate = CertCreate.builder()
//                .userId(userId)
//                .placeName(testPlaceName)
//                .description(testDescription)
//                .mungpleId(mungpleId)
//                .categoryCode(CategoryCode.CA0002)
//                .latitude("")
//                .longitude("")
//                .isHideAddress(false)
//                .build();
//
//        //when
//        Certification savedCertification = certService.createByMungple(certCreate);
//
//        //then
//        assertThat(savedCertification.getMungpleId()).isEqualTo(mungpleId);
//        assertThat(savedCertification.getDescription()).isEqualTo(testDescription);
//        assertThat(savedCertification.getUser().getUserId()).isEqualTo(434);
    }


    @Test
    public void getById는_certId로_인증을_조회할_수_있다() {
        //given
        int certId = 1;

        //when
        Certification certification = certService.getById(certId);

        //then
        assertThat(certification).isNotNull();
        assertThat(certification.getCertificationId()).isEqualTo(certId);
    }

    @Test
    public void getById는_certId가_없을_경우_Exception이_발생시킨다() {
        //given
        int certificationId = 10;

        //when & then
        assertThatThrownBy(() -> certService.getById(certificationId))
                .isInstanceOf(NoSuchElementException.class);
    }


    @Test
    public void getListByCondition_전체_인증리스트를_조회할_수_있다() {
        //given
        Boolean isCorrect = null;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.from(isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page.getContent().size()).isGreaterThan(0);
        assertThat(page.getTotalCount()).isEqualTo(page.getContent().size());
    }

    @Test
    public void  getListByCondition_전체_올바른_인증리스트를_조회할_수_있다() {
        //given
        Boolean isCorrect = true;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.from(isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page.getContent().size()).isGreaterThan(0);
        assertThat(page.getTotalCount()).isEqualTo(page.getContent().size());
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void  getListByCondition_페이징으로_리스트를_조회할_수_있다() {
        //given
        Boolean isCorrect = null;
        Pageable pageable = PageRequest.of(0,3);
        CertCondition condition = CertCondition.from(isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page.getContent().size()).isGreaterThan(0);
        assertThat(page.getTotalCount()).isGreaterThan(0);
        assertThat(page.getContent().size()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getSize()).isEqualTo(3);
        assertThat(page.isLast()).isEqualTo(false);
    }

    @Test
    public void  getListByCondition_페이징으로_마지막_리스트를_조회할_수_있다() {
        //given
        Boolean isCorrect = null;
        Pageable pageable = PageRequest.of(2,3);
        CertCondition condition = CertCondition.from(isCorrect, pageable);

        //when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        //then
        assertThat(page.getContent().size()).isGreaterThan(0);
        assertThat(page.getTotalCount()).isGreaterThan(0);
        assertThat(page.getNumber()).isEqualTo(2);
        assertThat(page.getSize()).isEqualTo(3);
        assertThat(page.isLast()).isEqualTo(true);
    }

    @Test
    public void getListByCondition_특정_유저가_등록한_인증리스트를_조회할_수_있다() {
        //given
        int userId = 1;
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
    public void getListByCondition_특정_유저가_등록한_올바른_인증리스트를_페이징으로_조회할_수_있다() {
        //given
        int userId = 1;
        Boolean isCorrect = true;
        int size = 2;
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
    public void getListByCondition_멍플_인증리스트를_조회할_수_있다() {
        //given
        int mungpleId = 2;
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
    public void getListByCondition_올바른_멍플_인증리스트를_페이징_조회할_수_있다() {
        // given
        int mungpleId = 2;
        int size = 2;
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
    public void getListByCondition_특정유저가_특정날짜에_등록한_인증리스트를_조회할_수_있다() {
        // given
        int userId = 1;
        LocalDate date = LocalDate.now();
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
    public void getListByCondition_특정유저가_특정날짜에_등록한_올바른_인증리스트를_페이징_조회할_수_있다() {
        // given
        int userId = 1;
        LocalDate date = LocalDate.now();
        Boolean isCorrect = true;
        int size = 2;
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
    public void update를_사용하여_인증을_수정할_수_있다() {
        //given
        int userId = 1;
        int certificationId = 2;
        Boolean isHideAddress = true;
        String desc = "change test";

        CertUpdate certUpdate = CertUpdate.builder()
                .userId(userId)
                .certificationId(certificationId)
                .description(desc)
                .isHideAddress(isHideAddress)
                .build();

        //when
        Certification updatedCertification = certService.update(certUpdate);

        //then
        assertThat(updatedCertification.getIsHideAddress()).isEqualTo(isHideAddress);
        assertThat(updatedCertification.getDescription()).isEqualTo(desc);
    }


    @Test
    public void delete를_사용하여_인증을_삭제할_수_있다() {
        //given
        int certificationId = 8;

        //when
        certService.delete(certificationId);

        //then
        assertThatThrownBy(() -> certService.getById(certificationId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void validate를_이용하여_유저가_권한이_있는경우_True를_반환한다() {
        //given
        int userId = 1;
        int certificationId = 3;

        //when
        Boolean result = certService.validate(userId, certificationId);

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void validate를_이용하여_유저가_권한이_없다면_false를_반환한다() {
        //given
        int userId = 1;
        int certificationId = 7;

        //when
        Boolean result = certService.validate(userId, certificationId);

        //then
        assertThat(result).isEqualTo(false);
    }
}
