package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.async.CertAsyncService;
import com.delgo.reward.comm.async.ClassificationAsyncService;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.cert.CertByAchvResDTO;
import com.delgo.reward.mongoService.ClassificationService;
import com.delgo.reward.record.certification.CertRecord;
import com.delgo.reward.record.certification.ModifyCertRecord;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.LikeListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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

    /*
     * 인증 등록
     * Request Data : PastCertificationDTO
     * Response Data : 등록한 인증 데이터 반환
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> register(@Validated @RequestPart(value = "data") CertRecord record, @RequestPart(required = false) MultipartFile photo) {
        if(photo.isEmpty()) ErrorReturn(ApiCode.PARAM_ERROR);

        CertByAchvResDTO resDto = certService.register(record, photo);
        log.info("{}", resDto.getCertificationId());

        // 비동기적 실행
        certAsyncService.doSomething(resDto.getCertificationId());
        classificationAsyncService.doClassification(resDto.getCertificationId());

        return SuccessReturn(resDto);
    }

    /*
     * 인증 수정
     * Request Data : CertificationModifyDTO
     * Response Data : 등록한 인증 데이터 반환
     */
    @PutMapping
    public ResponseEntity modify(@Validated @RequestBody ModifyCertRecord record) {
        if (record.userId() != certService.getCertById(record.certificationId()).getUser().getUserId())
            return ErrorReturn(ApiCode.INVALID_USER_ERROR);

        // 인증 분류 삭제
        classificationService.deleteClassificationWhenModifyCert(record);

        Certification certification = certService.modify(record);

        // 비동기적 실행
        classificationAsyncService.doClassification(certification.getCertificationId());
        return SuccessReturn(certification);
    }

    /*
     * CertificationId로 조회
     * Request Data : CertificationId로
     * Response Data : Certification
     */
    @GetMapping
    public ResponseEntity getData(@RequestParam Integer userId, @RequestParam Integer certificationId) {
        return SuccessReturn(certService.getCertByUserIdAndCertId(userId, certificationId));
    }

    /*
     * 날짜로 Certification 조회
     * Request Data : CertificationId로
     * Response Data : Certification
     */
    @GetMapping("/date")
    public ResponseEntity getDataByDate(@RequestParam Integer userId, @RequestParam String date) {
        return SuccessReturn(certService.getCertByDateAndUser(userId, LocalDate.parse(date)));
    }

    /*
     * 인증 카테고리 별 조회
     * Request Data : userId, categoryCode
     * - CA0000 = 전체 조회
     * Response Data : 카테고리별 인증 리스트 반환
     */
    @GetMapping("/category")
    public ResponseEntity getCategory(
            @RequestParam Integer userId,
            @RequestParam String categoryCode,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        // Validate - Blank Check; [ String 만 해주면 됨 ]
        if (categoryCode.isBlank()) return ErrorReturn(ApiCode.PARAM_ERROR);
        return SuccessReturn(certService.getCertListByCategory(userId, categoryCode, pageable));
    }

    /*
     * 카테고리 별 인증 개수 반환 ( 유저 )
     * Request Data : userId
     * Response Data : 카테고리별 인증 개수 반환
     */
    @GetMapping(value = {"/category/count/{userId}", "/category/count/"})
    public ResponseEntity getCategoryCount(@PathVariable Integer userId) {
        return SuccessReturn(certService.getCountByCategory(userId));
    }

    /*
     * 유저 별 인증 개수 반환
     * Request Data : userId
     * Response Data : 총 개수 반환
     */
    @GetMapping(value = {"/count/{userId}", "/count/"})
    public ResponseEntity getTotalCount(@PathVariable Integer userId) {
        return SuccessReturn(certService.getTotalCertCountByUser(userId));
    }

    /*
     * 인증 게시글의 좋아요 + 1
     * Request Data : userId, certificationId
     * - ConcurrentHashMap 사용 이유 - 모든 요청 DB Connection 시 감당 불가능
     * Response Data : X
     */
    @PostMapping(value = {"/like/{userId}/{certificationId}", "/like/"})
    public ResponseEntity setLike(@PathVariable Integer userId, @PathVariable Integer certificationId) throws IOException {
        likeListService.like(userId, certificationId, certService.getCertById(certificationId).getUser().getUserId());

        return SuccessReturn();
    }

    /*
     * 가장 최근 등록한 인증 반환
     * Request Data : userId, count(N)
     * Response Data : 최근 등록 인증 N개 반환
     */
    @GetMapping("/recent")
    public ResponseEntity getRecentData(@RequestParam Integer userId, @RequestParam Integer count) {
        return SuccessReturn(certService.getRecentCert(userId, count));
    }

    /*
     * 특정 Mungple 관련 인증 반환
     * Request Data : mungpleId
     * Response Data : 특정 Mungple 관련 인증 반환
     */
    @GetMapping("/mungple")
    public ResponseEntity getMungplePagingData(
            @RequestParam Integer userId,
            @RequestParam Integer mungpleId,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return SuccessReturn(certService.getCertListByMungpleId(userId, mungpleId, pageable));
    }

    /*
     * 모든 인증 리스트 페이징으로 반환 [ Main ]
     * Request Data : currentPage ( 현재 페이지 번호 ), pageSize ( 페이지 크기 )
     * certificationId - 해당 Cert는 제외하고 조회한다.
     * Response Data : 인증 모두 조회 ( 페이징 처리 되어 있음 )
     */
    @GetMapping("/all")
    public ResponseEntity getPagingData(
            @RequestParam Integer userId,
            @RequestParam(required = false) Integer certificationId,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return (certificationId == null)
                ? SuccessReturn(certService.getCertAll(userId, pageable))
                : SuccessReturn(certService.getCertAllExcludeSpecificCert(userId, certificationId,pageable));
    }

    /*
     * 인증 삭제
     * Request Data : userId ( 삭제 요청 userId ), certificationId ( 삭제할 인증 )
     * 요청 userId랑 등록 userId랑 비교 해야 함.
     * Response Data : null
     */
    @DeleteMapping(value = {"/{userId}/{certificationId}"})
    public ResponseEntity delete(@PathVariable Integer userId, @PathVariable Integer certificationId) {
        if (!Objects.equals(userId, certService.getCertById(certificationId).getUser().getUserId()))
            return ErrorReturn(ApiCode.INVALID_USER_ERROR);

        certService.delete(certificationId); // DB에서 삭제
        return SuccessReturn();
    }
}