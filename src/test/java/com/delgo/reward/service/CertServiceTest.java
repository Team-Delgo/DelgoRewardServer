package com.delgo.reward.service;

import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.service.CertService;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.comm.Page;
import com.delgo.reward.fake.FakeCertRepositoryImpl;
import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.certification.service.port.CertRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class CertServiceTest {

    CertService certService;
    List<Certification> initCertList;

    @Before
    public void setUp() {
        // userId 다른 최소 2개의 인증
        // mungpleId 0 또는 상수 2개의 인증
        // date 다른 2개의 인증
        // isCorrect = false, true인 인증
        initCertList = new ArrayList<Certification>();
        Pet pet1 = Pet.builder()
                .petId(1)
                .build();
        User user1 = User.builder()
                .userId(1)
                .name("Test User")
                .pet(pet1)
                .build();

        Pet pet2 = Pet.builder()
                .petId(2)
                .build();
        User user2 = User.builder()
                .userId(2)
                .name("Test User")
                .pet(pet2)
                .build();


        // user 1 인증 & mungpleId 0 & isCorrect true
        Certification certification_user1_1 = Certification.builder()
                .certificationId(1)
                .user(user1)
                .mungpleId(0)
                .isCorrect(true)
                .build();
        // user 1 인증 & mungpleId 0 & isCorrect false
        Certification certification_user1_2 = Certification.builder()
                .certificationId(2)
                .user(user1)
                .mungpleId(0)
                .isCorrect(false)
                .build();
        // user 1 인증 & mungpleId 2 & isCorrect true
        Certification certification_user1_3 = Certification.builder()
                .certificationId(3)
                .user(user1)
                .mungpleId(2)
                .isCorrect(true)
                .build();
        // user 1 인증 & mungpleId 2 & isCorrect false
        Certification certification_user1_4 = Certification.builder()
                .certificationId(4)
                .user(user1)
                .mungpleId(2)
                .isCorrect(false)
                .certificationId(2)
                .build();

        // user 1 인증 & mungpleId 0 & isCorrect true
        Certification certification_user2_1 = Certification.builder()
                .certificationId(5)
                .user(user2)
                .mungpleId(0)
                .isCorrect(true)
                .build();
        // user 1 인증 & mungpleId 0 & isCorrect false
        Certification certification_user2_2 = Certification.builder()
                .certificationId(6)
                .user(user2)
                .mungpleId(0)
                .isCorrect(false)
                .build();
        // user 1 인증 & mungpleId 2 & isCorrect true
        Certification certification_user2_3 = Certification.builder()
                .certificationId(7)
                .user(user2)
                .mungpleId(2)
                .isCorrect(true)
                .build();
        // user 1 인증 & mungpleId 2 & isCorrect false
        Certification certification_user2_4 = Certification.builder()
                .certificationId(8)
                .user(user2)
                .mungpleId(2)
                .isCorrect(false)
                .build();

        initCertList.add(certification_user1_1);
        initCertList.add(certification_user1_2);
        initCertList.add(certification_user1_3);
        initCertList.add(certification_user1_4);
        initCertList.add(certification_user2_1);
        initCertList.add(certification_user2_2);
        initCertList.add(certification_user2_3);
        initCertList.add(certification_user2_4);

//        for(Certification c : initCertList) {
//            System.out.println("c.getCertificationId() = " + c.getCertificationId());
//            System.out.println("c.getUserId() = " + c.getUser().getUserId());
//            System.out.println("****************");
//        }

        CertRepository repository = new FakeCertRepositoryImpl(initCertList);
        certService = CertService.builder()
                .certRepository(repository)
                .build();
    }

    @Test
    public void 일반_인증_등록에_성공한다() {
        //given

        //when

        //then
    }

    @Test
    public void 멍플_인증_등록에_성공한다() {
        //given

        //when

        //then
    }

    @Test
    public void CertID_로_인증_단_건_조회에_성공한다() {
        //given

        //when

        //then
    }

    @Test
    public void CertID_로_인증_단_건_조회에_실패한다() {
        //given

        //when

        //then
    }

    @Test
    public void 전체_인증_리스트를_조회한다() {
        //given
        CertCondition condition = CertCondition.builder()
                .pageable(Pageable.unpaged())
                .build();

        //when
        Page<Certification> page = certService.getListByCondition(condition);

        System.out.println("page.totalCount = " + page.getTotalCount());
        //then
        assertThat(page.getContent().size()).isEqualTo(initCertList.size());
        assertThat(page.getTotalCount()).isEqualTo(page.getContent().size());
    }

    @Test
    public void 전체_올바른_인증_리스트를_조회한다() {
        //given
        CertCondition condition = CertCondition.builder()
                .isCorrect(true)
                .pageable(Pageable.unpaged())
                .build();

        //when
        Page<Certification> page = certService.getListByCondition(condition);
        System.out.println("page.totalCount = " + page.getTotalCount());

        //then
        assertThat(page.getTotalCount()).isEqualTo(4);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void 전체_인증_리스트를_조회한다_Paging() {
        //given
        Pageable pageable = PageRequest.of(0,2);
        CertCondition condition = CertCondition.builder()
                .pageable(pageable)
                .build();

        //when
        Page<Certification> page = certService.getListByCondition(condition);
        System.out.println("page.totalCount = " + page.getTotalCount());

        //then
        assertThat(page.getTotalCount()).isEqualTo(8);
        assertThat(page.getContent().size()).isEqualTo(2);
    }

    @Test
    public void 올바른_인증_리스트를_조회한다_Paging() {
        //given
        Pageable pageable = PageRequest.of(0,2);
        CertCondition condition = CertCondition.builder()
                .isCorrect(true)
                .pageable(pageable)
                .build();

        //when
        Page<Certification> page = certService.getListByCondition(condition);
        System.out.println("page.totalCount = " + page.getTotalCount());

        //then
        assertThat(page.getTotalCount()).isEqualTo(4);
        assertThat(page.getContent().size()).isEqualTo(2);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void User_인증_리스트를_조회한다() {
        //given
        int userId = 1;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.builder()
                .userId(1)
                .pageable(pageable)
                .build();

        //when
        Page<Certification> page = certService.getListByCondition(condition);
        System.out.println("page.totalCount = " + page.getTotalCount());

        //then
        assertThat(page.getTotalCount()).isEqualTo(4);
        assertThat(page.getContent()).extracting(c-> c.getUser().getUserId()).containsOnly(userId);
    }

    @Test
    public void User_Correct_인증_리스트를_조회한다() {
        //given
        int userId = 1;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.builder()
                .userId(userId)
                .isCorrect(true)
                .pageable(pageable)
                .build();

        //when
        Page<Certification> page = certService.getListByCondition(condition);
        System.out.println("page.totalCount = " + page.getTotalCount());

        //then
        assertThat(page.getTotalCount()).isEqualTo(2);
        assertThat(page.getContent()).extracting(c-> c.getUser().getUserId()).containsOnly(userId);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void User_인증_리스트를_조회한다_Paging() {
        //given
        int userId = 1;
        Pageable pageable = PageRequest.of(0,1);
        CertCondition condition = CertCondition.builder()
                .userId(userId)
                .pageable(pageable)
                .build();

        //when
        Page<Certification> page = certService.getListByCondition(condition);
        System.out.println("page.totalCount = " + page.getTotalCount());

        //then
        assertThat(page.getTotalCount()).isEqualTo(4);
        assertThat(page.getContent().size()).isEqualTo(1);
        assertThat(page.getContent()).extracting(c-> c.getUser().getUserId()).containsOnly(userId);

    }

    @Test
    public void User_Correct_인증_리스트를_조회한다_Paging() {
        //given
        int userId = 1;
        Pageable pageable = PageRequest.of(0,1);
        CertCondition condition = CertCondition.builder()
                .userId(userId)
                .isCorrect(true)
                .pageable(pageable)
                .build();

        //when
        Page<Certification> page = certService.getListByCondition(condition);
        System.out.println("page.totalCount = " + page.getTotalCount());

        //then
        assertThat(page.getTotalCount()).isEqualTo(2);
        assertThat(page.getContent().size()).isEqualTo(1);
        assertThat(page.getContent()).extracting(c-> c.getUser().getUserId()).containsOnly(userId);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void Mungple_Correct_인증_리스트를_조회한다() {
        //given
        int mungpleId = 2;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.builder()
                .mungpleId(mungpleId)
                .isCorrect(true)
                .pageable(pageable)
                .build();

        //when
        Page<Certification> page = certService.getListByCondition(condition);
        System.out.println("page.totalCount = " + page.getTotalCount());

        //then
        assertThat(page.getTotalCount()).isEqualTo(2);
        assertThat(page.getContent()).extracting(Certification::getMungpleId).containsOnly(mungpleId);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void Mungple_Correct_인증_리스트를_조회한다_Paging() {
        //given
        int mungpleId = 2;
        Pageable pageable = PageRequest.of(0,1);
        CertCondition condition = CertCondition.builder()
                .mungpleId(mungpleId)
                .isCorrect(true)
                .pageable(pageable)
                .build();

        //when
        Page<Certification> page = certService.getListByCondition(condition);
        System.out.println("page.totalCount = " + page.getTotalCount());

        //then
        assertThat(page.getTotalCount()).isEqualTo(2);
        assertThat(page.getContent().size()).isEqualTo(1);
        assertThat(page.getContent()).extracting(Certification::getMungpleId).containsOnly(mungpleId);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);

    }

    @Test
    public void Date_인증_리스트를_조회한다() {
        // TODO 작성해야 함.
//        //given
//        LocalDate date = LocalDate.of(2023,5,7);
//        Pageable pageable = Pageable.unpaged();
//        CertCondition condition = CertCondition.builder()
//                .date(date)
//                .pageable(pageable)
//                .build();
//
//        //when
//        PageDTO<Certification> page = certService.getListByCondition(condition);
//        System.out.println("page.totalCount = " + page.getTotalCount());
//
//        //then
////        assertThat(page.getContent().size()).isEqualTo(2);
//        assertThat(page.getContent())
//                .extracting(Certification::getRegistDt)
//                .map(LocalDateTime::toLocalDate)
//                .containsOnly(date);
//        assertThat(page.getTotalCount()).isGreaterThan(0);
    }

    @Test
    public void Date_User_인증_리스트를_조회한다() {
        // TODO 작성해야 함.
        //given
//        int userId = 300;
//        LocalDate date = LocalDate.of(2023,5,7);
//        Pageable pageable = Pageable.unpaged();
//        CertCondition condition = CertCondition.builder()
//                .userId(userId)
//                .date(date)
//                .pageable(pageable)
//                .build();
//
//        //when
//        Page<Certification> page = certService.getListByCondition(condition);
//        System.out.println("page.totalCount = " + page.getTotalCount());
//
//        //then
//        assertThat(page.getContent()).extracting(c -> c.getUser().getUserId()).containsOnly(userId);
//        assertThat(page.getContent())
//                .extracting(Certification::getRegistDt)
//                .map(LocalDateTime::toLocalDate)
//                .containsOnly(date);
//        assertThat(page.getTotalCount()).isGreaterThan(0);
    }
}
