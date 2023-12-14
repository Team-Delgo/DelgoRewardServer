package com.delgo.reward.service;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.comm.ncp.geo.GeoDataService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.code.Code;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.record.certification.CertRecord;
import com.delgo.reward.record.certification.ModifyCertRecord;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CertService {

    // Service
    private final UserService userService;
    private final MongoMungpleService mongoMungpleService;
    private final GeoDataService geoDataService;
    private final CodeService codeService;
    private final ObjectStorageService objectStorageService;

    // Repository
    private final CertRepository certRepository;
    private final ReactionRepository reactionRepository;

    /**
     * 인증 생성
     * 일반 인증 - (위도,경도)로 NCP에서 주소 조회 필요
     */
    public Certification create(CertRecord record, List<MultipartFile> photos) {
        User user = userService.getUserById(record.userId());
        String address = geoDataService.getReverseGeoData(record.latitude(), record.longitude());
        Code code = codeService.getGeoCodeByAddress(address);

        return saveCert(record.toEntity(address, code, user));
    }

    public Certification createByMungple(CertRecord record, List<MultipartFile> photos) {
        User user = userService.getUserById(record.userId());
        MongoMungple mongoMungple = mongoMungpleService.getMungpleByMungpleId(record.mungpleId());
        return saveCert(record.toEntity(mongoMungple, user));
    }

    /**
     * 인증 저장
     */
    public Certification saveCert(Certification certification) {
        return certRepository.save(certification);
    }

    /**
     * [certId] 인증 조회
     */
    public Certification getCertById(int certificationId) {
        return certRepository.findCertByCertificationId(certificationId)
                .orElseThrow(() -> new NotFoundDataException("[Certification] certificationId : " + certificationId));
    }

    /**
     * 전체 인증 조회 - Not Paging
     */
    public List<Certification> getCertsByUserId(int userId) {
        List<Integer> certIds = certRepository.findCertIdByUserUserId(userId);
        return getCertsByIds(certIds);
    }

    /**
     * [Date] 인증 조회
     */
    public List<Certification> getCertsByDate(int userId, LocalDate date) {
        return certRepository.findCertByDateAndUser(userId, date.atStartOfDay(), date.atTime(23, 59, 59));
    }

    /**
     * [Date] 인증 조회 - ClassificationCategory 사용
     */
    public List<Certification> getCertsByDateWithoutUser(LocalDate localDate) {
        return certRepository.findCertByDate(localDate.minusDays(1).atStartOfDay(), localDate.atStartOfDay());
    }

    /**
     * [Recent] 인증 조회
     */
    public List<Certification> getRecentCerts(int userId, int count) {
        List<Integer> certIds = certRepository.findRecentCertId(userId, PageRequest.of(0, count));
        return getCertsByIds(certIds);
    }


    /**
     * [ids] 인증 조회
     */
    public List<Certification> getCertsByIds(List<Integer> ids) {
        return certRepository.findCertByIds(ids);
    }


    /**
     * [My] 내가 작성한 모든 인증 조회
     */
    public List<Certification> getAllMyCerts(int userId) {
        List<Integer> certIdList = certRepository.findAllCertIdByUserId(userId);
        return getCertsByIds(certIdList);
    }

    /**
     * [User] 작성한 올바른 인증 조회
     */
    public List<Certification> getCorrectCertsByUserId(int userId) {
        return certRepository.findCorrectCertByUserId(userId);
    }


    /**
     * [User] 인증 개수 조회
     */
    public int getCorrectCertCountByUserId(int userId) {
        return certRepository.countByUserUserIdAndIsCorrect(userId, true);
    }

    /**
     * 유저 인증 중 가장 많이 방문한 멍플 조회
     */
    public List<UserVisitMungpleCountDTO> getVisitedMungpleIdListTop3ByUserId(int userId) {
        Pageable pageable = PageRequest.of(0, 3);

        List<UserVisitMungpleCountDTO> userVisitMungpleCountDTOList =
                certRepository.findVisitTop3MungpleIdByUserId(userId, pageable);
        return mongoMungpleService.getMungpleListByIds(userVisitMungpleCountDTOList);
    }

    /**
     * 인증 수정
     */
    public Certification modifyCert(Certification certification, ModifyCertRecord record) {
        return certification.modify(record);
    }

    /**
     * isCorrect 수정
     */
    public void changeIsCorrect(int certId, boolean isCorrect) {
        Certification cert = getCertById(certId);
        cert.setIsCorrect(isCorrect);
    }

    /**
     * 인증 삭제
     * 관련 좋아요 삭제, NCP Object Storage 삭제 필요
     */
    public void deleteCert(int certificationId) {
        certRepository.deleteById(certificationId);
        reactionRepository.deleteByCertificationId(certificationId);
        objectStorageService.deleteObject(BucketName.CERTIFICATION, certificationId + "_cert.webp");
    }

    /**
     * 전체 인증 조회 - Paging
     * 1. Paging으로 CertIds 조회
     * 2. CertId로 (EntityGraph) 실제 객체 조회. ( [ids] 인증 조회 )
     * - Paging, EntityGraph 같이 사용시 메모리 과부하
     */
    public Page<Certification> getPagingList(int userId, Pageable pageable) {
        return certRepository.findCorrectPage(userId, pageable);
    }

    /**
     * [Mungple] 인증 조회
     */
    public Page<Certification> getPagingListByMungpleId(int userId, int mungpleId, Pageable pageable) {
         return certRepository.findCorrectPageByMungple(mungpleId, userId, pageable);
    }

    /**
     * [My] 내가 작성한 인증 조회
     */
    public Page<Certification> getMyCerts(int userId, CategoryCode categoryCode, Pageable pageable) {
        return categoryCode.equals(CategoryCode.CA0000)
                ? certRepository.findPageByUserId(userId, pageable)
                : certRepository.findPageByUserIdAndCategoryCode(userId, categoryCode, pageable);
    }

    /**
     * [Other] 다른 사용자가 작성한 인증 조회
     */
    public Page<Certification> getOtherCerts(int userId, CategoryCode categoryCode, Pageable pageable) {
        return categoryCode.equals(CategoryCode.CA0000)
                ? certRepository.findCorrectPageByUserId(userId, pageable)
                : certRepository.findCorrectPageByUserIdAndCategoryCode(userId, categoryCode, pageable);

    }
}
