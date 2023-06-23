package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.record.mungple.MungpleDetailRecord;
import com.delgo.reward.record.mungple.MungpleRecord;
import com.delgo.reward.service.MungpleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mungple")
public class MungpleController extends CommController {

    private final MungpleService mungpleService;
    private final MongoMungpleService mongoMungpleService;

    /*
     * 멍플 등록
     * Request Data : MungpleDTO
     * - 주소로 위도, 경도 구할 수 있어야 함.
     * Response Data : 등록한 멍플 데이터 반환
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity register(@Validated @RequestPart(value = "data") MungpleRecord record, @RequestPart(required = false) MultipartFile photo) {
        return mungpleService.isMungpleExisting(record.address())
                ? ErrorReturn(APICode.MUNGPLE_DUPLICATE_ERROR) // 중복 에러
                : SuccessReturn(mungpleService.register(record, photo));
    }

    /*
     * 멍플 카테고리 별 조회
     * Request Data : categoryCodeF
     * - CA0000 = 전체 조회
     * Response Data : 카테고리별 멍플 리스트 반환
     */
    @GetMapping(value={"/category/{categoryCode}","/category"})
    public ResponseEntity getCategory(@PathVariable String categoryCode) {
        if (categoryCode.isBlank()) return ParamErrorReturn("categoryCode"); // Validate - Blank Check
        return SuccessReturn(mungpleService.getMungpleByCategoryCode(categoryCode));
    }

    /*
     * Mungple 인증 개수 많은 순으로 조회 ( 추후 광고 관련 코드 넣을 예정 )
     * Request Data : count
     * Response Data :
     */
    @GetMapping("/top")
    public ResponseEntity getTopData(@RequestParam Integer count) {
        return SuccessReturn(mungpleService.getMungpleOfMostCount(count));
    }

    /*
     * Mungple 삭제
     * Request Data : mungpleId
     * Response Data :
     */
    @DeleteMapping(value={"/{mungpleId}",""})
    public ResponseEntity deleteMungple(@PathVariable Integer mungpleId) {
        mungpleService.delete(mungpleId);
        return SuccessReturn();
    }

    // -------------------------------------- DETAIL --------------------------------------

    /**
     * 멍플 Id로 디테일 데이터 조회
     * @param mungpleId
     * @return 디테일 데이터
     */
    @GetMapping("/detail")
    public ResponseEntity getDetailDataByMungpleId(@RequestParam int mungpleId){
        return SuccessReturn(mongoMungpleService.getMungpleDetailDataByMungpleId(mungpleId));
    }

    /*
     * Mungple Detail 등록
     * Request Data : mungpleId
     * Response Data :
     */
    @PostMapping("/detail")
    public ResponseEntity createDetailData(@RequestBody MungpleDetailRecord record){
        if(mongoMungpleService.isExist(record.mungpleId()))
            return ErrorReturn(APICode.MUNGPLE_DUPLICATE_ERROR);

        return SuccessReturn(mongoMungpleService.createMungpleDetail(record));
    }
}