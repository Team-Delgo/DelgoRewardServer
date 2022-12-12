package com.delgo.reward.comm.fcm;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.dto.user.FcmTokenDTO;
import com.delgo.reward.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class FcmController extends CommController {
    private final FcmService fcmService;
    private final TokenService tokenService;

    @PostMapping("/api/fcm/token")
    public ResponseEntity getTokenFromAndroid(@RequestBody FcmTokenDTO fcmTokenDTO){
        tokenService.saveFcmToken(fcmTokenDTO);
        return SuccessReturn();
    }

    @PostMapping("/api/fcm")
    public ResponseEntity pushMessage(@RequestBody SendFcmDTO sendFcmDTO) throws IOException {
        System.out.println(sendFcmDTO.getTargetToken() + " "
                +sendFcmDTO.getTitle() + " " + sendFcmDTO.getBody());

        fcmService.sendMessageTo(
                sendFcmDTO.getTargetToken(),
                sendFcmDTO.getTitle(),
                sendFcmDTO.getBody());

        return SuccessReturn();
    }
}
