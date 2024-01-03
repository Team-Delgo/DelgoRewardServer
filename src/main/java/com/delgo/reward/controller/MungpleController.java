package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.MungpleSort;
import com.delgo.reward.comm.googlesheet.GoogleSheetService;
import com.delgo.reward.domain.user.Bookmark;
import com.delgo.reward.dto.mungple.MungpleResponse;
import com.delgo.reward.dto.mungple.MungpleDetailResponse;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import com.delgo.reward.service.mungple.MungpleService;
import com.delgo.reward.service.BookmarkService;
import com.delgo.reward.cert.service.CertQueryService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mungple")
public class MungpleController extends CommController {
    private final MungpleService mungpleService;
    private final BookmarkService bookmarkService;
    private final CertQueryService certQueryService;
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
     * [Category] Mungple 조회
     */
    @Operation(summary = "[Category] Mungple 조회", description = "Category 별로 Mungple 조회 [지도, 목록 조회 에서 사용]")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MungpleResponse.class))})
    @GetMapping("/category")
    public ResponseEntity getMungplesByCategory(
            @RequestParam int userId, // isBookmarked 체크 필요
            @RequestParam CategoryCode categoryCode,
            @RequestParam MungpleSort sort,
            @RequestParam(required = false) String latitude,
            @RequestParam(required = false) String longitude) {
        List<Mungple> mungpleList = !categoryCode.equals(CategoryCode.CA0000)
                ? mungpleService.getListByCategoryCode(categoryCode)
                : mungpleService.getAll();

        return SuccessReturn(MungpleResponse.fromList(
                mungpleService.getSortingStrategy(sort, latitude, longitude, userId).sort(mungpleList), // sort
                certQueryService.getCountMapByMungple(), // cert count
                bookmarkService.getCountMapByMungple(), // bookmark count
                bookmarkService.getActiveBookmarkByUserId(userId))); // isBookmarked
    }

    /**
     * [Bookmark] Mungple 조회
     */
    @Operation(summary = "[Bookmark] Mungple 조회", description = "특정 사용자가 저장한 멍플 조회 [ 목록 조회 에서 사용 ]")
    @ApiResponse(responseCode = "200", description = "", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MungpleResponse.class))})
    @GetMapping("/bookmark")
    public ResponseEntity getMungplesByBookmark(
            @RequestParam int userId,
            @RequestParam MungpleSort sort,
            @RequestParam(required = false) String latitude,
            @RequestParam(required = false) String longitude) {
        List<Bookmark> bookmarkList = bookmarkService.getActiveBookmarkByUserId(userId);
        List<Mungple> mungpleList = mungpleService.getListByIds(Bookmark.getMungpleIdList(bookmarkList));
        return SuccessReturn(MungpleResponse.fromList(
                mungpleService.getSortingStrategy(sort, latitude, longitude, userId).sort(mungpleList), // sort
                certQueryService.getCountMapByMungple(), // cert count
                bookmarkService.getCountMapByMungple(), // bookmark count
                bookmarkList)); // isBookmarked
    }

    /**
     * Mungple 삭제
     */
    @Operation(summary = "Mungple 삭제", description = "멍플 삭제 API")
    @DeleteMapping("/{mungpleId}")
    public ResponseEntity deleteMungple(@PathVariable Integer mungpleId) {
        mungpleService.delete(mungpleId);
        return SuccessReturn();
    }

    // -------------------------------------- DETAIL --------------------------------------

    /**
     * [MungpleId] Mungple Detail 조회
     */
    @Operation(summary = "[MungpleId] Mungple Detail 조회", description = "멍플 상세 조회 [특정 사용자가 저장 했는지 여부 체크를 위해 UserId도 받는다]")
    @ApiResponse(responseCode = "200", description = "", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MungpleDetailResponse.class))})
    @GetMapping("/detail")
    public ResponseEntity getMungpleDetailByMungpleIdAndUserId(@RequestParam int mungpleId, @RequestParam int userId) {
        return SuccessReturn(MungpleDetailResponse.from(
                mungpleService.getOneByMungpleId(mungpleId), // mungple
                certQueryService.getCountByMungpleId(mungpleId), // certCount
                bookmarkService.getActiveBookmarkCount(mungpleId), // bookmarkCount
                bookmarkService.hasBookmarkByIsBookmarked(userId, mungpleId, true))); // isBookmarked
    }
}