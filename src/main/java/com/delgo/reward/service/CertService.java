package com.delgo.reward.service;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.GeoCode;
import com.delgo.reward.comm.code.PGeoCode;
import com.delgo.reward.comm.fcm.FcmService;
import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.notify.NotifyType;
import com.delgo.reward.dto.cert.CertResDTO;
import com.delgo.reward.record.certification.CertRecord;
import com.delgo.reward.record.certification.ModifyCertRecord;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.repository.JDBCTemplateRankingRepository;
import com.google.api.client.util.ArrayMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final FcmService fcmService;
    private final UserService userService;
    private final PointService pointService;
    private final PhotoService photoService;
    private final NotifyService notifyService;
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
        return certRepository.findById(certificationId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND Certification id : " + certificationId));
    }

    // Certification 수정
    public Certification modify(ModifyCertRecord record) {
        return getCertById(record.certificationId()).modify(record.description());
    }

    // Certification 삭제
    public void delete(int certificationId) {
        certRepository.deleteById(certificationId);
        likeListService.deleteCertificationRelatedLike(certificationId);
        objectStorageService.deleteObject(BucketName.CERTIFICATION,certificationId + "_cert.webp");
    }

    // Certification 등록
    public Certification register(CertRecord record, MultipartFile photo) {
        Certification certification = save(
                (record.mungpleId() == 0) // 일반 인증의 경우 - (위도,경도)로 주소 가져와서 등록해야 함.
                        ? record.toEntity(reverseGeoService.getReverseGeoData(new Location(record.latitude(), record.longitude())))
                        : record.toEntity(mungpleService.getMungpleById(record.mungpleId())));

        // 획득 가능한 업적 Check
        List<Achievements> earnAchievements = achievementsService.checkEarnedAchievements(record.userId(), record.mungpleId() != 0);
        if (!earnAchievements.isEmpty()) {
            archiveService.registerArchives(earnAchievements.stream()
                    .map(achievement -> achievement.toArchive(record.userId())).collect(Collectors.toList()));
            certification.setAchievements(earnAchievements);
        }

        // Point 부여
        pointService.givePoint(userService.getUserById(record.userId()).getUserId(), CategoryCode.valueOf(record.categoryCode()).getPoint());
        // 인증 사진 NCP Upload
        String ncpLink = photoService.uploadCertMultipartForJPG(certification.getCertificationId(), photo);
        certification.setPhotoUrl(ncpLink);

        return certification;
    }


    // Certification 조회 및 좋아요 여부 설정 후 반환
    public List<CertResDTO> getCert(int userId, int certificationId) {
        CertResDTO resDto = new CertResDTO(getCertById(certificationId));
        setUserAndLike(userId, resDto);

        // 프론트 편의를 위해 LIST로 반환
        return new ArrayList<>(Collections.singletonList(resDto));
    }

    // 날짜, 유저 별 Certification 조회
    public List<CertResDTO> getCertByDateAndUser(int userId, LocalDate date) {
        return certRepository.findCertByDateAndUser(userId, date.atStartOfDay(), date.atTime(23, 59, 59))
                .stream()
                .map(CertResDTO::new) // ResDTO로 변환
                .peek(cert -> setUserAndLike(userId, cert)) // 작성자, 좋아요 정보 SET
                .collect(Collectors.toList());
    }

    // 전체 Certification 리스트 조회
    public Slice<CertResDTO> getCertAll(int userId, int currentPage, int pageSize, boolean isDesc) {
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize,
                Sort.by(isDesc ? Sort.Direction.DESC : Sort.Direction.ASC, "createdDate"));

        Slice<Certification> certifications = certRepository.findAllByPaging(userId, pageRequest);
        return certifications.map(cert -> setUserAndLike(userId, new CertResDTO(cert)));
    }

    // 전체 Certification 리스트 조회 ( 특정 인증 제외 )
    public Slice<CertResDTO> getCertAllExcludeSpecificCert(int userId, int currentPage, int pageSize, boolean isDesc, int certificationId) {
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize,
                Sort.by(isDesc ? Sort.Direction.DESC : Sort.Direction.ASC, "createdDate"));

        Slice<Certification> certifications = certRepository.findAllByPaging(userId, pageRequest);
        return certifications.map(cert -> setUserAndLike(userId, new CertResDTO(cert)));
    }

    // mungpleId로 Certification 조회
    public Slice<CertResDTO> getCertByMungpleId(int userId, int mungpleId, int currentPage, int pageSize, boolean isDesc) {
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize,
                Sort.by(isDesc ? Sort.Direction.DESC : Sort.Direction.ASC, "createdDate"));

        Slice<Certification> certifications = certRepository.findCertByMungple(mungpleId, pageRequest);
        return certifications.map(cert -> setUserAndLike(userId, new CertResDTO(cert)));
    }

    // 카테고리 별 조회
    public Slice<CertResDTO> getCertByCategory(int userId, String categoryCode, int currentPage, int pageSize, boolean isDesc) {
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize,
                Sort.by(isDesc ? Sort.Direction.DESC : Sort.Direction.ASC, "createdDate"));

        Slice<Certification> certifications = (!categoryCode.equals(CategoryCode.TOTAL.getCode()))
                ? certRepository.findByUserIdAndCategoryCode(userId, categoryCode, pageRequest)
                : certRepository.findByUserId(userId, pageRequest);

        return certifications.map(cert -> setUserAndLike(userId, new CertResDTO(cert)));
    }

    // 카테고리 별 개수 조회
    public Map<String, Long> getCountByCategory(int userId) {
        Map<String, Long> map = getCertByUserId(userId).stream().collect(groupingBy(cert -> CategoryCode.valueOf(cert.getCategoryCode()).getValue(),counting()));
        //*** putIfAbsent
        //- Key 값이 존재하는 경우 Map의 Value의 값을 반환하고, Key값이 존재하지 않는 경우 Key와 Value를 Map에 저장하고 Null을 반환합니다.
        for (CategoryCode categoryCode : CategoryCode.values())
            map.putIfAbsent(categoryCode.getValue(), 0L);
        return map;
    }

    // 유저별 전체 개수 조회
    public int getTotalCountByUser(int userId) {
        return certRepository.countByUserId(userId);
    }

    // 유저별 전체 멍플 관련 인증 개수 조회
    public int getTotalCountOfCertByMungpleAndUser(int userId) {
        return certRepository.countOfCertByMungpleAndUser(userId);
    }

    // 최근 인증 조회
    public List<CertResDTO> getRecentCert(int userId, int count) {
        return certRepository.findRecentCert(userId, PageRequest.of(0, count)).stream()
                .map(cert -> setUserAndLike(userId, new CertResDTO(cert))).collect(Collectors.toList());
    }

    public CertResDTO setUserAndLike(int userId, CertResDTO dto) {
        return dto.setUserAndLike(
                userService.getUserById(dto.getUserId()), // Certification 작성한 User 정보
                userId != 0 && likeListService.hasLiked(userId, dto.getCertificationId()), // (조회)User is Liked?
                likeListService.getLikeCount(dto.getCertificationId()) // Certification Like Count
        );
    }

    // 좋아요
    public void like(int userId, int certificationId, int ownerId) throws IOException {
        // 이미 기존의 좋아요 Data가 존재할 경우
        if (likeListService.hasLiked(userId, certificationId)) {
            likeListService.updateIsLike(userId, certificationId);
        } else {// 첫 좋아요 눌렀을 경우
            log.info("첫 좋아요 : userId :{} , ownerId : {}", userId, ownerId);
            likeListService.firstLike(userId, certificationId);
            if (userId != ownerId) { // 자신이 누른 좋아요 는 알람 보내지 않는다.
                String notifyMsg = userService.getUserById(userId).getName() + "님이 회원님의 게시물을 좋아합니다.";
                notifyService.saveNotify(ownerId, NotifyType.LIKE, notifyMsg);
                fcmService.likePush(ownerId, notifyMsg);
            }
        }
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
    public List<Certification> getCertByUserId(int userId) {
        return certRepository.findByUserId(userId);
    }

    // 노출 가능한 인증 조회 ( Map에서 사용 )
    public List<Certification> getExposedCert(int count) {
        return certRepository.findByIsExpose(PageRequest.of(0, count));
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
    public List<Certification> getCertByDate(LocalDate localDate){
        return certRepository.findCertByDate(localDate.minusDays(1).atStartOfDay(), localDate.atStartOfDay());
    }
}
