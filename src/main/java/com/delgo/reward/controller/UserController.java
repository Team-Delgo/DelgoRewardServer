package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.comm.security.jwt.JwtService;
import com.delgo.reward.comm.security.jwt.JwtToken;
import com.delgo.reward.domain.SmsAuth;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.dto.comm.PageCustomSearchUserDTO;
import com.delgo.reward.dto.user.OtherUserResDTO;
import com.delgo.reward.dto.user.UserResDTO;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.record.signup.OAuthSignUpRecord;
import com.delgo.reward.record.signup.SignUpRecord;
import com.delgo.reward.record.user.ResetPasswordRecord;
import com.delgo.reward.service.SmsAuthService;
import com.delgo.reward.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final UserService userService;
    private final SmsAuthService smsAuthService;
    private final MongoMungpleService mongoMungpleService;

    /**
     * 다른 User 정보 조회
     * @param userId
     * @return 성공 OtherUserResDTO / 실패 여부
     */
    @Operation(summary = "다른 User 정보 조회", description = "다른 User 정보 조회 API [Other 페이지에서 사용]")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OtherUserResDTO.class))})
    @GetMapping("/other")
    public ResponseEntity<?> getOtherUser(@RequestParam int userId) {
        return SuccessReturn(new OtherUserResDTO(
                userService.getUserById(userId),
                userService.getActivityByUserId(userId),
                mongoMungpleService.getMostVisitedList(userId, 3)));
    }

    /**
     * User 검색
     * @param searchWord, pageable(페이징) [ page, size ]
     * @return 성공 List<SearchUserResDTO> / 실패 여부
     */
    @Operation(summary = "User 검색", description = "User 검색 API [Search Page 에서 사용] \n 유저 이름, 펫 이름 검색 ")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageCustomSearchUserDTO.class))})
    @GetMapping("/search")
    public ResponseEntity<?> getSearchUser(@RequestParam String searchWord, @PageableDefault Pageable pageable) {
        if (!StringUtils.hasText(searchWord))
            return ParamErrorReturn("searchWord");

        return SuccessReturn(userService.getSearchUsers(searchWord, pageable));
    }

    /**
     * 비밀번호 재설정
     * @param resetPasswordRecord
     * @return 성공 / 실패 여부
     */
    @Operation(summary = "비밀번호 재설정", description = "비밀번호 재설정 API")
    @PutMapping("/password")
    public ResponseEntity<?> resetPassword(@Validated @RequestBody ResetPasswordRecord resetPasswordRecord) {
        User user = userService.getUserByEmail(resetPasswordRecord.email()); // 유저 조회
        SmsAuth smsAuth = smsAuthService.getSmsAuthByPhoneNo(user.getPhoneNo()); // SMS DATA 조회
        if (!smsAuthService.isAuth(smsAuth))
            return ErrorReturn(APICode.SMS_ERROR);

        userService.changePassword(resetPasswordRecord.email(), resetPasswordRecord.newPassword());
        return SuccessReturn();
    }

    /**
     * 소셜 회원가입 ( Kakao, Naver, Apple )
     * Request Data : OAuthSignUpDTO, MultipartFile (프로필 사진)
     * Response Data : 등록한 인증 데이터 반환
     */
    @Operation(summary = "OAuth 회원가입", description = "소셜 회원가입 ( Kakao, Naver, Apple ) \n Apple, Kakao는 고유 Id 보내야 함.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResDTO.class))})
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

        User user = userService.oAuthSignup(oAuthSignUpRecord, profile, version);
        JwtToken jwt = jwtService.createToken(user.getUserId());
        jwtService.publishToken(response, jwt);

        return SuccessReturn(new UserResDTO(user));
    }

    /**
     * 일반 회원가입
     * Request Data : SignUpDTO, MultipartFile (프로필 사진)
     * Response Data : 등록한 인증 데이터 반환
     */
    @Operation(summary = "일반 회원가입", description = "일반 회원가입")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResDTO.class))})
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> registerUser(
            @Validated @RequestPart(value = "data") SignUpRecord signUpRecord,
            @RequestPart(required = false) MultipartFile profile,
            @RequestHeader("version") String version,
            HttpServletResponse response) {
        if (userService.isEmailExisting(signUpRecord.email())) // Email 중복확인
            return ErrorReturn(APICode.EMAIL_DUPLICATE_ERROR);

        User user = userService.signup(signUpRecord, profile, version);
        JwtToken jwt = jwtService.createToken(user.getUserId());
        jwtService.publishToken(response, jwt);

        return SuccessReturn(new UserResDTO(user));
    }

    /**
     * Map View Count
     * @param userId
     */
    @Operation(summary = "지도 조회 수 증가", description = "다른 사용자가 내 지도 조회 시 조회 수 증가 시키는 API")
    @PostMapping(value = {"/view/{userId}", "/view/"})
    public ResponseEntity increaseViewCount(@PathVariable Integer userId) {
        userService.increaseViewCount(userId);
        return SuccessReturn();
    }
}
