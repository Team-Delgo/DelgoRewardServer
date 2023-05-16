package com.delgo.reward.service;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.GeoCode;
import com.delgo.reward.comm.code.PGeoCode;
import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.cert.CertByAchvResDTO;
import com.delgo.reward.dto.cert.CertResDTO;
import com.delgo.reward.dto.comm.PageResDTO;
import com.delgo.reward.record.certification.CertRecord;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.repository.JDBCTemplateRankingRepository;
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

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CertService {

    // Service
    private final UserService userService;
    private final PhotoService photoService;
    private final ArchiveService archiveService;
    private final MungpleService mungpleService;
    private final LikeListService likeListService;
    private final ReverseGeoService reverseGeoService;
    private final AchievementsService achievementsService;
    private final ObjectStorageService objectStorageService;

    // Repository
    private final CertRepository certRepository;
    private final JDBCTemplateRankingRepository jdbcTemplateRankingRepository;


    // Certification DB에 저장
    public Certification save(Certification certification) {
        return certRepository.save(certification);
    }

    // Id로 Certification 조회
    public Certification getCertById(int certificationId) {
        return certRepository.findCertByCertificationId(certificationId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND Certification id : " + certificationId));
    }

    // Certification 수정
    public Certification modify(Certification certification, String newDescription) {
        return certification.modify(newDescription);
    }

    // Certification 삭제
    public void delete(int certificationId) {
        certRepository.deleteById(certificationId);
        likeListService.deleteCertificationRelatedLike(certificationId);
        objectStorageService.deleteObject(BucketName.CERTIFICATION,certificationId + "_cert.webp");
    }

    // Certification 등록
    public CertByAchvResDTO register(CertRecord record, MultipartFile photo) {
        User user = userService.getUserById(record.userId());
        Certification certification = save(
                (record.mungpleId() == 0) // 일반 인증의 경우 - (위도,경도)로 주소 가져와서 등록해야 함.
                        ? record.toEntity(reverseGeoService.getReverseGeoData(new Location(record.latitude(), record.longitude())), user)
                        : record.toEntity(mungpleService.getMungpleById(record.mungpleId()),user));

        // 인증 사진 NCP Upload
        String ncpLink = photoService.uploadCertMultipartForJPG(certification.getCertificationId(), photo);
        certification.setPhotoUrl(ncpLink);
        // Point 부여
//        pointService.givePoint(userService.getUserById(record.userId()).getUserId(), CategoryCode.valueOf(record.categoryCode()).getPoint());

        CertByAchvResDTO resDto = new CertByAchvResDTO(certification, record.userId());
        // 획득 가능한 업적 Check
        List<Achievements> earnAchievements = achievementsService.checkEarnedAchievements(record.userId(), record.mungpleId() != 0);
        if (!earnAchievements.isEmpty()) {
            archiveService.registerArchives(earnAchievements.stream()
                    .map(achievement -> achievement.toArchive(record.userId())).collect(Collectors.toList()));
            resDto.setAchievements(earnAchievements);
        }

        return resDto;
    }

    // Certification 조회 및 좋아요 여부 설정 후 반환 // 프론트 편의를 위해 LIST로 반환
    public List<CertResDTO> getCertByUserIdAndCertId(int userId, int certificationId) {
        return new ArrayList<>(Collections.singletonList(new CertResDTO(getCertById(certificationId),userId)));
    }

    // 날짜, 유저 별 Certification 조회
    public List<CertResDTO> getCertByDateAndUser(int userId, LocalDate date) {
        return certRepository.findCertByDateAndUser(userId, date.atStartOfDay(), date.atTime(23, 59, 59))
                .stream()
                .map(c -> new CertResDTO(c,userId)) // ResDTO로 변환
                .collect(Collectors.toList());
    }

    // Ids로 List<Certification> 조회
    public List<Certification> getCertByIds(List<Integer> ids){
        return certRepository.findCertByIds(ids);
    }

    /*
     * 전체 Certification 리스트 조회 Pagaing, EntityGraph 같이 사용시 메모리 과부하..
     * 1. 페이징으로 CertId 조회
     * 2. CertId로 EntityGraph사용해서 실제 객체 조회해오기.
     */
    public PageResDTO<CertResDTO, Integer> getCertAll(int userId, Pageable pageable) {
        Slice<Integer> slice = certRepository.findAllCertIdByPaging(userId, pageable);
        List<CertResDTO> certs = getCertByIds(slice.getContent()).stream().map(CertResDTO::new).toList();

        return new PageResDTO<>(certs, slice);
    }

    // 전체 Certification 리스트 조회 ( 특정 인증 제외 )
    public PageResDTO<CertResDTO, Integer> getCertAllExcludeSpecificCert(int userId, int certificationId, Pageable pageable) {
        Slice<Integer> slice = certRepository.findAllExcludeSpecificCert(userId, certificationId, pageable);
        List<CertResDTO> certs = getCertByIds(slice.getContent()).stream().map(CertResDTO::new).toList();

        return new PageResDTO<>(certs, slice);
    }

    // mungpleId로 Certification 조회
    public Slice<CertResDTO> getCertListByMungpleId(int userId, int mungpleId, Pageable pageable) {
        return certRepository.findCertByMungple(mungpleId, pageable).map(cert -> new CertResDTO(cert, userId));
    }

    // 카테고리 별 조회
    public PageResDTO<CertResDTO, Integer> getCertListByCategory(int userId, String categoryCode, Pageable pageable) {
        Slice<Integer> slice = (!categoryCode.equals(CategoryCode.TOTAL.getCode()))
                ? certRepository.findByUserUserIdAndCategoryCode(userId, categoryCode, pageable)
                : certRepository.findByUserUserId(userId, pageable);

        List<CertResDTO> certs = getCertByIds(slice.getContent()).stream().map(cert -> new CertResDTO(cert, userId)).toList();
        return new PageResDTO<>(certs, slice);
    }

    // 카테고리 별 개수 조회
    public Map<String, Long> getCountByCategory(int userId) {
        Map<String, Long> map = getCertListByUserId(userId).stream().collect(groupingBy(cert -> CategoryCode.valueOf(cert.getCategoryCode()).getValue(),counting()));
        //*** putIfAbsent
        //- Key 값이 존재하는 경우 Map의 Value의 값을 반환하고, Key값이 존재하지 않는 경우 Key와 Value를 Map에 저장하고 Null을 반환합니다.
        for (CategoryCode categoryCode : CategoryCode.values())
            map.putIfAbsent(categoryCode.getValue(), 0L);
        return map;
    }

    // 유저별 전체 개수 조회
    public int getTotalCertCountByUser(int userId) {
        return certRepository.countByUserUserId(userId);
    }

    // 유저별 전체 멍플 관련 인증 개수 조회
    public int getTotalCertCountByMungpleAndUser(int userId) {
        return certRepository.countOfCertByMungpleAndUser(userId);
    }

    // 최근 인증 조회
    public List<CertResDTO> getRecentCert(int userId, int count) {
        return certRepository.findRecentCert(userId, PageRequest.of(0, count)).stream()
                .map(cert -> new CertResDTO(cert,userId)).collect(Collectors.toList());
    }

    public Certification changeCertPhotoUrl(Certification certification, String text) {
        return certification.setPhotoUrl(text);
    }

    // Comment Count + 1
    public void plusCommentCount(int certificationId) {
        jdbcTemplateRankingRepository.plusCommentCount(certificationId);
    }

    // Comment Count - 1
    public void minusCommentCount(int certificationId) {
        jdbcTemplateRankingRepository.minusCommentCount(certificationId);
    }

    // ---------------------------- Calendar, Map 사용 ----------------------------

    // userId로 Certification List 조회
    public List<Certification> getCertListByUserId(int userId) {
        List<Integer> certIds = certRepository.findCertIdByUserUserId(userId);
        return getCertByIds(certIds);
    }

    // 노출 가능한 인증 조회 ( Map에서 사용 )
    public List<Certification> getExposedCertList(int count) {
        List<Integer> certIds = certRepository.findCertIdByIsExpose(PageRequest.of(0, count));
        return getCertByIds(certIds);
    }

    // Map TEST
    public Map<String, List<Certification>> test(int count){
        Map<String, List<Certification>> certByPGeoCode = new ArrayMap<>();
        List<Certification> certificationList = new ArrayList<>();

        for(PGeoCode p: PGeoCode.values()){
            if(p.getPGeoCode().equals(PGeoCode.P101000.getPGeoCode())){
                List<Certification> certificationListOfSongPa = certRepository.findByGeoCode(GeoCode.C101180.getGeoCode(), PageRequest.of(0, 3));
                List<Certification> certificationListNotOfSongPa = certRepository.findByPGeoCodeExceptGeoCode(p.getPGeoCode(), GeoCode.C101180.getGeoCode(), PageRequest.of(0, 3));

                certificationList = Stream.concat(certificationListOfSongPa.stream(), certificationListNotOfSongPa.stream())
                        .collect(Collectors.toList());
            } else {
                certificationList = certRepository.findByPGeoCode(p.getPGeoCode(), PageRequest.of(0, count));
            }
            if(certificationList.size() > 0){
                certByPGeoCode.put(p.getPGeoCode(), certificationList);
            }
        }

        return certByPGeoCode;
    }

    // ---------------------------- ClassificationCategory 사용 ----------------------------

    // 날짜 별 Certification 조회
    public List<Certification> getCertListByDate(LocalDate localDate){
        return certRepository.findCertByDate(localDate.minusDays(1).atStartOfDay(), localDate.atStartOfDay());
    }
}
