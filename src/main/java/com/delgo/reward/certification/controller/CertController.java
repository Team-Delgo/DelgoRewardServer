package com.delgo.reward.certification.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.async.CertAsyncService;
import com.delgo.reward.comm.async.ClassificationAsyncService;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.domain.certification.Reaction;
import com.delgo.reward.certification.controller.res.CertResponse;
import com.delgo.reward.certification.controller.res.PageCertResponse;
import com.delgo.reward.dto.comm.Page;
import com.delgo.reward.mongoService.ClassificationService;
import com.delgo.reward.certification.domain.CertCreate;
import com.delgo.reward.certification.domain.CertUpdate;
import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.certification.service.CertService;
import com.delgo.reward.service.ReactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
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
     * 인증 List 조회
     * @return PageCertResDTO
     */
    @Operation(summary = "인증 List 조회 [Paging]", description = "모든 isCorrect = true인 인증 조회")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResponse.class))})
    @GetMapping("/all")
    public ResponseEntity getAllCert(@RequestParam Integer userId, @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Certification> page = certService.getListByCondition(CertCondition.builder()
                        .isCorrect(true)
                        .pageable(pageable)
                        .build());

        return SuccessReturn(new PageCertResponse(page, userId));
    }

    /**
     * [Other]다른 사용자 작성 인증 조회  ex) CA0000(전체 조회), CA0002(카페 조회)
     * @param userId, categoryCode, pageable
     * @return PageCertResDTO
     */
    @Operation(summary = "다른 사용자 작성 인증 조회 [Paging]", description = "다른 사용자가 작성한 인증 조회 [권한 필요 없음]")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResponse.class))})
    @GetMapping("/other")
    public ResponseEntity getOtherCerts(@RequestParam Integer userId, @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Certification> page = certService.getListByCondition(
                CertCondition.builder()
                        .userId(userId)
                        .isCorrect(true)
                        .pageable(pageable)
                        .build());

        return SuccessReturn(new PageCertResponse(page, userId));
    }

    /**
     * [Mungple] 인증 조회
     * @param userId, mungpleId, pageable
     * @return PageResDTO<CertByMungpleResDTO, Integer>
     */
    @Operation(summary = "[Mungple] 인증 조회 [Paging]", description ="mungpleId로 멍플 인증 조회 [권한 필요 없음]")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResponse.class))})
    @GetMapping("/mungple")
    public ResponseEntity getCertsByMungple(
            @RequestParam Integer userId,
            @RequestParam Integer mungpleId,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Certification> page = certService.getListByCondition(
                CertCondition.builder()
                        .mungpleId(mungpleId)
                        .isCorrect(true)
                        .pageable(pageable)
                        .build());

        return SuccessReturn(new PageCertResponse(page, userId));
    }

    /**
     * [certId] 인증 조회
     * @param userId, certificationId
     * @return List<CertResDTO>
     */
    @Operation(summary = "[certId] 인증 조회", description ="고유 ID로 인증 데이터 조회  \n  단 건이지만 Front 요청으로 LIST로 반환")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CertResponse.class)))})
    @GetMapping("/id")
    public ResponseEntity getCertsByCertId(@RequestParam Integer userId, @RequestParam Integer certificationId) {
        Certification certification = certService.getById(certificationId);

        return SuccessReturn(Collections.singletonList(new CertResponse(certification, userId)));
    }

    // ---------------------------------권한 필요--------------------------------------------

    /**
     * 인증 생성
     * @param record, photo
     * @return CertByAchvResDTO
     */
    @Operation(summary = "인증 생성", description ="인증 생성")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CertResponse.class))})
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createCert(@Validated @RequestPart(value = "data") CertCreate record, @RequestPart(required = false) List<MultipartFile> photos) throws JsonProcessingException {
        if (photos.isEmpty()) ErrorReturn(APICode.PARAM_ERROR);

        Certification cert = certService.create(record, photos);

        // 비동기적 실행
        certAsyncService.doSomething(cert.getCertificationId());
        classificationAsyncService.doClassification(cert.getCertificationId());

        return SuccessReturn(new CertResponse(cert, record.userId()));
    }

    /**
     * [Date] 인증 조회 ex) 2023.07.10에 등록한 인증
     * @param userId, date
     * @return List<CertResDTO>
     */
    @Operation(summary = "[Date] 인증 조회", description ="특정 날짜에 인증한 모든 인증글 조회 [캘린더] \n  ex) 2023.07.10에 등록한 인증")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CertResponse.class)))})
    @GetMapping("/date")
    public ResponseEntity getCertsByDate(@RequestParam Integer userId, @RequestParam String date) {
        Page<Certification> page = certService.getListByCondition(CertCondition.builder()
                .userId(userId)
                .date(LocalDate.parse(date))
                .pageable(Pageable.unpaged())
                .build());

        return SuccessReturn(page.getContent().stream().map(c -> new CertResponse(c, userId)).toList());
    }

    /**
     * [My]내가 작성한 인증 조회
     * @param userId, categoryCode, pageable
     * @return PageResDTO<CertResDTO, Integer>
     */
    @Operation(summary = "[My] 내가 작성한 인증 조회 [paging]", description = "내가 작성한 모든 인증글 조회 [ \n isCorrect = false 여도 조회.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResponse.class))})
    @GetMapping("/my")
    public ResponseEntity getMyCerts(
            @RequestParam Integer userId,
            @RequestParam(required = false) boolean unPaged,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Certification> page = certService.getListByCondition(
                CertCondition.builder()
                        .userId(userId)
                        .pageable(unPaged? Pageable.unpaged() :pageable)
                        .build());

        return SuccessReturn(new PageCertResponse(page, userId));
    }


    /**
     * 인증 수정
     * @param record
     * @return CertResDTO
     */
    @Operation(summary = "인증 수정", description = "인증 수정 - 내용, 주소 숨김 여부만 수정 가능")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CertResponse.class))})
    @PutMapping
    public ResponseEntity modifyCert(@Validated @RequestBody CertUpdate record) {
        Certification certification = certService.getById(record.certificationId());
        if (record.userId() != certification.getUser().getUserId())
            return ErrorReturn(APICode.INVALID_USER_ERROR);

        // 인증 분류 삭제
        classificationService.deleteClassificationWhenModifyCert(certification);
        Certification updatedCertification = certService.modifyCert(certification, record);

        // 비동기적 실행
        classificationAsyncService.doClassification(updatedCertification.getCertificationId());
        return SuccessReturn(new CertResponse(updatedCertification));
    }

    /**
     * 인증 삭제
     * @param userId, certificationId
     */
    @Operation(summary = "인증 삭제", description = "인증 삭제")
    @DeleteMapping(value = {"/{userId}/{certificationId}"})
    public ResponseEntity deleteCert(@PathVariable Integer userId, @PathVariable Integer certificationId) {
        if (!Objects.equals(userId, certService.getById(certificationId).getUser().getUserId()))
            return ErrorReturn(APICode.INVALID_USER_ERROR);

        // 인증 분류 삭제
        classificationService.deleteClassificationWhenModifyCert(certService.getById(certificationId));

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
    public ResponseEntity reaction(@PathVariable Integer userId, @PathVariable Integer certificationId, @PathVariable String reactionCode){
        return SuccessReturn(reactionService.reaction(userId, certificationId, ReactionCode.from(reactionCode)));
    }

    /**
     * [Recent] 인증 조회 TODO [ Deprecated ]
     * @param userId, count(조회 개수)
     * @return List<CertResDTO>
     */
    @Operation(summary = "[Recent] 인증 조회", description = "param count 만큼 최근 인증 조회")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CertResponse.class)))})
    @GetMapping("/recent")
    public ResponseEntity getRecentCerts(@RequestParam Integer userId, @RequestParam Integer count) {
        PageRequest pageable = PageRequest.of(0, count, Sort.by(Sort.Direction.DESC, "registDt"));
        Page<Certification> page = certService.getListByCondition(
                CertCondition.builder()
                        .isCorrect(true)
                        .pageable(pageable)
                        .build());

        return SuccessReturn(page.getContent().stream()
                .map(cert -> new CertResponse(cert, userId))
                .toList());
    }
}