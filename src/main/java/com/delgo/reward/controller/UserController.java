package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.security.jwt.JwtService;
import com.delgo.reward.comm.security.jwt.JwtToken;
import com.delgo.reward.comm.security.jwt.config.AccessTokenProperties;
import com.delgo.reward.comm.security.jwt.config.RefreshTokenProperties;
import com.delgo.reward.domain.SmsAuth;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;
import com.delgo.reward.dto.user.UserResDTO;
import com.delgo.reward.record.signup.OAuthSignUpRecord;
import com.delgo.reward.record.signup.SignUpRecord;
import com.delgo.reward.record.user.ResetPasswordRecord;
import com.delgo.reward.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController extends CommController {

    private final JwtService jwtService;
    private final UserService userService;
    private final SmsAuthService smsAuthService;

    // User 조회
    @GetMapping
    public ResponseEntity<?> getUser(@RequestParam Integer userId) {
        return SuccessReturn(new UserResDTO(userService.getUserById(userId)));
    }

    /**
     * 비밀번호 재설정
     * @param resetPasswordRecord
     * @return 성공 / 실패 여부
     */
    @PutMapping("/password")
    public ResponseEntity<?> resetPassword(@Validated @RequestBody ResetPasswordRecord resetPasswordRecord) {
        User user = userService.getUserByEmail(resetPasswordRecord.email()); // 유저 조회
        SmsAuth smsAuth = smsAuthService.getSmsAuthByPhoneNo(user.getPhoneNo()); // SMS DATA 조회
        if (!smsAuthService.isAuth(smsAuth))
            return ErrorReturn(ApiCode.SMS_ERROR);

        userService.changePassword(resetPasswordRecord.email(), resetPasswordRecord.newPassword());
        return SuccessReturn();
    }

    /*
     * 소셜 회원가입 ( Kakao, Naver, Apple )
     * Request Data : OAuthSignUpDTO, MultipartFile (프로필 사진)
     * Response Data : 등록한 인증 데이터 반환
     */
    @PostMapping(value = "/oauth",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> registerUserByOAuth(@Validated @RequestPart(value = "data") OAuthSignUpRecord oAuthSignUpRecord, @RequestPart(required = false) MultipartFile profile, HttpServletResponse response) {
        // Apple 회원가입 시 appleUniqueNo 넣어주어야 함.
        if ((oAuthSignUpRecord.appleUniqueNo() == null || oAuthSignUpRecord.appleUniqueNo().isBlank())
                && oAuthSignUpRecord.userSocial() == UserSocial.A)
            return ErrorReturn(ApiCode.PARAM_ERROR);

        User user = userService.oAuthSignup(oAuthSignUpRecord, profile);
        JwtToken jwt = jwtService.createToken(user.getUserId());
        response.addHeader(AccessTokenProperties.HEADER_STRING, AccessTokenProperties.TOKEN_PREFIX + jwt.getAccessToken());
        response.addHeader(RefreshTokenProperties.HEADER_STRING, RefreshTokenProperties.TOKEN_PREFIX + jwt.getRefreshToken());

        return SuccessReturn(new UserResDTO(user));
    }

    /*
     * 일반 회원가입
     * Request Data : SignUpDTO, MultipartFile (프로필 사진)
     * Response Data : 등록한 인증 데이터 반환
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> registerUser(@Validated @RequestPart(value = "data") SignUpRecord signUpRecord, @RequestPart(required = false) MultipartFile profile, HttpServletResponse response) {
        if (userService.isEmailExisting(signUpRecord.email())) // Email 중복확인
            return ErrorReturn(ApiCode.EMAIL_DUPLICATE_ERROR);

        User user = userService.signup(signUpRecord, profile);
        JwtToken jwt = jwtService.createToken(user.getUserId());
        response.addHeader(AccessTokenProperties.HEADER_STRING, AccessTokenProperties.TOKEN_PREFIX + jwt.getAccessToken());
        response.addHeader(RefreshTokenProperties.HEADER_STRING, RefreshTokenProperties.TOKEN_PREFIX + jwt.getRefreshToken());

        return SuccessReturn(new UserResDTO(user));
    }
}
