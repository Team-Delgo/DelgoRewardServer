package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.dto.SmsAuthDTO;
import com.delgo.reward.service.SmsAuthService;
import com.delgo.reward.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController extends CommController {
    private final UserQueryService userQueryService;
    private final SmsAuthService smsAuthService;

    // 이메일 존재 유무 확인
    @GetMapping("/email")
    public ResponseEntity<?> emailAuth(@RequestParam @NotBlank String email) {
        return userQueryService.isEmailExisting(email)
                ? SuccessReturn(userQueryService.getOneByEmail(email).getPhoneNo())
                : ErrorReturn(APICode.NOT_FOUND_DATA);
    }

    // 이메일 중복 확인
    @GetMapping("/email/check")
    public ResponseEntity<?> emailCheck(@RequestParam @NotBlank String email) {
        return userQueryService.isEmailExisting(email)
                ? ErrorReturn(APICode.EMAIL_DUPLICATE_ERROR)
                : SuccessReturn();
    }

    // 이름 중복 확인
    @GetMapping("/name/check")
    public ResponseEntity<?> nameCheck(@RequestParam @NotBlank String name) {
        return userQueryService.isNameExisting(name)
                ? ErrorReturn(APICode.NAME_DUPLICATE_ERROR)
                : SuccessReturn();
    }

    // 인증번호 생성
    @PostMapping("/sms")
    public ResponseEntity<?> phoneNoAuth(@RequestBody @Validated SmsAuthDTO smsAuthDTO) {
        smsAuthDTO.setPhoneNo(smsAuthDTO.getPhoneNo().replaceAll("[^0-9]", ""));

        if (smsAuthDTO.getIsJoin() && !userQueryService.isPhoneNoExisting(smsAuthDTO.getPhoneNo())) return ErrorReturn(APICode.PHONE_NO_NOT_EXIST);
        if (!smsAuthDTO.getIsJoin() && userQueryService.isPhoneNoExisting(smsAuthDTO.getPhoneNo())) return ErrorReturn(APICode.PHONE_NO_DUPLICATE_ERROR);

        return SuccessReturn(smsAuthService.makeAuth(smsAuthDTO.getPhoneNo()));
    }

    // 인증번호 확인
    @GetMapping("/sms/check")
        public ResponseEntity<?> randNumCheck(@RequestParam @NotNull Integer smsId, @RequestParam @NotBlank String enterNum) {
            if(Objects.equals(enterNum, "9999")) // 9999로 무조건 되는 코드 추가
                return SuccessReturn();

        Optional<APICode> apiCode = smsAuthService.checkSMS(smsId, enterNum);
        return  smsAuthService.checkSMS(smsId, enterNum).isPresent()
                ? ErrorReturn(apiCode.get())
                : SuccessReturn();
    }
}
