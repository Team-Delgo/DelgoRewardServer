package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.async.CertAsyncService;
import com.delgo.reward.comm.async.ClassificationAsyncService;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.cert.CertByAchvResDTO;
import com.delgo.reward.dto.cert.CertResDTO;
import com.delgo.reward.mongoService.ClassificationService;
import com.delgo.reward.record.certification.CertRecord;
import com.delgo.reward.record.certification.ModifyCertRecord;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.LikeListService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certification")
public class CertController extends CommController {

    private final CertService certService;
    private final LikeListService likeListService;
    private final CertAsyncService certAsyncService;
    private final ClassificationService classificationService;
    private final ClassificationAsyncService classificationAsyncService;

    /**
     * 전체 인증 조회
     * @param userId, certificationId(제외할 인증 번호), pageable
     * @return PageResDTO<CertResDTO, Integer>
     */
    @GetMapping("/all")
    public ResponseEntity getAllCert(
            @RequestParam Integer userId,
            @RequestParam(required = false) Integer certificationId,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        return (certificationId == null)
                ? SuccessReturn(certService.getAllCert(userId, pageable))
                : SuccessReturn(certService.getAllCertExcludeSpecificCert(userId, certificationId,pageable));
    }

    /**
     * [Other]다른 사용자 작성 인증 조회  ex) CA0000(전체 조회), CA0002(카페 조회)
     * @param userId, categoryCode, pageable
     * @return PageResDTO<CertResDTO, Integer>
     */
    @GetMapping("/other")
    public ResponseEntity getOtherCerts(
            @RequestParam Integer userId,
            @RequestParam String categoryCode,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        if (!StringUtils.hasText(categoryCode)) return ErrorReturn(APICode.PARAM_ERROR);
        return SuccessReturn(certService.getOtherCerts(userId, categoryCode, pageable));
    }


    /**
     * [Mungple] 인증 조회
     * @param userId, mungpleId, pageable
     * @return PageResDTO<CertByMungpleResDTO, Integer>
     */
    @GetMapping("/mungple")
    public ResponseEntity getCertsByMungple(
            @RequestParam Integer userId,
            @RequestParam Integer mungpleId,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        return SuccessReturn(certService.getCertsByMungpleId(userId, mungpleId, pageable));
    }

    // ---------------------------------권한 필요--------------------------------------------

    /**
     * 인증 생성
     * @param record, photo
     * @return CertByAchvResDTO
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createCert(@Validated @RequestPart(value = "data") CertRecord record, @RequestPart(required = false) List<MultipartFile> photos) throws JsonProcessingException {
        if(photos.isEmpty()) ErrorReturn(APICode.PARAM_ERROR);

        CertByAchvResDTO resDto = certService.createCert(record, photos);
        log.info("{}", resDto.getCertificationId());

        // 비동기적 실행
        certAsyncService.doSomething(resDto.getCertificationId());
        classificationAsyncService.doClassification(resDto.getCertificationId());

        return SuccessReturn(resDto);
    }

    /**
     * [certId] 인증 조회
     * @param userId, certificationId
     * @return List<CertResDTO>
     */
    @GetMapping
    public ResponseEntity getCertsByCertId(@RequestParam Integer userId, @RequestParam Integer certificationId) {
        return SuccessReturn(certService.getCertsByIdWithLike(userId, certificationId));
    }


    /**
     * [Date] 인증 조회 ex) 2023.07.10에 등록한 인증
     * @param userId, date
     * @return List<CertResDTO>
     */
    @GetMapping("/date")
    public ResponseEntity getCertsByDate(@RequestParam Integer userId, @RequestParam String date) {
        return SuccessReturn(certService.getCertsByDate(userId, LocalDate.parse(date)));
    }

    /**
     * [My]내가 작성한 인증 조회  ex) CA0000(전체 조회), CA0002(카페 조회)
     * @param userId, categoryCode, pageable
     * @return PageResDTO<CertResDTO, Integer>
     */
    @GetMapping("/my")
    public ResponseEntity getMyCerts(
            @RequestParam Integer userId,
            @RequestParam String categoryCode,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        if (!StringUtils.hasText(categoryCode)) return ErrorReturn(APICode.PARAM_ERROR);
        return SuccessReturn(certService.getMyCerts(userId, categoryCode, pageable));
    }

    /**
     * [Recent] 인증 조회
     * @param userId, count(조회 개수)
     * @return List<CertResDTO>
     */
    @GetMapping("/recent")
    public ResponseEntity getRecentCerts(@RequestParam Integer userId, @RequestParam Integer count) {
        return SuccessReturn(certService.getRecentCerts(userId, count));
    }

    /**
     * [User] 인증 개수 조회
     * @param userId
     * @return int
     */
    @GetMapping(value = {"/count/{userId}", "/count/"})
    public ResponseEntity getCertCountByUser(@PathVariable Integer userId) {
        return SuccessReturn(certService.getCertCountByUser(userId));
    }


    /**
     * 인증 수정
     * @param record
     * @return CertResDTO
     */
    @PutMapping
    public ResponseEntity modifyCert(@Validated @RequestBody ModifyCertRecord record) {
        Certification certification = certService.getCertById(record.certificationId());
        if (record.userId() != certification.getUser().getUserId())
            return ErrorReturn(APICode.INVALID_USER_ERROR);

        // 인증 분류 삭제
        classificationService.deleteClassificationWhenModifyCert(certification);
        Certification updatedCertification = certService.modifyCert(certification, record);

        // 비동기적 실행
        classificationAsyncService.doClassification(updatedCertification.getCertificationId());
        return SuccessReturn(new CertResDTO(updatedCertification));
    }

    /**
     * 인증 삭제
     * @param userId, certificationId
     */
    @DeleteMapping(value = {"/{userId}/{certificationId}"})
    public ResponseEntity deleteCert(@PathVariable Integer userId, @PathVariable Integer certificationId) {
        if (!Objects.equals(userId, certService.getCertById(certificationId).getUser().getUserId()))
            return ErrorReturn(APICode.INVALID_USER_ERROR);

        certService.deleteCert(certificationId);
        return SuccessReturn();
    }

    /**
     * 인증 LIKE
     * @param userId, certificationId
     */
    @PostMapping(value = {"/like/{userId}/{certificationId}", "/like/"})
    public ResponseEntity like(@PathVariable Integer userId, @PathVariable Integer certificationId) throws IOException {
        likeListService.like(userId, certificationId, certService.getCertById(certificationId).getUser().getUserId());

        return SuccessReturn();
    }
}