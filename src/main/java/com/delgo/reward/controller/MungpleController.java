package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.record.mungple.MungpleDetailRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mungple")
public class MungpleController extends CommController {
    private final MongoMungpleService mongoMungpleService;

    /**
     * [Category] Mungple 조회 - (CA0000: 전체 조회)
     * @param categoryCode
     * @return List<MungpleResDTO>
     *
     */
    @GetMapping(value={"/category/{categoryCode}","/category"})
    public ResponseEntity getMungplesByCategory(@PathVariable String categoryCode) {
        if (categoryCode.isBlank()) return ParamErrorReturn("categoryCode"); // Validate - Blank Check
        return SuccessReturn(mongoMungpleService.getMungpleByCategoryCode(categoryCode));
    }

    /**
     * [인증 개수 많은 순] Mungple 조회 - (추후 광고 관련 코드 넣을 예정)
     * @param count
     * @return List<MungpleResDTO>
     *
     */
    @GetMapping("/top")
    public ResponseEntity getMungpleOfMostCertCount(@RequestParam Integer count) {
        return SuccessReturn(mongoMungpleService.getMungpleOfMostCertCount(count));
    }

    /**
     * Mungple 삭제
     * @param mungpleId
     */
    @DeleteMapping(value={"/{mungpleId}",""})
    public ResponseEntity deleteMungple(@PathVariable Integer mungpleId) {
        mongoMungpleService.deleteMungple(mungpleId);
        return SuccessReturn();
    }

    // -------------------------------------- DETAIL --------------------------------------

    /**
     * Mungple Detail 등록
     * @param record
     * @return MungpleDetailResDTO
     */
    @PostMapping("/detail")
    public ResponseEntity createMungpleDetail(@RequestBody MungpleDetailRecord record){
        if(mongoMungpleService.isExist(record.mungpleId()))
            return ErrorReturn(APICode.MUNGPLE_DUPLICATE_ERROR);

        return SuccessReturn(mongoMungpleService.createMungpleDetail(record));
    }

    /**
     * [MungpleId] Mungple Detail 조회
     * @param mungpleId
     * @return MungpleDetailResDTO
     */
    @GetMapping("/detail")
    public ResponseEntity getMungpleDetailByMungpleId(@RequestParam int mungpleId) {
        return SuccessReturn(mongoMungpleService.getMungpleDetailByMungpleId(mungpleId));
    }
}