package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.security.jwt.JwtService;
import com.delgo.reward.comm.security.jwt.JwtToken;
import com.delgo.reward.comm.security.jwt.config.AccessTokenProperties;
import com.delgo.reward.comm.security.jwt.config.RefreshTokenProperties;
import com.delgo.reward.domain.SmsAuth;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;
import com.delgo.reward.record.signup.OAuthSignUpRecord;
import com.delgo.reward.record.signup.SignUpRecord;
import com.delgo.reward.record.user.ResetPasswordRecord;
import com.delgo.reward.record.user.UserResRecord;
import com.delgo.reward.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController extends CommController {
    private final PasswordEncoder passwordEncoder;

    private final PetService petService;
    private final RankingService rankingService;
    private final UserService userService;
    private final CodeService codeService;
    private final JwtService jwtService;
    private final SmsAuthService smsAuthService;
    private final ArchiveService archiveService;

    // User 전체 조회 [ Admin 사용 ]
    @GetMapping("/all")
    public ResponseEntity<?> getUserAll() {
        List<UserResRecord> resRecords = userService.getUserAll().stream()
                .map(user -> new UserResRecord(user.setPassword(""), petService.getPetByUserId(user.getUserId())))
                .collect(Collectors.toList());

        return SuccessReturn(resRecords);
    }

    // User. Pet 조회
    @GetMapping
    public ResponseEntity<?> getUser(@RequestParam Integer userId) {
        return SuccessReturn(new UserResRecord(userService.getUserById(userId).setPassword(""), petService.getPetByUserId(userId)));
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
            ErrorReturn(ApiCode.SMS_ERROR);

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

        // 주소 설정
        String address = (oAuthSignUpRecord.geoCode().equals("0"))  // 세종시는 구가 없음.
                ? codeService.getAddress(oAuthSignUpRecord.pGeoCode(), true)
                : codeService.getAddress(oAuthSignUpRecord.geoCode(), false);

        User user = userService.signup(oAuthSignUpRecord.makeUserSocial(oAuthSignUpRecord.userSocial(), address), profile);
        Pet pet = petService.register(oAuthSignUpRecord.makePet(user.getUserId()));

        archiveService.registerWelcome(user.getUserId()); // WELCOME 업적 부여
        rankingService.rankingByPoint(); // 랭킹 업데이트

        JwtToken jwt = jwtService.createToken(user.getUserId());
        response.addHeader(AccessTokenProperties.HEADER_STRING, AccessTokenProperties.TOKEN_PREFIX + jwt.getAccessToken());
        response.addHeader(RefreshTokenProperties.HEADER_STRING, RefreshTokenProperties.TOKEN_PREFIX + jwt.getRefreshToken());

        UserResRecord returnRecord = new UserResRecord(user, pet);
        log.info("user : {}", returnRecord);
        return SuccessReturn(returnRecord);
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

        // 주소 설정
        String address = (signUpRecord.geoCode().equals("0"))  // 세종시는 구가 없음.
                ? codeService.getAddress(signUpRecord.pGeoCode(), true)
                : codeService.getAddress(signUpRecord.geoCode(), false);

        User user = userService.signup(signUpRecord.makeUser(passwordEncoder.encode(signUpRecord.password()), address), profile);
        Pet pet = petService.register(signUpRecord.makePet(user.getUserId()));

        archiveService.registerWelcome(user.getUserId()); // WELCOME 업적 부여
        rankingService.rankingByPoint(); // 랭킹 업데이트

        JwtToken jwt = jwtService.createToken(user.getUserId());
        response.addHeader(AccessTokenProperties.HEADER_STRING, AccessTokenProperties.TOKEN_PREFIX + jwt.getAccessToken());
        response.addHeader(RefreshTokenProperties.HEADER_STRING, RefreshTokenProperties.TOKEN_PREFIX + jwt.getRefreshToken());

        UserResRecord returnRecord = new UserResRecord(user, pet);
        log.info("user : {}", returnRecord);
        return SuccessReturn(returnRecord);
    }
}
