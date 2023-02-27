package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.domain.Mungple;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.dto.MungpleDTO;
import com.delgo.reward.service.MungpleService;
import com.delgo.reward.service.PhotoService;
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

    private final GeoService geoService;
    private final MungpleService mungpleService;
    private final PhotoService photoService;

    /*
     * 멍플 등록
     * Request Data : MungpleDTO
     * - 주소로 위도, 경도 구할 수 있어야 함.
     * Response Data : 등록한 멍플 데이터 반환
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity register(@Validated @RequestPart MungpleDTO dto, @RequestPart MultipartFile thumbnail, @RequestPart MultipartFile mungpleNote) {
        if (thumbnail.isEmpty()) return ErrorReturn(ApiCode.PARAM_ERROR);
        if (mungpleNote.isEmpty()) return ErrorReturn(ApiCode.PARAM_ERROR);

        Location location = geoService.getGeoData(dto.getAddress()); // 위도, 경도
        if (!mungpleService.isMungpleExisting(location)) ErrorReturn(ApiCode.MUNGPLE_DUPLICATE_ERROR);

        Mungple mungple = mungpleService.register(dto.toEntity(location));

        photoService.uploadMungple(mungple.getMungpleId(), thumbnail);
        photoService.uploadMungpleNote(mungple.getMungpleId(), mungpleNote);

        return SuccessReturn(mungple);
    }

    /*
     * 멍플 카테고리 별 조회
     * Request Data : categoryCode
     * - CA0000 = 전체 조회
     * Response Data : 카테고리별 멍플 리스트 반환
     */
    @GetMapping("/category/{categoryCode}")
    public ResponseEntity getCategory(@PathVariable String categoryCode) {
        if (categoryCode.isBlank()) return ErrorReturn(ApiCode.PARAM_ERROR); // Validate - Blank Check

        return SuccessReturn((!categoryCode.equals(CategoryCode.TOTAL.getCode()))
                ? mungpleService.getMungpleByCategoryCode(categoryCode)
                : mungpleService.getMungpleAll());
    }

    /*
     * 인증 개수 많은 순으로 정렬해서 반환 ( 추후 광고 관련 코드 넣을 예정 )
     * Request Data : count
     * Response Data :
     */
    @GetMapping("/top")
    public ResponseEntity getTopData(@RequestParam Integer count) {
        return SuccessReturn(mungpleService.getMungpleOfMostCount(count));
    }
}