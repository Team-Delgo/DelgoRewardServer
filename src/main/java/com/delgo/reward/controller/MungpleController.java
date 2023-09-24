package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.MungpleSort;
import com.delgo.reward.comm.googlesheet.GoogleSheetService;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.record.mungple.MungpleDetailRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mungple")
public class MungpleController extends CommController {
    private final MongoMungpleService mongoMungpleService;
    private final GoogleSheetService googleSheetService;

    /**
     * Mungple 등록 From GoogleSheet, Figma
     * @return MungpleDetailResDTO
     */
    @GetMapping("/parsing")
    public ResponseEntity parsingGoogleSheetMungple() {
        List<String> placeNames = googleSheetService.saveSheetsDataToDB();

        return SuccessReturn(placeNames);
    }

    /**
     * 모든 멍플 조회 [ 지도, 검색 리스트 생성 ]
     *
     * @return List<MungpleResDTO>
     */
    @GetMapping
    public ResponseEntity getMungples() {
        return SuccessReturn(mongoMungpleService.getAllActiveMungple());
    }

    /**
     * [Category] Mungple 조회 - (CA0000: 전체 조회) [TODO Deprecated]
     * @param categoryCode
     * @return List<MungpleResDTO>
     *
     */
    @GetMapping("/category/{categoryCode}")
    public ResponseEntity getMungplesByCategoryDeprecated(@PathVariable CategoryCode categoryCode) {
        return SuccessReturn(mongoMungpleService.getActiveMungpleByCategoryCode(categoryCode));
    }

    /**
     * [Category] Mungple 조회 - (CA0000: 전체 조회)
     *
     * @param categoryCode. sort, latitude, longitude
     * @return List<MungpleResDTO>
     */
    @GetMapping("/category")
    public ResponseEntity getMungplesByCategory(
            @RequestParam CategoryCode categoryCode,
            @RequestParam MungpleSort sort,
            @RequestParam(required = false) String latitude,
            @RequestParam(required = false) String longitude) {
        return SuccessReturn(mongoMungpleService.getActiveMungpleByCategoryCode(categoryCode, sort, latitude, longitude));
    }

    /**
     * [Bookmark] Mungple 조회
     * @param userId
     * @return List<MungpleResDTO>
     *
     */
    @GetMapping("/bookmark")
    public ResponseEntity getMungplesByBookmark(
            @RequestParam int userId,
            @RequestParam MungpleSort sort,
            @RequestParam(required = false) String latitude,
            @RequestParam(required = false) String longitude) {
        return SuccessReturn(mongoMungpleService.getActiveMungpleByBookMark(userId, sort, latitude, longitude));
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
    public ResponseEntity getMungpleDetailByMungpleIdAndUserId(@RequestParam int mungpleId, @RequestParam int userId) {
        return SuccessReturn(mongoMungpleService.getMungpleDetailByMungpleIdAndUserId(mungpleId, userId));
    }

    /**
     * Mungple Cache Reset
     */
    @PutMapping("/cache")
    public ResponseEntity resetMungpleCache() {
        mongoMungpleService.resetMungpleCache();
        return SuccessReturn();
    }
}