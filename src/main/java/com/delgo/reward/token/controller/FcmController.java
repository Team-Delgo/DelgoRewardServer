package com.delgo.reward.token.controller;

import com.delgo.reward.common.controller.CommController;
import com.delgo.reward.token.controller.request.FcmTokenCreate;
import com.delgo.reward.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
public class FcmController extends CommController {
    private final TokenService tokenService;

    @PostMapping("/api/fcm/token")
    public ResponseEntity createTokenFromAndroid(@RequestBody FcmTokenCreate fcmTokenCreate){
        return SuccessReturn(tokenService.create(fcmTokenCreate));
    }
}