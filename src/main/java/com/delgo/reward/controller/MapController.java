package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.Certification;
import com.delgo.reward.domain.Mungple;
import com.delgo.reward.service.CertificationService;
import com.delgo.reward.service.MungpleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController extends CommController {

    private final MungpleService mungpleService;
    private final CertificationService certificationService;

    /*
     * Map 멍플,인증 장소 리스트 조회
     * Request Data : userId, 위도, 경도
     * - 멍플, 일반 인증 장소, 멍플 인증 장소 구분 필요
     * Response Data : 멍플 List, 일반 인증 List, 멍플 인증 List
     */
    @GetMapping("/getData")
    public ResponseEntity getData(@RequestParam Integer userId) {
        // 멍플 조회
        List<Mungple> mungpleList = mungpleService.getMungpleAll();

        // 인증 리스트 조회
        List<Certification> certificationList = certificationService.getCertificationByUserId(userId);

        // 일반 인증, 멍플 인증 구분
        List<Certification> certNormalList = certificationList.stream().filter(c -> c.getMungpleId() == 0).collect(Collectors.toList());
        List<Certification> certMunpleList = certificationList.stream().filter(c -> c.getMungpleId() != 0).collect(Collectors.toList());

        // 멍플 인증 된 장소 멍플에서 제거
        List<Mungple> returnMungpleList = new ArrayList<Mungple>();
        for (Mungple mungple : mungpleList) {
            int isCertification = 0;

            for (Certification certification : certMunpleList)
                if (mungple.getMungpleId() == certification.getMungpleId()) isCertification = 1;

            if (isCertification == 0) returnMungpleList.add(mungple);
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("mungpleList", returnMungpleList); // mungpleList :  멍플 리스트 ( 인증된 멍플은 제거된 리스트 )
        resultMap.put("certNormalList", certNormalList);  // certNormalList : 일반 인증 리스트 ( 하얀 테두리 )
        resultMap.put("certMunpleList", certMunpleList); // certMunpleList : 멍플 인증 리스트 ( 주황 테두리 )

        return SuccessReturn(resultMap);
    }
}