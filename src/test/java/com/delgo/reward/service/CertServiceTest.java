//package com.delgo.reward.service;
//
//import com.delgo.reward.comm.ncp.ReverseGeoService;
//import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
//import com.delgo.reward.domain.achievements.Achievements;
//import com.delgo.reward.domain.certification.Certification;
//import com.delgo.reward.domain.common.Location;
//import com.delgo.reward.domain.like.LikeList;
//import com.delgo.reward.domain.mungple.Mungple;
//import com.delgo.reward.domain.user.User;
//import com.delgo.reward.dto.cert.CertByAchvResDTO;
//import com.delgo.reward.dto.cert.CertResDTO;
//import com.delgo.reward.record.certification.CertRecord;
//import com.delgo.reward.repository.CertRepository;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//@ExtendWith(MockitoExtension.class)
//public class CertServiceTest {
//    String DIR = "C:\\\\testPhoto\\\\"; // local
////    String DIR = "/var/www/reward-backend/"; // dev, real
//
//    @Mock private UserService userService;
//    @Mock private PhotoService photoService;
//    @Mock private ArchiveService archiveService;
//    @Mock private MungpleService mungpleService;
//    @Mock private LikeListService likeListService;
//    @Mock private ReverseGeoService reverseGeoService;
//    @Mock private AchievementsService achievementsService;
//    @Mock private ObjectStorageService objectStorageService;
//    @Mock private CertRepository certRepository;
//
//    @InjectMocks private CertService certService;
//
//    private static User user;
//    private static Certification certification;
//
//    @BeforeAll
//    public static void setup() {
//        user = User.builder()
//                .userId(1)
//                .name("Test User")
//                .profile("https://example.com/profile.jpg")
//                .build();
//        certification = Certification.builder()
//                .certificationId(10)
//                .placeName("Test Place")
//                .description("Test Description")
//                .photoUrl("https://example.com/photo.jpg")
//                .mungpleId(0)
//                .isHideAddress(false)
//                .address("Seoul, South Korea")
//                .commentCount(0)
//                .latitude("37.5101562")
//                .longitude("127.1091707")
//                .user(user)
//                .build();
//    }
//
//    @Nested
//    @DisplayName("[TEST] 인증 생성")
//    class createCertTest {
//        @Test
//        @DisplayName("[SUCCESS] 일반 인증 생성")
//        public void createCertTest_Success_1() throws IOException {
//            // given
//            CertRecord certRecord = new CertRecord(1, "CA0002", 0, "TEST PLACE", "TEST DESCRIPTION", "", "", true);
//            MockMultipartFile photo = new MockMultipartFile("photo", "testPhoto.webp", "webp", new FileInputStream(DIR + "testPhoto.webp"));
//            Location location = new Location(certRecord.latitude(), certRecord.longitude());
//
//            Mockito.when(userService.getUserById(certRecord.userId())).thenReturn(user);
//            Mockito.when(reverseGeoService.getReverseGeoData(location)).thenReturn(new Location());
//            Mockito.when(certRepository.save(Mockito.any(Certification.class))).thenReturn(certification);
////            Mockito.when(photoService.uploadCertMultipartForJPG(certification.getCertificationId(), photo)).thenReturn("https://example.com/photo.jpg");
//            Mockito.when(achievementsService.checkEarnedAchievements(certRecord.userId(),
//                    certRecord.mungpleId() != 0)).thenReturn(List.of(new Achievements()));
//
//            // when
//            CertByAchvResDTO result = certService.createCert(certRecord, photo);
//
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getMungpleId()).isEqualTo(0);
//            assertThat(result.getCertificationId()).isEqualTo(certification.getCertificationId());
//        }
//
//        @Test
//        @DisplayName("[SUCCESS] 멍플 인증 생성")
//        public void createCertTest_Success_2() throws IOException {
//            // given
//            int mungpleId = 3;
//            CertRecord certRecord = new CertRecord(1, "CA0002", mungpleId, "TEST PLACE", "TEST DESCRIPTION", "", "",
//                    true);
//            MockMultipartFile photo = new MockMultipartFile("photo", "testPhoto.webp", "webp", new FileInputStream(DIR + "testPhoto.webp"));
//            Mungple mungple = Mungple.builder().jibunAddress("TEST JIBUN ADDRESS").build();
//
//            Certification certificationByMungple = Certification.builder()
//                    .certificationId(13)
//                    .placeName("Test Place")
//                    .description("Test Description")
//                    .photoUrl("https://example.com/photo.jpg")
//                    .mungpleId(3)
//                    .isHideAddress(false)
//                    .address("Seoul, South Korea")
//                    .commentCount(0)
//                    .latitude("37.5101562")
//                    .longitude("127.1091707")
//                    .user(user)
//                    .build();
//
//            // when
//            Mockito.when(userService.getUserById(certRecord.userId())).thenReturn(user);
//            Mockito.when(mungpleService.getMungpleById(certRecord.mungpleId())).thenReturn(mungple);
//            Mockito.when(certRepository.save(Mockito.any(Certification.class))).thenReturn(certificationByMungple);
//            Mockito.when(photoService.uploadCertMultipartForJPG(certificationByMungple.getCertificationId(), photo)).thenReturn("https://example.com/photo.jpg");
//            Mockito.when(achievementsService.checkEarnedAchievements(certRecord.userId(), certRecord.mungpleId() != 0)).thenReturn(List.of(new Achievements()));
//
//            CertByAchvResDTO result = certService.createCert(certRecord, photo);
//
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getMungpleId()).isEqualTo(mungpleId);
//            assertThat(result.getCertificationId()).isEqualTo(certificationByMungple.getCertificationId());
//        }
//    }
//
//    @Nested
//    @DisplayName("[TEST] [certId] 인증 조회")
//    class getCertByIdTest {
//        @Test
//        @DisplayName("[SUCCESS] 유효한 certId로 인증 조회")
//        public void getCertByIdTest_Success_1() {
//            // given
//            int certificationId = 10;
//
//            // when
//            Mockito.when(certRepository.findCertByCertificationId(certificationId)).thenReturn(Optional.of(certification));
//            Certification result = certService.getCertById(certificationId);
//
//            // then
//            assertThat(certification).isEqualTo(result);
//        }
//
//        @Test
//        @DisplayName("[FAIL] 유효 하지 않은 certId로 인증 조회")
//        public void getCertByIdTest_Fail_1() {
//            // given
//            int certificationId = 2;
//
//            // when
//            Mockito.when(certRepository.findCertByCertificationId(certificationId)).thenReturn(Optional.empty());
//
//            // then
//            assertThatThrownBy(() -> certService.getCertById(certificationId))
//                    .isInstanceOf(NullPointerException.class);
//        }
//    }
//
//    @Nested
//    @DisplayName("[TEST] 인증 DB 저장")
//    class saveTest {
//        @Test
//        @DisplayName("[SUCCESS] 인증 DB 저장")
//        public void saveTest_Success() {
//            // given
//
//            // when
//            Mockito.when(certRepository.save(certification)).thenReturn(certification);
//            Certification result = certService.saveCert(certification);
//
//            // then
//            assertThat(result).isEqualTo(certification);
//        }
//    }
//
//    @Nested
//    @DisplayName("[TEST] 전체 인증 조회 - Not Paging")
//    class getCertsByUserIdTest {
//        @Test
//        @DisplayName("[SUCCESS] 전체 인증 조회")
//        public void getCertsByUserIdTest_Success() {
//            // given
//            int userId = 1;
//            List<Integer> certIds = Arrays.asList(10, 20, 30);
//            List<Certification> dummyCerts = Arrays.asList(
//                    Certification.builder().certificationId(10).build(),
//                    Certification.builder().certificationId(20).build(),
//                    Certification.builder().certificationId(30).build());
//
//            // when
//            Mockito.when(certRepository.findCertIdByUserUserId(userId)).thenReturn(certIds);
//            Mockito.when(certService.getCertsByIds(certIds)).thenReturn(dummyCerts);
//
//            List<Certification> result = certService.getCertsByUserId(userId);
//
//            // then
//            assertThat(result).isEqualTo(dummyCerts);
//        }
//    }
//
//    @Nested
//    @DisplayName("[TEST] [certId & Like] 인증 조회")
//    class getCertsByIdWithLikeTEST {
//        @Test
//        @DisplayName("[SUCCESS] 본인 게시글 좋아요 클릭 O")
//        void getCertsByIdWithLikeTEST_Success_1() {
//            // given
//            int userId = 1;
//            int certificationId = 10;
//
//            List<LikeList> likeLists = Arrays.asList(
//                    LikeList.builder().userId(1).certificationId(10).isLike(true).build(),
//                    LikeList.builder().userId(2).certificationId(20).isLike(true).build(),
//                    LikeList.builder().userId(3).certificationId(30).isLike(true).build());
//
//            Certification certification = Certification.builder()
//                    .certificationId(10)
//                    .placeName("Test Place")
//                    .description("Test Description")
//                    .photoUrl("https://example.com/photo.jpg")
//                    .mungpleId(0)
//                    .isHideAddress(false)
//                    .address("Seoul, South Korea")
//                    .commentCount(0)
//                    .latitude("37.5101562")
//                    .longitude("127.1091707")
//                    .user(user)
//                    .likeLists(likeLists)
//                    .build();
//
//            // when
//            Mockito.when(certRepository.findCertByCertificationId(certificationId)).thenReturn(Optional.of(certification));
//            List<CertResDTO> result = certService.getCertsByIdWithLike(userId, certificationId);
//
//            // then
//            assertThat(result).hasSize(1);
//            assertThat(result.get(0).getCertificationId()).isEqualTo(certification.getCertificationId());
//            assertThat(result.get(0).getUserId()).isEqualTo(userId);
//
//            assertThat(result.get(0).getIsOwner()).isEqualTo(true);
//            assertThat(result.get(0).getIsLike()).isEqualTo(true);
//            assertThat(result.get(0).getLikeCount()).isEqualTo(3);
//        }
//
//        @Test
//        @DisplayName("[SUCCESS] 본인 게시글 좋아요 클릭 X")
//        void getCertsByIdWithLikeTEST_Success_2() {
//            // given
//            int userId = 1;
//            int certificationId = 10;
//
//            List<LikeList> likeLists = Arrays.asList(
//                    LikeList.builder().userId(1).certificationId(10).isLike(false).build(),
//                    LikeList.builder().userId(2).certificationId(20).isLike(true).build(),
//                    LikeList.builder().userId(3).certificationId(30).isLike(true).build());
//
//            Certification certification = Certification.builder()
//                    .certificationId(10)
//                    .placeName("Test Place")
//                    .description("Test Description")
//                    .photoUrl("https://example.com/photo.jpg")
//                    .mungpleId(0)
//                    .isHideAddress(false)
//                    .address("Seoul, South Korea")
//                    .commentCount(0)
//                    .latitude("37.5101562")
//                    .longitude("127.1091707")
//                    .user(user)
//                    .likeLists(likeLists)
//                    .build();
//
//            // when
//            Mockito.when(certRepository.findCertByCertificationId(certificationId)).thenReturn(Optional.of(certification));
//            List<CertResDTO> result = certService.getCertsByIdWithLike(userId, certificationId);
//
//            // then
//            assertThat(result).hasSize(1);
//            assertThat(result.get(0).getCertificationId()).isEqualTo(certification.getCertificationId());
//            assertThat(result.get(0).getUserId()).isEqualTo(userId);
//
//            assertThat(result.get(0).getIsOwner()).isEqualTo(true);
//            assertThat(result.get(0).getIsLike()).isEqualTo(false);
//            assertThat(result.get(0).getLikeCount()).isEqualTo(2);
//        }
//    }
//}
