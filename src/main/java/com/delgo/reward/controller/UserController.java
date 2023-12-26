package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.comm.security.jwt.JwtService;
import com.delgo.reward.comm.security.jwt.JwtToken;
import com.delgo.reward.domain.SmsAuth;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.dto.cert.UserVisitMungpleCountDTO;
import com.delgo.reward.dto.user.PageUserResponse;
import com.delgo.reward.dto.user.UserResponse;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import com.delgo.reward.record.signup.OAuthSignUpRecord;
import com.delgo.reward.record.signup.SignUpRecord;
import com.delgo.reward.record.user.ResetPasswordRecord;
import com.delgo.reward.service.SmsAuthService;
import com.delgo.reward.service.user.UserCommandService;
import com.delgo.reward.service.user.UserQueryService;
import com.delgo.reward.service.cert.CertQueryService;
import com.delgo.reward.service.mungple.MungpleService;
import com.delgo.reward.service.user.CategoryCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 해당 Controller는 권한 체크 없이 호출이 가능하다.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController extends CommController {
    private final JwtService jwtService;
    private final MungpleService mungpleService;
    private final SmsAuthService smsAuthService;
    private final CertQueryService certQueryService;
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final CategoryCountService categoryCountService;

    /**
     * 다른 User 정보 조회
     * @param userId
     * @return 성공 OtherUserResDTO / 실패 여부
     */
    @Operation(summary = "다른 User 정보 조회", description = "다른 User 정보 조회 API [Other 페이지에서 사용]")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @GetMapping("/other")
    public ResponseEntity<?> getOtherUser(@RequestParam int userId) {
        return SuccessReturn(UserResponse.fromOther(
                userQueryService.getOneByUserId(userId), // User
                userCommandService.getActivityByUserId(userId), // Activity Data
                UserVisitMungpleCountDTO.setMungpleData( // UserVisitMungpleCountDTO
                        certQueryService.getVisitedMungpleIdListTop3ByUserId(userId), // UserVisitMungpleCountDTO List
                        Mungple.listToMap(mungpleService.getAll())))); // Mungple List
    }

    /**
     * User 검색
     * @param searchWord, pageable(페이징) [ page, size ]
     * @return 성공 List<SearchUserResDTO> / 실패 여부
     */
    @Operation(summary = "User 검색", description = "User 검색 API [Search Page 에서 사용] \n 유저 이름, 펫 이름 검색 ")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageUserResponse.class))})
    @GetMapping("/search")
    public ResponseEntity<?> getPagingListBySearchWord(@RequestParam String searchWord, @PageableDefault Pageable pageable) {
        if (!StringUtils.hasText(searchWord))
            return ParamErrorReturn("searchWord");

        Page<User> pagingList = userQueryService.getPagingListBySearchWord(searchWord, pageable);
        return SuccessReturn(PageUserResponse.from(pagingList));
    }

    /**
     * 비밀번호 재설정
     * @param resetPasswordRecord
     * @return 성공 / 실패 여부
     */
    @Operation(summary = "비밀번호 재설정", description = "비밀번호 재설정 API")
    @PutMapping("/password")
    public ResponseEntity<?> resetPassword(@Validated @RequestBody ResetPasswordRecord resetPasswordRecord) {
        User user = userQueryService.getOneByEmail(resetPasswordRecord.email()); // 유저 조회
        SmsAuth smsAuth = smsAuthService.getSmsAuthByPhoneNo(user.getPhoneNo()); // SMS DATA 조회
        if (!smsAuthService.isAuth(smsAuth))
            return ErrorReturn(APICode.SMS_ERROR);

        userCommandService.changePassword(resetPasswordRecord.email(), resetPasswordRecord.newPassword());
        return SuccessReturn();
    }

    /**
     * 소셜 회원가입 ( Kakao, Naver, Apple )
     * Request Data : OAuthSignUpDTO, MultipartFile (프로필 사진)
     * Response Data : 등록한 인증 데이터 반환
     */
    @Operation(summary = "OAuth 회원가입", description = "소셜 회원가입 ( Kakao, Naver, Apple ) \n Apple, Kakao는 고유 Id 보내야 함.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @PostMapping(value = "/oauth",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> registerUserByOAuth(
            @Validated @RequestPart(value = "data") OAuthSignUpRecord oAuthSignUpRecord,
            @RequestPart(required = false) MultipartFile profile,
            @RequestHeader("version") String version,
            HttpServletResponse response) {
        // Apple 회원가입 시 appleUniqueNo 넣어주어야 함.
        if ((oAuthSignUpRecord.appleUniqueNo() == null || oAuthSignUpRecord.appleUniqueNo().isBlank())
                && oAuthSignUpRecord.userSocial() == UserSocial.A)
            return ErrorReturn(APICode.PARAM_ERROR);

        User user = userCommandService.oAuthSignup(oAuthSignUpRecord, profile, version);
        categoryCountService.create(user.getUserId());
        JwtToken jwt = jwtService.createToken(user.getUserId());
        jwtService.publishToken(response, jwt);

        return SuccessReturn(UserResponse.from(user));
    }

    /**
     * 일반 회원가입
     * Request Data : SignUpDTO, MultipartFile (프로필 사진)
     * Response Data : 등록한 인증 데이터 반환
     */
    @Operation(summary = "일반 회원가입", description = "일반 회원가입")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> registerUser(
            @Validated @RequestPart(value = "data") SignUpRecord signUpRecord,
            @RequestPart(required = false) MultipartFile profile,
            @RequestHeader("version") String version,
            HttpServletResponse response) {
        if (userQueryService.isEmailExisting(signUpRecord.email())) // Email 중복확인
            return ErrorReturn(APICode.EMAIL_DUPLICATE_ERROR);

        User user = userCommandService.signup(signUpRecord, profile, version);
        categoryCountService.create(user.getUserId());
        JwtToken jwt = jwtService.createToken(user.getUserId());
        jwtService.publishToken(response, jwt);

        return SuccessReturn(UserResponse.from(user));
    }

    /**
     * Map View Count
     * @param userId
     */
    @Operation(summary = "지도 조회 수 증가", description = "다른 사용자가 내 지도 조회 시 조회 수 증가 시키는 API")
    @PostMapping(value = {"/view/{userId}", "/view/"})
    public ResponseEntity increaseViewCount(@PathVariable Integer userId) {
        userCommandService.increaseViewCount(userId);
        return SuccessReturn();
    }
}
