package com.delgo.reward.controller;

import com.delgo.reward.code.CategoryCode;
import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.domain.Mungple;
import com.delgo.reward.dto.MungpleDTO;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.service.MungpleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mungple")
public class MungpleController extends CommController {

    private final MungpleService mungpleService;
    private final GeoService geoService;

    /*
     * 멍플 조회
     * Request Data : MungpleDTO
     * - 주소로 위도, 경도 구할 수 있어야 함.
     * Response Data : 등록한 멍플 데이터 반환
     */
    @PostMapping("/register")
    public ResponseEntity register(@Validated @RequestBody MungpleDTO mungpleDTO) {
        // 위도, 경도
        Location location = geoService.getGeoData(mungpleDTO.getAddress());

        return (!mungpleService.isMungpleExisting(location))
                ? SuccessReturn(mungpleService.registerMungple(mungpleDTO.makeMungple(location)))
                : ErrorReturn(ApiCode.MUNGPLE_DUPLICATE_ERROR);
    }

    /*
     * 멍플 카테고리 별 조회
     * Request Data : categoryCode
     * - CA0000 = 전체 조회
     * Response Data : 카테고리별 멍플 리스트 반환
     */
    @GetMapping("/getData")
    public ResponseEntity getData(@RequestParam String categoryCode) {
        // Validate - Blank Check; [ String 만 해주면 됨 ]
        if (categoryCode.isBlank())
            return ErrorReturn(ApiCode.PARAM_ERROR);

        List<Mungple> mungpleList = (!categoryCode.equals(CategoryCode.TOTAL.getCode()))
                ? mungpleService.getMungpleByCategoryCode(categoryCode)
                : mungpleService.getMungpleAll();

        return SuccessReturn(mungpleList);
    }
}