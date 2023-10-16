package com.delgo.reward.service;


import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.certification.CertPhoto;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.record.certification.CertRecord;
import com.delgo.reward.record.certification.ModifyCertRecord;
import com.delgo.reward.repository.CertPhotoRepository;
import com.delgo.reward.repository.CertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CertService {

    // Service
    private final UserService userService;
    private final PhotoService photoService;
    private final MongoMungpleService mongoMungpleService;
    private final ReactionService reactionService;
    private final ReverseGeoService reverseGeoService;
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
     * [date, userId] List 조회
     */
    public List<Certification> getListByDate(int userId, LocalDate date) {
        return certRepository.findListByDateAndUserId(userId, date.atStartOfDay(), date.atTime(23, 59, 59));
    }

    /**
     * [date] List 조회 - ClassificationCategory 사용
     */
    public List<Certification> getListByDate(LocalDate localDate){
        return certRepository.findListByDate(localDate.minusDays(1).atStartOfDay(), localDate.atStartOfDay());
    }
    
    /**
     * [ids] 인증 조회
     */
    public List<Certification> getListByIds(List<Integer> ids){
        return certRepository.findListByIds(ids);
    }

    /**
     * List 조회 - Paging
     * 1. Paging CertIds 조회
     * 2. CertId로 (EntityGraph) 실제 객체 조회. ( [ids] 인증 조회 )
     * - Paging, EntityGraph 같이 사용시 메모리 과부하
     */
    public List<Certification> getCorrectListByPaging(Pageable pageable) {
        Slice<Integer> slice = certRepository.findCorrectIds(pageable);
        return getListByIds(slice.getContent());
    }

    /**
     * Slice 조회 - Paging
    */
    public Slice<Integer> getCorrectSliceByPaging(Pageable pageable){
        return certRepository.findCorrectIds(pageable);
    }

    /**
     * [MungpleId] List 조회 - Paging
     */
    public List<Certification> getCorrectListByMungpleId(int mungpleId, Pageable pageable) {
        Slice<Integer> slice = certRepository.findCorrectIdsByMungpleId(mungpleId, pageable);
        return getListByIds(slice.getContent());
    }

    /**
     * [MungpleId] Slice 조회
     */
    public Slice<Integer> getSliceByMungpleId(int mungpleId, Pageable pageable) {
        return certRepository.findCorrectIdsByMungpleId(mungpleId, pageable);
    }

    /**
     * [userId] List 조회 - Paging
     */
    public List<Certification> getListByUserId(int userId, Pageable pageable) {
        Slice<Integer> slice = certRepository.findIdsByUserId(userId, pageable);
        return getListByIds(slice.getContent());
    }

    /**
     * [userId] Slice 조회 - Paging
     */
    public Slice<Integer> getSliceByUserId(int userId, Pageable pageable) {
        return certRepository.findIdsByUserId(userId, pageable);
    }

    /**
     * [userId] Correct List 조회 - Paging
     */
    public List<Certification> getCorrectListByUserId(int userId, Pageable pageable) {
        Slice<Integer> slice = certRepository.findCorrectIdsByUserId(userId, pageable);
        return getListByIds(slice.getContent());
    }

    /**
     * [userId] Correct Slice 조회 - Paging
     */
    public Slice<Integer> getCorrectSliceByUserId(int userId, Pageable pageable) {
        return certRepository.findCorrectIdsByUserId(userId, pageable);
    }

    /**
     * [userId] Correct Count 조회
     */
    public int getCorrectCountByUserId(int userId) {
        return certRepository.countOfCorrectByUserId(userId);
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
