package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.async.CertAsyncService;
import com.delgo.reward.comm.async.ClassificationAsyncService;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.certification.Reaction;
import com.delgo.reward.dto.cert.CertResDTO;
import com.delgo.reward.dto.comm.PageCertByMungpleResDTO;
import com.delgo.reward.dto.comm.PageCertResDTO;
import com.delgo.reward.mongoService.ClassificationService;
import com.delgo.reward.record.certification.CertRecord;
import com.delgo.reward.record.certification.ModifyCertRecord;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.ReactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certification")
public class CertController extends CommController {

    private final CertService certService;
    private final ReactionService reactionService;
    private final CertAsyncService certAsyncService;
    private final ClassificationService classificationService;
    private final ClassificationAsyncService classificationAsyncService;

    /**
     * 전체 인증 조회
     * @param userId, certificationId(제외할 인증 번호), pageable
     * @return PageCertResDTO
     */
    @Operation(summary = "전체 인증 조회 [Paging]", description = "모든 isCorrect = true인 인증 조회 \n certificationId의 경우 특정 인증을 제외 하고 싶을 때 사용")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResDTO.class))})
    @GetMapping("/all")
    public ResponseEntity getAllCert(
            @RequestParam Integer userId,
            @Parameter(description = "조회에서 제외할 인증 번호") @RequestParam(required = false) Integer certificationId,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        return (certificationId == null)
                ? SuccessReturn(certService.getAllCert(userId, pageable))
                : SuccessReturn(certService.getAllCertExcludeSpecificCert(userId, certificationId,pageable));
    }

    /**
     * [Other]다른 사용자 작성 인증 조회  ex) CA0000(전체 조회), CA0002(카페 조회)
     * @param userId, categoryCode, pageable
     * @return PageCertResDTO
     */
    @Operation(summary = "다른 사용자 작성 인증 조회 [Paging]", description = "다른 사용자가 작성한 인증 조회 [권한 필요 없음]")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResDTO.class))})
    @GetMapping("/other")
    public ResponseEntity getOtherCerts(
            @RequestParam Integer userId,
            @RequestParam CategoryCode categoryCode,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        return SuccessReturn(certService.getOtherCerts(userId, categoryCode, pageable));
    }


    /**
     * [Mungple] 인증 조회
     * @param userId, mungpleId, pageable
     * @return PageResDTO<CertByMungpleResDTO, Integer>
     */
    @Operation(summary = "[Mungple] 인증 조회 [Paging]", description ="mungpleId로 멍플 인증 조회 [권한 필요 없음]")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertByMungpleResDTO.class))})
    @GetMapping("/mungple")
    public ResponseEntity getCertsByMungple(
            @RequestParam Integer userId,
            @RequestParam Integer mungpleId,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        return SuccessReturn(certService.getCertsByMungpleId(userId, mungpleId, pageable));
    }

    /**
     * [certId] 인증 조회
     * @param userId, certificationId
     * @return List<CertResDTO>
     */
    @Operation(summary = "[certId] 인증 조회", description ="고유 ID로 인증 데이터 조회  \n  단 건이지만 Front 요청으로 LIST로 반환")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CertResDTO.class)))})
    @GetMapping("/id")
    public ResponseEntity getCertsByCertId(@RequestParam Integer userId, @RequestParam Integer certificationId) {
        return SuccessReturn(certService.getCertsByIdWithLike(userId, certificationId));
    }

    // ---------------------------------권한 필요--------------------------------------------

    /**
     * 인증 생성
     * @param record, photo
     * @return CertByAchvResDTO
     */
    @Operation(summary = "인증 생성", description ="인증 생성")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CertResDTO.class))})
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createCert(@Validated @RequestPart(value = "data") CertRecord record, @RequestPart(required = false) List<MultipartFile> photos) throws JsonProcessingException {
        if(photos.isEmpty()) ErrorReturn(APICode.PARAM_ERROR);

        CertResDTO resDto = certService.createCert(record, photos);
        log.info("{}", resDto.getCertificationId());

        // 비동기적 실행
        certAsyncService.doSomething(resDto.getCertificationId());
        classificationAsyncService.doClassification(resDto.getCertificationId());

        return SuccessReturn(resDto);
    }

    /**
     * [Date] 인증 조회 ex) 2023.07.10에 등록한 인증
     * @param userId, date
     * @return List<CertResDTO>
     */
    @Operation(summary = "[Date] 인증 조회", description ="특정 날짜에 인증한 모든 인증글 조회 [캘린더] \n  ex) 2023.07.10에 등록한 인증")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CertResDTO.class)))})
    @GetMapping("/date")
    public ResponseEntity getCertsByDate(@RequestParam Integer userId, @RequestParam String date) {
        return SuccessReturn(certService.getCertsByDate(userId, LocalDate.parse(date)));
    }

    /**
     * [My]내가 작성한 인증 조회  ex) CA0000(전체 조회), CA0002(카페 조회)
     * @param userId, categoryCode, pageable
     * @return PageResDTO<CertResDTO, Integer>
     */
    @Operation(summary = "[My] 내가 작성한 인증 조회 [paging]", description = "내가 작성한 모든 인증글 조회 [ \n isCorrect = false 여도 조회.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResDTO.class))})
    @GetMapping("/my")
    public ResponseEntity getMyCerts(
            @RequestParam Integer userId,
            @RequestParam CategoryCode categoryCode,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        return SuccessReturn(certService.getMyCerts(userId, categoryCode, pageable));
    }

    /**
     * [My]내가 작성한 인증 전체 조회
     * @param userId, categoryCode, pageable
     * @return PageResDTO<CertResDTO, Integer>
     */
    @Operation(summary = "[My] 내가 작성한 인증 전체 조회", description = "내가 작성한 모든 인증글 조회 \n isCorrect = false 여도 조회.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResDTO.class))})
    @GetMapping("/my/all")
    public ResponseEntity getAllMyCerts(@RequestParam Integer userId) {
        return SuccessReturn(certService.getAllMyCerts(userId));
    }

    /**
     * [Recent] 인증 조회
     * @param userId, count(조회 개수)
     * @return List<CertResDTO>
     */
    @Operation(summary = "[Recent] 인증 조회", description = "param count 만큼 최근 인증 조회")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CertResDTO.class)))})
    @GetMapping("/recent")
    public ResponseEntity getRecentCerts(@RequestParam Integer userId, @RequestParam Integer count) {
        return SuccessReturn(certService.getRecentCerts(userId, count));
    }

    /**
     * 인증 수정
     * @param record
     * @return CertResDTO
     */
    @Operation(summary = "인증 수정", description = "인증 수정 - 내용, 주소 숨김 여부만 수정 가능")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CertResDTO.class))})
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
    @Operation(summary = "인증 삭제", description = "인증 삭제")
    @DeleteMapping(value = {"/{userId}/{certificationId}"})
    public ResponseEntity deleteCert(@PathVariable Integer userId, @PathVariable Integer certificationId) {
        if (!Objects.equals(userId, certService.getCertById(certificationId).getUser().getUserId()))
            return ErrorReturn(APICode.INVALID_USER_ERROR);

        // 인증 분류 삭제
        classificationService.deleteClassificationWhenModifyCert(certService.getCertById(certificationId));

        certService.deleteCert(certificationId);
        return SuccessReturn();
    }

    /**
     * 인증 Reaction
     * @param userId, certificationId, reactionCode
     */
    @Operation(summary = "인증 Reaction", description = "Reaction 등록 및 수정 - 인증에 대한 반응을 등록 및 수정")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Reaction.class))})
    @PostMapping(value = {"/reaction/{userId}/{certificationId}/{reactionCode}"})
    public ResponseEntity reaction(@PathVariable Integer userId, @PathVariable Integer certificationId, @PathVariable String reactionCode) throws IOException {
        return SuccessReturn(reactionService.reaction(userId, certificationId, ReactionCode.from(reactionCode)));
    }

    // ---------------------------------------- Deprecated ----------------------------------------

    /**
     * [User] 인증 개수 조회 [ Deprecated ]
     * @param userId
     * @return int
     */
    @Hidden
    @GetMapping(value = {"/count/{userId}", "/count/"})
    public ResponseEntity getCertCountByUser(@PathVariable Integer userId) {
        return SuccessReturn(certService.getCertCountByUser(userId));
    }
}