package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.certification.LiveCertDTO;
import com.delgo.reward.dto.certification.ModifyCertDTO;
import com.delgo.reward.dto.certification.PastCertDTO;
import com.delgo.reward.service.CertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/certification")
public class CertController extends CommController {

    private final GeoService geoService;
    private final CertService certService;

    /*
     * 인증 등록 [Live]
     * Request Data : LiveCertDTO
     * Response Data : 등록한 인증 데이터 반환
     */
    @PostMapping("/live")
    public ResponseEntity registerLive(@Validated @RequestBody LiveCertDTO dto) {
        // 하루에 같은 카테고리 5번 이상 인증 불가능
        if (!certService.checkCategoryCountIsFive(dto.getUserId(), dto.getCategoryCode(), true))
            return ErrorReturn(ApiCode.CERTIFICATION_CATEGPRY_COUNT_ERROR);
        // 멍플 인증 + 100m 이상 떨어진 곳에서 인증시 인증 불가
        if (dto.getMungpleId() != 0 && geoService.getDistance(dto.getMungpleId(), dto.getLongitude(), dto.getLatitude()) > 100)
            return ErrorReturn(ApiCode.TOO_FAR_DISTANCE);

        return SuccessReturn(certService.registerLive(dto));
    }

    /*
     * 인증 등록 [Past]
     * Request Data : PastCertificationDTO
     * Response Data : 등록한 인증 데이터 반환
     */
    @PostMapping("/past")
    public ResponseEntity registerPast(@Validated @RequestBody PastCertDTO dto) {
        return SuccessReturn(certService.registerPast(dto));
    }

    /*
     * 인증 수정
     * Request Data : CertificationModifyDTO
     * Response Data : 등록한 인증 데이터 반환
     */
    @PutMapping
    public ResponseEntity modify(@Validated @RequestBody ModifyCertDTO dto) {
        return SuccessReturn(certService.modify(dto));
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
            @RequestParam Integer currentPage,
            @RequestParam Integer pageSize,
            @RequestParam Boolean isDesc) {
        // Validate - Blank Check; [ String 만 해주면 됨 ]
        if (categoryCode.isBlank()) return ErrorReturn(ApiCode.PARAM_ERROR);
        return SuccessReturn(certService.getCertByCategory(userId, categoryCode, currentPage, pageSize, isDesc));
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
     * 인증 게시글의 좋아요 + 1
     * Request Data : userId, certificationId
     * - plusLikeCount: JPA 안 쓰고 JDBC_TEMPLATE 사용한 이유
     * : 업데이트 속도 때문에, JPA로 업데이트 할 경우 너무 느려서 놓치는 요청이 발생.
     * Response Data : X
     */
    @PostMapping(value = {"/like/{userId}/{certificationId}", "/like/"})
    public ResponseEntity setLike(@PathVariable Integer userId, @PathVariable Integer certificationId) throws IOException {
        certService.like(userId, certificationId, certService.getCert(certificationId).getUserId());

        return SuccessReturn();
    }

    /*
     * 가장 최근 등록한 인증 반환 [ Main ]
     * Request Data :
     * Response Data : 최근 등록 인증 2개 반환
     */
    @GetMapping("/main")
    public ResponseEntity getMainData(@RequestParam Integer userId) {
        return SuccessReturn(certService.getTheLastTwoCert(userId));
    }

    /*
     * 모든 인증 리스트 페이징으로 반환 [ Main ]
     * Request Data : currentPage ( 현재 페이지 번호 ), pageSize ( 페이지 크기 )
     * Response Data : 인증 모두 조회 ( 페이징 처리 되어 있음 )
     */
    @GetMapping("/all")
    public ResponseEntity getPagingData(@RequestParam Integer userId, @RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        return SuccessReturn(certService.getCertAll(userId, currentPage, pageSize, true));
    }

    /*
     * 인증 삭제
     * Request Data : userId ( 삭제 요청 userId ), certificationId ( 삭제할 인증 )
     * 요청 userId랑 등록 userId랑 비교 해야 함.
     * Response Data : null
     */
    @DeleteMapping(value = {"/{userId}/{certificationId}"})
    public ResponseEntity delete(@PathVariable Integer userId, @PathVariable Integer certificationId) {
        Certification certification = certService.getCert(certificationId);

        if (!Objects.equals(userId, certification.getUserId()))
            return ErrorReturn(ApiCode.INVALID_USER_ERROR);

        certService.delete(certification);
        return SuccessReturn();
    }
}