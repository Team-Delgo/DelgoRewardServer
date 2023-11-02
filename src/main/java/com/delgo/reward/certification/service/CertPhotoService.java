package com.delgo.reward.certification.service;


import com.delgo.reward.certification.domain.CertPhoto;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.service.port.CertPhotoRepository;
import com.delgo.reward.service.PhotoService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Builder
@RequiredArgsConstructor
public class CertPhotoService {
    private final PhotoService photoService;
    private final CertPhotoRepository certPhotoRepository;

    /**
     * 인증 사진 저장
     */
    @Transactional
    public List<CertPhoto> create(int certificationId, List<MultipartFile> photos) {
        List<String> urlList = photoService.uploadCertPhotos(certificationId, photos);
        List<CertPhoto> certPhotoList = urlList.stream().map(url -> CertPhoto.from(certificationId, url)).toList();
        return certPhotoRepository.saveAll(certPhotoList);
    }

    /**
     *  단 건 인증 사진 조회
     */
    public List<CertPhoto> getListByCertId(int certificationId) {
        return certPhotoRepository.findListByCertId(certificationId);
    }

    /**
     *  인증 List 사진 조회
     */
    public Map<Integer, List<CertPhoto>> getMapByCertList(List<Certification> certList) {
        List<Integer> certIdList = certList.stream().map(Certification::getCertificationId).toList();
        List<CertPhoto> certPhotoList = certPhotoRepository.findListByCertIdList(certIdList);
        return certPhotoList.stream().collect(Collectors.groupingBy(CertPhoto::getCertificationId));
    }
}
