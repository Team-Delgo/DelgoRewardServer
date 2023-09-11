package com.delgo.reward.service;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.GeoCode;
import com.delgo.reward.comm.code.PGeoCode;
import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.domain.certification.CertPhoto;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.cert.CertByAchvResDTO;
import com.delgo.reward.dto.cert.CertByMungpleResDTO;
import com.delgo.reward.dto.cert.CertResDTO;
import com.delgo.reward.dto.comm.PageResDTO;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.record.certification.CertRecord;
import com.delgo.reward.record.certification.ModifyCertRecord;
import com.delgo.reward.repository.CertPhotoRepository;
import com.delgo.reward.repository.CertRepository;
import com.google.api.client.util.ArrayMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CertService {

    // Service
    private final UserService userService;
    private final PhotoService photoService;
    private final ArchiveService archiveService;
    private final MongoMungpleService mongoMungpleService;
    private final LikeListService likeListService;
    private final ReverseGeoService reverseGeoService;
    private final AchievementsService achievementsService;
    private final ObjectStorageService objectStorageService;

    // Repository
    private final CertRepository certRepository;
    private final CertPhotoRepository certPhotoRepository;
//    private final JDBCTemplateRankingRepository jdbcTemplateRankingRepository;

    /**
     * 인증 생성
     * 일반 인증 - (위도,경도)로 NCP에서 주소 조회 필요
     */
    public CertByAchvResDTO createCert(CertRecord record, List<MultipartFile> photos) {
        User user = userService.getUserById(record.userId());
        Certification certification = saveCert(
                (record.mungpleId() == 0)
                        ? record.toEntity(reverseGeoService.getReverseGeoData(new Location(record.latitude(), record.longitude())), user)
                        : record.toEntity(mongoMungpleService.getMungpleByMungpleId(record.mungpleId()),user));

        List<String> photoUrls = photoService.uploadCertPhotos(certification.getCertificationId(), photos);
        List<CertPhoto> certPhotos = photoUrls.stream().map(photoUrl -> CertPhoto.builder()
                .certificationId(certification.getCertificationId())
                .url(photoUrl)
                .isCorrect(true)
                .build()).toList();

        certPhotoRepository.saveAll(certPhotos);
        certification.setPhotos(certPhotos);

        CertByAchvResDTO resDto = new CertByAchvResDTO(certification, record.userId());
        // 획득 가능한 업적 Check
        List<Achievements> earnAchievements = achievementsService.checkEarnedAchievements(record.userId(),
                record.mungpleId() != 0);
        if (!earnAchievements.isEmpty()) {
            archiveService.registerArchives(earnAchievements.stream()
                    .map(achievement -> achievement.toArchive(record.userId())).collect(Collectors.toList()));
            resDto.setAchievements(earnAchievements);
        }

        return resDto;
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
                .orElseThrow(() -> new NullPointerException("NOT FOUND Certification id : " + certificationId));
    }

    /**
     * 전체 인증 조회 - Not Paging
     */
    public List<Certification> getCertsByUserId(int userId) {
        List<Integer> certIds = certRepository.findCertIdByUserUserId(userId);
        return getCertsByIds(certIds);
    }

    /**
     * [certId & Like] 인증 조회
     * 좋아요 여부 설정
     * 단 건이지만 Front 요청으로 LIST로 반환
     */
    public List<CertResDTO> getCertsByIdWithLike(int userId, int certificationId) {
        return new ArrayList<>(Collections.singletonList(new CertResDTO(getCertById(certificationId),userId)));
    }

    /**
     * [Date] 인증 조회
     */
    public List<CertResDTO> getCertsByDate(int userId, LocalDate date) {
        return certRepository.findCertByDateAndUser(userId, date.atStartOfDay(), date.atTime(23, 59, 59))
                .stream()
                .map(c -> new CertResDTO(c,userId))
                .collect(Collectors.toList());
    }

    /**
     * [Date] 인증 조회 - ClassificationCategory 사용
     */
    public List<Certification> getCertsByDateWithoutUser(LocalDate localDate){
        return certRepository.findCertByDate(localDate.minusDays(1).atStartOfDay(), localDate.atStartOfDay());
    }

    /**
     * [Recent] 인증 조회
     */
    public List<CertResDTO> getRecentCerts(int userId, int count) {
        List<Integer> certIds = certRepository.findRecentCertId(userId, PageRequest.of(0, count));
        return getCertsByIds(certIds).stream().map(cert -> new CertResDTO(cert, userId)).toList();
    }

    /**
     * [Exposed] 인증 조회 - Map 사용
     */
    public List<Certification> getExposedCerts(int count) {
        List<Integer> certIds = certRepository.findCertIdByIsExpose(PageRequest.of(0, count));
        return getCertsByIds(certIds);
    }

    /**
     * [ids] 인증 조회
     */
    public List<Certification> getCertsByIds(List<Integer> ids){
        return certRepository.findCertByIds(ids);
    }

    /**
     * 전체 인증 조회 - Paging
     * 1. Paging으로 CertIds 조회
     * 2. CertId로 (EntityGraph) 실제 객체 조회. ( [ids] 인증 조회 )
     *   - Paging, EntityGraph 같이 사용시 메모리 과부하
     */
    public PageResDTO<CertResDTO> getAllCert(int userId, Pageable pageable) {
        Slice<Integer> slice = certRepository.findAllCertIdByPaging(userId, pageable);
        List<CertResDTO> certs = getCertsByIds(slice.getContent()).stream().map(cert -> new CertResDTO(cert, userId)).toList();

        return new PageResDTO<>(certs, slice.getSize(), slice.getNumber(), slice.isLast());
    }

    /**
     * 전체 인증 조회 (특정 인증 제외)
     * 클라이언트에서 특정 인증글을 맨 위로 올렸을 때 중복을 방지하기 위해 해당 인증글은 반환 리스트에서 제거한다.
     */
    public PageResDTO<CertResDTO> getAllCertExcludeSpecificCert(int userId, int certificationId, Pageable pageable) {
        Slice<Integer> slice = certRepository.findAllExcludeSpecificCert(userId, certificationId, pageable);
        List<CertResDTO> certs = getCertsByIds(slice.getContent()).stream().map(cert -> new CertResDTO(cert, userId)).toList();

        return new PageResDTO<>(certs, slice.getSize(), slice.getNumber(), slice.isLast());
    }

    /**
     * [Mungple] 인증 조회
     */
    public PageResDTO<CertByMungpleResDTO> getCertsByMungpleId(int userId, int mungpleId, Pageable pageable) {
        Slice<Integer> slice = certRepository.findCertByMungple(mungpleId, pageable);
        List<CertByMungpleResDTO> certs = getCertsByIds(slice.getContent()).stream().map(cert -> new CertByMungpleResDTO(cert, userId)).toList();

        return new PageResDTO<>(certs, slice.getSize(), slice.getNumber(), slice.isLast());
    }

    /**
     * [My] 내가 작성한 인증 조회
     */
    public PageResDTO<CertResDTO> getMyCerts(int userId, String categoryCode, Pageable pageable) {
        Slice<Integer> slice = (!categoryCode.equals(CategoryCode.TOTAL.getCode()))
                ? certRepository.findCertIdByUserIdAndCategoryCode(userId, categoryCode, pageable)
                : certRepository.findCertIdByUserId(userId, pageable);

        List<CertResDTO> certs = getCertsByIds(slice.getContent()).stream().map(cert -> new CertResDTO(cert, userId)).toList();
        return new PageResDTO<>(certs, slice.getSize(), slice.getNumber(), slice.isLast(), getCertCountByUser(userId));
    }

    /**
     *  [Other] 다른 사용자가 작성한 인증 조회
     */
    public PageResDTO<CertResDTO> getOtherCerts(int userId, String categoryCode, Pageable pageable) {
        Slice<Integer> slice = (!categoryCode.equals(CategoryCode.TOTAL.getCode()))
                ? certRepository.findCorrectCertIdByUserIdAndCategoryCode(userId, categoryCode, pageable)
                : certRepository.findCorrectCertIdByUserId(userId, pageable);

        List<CertResDTO> certs = getCertsByIds(slice.getContent()).stream().map(cert -> new CertResDTO(cert, userId)).toList();
        return new PageResDTO<>(certs, slice.getSize(), slice.getNumber(), slice.isLast(), getCorrectCertCountByUserId(userId));
    }

    /**
     *  [User] 작성한 올바른 인증 조회
     */
    public List<Certification> getCorrectCertsByUserId(int userId) {
        return certRepository.findCorrectCertByUserId(userId);
    }

    /**
     * [User] 인증 개수 조회
     */
    public int getCertCountByUser(int userId) {
        return certRepository.countByUserUserId(userId);
    }

    /**
     * [User] 인증 개수 조회
     */
    public int getCorrectCertCountByUserId(int userId) {
        return certRepository.countByUserUserIdAndIsCorrect(userId, true);
    }

    /**
     * [User & Mungple] 인증 개수 조회
     */
    public int getCertCountByMungpleOfSpecificUser(int userId) {
        return certRepository.countOfCertByMungpleAndUser(userId);
    }

    /**
     * [Mungple] 인증 개수 조회
     */
    public int getCertCountByMungple(int mungpleId) {
        return certRepository.countOfCertByMungple(mungpleId);
    }

    /**
     * 인증 수정
     */
    public Certification modifyCert(Certification certification, ModifyCertRecord record) {
        return certification.modify(record);
    }

    /**
     * 인증 사진 수정
     */
    public Certification changeCertPhotoUrl(Certification certification, String text) {
        return certification.setPhotoUrl(text);
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
        likeListService.deleteCertificationRelatedLike(certificationId);
        objectStorageService.deleteObject(BucketName.CERTIFICATION,certificationId + "_cert.webp");
    }

    /**
     * Map TEST
     */
    public Map<String, List<Certification>> test(int count){
        Map<String, List<Certification>> certByPGeoCode = new ArrayMap<>();
        List<Certification> certificationList = new ArrayList<>();

        for(PGeoCode p: PGeoCode.values()){
            if(p.getPGeoCode().equals(PGeoCode.P101000.getPGeoCode())){
                List<Certification> certificationListOfSongPa = certRepository.findCertByGeoCode(GeoCode.C101180.getGeoCode(), PageRequest.of(0, 3));
                List<Certification> certificationListNotOfSongPa = certRepository.findCertByPGeoCodeExceptGeoCode(p.getPGeoCode(), GeoCode.C101180.getGeoCode(), PageRequest.of(0, 3));

                certificationList = Stream.concat(certificationListOfSongPa.stream(), certificationListNotOfSongPa.stream())
                        .collect(Collectors.toList());
            } else {
                certificationList = certRepository.findCertByPGeoCode(p.getPGeoCode(), PageRequest.of(0, count));
            }
            if(certificationList.size() > 0){
                certByPGeoCode.put(p.getPGeoCode(), certificationList);
            }
        }

        return certByPGeoCode;
    }

    //    /**
//     * [Category] 인증 개수 조회
//     */
//    public Map<String, Long> getCertCountByCategory(int userId) {
//        Map<String, Long> map = getCertsByUserId(userId).stream().collect(groupingBy(cert -> CategoryCode.valueOf(cert.getCategoryCode()).getValue(),counting()));
//        //*** putIfAbsent
//        //- Key 값이 존재하는 경우 Map의 Value의 값을 반환하고, Key값이 존재하지 않는 경우 Key와 Value를 Map에 저장하고 Null을 반환합니다.
//        for (CategoryCode categoryCode : CategoryCode.values())
//            map.putIfAbsent(categoryCode.getValue(), 0L);
//        return map;
//    }

}
