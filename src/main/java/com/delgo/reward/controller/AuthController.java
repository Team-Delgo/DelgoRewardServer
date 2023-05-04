package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.dto.SmsAuthDTO;
import com.delgo.reward.service.SmsAuthService;
import com.delgo.reward.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController extends CommController {
    private final UserService userService;
    private final SmsAuthService smsAuthService;

    // 이메일 존재 유무 확인
    @GetMapping("/email")
    public ResponseEntity<?> emailAuth(@RequestParam String email) {
        if (email.isBlank()) {
            return ErrorReturn(ApiCode.PARAM_ERROR);
        }

        if (userService.isEmailExisting(email)) {
            return SuccessReturn(userService.getUserByEmail(email).getPhoneNo());
        }
        return ErrorReturn(ApiCode.NOT_FOUND_DATA);
    }

    // 이메일 중복 확인
    @GetMapping("/email/check")
    public ResponseEntity<?> emailCheck(@RequestParam String email) {
        if (email.isBlank()) {
            return ErrorReturn(ApiCode.PARAM_ERROR);
        }
        if (!userService.isEmailExisting(email)) {
            return SuccessReturn();
        } else {
            return ErrorReturn(ApiCode.UNKNOWN_ERROR);
        }
    }

    // 이름 중복 확인
    @GetMapping("/name/check")
    public ResponseEntity<?> nameCheck(@RequestParam String name) {
        if (name.isBlank()) {
            return ErrorReturn(ApiCode.PARAM_ERROR);
        }
        if (!userService.isNameExisting(name))
            return SuccessReturn();
        else
            return ErrorReturn(ApiCode.UNKNOWN_ERROR);
    }

    // 인증번호 생성
    @PostMapping("/sms")
    public ResponseEntity<?> phoneNoAuth(@RequestBody @Validated SmsAuthDTO smsAuthDTO) {
        smsAuthDTO.setPhoneNo(smsAuthDTO.getPhoneNo().replaceAll("[^0-9]", ""));

        if (smsAuthDTO.getIsJoin() && !userService.isPhoneNoExisting(smsAuthDTO.getPhoneNo())) return ErrorReturn(ApiCode.PHONE_NO_NOT_EXIST);
        if (!smsAuthDTO.getIsJoin() && userService.isPhoneNoExisting(smsAuthDTO.getPhoneNo())) return ErrorReturn(ApiCode.PHONE_NO_DUPLICATE_ERROR);

        return SuccessReturn(smsAuthService.makeAuth(smsAuthDTO.getPhoneNo()));
    }

    // 인증번호 확인
    @GetMapping("/sms/check")
        public ResponseEntity<?> randNumCheck(@RequestParam Integer smsId, @RequestParam String enterNum) {
            if(Objects.equals(enterNum, "9999")) // 9999로 무조건 되는 코드 추가
                return SuccessReturn();

        if (enterNum.isBlank()) {
            return ErrorReturn(ApiCode.PARAM_ERROR);
        }
        Optional<ApiCode> apiCode = smsAuthService.checkSMS(smsId, enterNum);
        if (apiCode.isPresent())
            return ErrorReturn(apiCode.get());

        return SuccessReturn();
    }
}
