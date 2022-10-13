package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.service.SmsAuthService;
import com.delgo.reward.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SmsAuthController extends CommController {
    private final UserService userService;
    private final SmsAuthService smsAuthService;

    // 회원가입 전 인증번호 전송
    @GetMapping("/phoneNoAuth")
    public ResponseEntity<?> phoneNoAuth(@RequestParam String phoneNo) {
        try {
            if (phoneNo.isBlank()) {
                return ErrorReturn(ApiCode.PARAM_ERROR);
            }
            phoneNo = phoneNo.replaceAll("[^0-9]", "");

            if (userService.isPhoneNoExisting(phoneNo)) {
                return ErrorReturn(ApiCode.PHONE_NO_DUPLICATE_ERROR);
            }

            if (smsAuthService.isSmsAuthExisting(phoneNo)) {
                int smsId = smsAuthService.updateSmsAuth(phoneNo);
                return SuccessReturn(smsId);
            } else {
                int smsId = smsAuthService.createSmsAuth(phoneNo);
                return SuccessReturn(smsId);
            }

        } catch (Exception e) {
            return ErrorReturn(ApiCode.UNKNOWN_ERROR);
        }
    }

    // 회원가입 후 인증번호 전송
    @GetMapping("/phoneNoCheck")
    public ResponseEntity<?> phoneNoCheck(@RequestParam String phoneNo) {
        try {
            if (phoneNo.isBlank()) {
                return ErrorReturn(ApiCode.PARAM_ERROR);
            }
            phoneNo = phoneNo.replaceAll("[^0-9]", "");
            if (!userService.isPhoneNoExisting(phoneNo)) {
                return ErrorReturn(ApiCode.PHONE_NO_NOT_EXIST);
            }
            if (smsAuthService.isSmsAuthExisting(phoneNo)) {
                int smsId = smsAuthService.updateSmsAuth(phoneNo);
                return SuccessReturn(smsId);
            } else {
                int smsId = smsAuthService.createSmsAuth(phoneNo);
                return SuccessReturn(smsId);
            }
        } catch (Exception e) {
            return ErrorReturn(ApiCode.UNKNOWN_ERROR);
        }
    }

    // 인증번호 확인
    @GetMapping("/authRandNum")
    public ResponseEntity<?> randNumCheck(@RequestParam Integer smsId, @RequestParam String enterNum) {
        if (enterNum.isBlank()) {
            return ErrorReturn(ApiCode.PARAM_ERROR);
        }
        Optional<ApiCode> apiCode = smsAuthService.checkSMS(smsId, enterNum);
        if (apiCode.isPresent())
            return ErrorReturn(apiCode.get());

        return SuccessReturn();
    }

    // OAuth 연동
//    @GetMapping("/oAuthConnect")
//    public ResponseEntity<?> connectUserByOAuth(HttpServletResponse response,
//                                                @RequestParam Integer smsId,
//                                                @RequestParam String enterNum,
//                                                @RequestParam UserSocial userSocial) {
//        if (enterNum.isBlank()) {
//            return ErrorReturn(ApiCode.PARAM_ERROR);
//        }
//        Optional<ApiCode> apiCode = smsAuthService.checkSMS(smsId, enterNum);
//        if (apiCode.isPresent())
//            return ErrorReturn(apiCode.get());
//
//        SmsAuth smsAuth = smsAuthService.getSmsAuthBySmsId(smsId);
//        User user = userService.getUserByPhoneNo(smsAuth.getPhoneNo());
//        // 이미 다른 API와 연동 되어 있음.
//        if(user.getUserSocial() != UserSocial.D)
//            ErrorReturn(ApiCode.ANOTHER_OAUTH_CONNECT);
//
//        user.setUserSocial(userSocial);
//        User updateUser = userService.updateUserData(user);
//        Pet pet = petService.getPetByUserId(updateUser.getUserId());
//
//        String Access_jwtToken = tokenService.createToken("Access", updateUser.getEmail()); // Access Token 생성
//        String Refresh_jwtToken = tokenService.createToken("Refresh", updateUser.getEmail()); // Refresh Token 생성
//
//        response.addHeader(Access_JwtProperties.HEADER_STRING, Access_JwtProperties.TOKEN_PREFIX + Access_jwtToken);
//        response.addHeader(Refresh_JwtProperties.HEADER_STRING, Refresh_JwtProperties.TOKEN_PREFIX + Refresh_jwtToken);
//
//        return SuccessReturn(new UserPetDTO(updateUser, pet));
//    }
}
