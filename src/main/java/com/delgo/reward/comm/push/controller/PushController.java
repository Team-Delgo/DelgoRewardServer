package com.delgo.reward.comm.push.controller;

import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.cert.service.CertQueryService;
import com.delgo.reward.common.controller.CommController;
import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.mungple.service.MungpleService;
import com.delgo.reward.comm.push.service.FcmService;
import com.delgo.reward.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/push")
public class PushController extends CommController {
    private final FcmService fcmService;
    private final MungpleService mungpleService;
    private final CertQueryService certQueryService;
    private final UserQueryService userQueryService;

    @GetMapping("/mungple")
    public ResponseEntity createTokenFromAndroid(@RequestParam int mungpleId, @RequestParam boolean isUserFound) {
        Mungple mungple = mungpleService.getOneByMungpleId(mungpleId);

        if (isUserFound) {
            // 멍플 이전 등록한 User
            List<Integer> founderIdList = certQueryService.getListByPlaceName(mungple.getPlaceName()).stream()
                    .map(Certification::getUserId)
                    .peek(founderId -> fcmService.mungpleByMe(founderId, mungple.getMungpleId()))
                    .toList();

            // 그 외 ( 지역으로 조회)
            userQueryService.getListByPGeoCode(mungple.getPGeoCode()).stream()
                    .filter(user -> !founderIdList.contains(user.getUserId()))
                    .forEach(receiver -> fcmService.mungpleByOther(founderIdList.get(0), receiver.getUserId(),
                            mungple.getMungpleId()));
        } else {
            userQueryService.getListByPGeoCode(mungple.getPGeoCode())
                    .forEach(user -> fcmService.mungple(user.getUserId(), mungple.getMungpleId()));
        }
        return SuccessReturn();
    }
}