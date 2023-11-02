package com.delgo.reward.certification.controller;


import com.delgo.reward.certification.domain.*;
import com.delgo.reward.certification.service.CertPhotoService;
import com.delgo.reward.certification.service.CertService;
import com.delgo.reward.certification.service.ReactionService;
import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.async.CertAsyncService;
import com.delgo.reward.comm.async.ClassificationAsyncService;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.certification.controller.response.CertResponse;
import com.delgo.reward.certification.controller.response.PageCertResponse;
import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.dto.comm.PageCustom;
import com.delgo.reward.mongoService.ClassificationService;
import com.delgo.reward.certification.domain.request.CertCreate;
import com.delgo.reward.certification.domain.request.CertUpdate;
import com.delgo.reward.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certification")
public class CertController extends CommController {

    private final UserService userService;
    private final CertService certService;
    private final ReactionService reactionService;
    private final CertPhotoService certPhotoService;

    private final CertAsyncService certAsyncService;
    private final ClassificationService classificationService;
    private final ClassificationAsyncService classificationAsyncService;

    @Operation(summary = "인증 List 조회 [Paging]", description = "isCorrect = true인 인증 조회 \n /recent 해당 API로 수정 가능")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResponse.class))})
    @GetMapping("/all")
    public ResponseEntity getList(@RequestParam Integer userId, @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageCustom<Certification> page = certService.getListByCondition(CertCondition.from(true, pageable));
        return SuccessReturn(PageCertResponse.from(userId, page, certPhotoService, reactionService));
    }

    @Operation(summary = "[Other] 다른 사용자 작성 인증 조회 [Paging]", description = "다른 사용자가 작성한 인증 조회 [권한 필요 없음]")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResponse.class))})
    @GetMapping("/other")
    public ResponseEntity getCorrectListByUser(@RequestParam Integer userId, @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageCustom<Certification> page = certService.getListByCondition(CertCondition.byUser(userId,true, pageable));
        return SuccessReturn(PageCertResponse.from(userId, page, certPhotoService, reactionService)
                .setViewCount(userService.getUserById(userId).getViewCount())); // viewCount 설정
    }

    @Operation(summary = "[Mungple] 인증 조회 [Paging]", description ="mungpleId로 멍플 인증 조회 [권한 필요 없음]")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResponse.class))})
    @GetMapping("/mungple")
    public ResponseEntity getListByMungple(
            @RequestParam Integer userId,
            @RequestParam Integer mungpleId,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageCustom<Certification> page = certService.getListByCondition(CertCondition.byMungple(mungpleId,true, pageable));
        return SuccessReturn(PageCertResponse.from(userId, page, certPhotoService, reactionService));
    }

    @Operation(summary = "[certId] 인증 조회", description ="고유 ID로 인증 데이터 조회  \n  단 건이지만 Front 요청으로 LIST로 반환")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CertResponse.class)))})
    @GetMapping("/id")
    public ResponseEntity getById(@RequestParam Integer userId, @RequestParam Integer certificationId) {
        Certification cert = certService.getById(certificationId);
        List<CertPhoto> photoList = certPhotoService.getListByCertId(certificationId);
        List<Reaction> reactionList = reactionService.getListByCertId(certificationId);

        return SuccessReturn(Collections.singletonList(CertResponse.from(userId, cert, photoList, reactionList)));
    }

    // ---------------------------------권한 필요--------------------------------------------


    @Operation(summary = "인증 생성")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CertResponse.class))})
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> create(@Validated @RequestPart(value = "data") CertCreate certCreate, @RequestPart List<MultipartFile> photos) throws JsonProcessingException {
        Certification cert = (certCreate.mungpleId() == 0)
                ? certService.createByMungple(certCreate)
                : certService.create(certCreate);

        List<CertPhoto> photoList = certPhotoService.create(cert.getCertificationId(), photos);

        // 비동기 실행
        certAsyncService.doSomething(cert.getCertificationId());
        classificationAsyncService.doClassification(cert.getCertificationId());

        return SuccessReturn(CertResponse.from(certCreate.userId(), cert, photoList, new ArrayList<>()));
    }

    @Operation(summary = "인증 수정", description = "인증 수정 - 내용, 주소 숨김 여부만 수정 가능")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CertResponse.class))})
    @PutMapping
    public ResponseEntity update(@Validated @RequestBody CertUpdate certUpdate) {
        if(certService.validate(certUpdate.userId(), certUpdate.certificationId()))
            return ErrorReturn(APICode.INVALID_USER_ERROR);

        // 인증 분류 삭제
        classificationService.deleteClassificationWhenModifyCert(certUpdate.certificationId(), certUpdate.userId());

        Certification updatedCert = certService.update(certUpdate);
        List<CertPhoto> photoList = certPhotoService.getListByCertId(certUpdate.certificationId());
        List<Reaction> reactionList = reactionService.getListByCertId(certUpdate.certificationId());

        // 비동기적 실행
        classificationAsyncService.doClassification(updatedCert.getCertificationId());
        return SuccessReturn(CertResponse.from(certUpdate.userId(), updatedCert, photoList, reactionList));
    }


    @Operation(summary = "[Date] 인증 조회", description ="특정 날짜에 인증한 모든 인증글 조회 [캘린더] \n  ex) 2023.07.10에 등록한 인증")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CertResponse.class)))})
    @GetMapping("/date")
    public ResponseEntity getListByDateAndUser(@RequestParam Integer userId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        PageCustom<Certification> page = certService.getListByCondition(CertCondition.byDateAndUser(date, userId, true, Pageable.unpaged()));
        Map<Integer,List<CertPhoto>> photoMap = certPhotoService.getMapByCertList(page.getContent());
        Map<Integer,List<Reaction>> reactionMap = reactionService.getMapByCertList(page.getContent());

        return SuccessReturn(page.getContent().stream().map(cert -> {
            List<CertPhoto> photoList = photoMap.get(cert.getCertificationId());
            List<Reaction> reactionList = reactionMap.get(cert.getCertificationId());

            return CertResponse.from(userId, cert, photoList, reactionList);
        }).toList());
    }

    @Operation(summary = "[My] 내가 작성한 인증 조회 [paging]", description = "내가 작성한 모든 인증글 조회 [ \n isCorrect = false 여도 조회.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCertResponse.class))})
    @GetMapping("/my")
    public ResponseEntity getListByUser(
            @RequestParam Integer userId,
            @RequestParam(required = false) boolean unPaged,
            @PageableDefault(sort = "registDt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageCustom<Certification> page = certService.getListByCondition(CertCondition.byUser(userId, null, unPaged ? Pageable.unpaged() : pageable));
        PageCertResponse response = PageCertResponse.from(userId, page, certPhotoService, reactionService);

        int viewCount = userService.getUserById(userId).getViewCount();
        response.setViewCount(viewCount);

        return SuccessReturn(viewCount);
    }

    @Operation(summary = "인증 삭제")
    @DeleteMapping(value = {"/{userId}/{certificationId}"})
    public ResponseEntity delete(@PathVariable Integer userId, @PathVariable Integer certificationId) {
        if(certService.validate(userId, certificationId))
            return ErrorReturn(APICode.INVALID_USER_ERROR);

        // 인증 분류 삭제
        classificationService.deleteClassificationWhenModifyCert(certificationId, userId);

        certService.delete(certificationId);
        reactionService.deleteByCertId(certificationId);
        return SuccessReturn();
    }

    @Operation(summary = "인증 Reaction", description = "Reaction 등록 및 수정 - 인증에 대한 반응을 등록 및 수정")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Reaction.class))})
    @PostMapping(value = {"/reaction/{userId}/{certificationId}/{reactionCode}"})
    public ResponseEntity reaction(@PathVariable Integer userId, @PathVariable Integer certificationId, @PathVariable ReactionCode reactionCode){
        return SuccessReturn((reactionService.hasReaction(userId, certificationId, reactionCode))
                ? reactionService.update(userId, certificationId, reactionCode)
                : reactionService.create(userId, certificationId, reactionCode));
    }
}