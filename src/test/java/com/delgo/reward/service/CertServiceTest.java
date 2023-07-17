package com.delgo.reward.service;

import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.mungple.Mungple;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.cert.CertByAchvResDTO;
import com.delgo.reward.record.certification.CertRecord;
import com.delgo.reward.repository.CertRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CertServiceTest {
    String DIR = "C:\\\\testPhoto\\\\"; // local
//    String DIR = "/var/www/reward-backend/"; // dev, real

    @Mock private UserService userService;
    @Mock private PhotoService photoService;
    @Mock private ArchiveService archiveService;
    @Mock private MungpleService mungpleService;
    @Mock private LikeListService likeListService;
    @Mock private ReverseGeoService reverseGeoService;
    @Mock private AchievementsService achievementsService;
    @Mock private ObjectStorageService objectStorageService;
    @Mock private CertRepository certRepository;

    @InjectMocks private CertService certService;

    private static User user;
    private static Certification certification;

    @BeforeAll
    public static void setup() {
        user = User.builder()
                .userId(1)
                .name("Test User")
                .profile("https://example.com/profile.jpg")
                .build();
        certification = Certification.builder()
                .certificationId(10)
                .placeName("Test Place")
                .description("Test Description")
                .photoUrl("https://example.com/photo.jpg")
                .mungpleId(0)
                .isHideAddress(false)
                .address("Seoul, South Korea")
                .commentCount(0)
                .latitude("37.5101562")
                .longitude("127.1091707")
                .user(user)
                .build();
    }

    @Nested
    @DisplayName("인증 생성 TEST")
    class createCertTest {
        @Nested
        @DisplayName("[Success Case]")
        class SuccessCase {
            @Test
            @DisplayName("일반 인증 생성")
            public void createCertTest_Success_1() throws IOException {
                // given
                CertRecord certRecord = new CertRecord(1, "CA0002", 0, "TEST PLACE", "TEST DESCRIPTION", "", "", true);
                MockMultipartFile photo = new MockMultipartFile("photo", "testPhoto.webp", "webp", new FileInputStream(DIR + "testPhoto.webp"));
                Location location = new Location(certRecord.latitude(), certRecord.longitude());

                Mockito.when(userService.getUserById(certRecord.userId())).thenReturn(user);
                Mockito.when(reverseGeoService.getReverseGeoData(location)).thenReturn(new Location());
                Mockito.when(certRepository.save(Mockito.any(Certification.class))).thenReturn(certification);
                Mockito.when(photoService.uploadCertMultipartForJPG(certification.getCertificationId(), photo)).thenReturn("https://example.com/photo.jpg");
                Mockito.when(achievementsService.checkEarnedAchievements(certRecord.userId(), certRecord.mungpleId() != 0)).thenReturn(List.of(new Achievements()));

                // when
                CertByAchvResDTO result = certService.createCert(certRecord, photo);

                // then
                assertThat(result).isNotNull();
                assertThat(result.getMungpleId()).isEqualTo(0);
                assertThat(result.getCertificationId()).isEqualTo(certification.getCertificationId());
            }

            @Test
            @DisplayName("멍플 인증 생성")
            public void createCertTest_Success_2() throws IOException {
                // given
                int mungpleId = 3;
                CertRecord certRecord = new CertRecord(1, "CA0002", mungpleId, "TEST PLACE", "TEST DESCRIPTION", "", "", true);
                MockMultipartFile photo = new MockMultipartFile("photo", "testPhoto.webp", "webp", new FileInputStream(DIR + "testPhoto.webp"));
                Mungple mungple = Mungple.builder().jibunAddress("TEST JIBUN ADDRESS").build();

                Certification certificationByMungple = Certification.builder()
                        .certificationId(13)
                        .placeName("Test Place")
                        .description("Test Description")
                        .photoUrl("https://example.com/photo.jpg")
                        .mungpleId(3)
                        .isHideAddress(false)
                        .address("Seoul, South Korea")
                        .commentCount(0)
                        .latitude("37.5101562")
                        .longitude("127.1091707")
                        .user(user)
                        .build();

                // when
                Mockito.when(userService.getUserById(certRecord.userId())).thenReturn(user);
                Mockito.when(mungpleService.getMungpleById(certRecord.mungpleId())).thenReturn(mungple);
                Mockito.when(certRepository.save(Mockito.any(Certification.class))).thenReturn(certificationByMungple);
                Mockito.when(photoService.uploadCertMultipartForJPG(certificationByMungple.getCertificationId(), photo)).thenReturn("https://example.com/photo.jpg");
                Mockito.when(achievementsService.checkEarnedAchievements(certRecord.userId(), certRecord.mungpleId() != 0)).thenReturn(List.of(new Achievements()));

                CertByAchvResDTO result = certService.createCert(certRecord, photo);

                // then
                assertThat(result).isNotNull();
                assertThat(result.getMungpleId()).isEqualTo(mungpleId);
                assertThat(result.getCertificationId()).isEqualTo(certificationByMungple.getCertificationId());
            }
        }
    }
}
