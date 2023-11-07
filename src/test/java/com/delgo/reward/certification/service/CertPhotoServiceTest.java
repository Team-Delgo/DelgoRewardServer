package com.delgo.reward.certification.service;

import com.delgo.reward.certification.domain.CertPhoto;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.fake.FakeCertPhotoRepositoryImpl;
import com.delgo.reward.common.service.PhotoService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CertPhotoServiceTest {
    CertPhotoService certPhotoService;

    @Before
    public void init() {
        List<CertPhoto> initList = new ArrayList<>();
        initList.add(CertPhoto.from(1, "test1.com"));
        initList.add(CertPhoto.from(1, "test2.com"));
        initList.add(CertPhoto.from(1, "test3.com"));
        initList.add(CertPhoto.from(1, "test4.com"));
        initList.add(CertPhoto.from(1, "test5.com"));
        initList.add(CertPhoto.from(2, "test6.com"));
        initList.add(CertPhoto.from(2, "test7.com"));
        initList.add(CertPhoto.from(2, "test8.com"));
        initList.add(CertPhoto.from(2, "test9.com"));
        initList.add(CertPhoto.from(2, "test10.com"));

        PhotoService photoService = Mockito.mock(PhotoService.class);
        String fakeFileName = "fake_file_name.jpg";
        String fakeUrl = "http://127.0.0.1/fake_file_name.jpg";
        Mockito.when(photoService.makeCertFileName(Mockito.anyInt(),Mockito.any(MultipartFile.class) ,Mockito.anyInt())).thenReturn(fakeFileName);
        Mockito.when(photoService.save(Mockito.anyString(), Mockito.any(MultipartFile.class))).thenReturn(fakeUrl);

        certPhotoService = CertPhotoService.builder()
                .certPhotoRepository(new FakeCertPhotoRepositoryImpl(initList))
                .photoService(photoService) // TODO: Fake 형식으로 변경 필요함.
                .build();
    }

    @Test
    public void 여러_사진파일을_한번에_등록할_수_있다() {
        // given
        int certificationId = 21;
        MockMultipartFile file1 = new MockMultipartFile("test_url_1", "file1.txt", "text/plain", "content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("test_url_2", "file2.txt", "text/plain", "content2".getBytes());
        List<MultipartFile> photos = List.of(file1, file2);

        // when
        List<CertPhoto> certPhotoList = certPhotoService.create(certificationId, photos);
        // then
        assertThat(certPhotoList.size()).isEqualTo(photos.size());
        assertThat(certPhotoList).extracting(CertPhoto::getCertificationId).containsOnly(21);
    }


    @Test
    public void certId로_특정_인증_사진_리스트를_조회할_수_있다() {
        // given
        int certificationId = 1;

        // when
        List<CertPhoto> certPhotoList = certPhotoService.getListByCertId(certificationId);

        // then
        assertThat(certPhotoList.size()).isEqualTo(5);
        assertThat(certPhotoList).extracting(CertPhoto::getCertificationId).containsOnly(certificationId);
    }

    @Test
    public void certId_List로_여러_인증_사진_리스트를_조회할_수_있다() {
        // given
        int firstCertId = 1;
        int secondCertId = 2;
        List<Certification> certList = new ArrayList<>();
        certList.add(Certification.builder().certificationId(firstCertId).build());
        certList.add(Certification.builder().certificationId(secondCertId).build());

        // when
        Map<Integer, List<CertPhoto>> photoMap = certPhotoService.getMapByCertList(certList);

        // then
        assertThat(photoMap.get(firstCertId)).isNotEmpty();
        assertThat(photoMap.get(firstCertId).size()).isEqualTo(5);
        assertThat(photoMap.get(secondCertId)).isNotEmpty();
        assertThat(photoMap.get(secondCertId).size()).isEqualTo(5);
        assertThat(photoMap.get(firstCertId)).extracting(CertPhoto::getCertificationId).containsOnly(firstCertId);
        assertThat(photoMap.get(secondCertId)).extracting(CertPhoto::getCertificationId).containsOnly(secondCertId);
    }
}
