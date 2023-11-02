package com.delgo.reward.certification.service;

import com.delgo.reward.certification.domain.CertPhoto;
import com.delgo.reward.certification.domain.Certification;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CertPhotoServiceTest {
    CertPhotoService certPhotoService;

    @Before
    public void init() {
        List<CertPhoto> initList = new ArrayList<>();
        initList.add(CertPhoto.from(1,"test1.com"));
        initList.add(CertPhoto.from(1,"test2.com"));
        initList.add(CertPhoto.from(1,"test3.com"));
        initList.add(CertPhoto.from(1,"test4.com"));
        initList.add(CertPhoto.from(1,"test5.com"));
        initList.add(CertPhoto.from(2,"test6.com"));
        initList.add(CertPhoto.from(2,"test7.com"));
        initList.add(CertPhoto.from(2,"test8.com"));
        initList.add(CertPhoto.from(2,"test9.com"));
        initList.add(CertPhoto.from(2,"test10.com"));

    }

    @Test
    public void 여러_사진파일을_한번에_등록할_수_있다(){
        // given
        int certificationId = 1;

        // when
//        certPhotoService.create();

        // then
    }


    @Test
    public void certId로_특정_인증_사진_리스트를_조회할_수_있다 (){
        // given
        int certificationId = 1;

        // when
        List<CertPhoto> certPhotoList = certPhotoService.getListByCertId(certificationId);

        // then
        assertThat(certPhotoList).isNotEmpty();
        assertThat(certPhotoList).extracting(CertPhoto::getCertificationId).containsOnly(certificationId);
    }

    @Test
    public void certId_List로_여러_인증_사진_리스트를_조회할_수_있다 (){
        // given
        int firstCertId = 1;
        int secondCertId = 2;
        List<Certification> certList = new ArrayList<>();
        certList.add(Certification.builder().certificationId(firstCertId).build());
        certList.add(Certification.builder().certificationId(secondCertId).build());

        // when
        Map<Integer, List<CertPhoto>> photoMap = certPhotoService.getMapByCertList(certList);

        // then
        assertThat(photoMap).isNotEmpty();
        assertThat(photoMap.get(firstCertId)).extracting(CertPhoto::getCertificationId).containsOnly(firstCertId);
        assertThat(photoMap.get(secondCertId)).extracting(CertPhoto::getCertificationId).containsOnly(secondCertId);
    }
}
