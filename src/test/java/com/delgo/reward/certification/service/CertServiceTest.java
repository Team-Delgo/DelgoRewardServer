package com.delgo.reward.certification.service;

import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.domain.request.CertCreate;
import com.delgo.reward.certification.domain.request.CertUpdate;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.common.domain.Code;
import com.delgo.reward.common.service.CodeService;
import com.delgo.reward.fake.FakeGeoDataAdapter;
import com.delgo.reward.fake.FakeObjectStorageAdapter;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.comm.PageCustom;
import com.delgo.reward.fake.FakeCertRepositoryImpl;
import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.service.UserService;
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

        UserService userService = Mockito.mock(UserService.class);
        Mockito.when(userService.getUserById(Mockito.anyInt())).thenReturn(user1);

        MongoMungple mongoMungple = MongoMungple.builder()
                .categoryCode(CategoryCode.CA0002)
                .placeName("Test Place Name")
                .jibunAddress("성남시 수정구 성남동")
                .geoCode("101010")
                .pGeoCode("101000")
                .latitude("37.4999429")
                .longitude("127.1256681")
                .build();
        MongoMungpleService mongoMungpleService = Mockito.mock(MongoMungpleService.class);
        Mockito.when(mongoMungpleService.getMungpleByMungpleId(Mockito.anyInt())).thenReturn(mongoMungple);

        Code code = Code.builder()
                .code("101010")
                .pCode("101000")
                .build();
        CodeService codeService = Mockito.mock(CodeService.class);
        Mockito.when(codeService.getGeoCodeByAddress(Mockito.anyString())).thenReturn(code);

        certService = CertService.builder()
                .certRepository(new FakeCertRepositoryImpl(initCertList))
                .objectStoragePort(new FakeObjectStorageAdapter())
                .geoDataPort(new FakeGeoDataAdapter())
                .userService(userService)
                .codeService(codeService)
                .mongoMungpleService(mongoMungpleService)
                .build();
    }

    @Test
    public void save를_이용하여_인증을_DB에_저장_할_수_있다() {
        // given
        User user = User.builder()
                .userId(1)
                .name("Test User")
                .pet(Pet.builder().petId(1).build())
                .build();
        Certification certification = Certification.builder()
                .certificationId(1)
                .placeName("Test Place Name")
                .description("Test Description")
                .user(user)
                .mungpleId(1)
                .isCorrect(true)
                .isHideAddress(false)
                .registDt(LocalDate.now().atStartOfDay())
                .build();

        // when
        Certification savedCertification = certService.save(certification);

        // then
        assertThat(savedCertification.getUser().getUserId()).isEqualTo(1);
        assertThat(savedCertification.getPlaceName()).isEqualTo("Test Place Name");
        assertThat(savedCertification.getDescription()).isEqualTo("Test Description");
        assertThat(savedCertification.getMungpleId()).isEqualTo(1);
        assertThat(savedCertification.getIsCorrect()).isEqualTo(true);
    }

    @Test
    public void create를_이용하여_mungpleId가_0일때_createByNormal을_호출한다() {
        // given
        int userId = 1;
        String latitude = "37.5101562";
        String longitude = "127.1091707";

        CertCreate certCreate = CertCreate.builder()
                .userId(userId)
                .placeName("Test Place Name")
                .description("Test Description")
                .mungpleId(0)
                .categoryCode(CategoryCode.CA0002)
                .latitude(latitude)
                .longitude(longitude)
                .isHideAddress(false)
                .build();

        // when
        Certification savedCertification = certService.create(certCreate);

        // then
        assertThat(savedCertification.getUser().getUserId()).isEqualTo(userId);
        assertThat(savedCertification.getPlaceName()).isEqualTo("Test Place Name");
        assertThat(savedCertification.getDescription()).isEqualTo("Test Description");
    }

    @Test
    public void createByNormal를_이용하여_일반_인증을_등록할_수_있다() {
        // given
        int userId = 1;
        String latitude = "37.5101562";
        String longitude = "127.1091707";

        CertCreate certCreate = CertCreate.builder()
                .userId(userId)
                .placeName("Test Place Name")
                .description("Test Description")
                .mungpleId(0)
                .categoryCode(CategoryCode.CA0002)
                .latitude(latitude)
                .longitude(longitude)
                .isHideAddress(false)
                .build();

        // when
        Certification savedCertification = certService.createByNormal(certCreate);

        // then
        assertThat(savedCertification.getUser().getUserId()).isEqualTo(userId);
        assertThat(savedCertification.getPlaceName()).isEqualTo("Test Place Name");
        assertThat(savedCertification.getDescription()).isEqualTo("Test Description");
    }

    @Test
    public void createByMungple를_이용하여_인증을_등록할_수_있다() {
        // given
        int userId = 1;
        int mungpleId = 20;
        CertCreate certCreate = CertCreate.builder()
                .userId(userId)
                .placeName("Test Place Name")
                .description("Test Description")
                .mungpleId(mungpleId)
                .categoryCode(CategoryCode.CA0002)
                .latitude("")
                .longitude("")
                .isHideAddress(false)
                .build();

        // when
        Certification savedCertification = certService.createByMungple(certCreate);

        // then
        assertThat(savedCertification.getUser().getUserId()).isEqualTo(userId);
        assertThat(savedCertification.getMungpleId()).isEqualTo(mungpleId);
        assertThat(savedCertification.getPlaceName()).isEqualTo("Test Place Name");
        assertThat(savedCertification.getDescription()).isEqualTo("Test Description");
    }


    @Test
    public void getById는_certId로_인증을_조회할_수_있다() {
        // given
        int certId = 1;

        // when
        Certification certification = certService.getById(certId);

        // then
        assertThat(certification).isNotNull();
        assertThat(certification.getCertificationId()).isEqualTo(certId);
    }

    @Test
    public void getById는_certId가_없을_경우_Exception이_발생시킨다() {
        // given
        int certificationId = 10;

        // when & then
        assertThatThrownBy(() -> certService.getById(certificationId))
                .isInstanceOf(NoSuchElementException.class);
    }


    @Test
    public void getListByCondition_전체_인증리스트를_조회할_수_있다() {
        // given
        Boolean isCorrect = null;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.from(isCorrect, pageable);

        // when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        // then
        assertThat(page.getContent().size()).isGreaterThan(0);
        assertThat(page.getTotalCount()).isEqualTo(page.getContent().size());
    }

    @Test
    public void  getListByCondition_전체_올바른_인증리스트를_조회할_수_있다() {
        // given
        Boolean isCorrect = true;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.from(isCorrect, pageable);

        // when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        // then
        assertThat(page.getContent().size()).isGreaterThan(0);
        assertThat(page.getTotalCount()).isEqualTo(page.getContent().size());
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void  getListByCondition_페이징으로_리스트를_조회할_수_있다() {
        // given
        Boolean isCorrect = null;
        Pageable pageable = PageRequest.of(0,3);
        CertCondition condition = CertCondition.from(isCorrect, pageable);

        // when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        // then
        assertThat(page.getContent().size()).isGreaterThan(0);
        assertThat(page.getTotalCount()).isGreaterThan(0);
        assertThat(page.getContent().size()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getSize()).isEqualTo(3);
        assertThat(page.isLast()).isEqualTo(false);
    }

    @Test
    public void  getListByCondition_페이징으로_마지막_리스트를_조회할_수_있다() {
        // given
        Boolean isCorrect = null;
        Pageable pageable = PageRequest.of(2,3);
        CertCondition condition = CertCondition.from(isCorrect, pageable);

        // when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        // then
        assertThat(page.getContent().size()).isGreaterThan(0);
        assertThat(page.getTotalCount()).isGreaterThan(0);
        assertThat(page.getNumber()).isEqualTo(2);
        assertThat(page.getSize()).isEqualTo(3);
        assertThat(page.isLast()).isEqualTo(true);
    }

    @Test
    public void getListByCondition_특정_유저가_등록한_인증리스트를_조회할_수_있다() {
        // given
        int userId = 1;
        Boolean isCorrect = null;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.byUser(userId, isCorrect, pageable);

        // when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        // then
        assertThat(page).isNotNull();
        assertThat(page.getTotalCount()).isEqualTo(page.getContent().size());
        assertThat(page.getContent()).extracting(c -> c.getUser().getUserId()).containsOnly(userId);
    }

    @Test
    public void getListByCondition_특정_유저가_등록한_올바른_인증리스트를_페이징으로_조회할_수_있다() {
        // given
        int userId = 1;
        Boolean isCorrect = true;
        int size = 2;
        Pageable pageable = PageRequest.of(0, size);
        CertCondition condition = CertCondition.byUser(userId, isCorrect, pageable);

        // when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        // then
        assertThat(page).isNotNull();
        assertThat(page.getTotalCount()).isGreaterThan(0);
        assertThat(page.getContent().size()).isEqualTo(size);
        assertThat(page.getContent()).extracting(c -> c.getUser().getUserId()).containsOnly(userId);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void getListByCondition_멍플_인증리스트를_조회할_수_있다() {
        // given
        int mungpleId = 2;
        Boolean isCorrect = null;
        Pageable pageable = Pageable.unpaged();
        CertCondition condition = CertCondition.byMungple(mungpleId, isCorrect, pageable);

        // when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        // then
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

        // when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        // then
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

        // when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        // then
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

        // when
        PageCustom<Certification> page = certService.getListByCondition(condition);

        // then
        assertThat(page).isNotNull();
        assertThat(page.getContent().size()).isEqualTo(size);
        assertThat(page.getContent()).extracting(c -> c.getUser().getUserId()).containsOnly(userId);
        assertThat(page.getContent()).extracting(c -> c.getRegistDt().toLocalDate()).containsOnly(date);
        assertThat(page.getContent()).extracting(Certification::getIsCorrect).containsOnly(true);
    }

    @Test
    public void update를_사용하여_인증을_수정할_수_있다() {
        // given
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

        // when
        Certification updatedCertification = certService.update(certUpdate);

        // then
        assertThat(updatedCertification.getIsHideAddress()).isEqualTo(isHideAddress);
        assertThat(updatedCertification.getDescription()).isEqualTo(desc);
    }


    @Test
    public void delete를_사용하여_인증을_삭제할_수_있다() {
        // given
        int certificationId = 8;

        // when
        certService.delete(certificationId);

        // then
        assertThatThrownBy(() -> certService.getById(certificationId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void validate를_이용하여_유저가_권한이_있는경우_True를_반환한다() {
        // given
        int userId = 1;
        int certificationId = 3;

        // when
        Boolean result = certService.validate(userId, certificationId);

        // then
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void validate를_이용하여_유저가_권한이_없다면_false를_반환한다() {
        // given
        int userId = 1;
        int certificationId = 7;

        // when
        Boolean result = certService.validate(userId, certificationId);

        // then
        assertThat(result).isEqualTo(false);
    }
}
