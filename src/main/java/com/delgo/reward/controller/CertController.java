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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * 인증 생성
     * @param record, photo
     * @return CertByAchvResDTO
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createCert(@Validated @RequestPart(value = "data") CertRecord record, @RequestPart(required = false) MultipartFile photo) {
        if(photo.isEmpty()) ErrorReturn(APICode.PARAM_ERROR);

        CertByAchvResDTO resDto = certService.register(record, photo);
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
    @Operation(summary = "Id로 인증 조회", description = "certificationId로 인증을 조회합니다.", tags = { "Certification" })
    @ApiResponses({
            @ApiResponse(responseCode = APICode.CODE.SUCCESS, description = APICode.MSG.SUCCESS, content = @Content(schema = @Schema(implementation = CertResDTO.class))),
            @ApiResponse(responseCode = APICode.CODE.PARAM_ERROR, description = APICode.MSG.PARAM_ERROR),
            @ApiResponse(responseCode = APICode.CODE.SERVER_ERROR, description = APICode.MSG.SERVER_ERROR)
    })
    @GetMapping
    public ResponseEntity getCertByCertId(@RequestParam Integer userId, @RequestParam Integer certificationId) {
        return SuccessReturn(certService.getCertByUserIdAndCertId(userId, certificationId));
    }

    /**
     * [Total] 인증 조회
     * @param userId, certificationId(제외할 인증 번호), pageable
     * @return PageResDTO<CertResDTO, Integer>
     */
    @GetMapping("/all")
    public ResponseEntity getTotalCert(
            @RequestParam Integer userId,
            @RequestParam(required = false) Integer certificationId,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        return (certificationId == null)
                ? SuccessReturn(certService.getCertAll(userId, pageable))
                : SuccessReturn(certService.getCertAllExcludeSpecificCert(userId, certificationId,pageable));
    }


    /**
     * [Date] 인증 조회 ex) 2023.07.10에 등록한 인증
     * @param userId, date
     * @return List<CertResDTO>
     */
    @GetMapping("/date")
    public ResponseEntity getCertByDate(@RequestParam Integer userId, @RequestParam String date) {
        return SuccessReturn(certService.getCertByDateAndUser(userId, LocalDate.parse(date)));
    }

    /**
     * [Category] 인증 조회  ex) CA0000(전체 조회), CA0002(카페 조회)
     * @param userId, categoryCode, pageable
     * @return PageResDTO<CertResDTO, Integer>
     */
    @GetMapping("/category")
    public ResponseEntity getCertByCategory(
            @RequestParam Integer userId,
            @RequestParam String categoryCode,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        if (categoryCode.isBlank()) return ErrorReturn(APICode.PARAM_ERROR);
        return SuccessReturn(certService.getCertListByCategory(userId, categoryCode, pageable));
    }

    /**
     * [Recent] 인증 조회
     * @param userId, count(조회 개수)
     * @return PageResDTO<CertResDTO, Integer>
     */
    @GetMapping("/recent")
    public ResponseEntity getRecentCert(@RequestParam Integer userId, @RequestParam Integer count) {
        return SuccessReturn(certService.getRecentCert(userId, count));
    }

    /**
     * [Mungple] 인증 조회
     * @param userId, mungpleId, pageable
     * @return PageResDTO<CertByMungpleResDTO, Integer>
     */
    @GetMapping("/mungple")
    public ResponseEntity getCertByMungple(
            @RequestParam Integer userId,
            @RequestParam Integer mungpleId,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        return SuccessReturn(certService.getCertListByMungpleId(userId, mungpleId, pageable));
    }

    /**
     * 전체 인증 개수 조회
     * @param userId
     * @return int
     */
    @GetMapping(value = {"/count/{userId}", "/count/"})
    public ResponseEntity getTotalCertCount(@PathVariable Integer userId) {
        return SuccessReturn(certService.getTotalCertCountByUser(userId));
    }

    /**
     * [Category] 인증 개수 조회
     * @param userId
     * @return Map<String, Long>
     */
    @GetMapping(value = {"/category/count/{userId}", "/category/count/"})
    public ResponseEntity getCertCountByCategory(@PathVariable Integer userId) {
        return SuccessReturn(certService.getCountByCategory(userId));
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
        Certification updatedCertification = certService.modify(certification, record);

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

        certService.delete(certificationId);
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