package com.delgo.reward.user.controller;


import com.delgo.reward.comm.ncp.sms.SmsService;
import com.delgo.reward.common.controller.CommController;
import com.delgo.reward.comm.code.ResponseCode;
import com.delgo.reward.user.controller.request.SmsAuthCreate;
import com.delgo.reward.user.domain.SmsAuth;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.service.SmsAuthService;
import com.delgo.reward.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static com.delgo.reward.common.service.CommService.numberGen;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController extends CommController {
    private final UserQueryService userQueryService;
    private final SmsAuthService smsAuthService;
    private final SmsService smsService;

    // 이메일 존재 유무 확인
    @GetMapping("/email")
    public ResponseEntity<?> emailAuth(@RequestParam @NotBlank String email) {
        return userQueryService.isEmailExisting(email)
                ? SuccessReturn(userQueryService.getOneByEmail(email).getPhoneNo())
                : ErrorReturn(ResponseCode.NOT_FOUND_DATA);
    }

    // 이메일 중복 확인
    @GetMapping("/email/check")
    public ResponseEntity<?> emailCheck(@RequestParam @NotBlank String email) {
        return userQueryService.isEmailExisting(email)
                ? ErrorReturn(ResponseCode.EMAIL_DUPLICATE_ERROR)
                : SuccessReturn();
    }

    // 이름 중복 확인
    @GetMapping("/name/check")
    public ResponseEntity<?> nameCheck(@RequestParam @NotBlank String name) {
        return userQueryService.isNameExisting(name)
                ? ErrorReturn(ResponseCode.NAME_DUPLICATE_ERROR)
                : SuccessReturn();

    }

    // 인증번호 생성
    @PostMapping("/sms")
    public ResponseEntity<?> phoneNoAuth(@RequestBody @Validated SmsAuthCreate smsAuthCreate) {
        String phoneNo = User.formattedPhoneNo(smsAuthCreate.phoneNo());

        if (smsAuthCreate.isJoin() && !userQueryService.isPhoneNoExisting(phoneNo)) return ErrorReturn(ResponseCode.PHONE_NO_NOT_EXIST);
        if (!smsAuthCreate.isJoin() && userQueryService.isPhoneNoExisting(phoneNo)) return ErrorReturn(ResponseCode.PHONE_NO_DUPLICATE_ERROR);

        String randNum = numberGen(4, 1);
        boolean sendResult = smsService.send(phoneNo, "[Delgo] 인증번호 " + randNum);
        if (!sendResult) ErrorReturn(ResponseCode.SMS_ERROR);

        return SuccessReturn(smsAuthService.isExisting(phoneNo)
                ? smsAuthService.update(phoneNo, randNum).getSmsId()
                : smsAuthService.create(phoneNo, randNum).getSmsId());
    }

    // 인증번호 확인
    @GetMapping("/sms/check")
    public ResponseEntity<?> randNumCheck(@RequestParam @NotNull Integer smsId, @RequestParam @NotBlank String enterNum) {
        if (Objects.equals(enterNum, "9999")) // 9999로 무조건 되는 코드 추가
            return SuccessReturn();

        SmsAuth smsAuth = smsAuthService.getOneBySmsId(smsId);
        return smsAuth.isRandNumEqual(enterNum) && smsAuth.isAuthTimeValid()
                ? SuccessReturn()
                : ErrorReturn(ResponseCode.AUTH_DO_NOT_MATCHING);
    }
}
