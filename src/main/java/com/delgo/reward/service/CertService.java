package com.delgo.reward.service;


import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.certification.CertPhoto;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.comm.PageResDTO;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.record.certification.CertRecord;
import com.delgo.reward.record.certification.ModifyCertRecord;
import com.delgo.reward.repository.CertPhotoRepository;
import com.delgo.reward.repository.certification.CertCondition;
import com.delgo.reward.repository.certification.CertRepository;
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
    public Certification create(CertRecord record, List<MultipartFile> photos) {
        User user = userService.getUserById(record.userId());
        Certification certification = certRepository.save(
                (record.mungpleId() == 0)
                        ? record.toEntity(reverseGeoService.getReverseGeoData(new Location(record.latitude(), record.longitude())), user)
                        : record.toEntity(mongoMungpleService.getMungpleByMungpleId(record.mungpleId()),user));

        List<String> photoUrls = photoService.uploadCertPhotos(certification.getCertificationId(), photos);
        List<CertPhoto> certPhotoList = photoUrls.stream().map(photoUrl -> CertPhoto.builder()
                .certificationId(certification.getCertificationId())
                .url(photoUrl)
                .isCorrect(true)
                .build()).toList();

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
    public PageResDTO<Certification> getListByCondition(CertCondition certCondition) {
        return certRepository.findListByCondition(certCondition);
    }


    /**
     * 인증 수정
     */
    public Certification modifyCert(Certification certification, ModifyCertRecord record) {
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
