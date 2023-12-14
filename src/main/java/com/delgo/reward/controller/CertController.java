package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.async.CertAsyncService;
import com.delgo.reward.comm.async.ClassificationAsyncService;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.certification.Reaction;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.cert.CertResponse;
import com.delgo.reward.dto.cert.PageCertResponse;
import com.delgo.reward.mongoService.ClassificationService;
import com.delgo.reward.record.certification.CertCreate;
import com.delgo.reward.record.certification.CertUpdate;
import com.delgo.reward.service.cert.CertCommandService;
import com.delgo.reward.service.ReactionService;
import com.delgo.reward.service.UserService;
import com.delgo.reward.service.cert.CertQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certification")
public class CertController extends CommController {

    private final UserService userService;
    private final ReactionService reactionService;
    private final CertAsyncService certAsyncService;
    private final CertQueryService certQueryService;
    private final CertCommandService certCommandService;
    private final ClassificationService classificationService;
    private final ClassificationAsyncService classificationAsyncService;

    /**
     * 전체 인증 조회
     */
    @Operation(summary = "전체 인증 조회 [Paging]", description = "모든 isCorrect = true인 인증 조회 \n certificationId의 경우 특정 인증을 제외 하고 싶을 때 사용")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResponse.class))})
    @GetMapping("/all")
    public ResponseEntity getAllCert(
            @RequestParam Integer userId,
            @Parameter(description = "조회에서 제외할 인증 번호") @RequestParam(required = false) Integer certificationId,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Certification> page = certQueryService.getCorrectPagingList(userId, pageable);
        Map<Integer, List<Reaction>> reactionMap = reactionService.getMapByCertList(page.getContent());

        return SuccessReturn(PageCertResponse.from(userId, page, reactionMap));
    }

    /**
     * [Other]다른 사용자 작성 인증 조회  ex) CA0000(전체 조회), CA0002(카페 조회)
     */
    @Operation(summary = "다른 사용자 작성 인증 조회 [Paging]", description = "다른 사용자가 작성한 인증 조회 [권한 필요 없음]")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResponse.class))})
    @GetMapping("/other")
    public ResponseEntity getOtherCerts(
            @RequestParam Integer userId,
            @RequestParam CategoryCode categoryCode,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Certification> page = categoryCode.equals(CategoryCode.CA0000)
                ? certQueryService.getCorrectPagingListByUserId(userId, pageable)
                : certQueryService.getCorrectPagingListByUserIdAndCategoryCode(userId, categoryCode, pageable);

        Map<Integer, List<Reaction>> reactionMap = reactionService.getMapByCertList(page.getContent());
        User user = userService.getUserById(userId);

        return SuccessReturn(PageCertResponse.from(userId, page, reactionMap)
                .setViewCount(user.getViewCount()));
    }


    /**
     * [Mungple] 인증 조회
     */
    @Operation(summary = "[Mungple] 인증 조회 [Paging]", description ="mungpleId로 멍플 인증 조회 [권한 필요 없음]")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResponse.class))})
    @GetMapping("/mungple")
    public ResponseEntity getCertsByMungple(
            @RequestParam Integer userId,
            @RequestParam Integer mungpleId,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Certification> page =  certQueryService.getPagingListByMungpleId(userId, mungpleId, pageable);
        Map<Integer, List<Reaction>> reactionMap = reactionService.getMapByCertList(page.getContent());

        return SuccessReturn(PageCertResponse.from(userId, page, reactionMap));
    }

    /**
     * [certId] 인증 조회
     */
    @Operation(summary = "[certId] 인증 조회", description ="고유 ID로 인증 데이터 조회  \n  단 건이지만 Front 요청으로 LIST로 반환")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CertResponse.class)))})
    @GetMapping("/id")
    public ResponseEntity getCertsByCertId(@RequestParam Integer userId, @RequestParam Integer certificationId) {
        Certification certification = certQueryService.getOneById(certificationId);
        List<Reaction> reactionList = reactionService.getListByCertId(certificationId);

        return SuccessReturn(new ArrayList<>(Collections.singletonList(CertResponse.from(userId, certification, reactionList))));
    }

    // ---------------------------------권한 필요--------------------------------------------

    /**
     * 인증 생성
     */
    @Operation(summary = "인증 생성", description ="인증 생성")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CertResponse.class))})
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createCert(@Validated @RequestPart(value = "data") CertCreate certCreate, @RequestPart(required = false) List<MultipartFile> photos) {
        if (photos.isEmpty()) ErrorReturn(APICode.PARAM_ERROR);
        Certification certification = (certCreate.mungpleId() == 0)
                ? certCommandService.create(certCreate, photos)
                : certCommandService.createByMungple(certCreate, photos);

        // 비동기적 실행
        certAsyncService.convertAndUpload(certification.getCertificationId());
        classificationAsyncService.doClassification(certification.getCertificationId());

        return SuccessReturn(CertResponse.from(certCreate.userId(), certification, new ArrayList<>()));
    }

    /**
     * [Date] 인증 조회 ex) 2023.07.10에 등록한 인증
     */
    @Operation(summary = "[Date] 인증 조회", description = "특정 날짜에 인증한 모든 인증글 조회 [캘린더] \n  ex) 2023.07.10에 등록한 인증")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array =
    @ArraySchema(schema = @Schema(implementation = CertResponse.class)))})
    @GetMapping("/date")
    public ResponseEntity getCertsByDate(@RequestParam Integer userId, @RequestParam String date) {
        List<Certification> certificationList = certQueryService.getListByDate(userId, LocalDate.parse(date));
        Map<Integer, List<Reaction>> reactionMap = reactionService.getMapByCertList(certificationList);

        return SuccessReturn(CertResponse.fromList(userId, certificationList, reactionMap));
    }

    /**
     * [My]내가 작성한 인증 조회  ex) CA0000(전체 조회), CA0002(카페 조회)
     */
    @Operation(summary = "[My] 내가 작성한 인증 조회 [paging]", description = "내가 작성한 모든 인증글 조회 [ \n isCorrect = false 여도 조회.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResponse.class))})
    @GetMapping("/my")
    public ResponseEntity getMyCerts(
            @RequestParam Integer userId,
            @RequestParam CategoryCode categoryCode,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Certification> page = categoryCode.equals(CategoryCode.CA0000)
                ? certQueryService.getPagingListByUserId(userId, pageable)
                : certQueryService.getPagingListByUserIdAndCategoryCode(userId, categoryCode, pageable);

        Map<Integer, List<Reaction>> reactionMap = reactionService.getMapByCertList(page.getContent());
        User user = userService.getUserById(userId);

        return SuccessReturn(PageCertResponse.from(userId, page, reactionMap)
                .setViewCount(user.getViewCount()));
    }

    /**
     * [My]내가 작성한 인증 전체 조회
     */
    @Operation(summary = "[My] 내가 작성한 인증 전체 조회", description = "내가 작성한 모든 인증글 조회 \n isCorrect = false 여도 조회.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResponse.class))})
    @GetMapping("/my/all")
    public ResponseEntity getAllMyCerts(@RequestParam Integer userId) {
        Page<Certification> page = certQueryService.getPagingListByUserId(userId, Pageable.unpaged());
        Map<Integer, List<Reaction>> reactionMap = reactionService.getMapByCertList(page.getContent());

        return SuccessReturn(CertResponse.fromList(userId, page.getContent(), reactionMap));
    }

    /**
     * [Recent] 인증 조회
     */
    @Operation(summary = "[Recent] 인증 조회", description = "param count 만큼 최근 인증 조회")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CertResponse.class)))})
    @GetMapping("/recent")
    public ResponseEntity getRecentCerts(@RequestParam Integer userId, @RequestParam Integer count) {
        Page<Certification> page = certQueryService.getCorrectPagingList(userId, PageRequest.of(0, count));
        Map<Integer, List<Reaction>> reactionMap = reactionService.getMapByCertList(page.getContent());

        return SuccessReturn(CertResponse.fromList(userId, page.getContent(), reactionMap));
    }

    /**
     * 인증 수정
     */
    @Operation(summary = "인증 수정", description = "인증 수정 - 내용, 주소 숨김 여부만 수정 가능")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CertResponse.class))})
    @PutMapping
    public ResponseEntity modifyCert(@Validated @RequestBody CertUpdate certUpdate) {
        Certification certification = certQueryService.getOneById(certUpdate.certificationId());
        if (certUpdate.userId() != certification.getUser().getUserId())
            return ErrorReturn(APICode.INVALID_USER_ERROR);

        // 인증 분류 삭제
        classificationService.deleteClassificationWhenModifyCert(certification);
        Certification updatedCertification = certCommandService.update(certUpdate);
        List<Reaction> reactionList = reactionService.getListByCertId(certification.getCertificationId());

        // 비동기적 실행
        classificationAsyncService.doClassification(updatedCertification.getCertificationId());
        return SuccessReturn(CertResponse.from(certUpdate.userId(), updatedCertification, reactionList));
    }

    /**
     * 인증 삭제
     */
    @Operation(summary = "인증 삭제", description = "인증 삭제")
    @DeleteMapping(value = {"/{userId}/{certificationId}"})
    public ResponseEntity deleteCert(@PathVariable Integer userId, @PathVariable Integer certificationId) {
        if (!Objects.equals(userId, certQueryService.getOneById(certificationId).getUser().getUserId()))
            return ErrorReturn(APICode.INVALID_USER_ERROR);

        // 인증 분류 삭제
        classificationService.deleteClassificationWhenModifyCert(certQueryService.getOneById(certificationId));

        certCommandService.delete(certificationId);
        reactionService.deleteByCertId(certificationId);
        return SuccessReturn();
    }

    /**
     * 인증 Reaction
     */
    @Operation(summary = "인증 Reaction", description = "Reaction 등록 및 수정 - 인증에 대한 반응을 등록 및 수정")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Reaction.class))})
    @PostMapping(value = {"/reaction/{userId}/{certificationId}/{reactionCode}"})
    public ResponseEntity reaction(@PathVariable Integer userId, @PathVariable Integer certificationId, @PathVariable ReactionCode reactionCode){
        return SuccessReturn((reactionService.hasReaction(userId, certificationId, reactionCode))
                ? reactionService.update(userId, certificationId, reactionCode)
                : reactionService.create(userId, certificationId, reactionCode));
    }
}