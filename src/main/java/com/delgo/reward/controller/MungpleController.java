package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.MungpleSort;
import com.delgo.reward.comm.googlesheet.GoogleSheetService;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.dto.mungple.detail.MungpleDetailResDTO;
import com.delgo.reward.mongoService.MongoMungpleService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
     */
    @Hidden
    @GetMapping("/parsing")
    public ResponseEntity parsingGoogleSheetMungple() {
        List<String> placeNames = googleSheetService.saveSheetsDataToDB();

        return SuccessReturn(placeNames);
    }

    /**
     * 모든 멍플 조회 [ 지도, 검색 리스트 생성 ] [Cache]
     */
    @Operation(summary = "ALL Active Mungple 조회 [Cache]", description = "모든 활성화 된 멍플 조회 [지도, 검색 리스트 생성에 사용]")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MungpleResDTO.class)))})
    @GetMapping
    public ResponseEntity getMungples() {
        return SuccessReturn(mongoMungpleService.getAllActiveMungple());
    }

    /**
     * [Category] Mungple 조회
     */
    @Operation(summary = "[Category] Mungple 조회", description = "Category 별로 Mungple 조회 [지도, 목록 조회 에서 사용]")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MungpleResDTO.class))})
    @GetMapping("/category")
    public ResponseEntity getMungplesByCategory(
            @RequestParam int userId, // isBookmarked 체크 필요
            @RequestParam CategoryCode categoryCode,
            @RequestParam MungpleSort sort,
            @RequestParam(required = false) String latitude,
            @RequestParam(required = false) String longitude) {
        return SuccessReturn(mongoMungpleService.getActiveMungpleByCategoryCode(userId, categoryCode, sort, latitude, longitude));
    }

    /**
     * [Bookmark] Mungple 조회
     */
    @Operation(summary = "[Bookmark] Mungple 조회", description = "특정 사용자가 저장한 멍플 조회 [ 목록 조회 에서 사용 ]")
    @ApiResponse(responseCode = "200", description = "", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MungpleResDTO.class))})
    @GetMapping("/bookmark")
    public ResponseEntity getMungplesByBookmark(
            @RequestParam int userId,
            @RequestParam MungpleSort sort,
            @RequestParam(required = false) String latitude,
            @RequestParam(required = false) String longitude) {
        return SuccessReturn(mongoMungpleService.getActiveMungpleByBookMark(userId, sort, latitude, longitude));
    }

    /**
     * Mungple 삭제
     */
    @Operation(summary = "Mungple 삭제", description = "멍플 삭제 API")
    @DeleteMapping("/{mungpleId}")
    public ResponseEntity deleteMungple(@PathVariable Integer mungpleId) {
        mongoMungpleService.deleteMungple(mungpleId);
        return SuccessReturn();
    }

    /**
     * Mungple Cache Reset
     */
    @Operation(summary = "Reset Mungple Cache", description = "멍플 등록 시, 혹은 업데이트 시 적용이 안될 때 Cache 업데이트를 위해 사용한다.")
    @GetMapping("/cache")
    public ResponseEntity resetMungpleCache() {
        mongoMungpleService.resetMungpleCache();
        return SuccessReturn();
    }

    // -------------------------------------- DETAIL --------------------------------------

    /**
     * [MungpleId] Mungple Detail 조회
     */
    @Operation(summary = "[MungpleId] Mungple Detail 조회", description = "멍플 상세 조회 [특정 사용자가 저장 했는지 여부 체크를 위해 UserId도 받는다]")
    @ApiResponse(responseCode = "200", description = "", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MungpleDetailResDTO.class))})
    @GetMapping("/detail")
    public ResponseEntity getMungpleDetailByMungpleIdAndUserId(@RequestParam int mungpleId, @RequestParam int userId) {
        return SuccessReturn(mongoMungpleService.getMungpleDetailByMungpleIdAndUserId(mungpleId, userId));
    }

    // -------------------------------------- Deprecated --------------------------------------

    /**
     * TODO: [Deprecated] Category Mungple 조회
     */
    @Hidden
    @GetMapping("/category/{categoryCode}")
    public ResponseEntity getMungplesByCategoryDeprecated(@PathVariable CategoryCode categoryCode) {
        return SuccessReturn(mongoMungpleService.getActiveMungpleByCategoryCode(categoryCode));
    }
}