package com.delgo.reward.certification.service;


import com.delgo.reward.certification.service.port.CertRepository;
import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.certification.CertPhoto;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.comm.Page;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.certification.domain.CertCreate;
import com.delgo.reward.certification.domain.CertUpdate;
import com.delgo.reward.repository.CertPhotoRepository;
import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.service.PhotoService;
import com.delgo.reward.service.ReactionService;
import com.delgo.reward.service.UserService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class CertService {

    // Service
    private final UserService userService;
    private final PhotoService photoService;
    private final ReactionService reactionService;
    private final ReverseGeoService reverseGeoService;
    private final MongoMungpleService mongoMungpleService;
    private final ObjectStorageService objectStorageService;

    // Repository
    private final CertRepository certRepository;
    private final CertPhotoRepository certPhotoRepository;

    /**
     * 인증 생성
     * 일반 인증 - (위도,경도)로 NCP에서 주소 조회 필요
     */
    public Certification create(CertCreate record, List<MultipartFile> photos) {
        // User 조회
        User user = userService.getUserById(record.userId());

        Certification certification;
        if(record.mungpleId() == 0) {
            // 위도 , 경도로 주소 조회
            Location location = reverseGeoService.getReverseGeoData(new Location(record.latitude(), record.longitude()));
            certification = certRepository.save(record.toEntity(location, user));
        } else {
            // 멍플 조회
            MongoMungple mungple = mongoMungpleService.getMungpleByMungpleId(record.mungpleId());
            certification = certRepository.save(record.toEntity(mungple, user));
        }

        // 인증 사진 서버 업로드 및 URL 반환
        List<String> photoUrls = photoService.uploadCertPhotos(certification.getCertificationId(), photos);

        // URL -> CertPhoto 변환
        List<CertPhoto> certPhotoList = photoUrls.stream().map(photoUrl -> CertPhoto.builder()
                .certificationId(certification.getCertificationId())
                .url(photoUrl)
                .isCorrect(true)
                .build()).toList();

        // CertPhoto 저장
        certPhotoRepository.saveAll(certPhotoList);
        certification.setPhotos(certPhotoList);

        return certification;
    }

    /**
     * [certId] 단 건 조회
     */
    public Certification getById(int certificationId) {
        return certRepository.findByCertId(certificationId)
                .orElseThrow(() -> new NullPointerException("[getById] NOT FOUND Certification Id : " + certificationId));
    }

    /**
     *  List 조건 조회 - TODO: CertPhoto, Reaction 넣어 주는 작업 추가
     */
    public Page<Certification> getListByCondition(CertCondition certCondition) {
        return certRepository.findListByCondition(certCondition);
    }


    /**
     * 인증 수정
     */
    public Certification modifyCert(Certification certification, CertUpdate record) {
        return certification.modify(record);
    }

    /**
     * 인증 삭제
     */
    public void deleteCert(int certificationId) {
        certRepository.deleteById(certificationId);
        reactionService.deleteCertRelatedReactions(certificationId);
        objectStorageService.deleteObject(BucketName.CERTIFICATION,certificationId + "_cert.webp");
    }

    /**
     * 유저 인증 중 가장 많이 방문한 멍플 조회
     */
    public List<UserVisitMungpleCountDTO> getVisitedMungpleIdListTop3ByUserId(int userId){
        Pageable pageable = PageRequest.of(0, 3);

        List<UserVisitMungpleCountDTO> userVisitMungpleCountDTOList = certRepository.findVisitTop3MungpleIdByUserId(userId, pageable);
        return mongoMungpleService.getMungpleListByIds(userVisitMungpleCountDTOList);
    }
}
