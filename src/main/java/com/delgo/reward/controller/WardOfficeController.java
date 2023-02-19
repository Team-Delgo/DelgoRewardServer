package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.domain.WardOffice;
import com.delgo.reward.domain.code.Code;
import com.delgo.reward.service.CodeService;
import com.delgo.reward.service.WardOfficeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wardoffice")
public class WardOfficeController extends CommController {

    private final GeoService geoService;
    private final CodeService codeService;
    private final WardOfficeService wardOfficeService;

    /*
     * Code geoCode에 존재하는 시,구,군청 등록하는 API
     * Request Data : null
     * Response Data : null
     */
    @PostMapping
    public ResponseEntity<?> ncpGeoTest() {
        // 특별시, 광역시, 도 제외.
        List<Code> geoCodes = codeService.getGeoCodeAll().stream().filter(g->!(g.getCode().equals(g.getPCode()))).collect(Collectors.toList());
        geoCodes.forEach(code->{
            String address = codeService.getAddress(code.getCode(),false);
            log.info("wardOffice : {}",new WardOffice().toEntity(code, geoService.getGeoData(address)));
//            wardOfficeService.register(new WardOffice().toEntity(code, geoService.getGeoData(address))); // 등록 필요시 주석 해제
        });

        return SuccessReturn();
    }

}