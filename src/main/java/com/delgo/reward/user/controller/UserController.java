package com.delgo.reward.user.controller;


import com.delgo.reward.common.controller.CommController;
import com.delgo.reward.comm.code.ResponseCode;
import com.delgo.reward.comm.encoder.CustomPasswordEncoder;
import com.delgo.reward.comm.security.service.JwtService;
import com.delgo.reward.user.domain.SmsAuth;
import com.delgo.reward.user.domain.Pet;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.cert.repository.dto.UserVisitMungpleCountDTO;
import com.delgo.reward.user.response.PageUserResponse;
import com.delgo.reward.user.response.UserResponse;
import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.user.controller.request.OAuthCreate;
import com.delgo.reward.user.controller.request.UserCreate;
import com.delgo.reward.user.controller.request.PasswordUpdate;
import com.delgo.reward.code.service.CodeService;
import com.delgo.reward.user.service.PetService;
import com.delgo.reward.common.service.PhotoService;
import com.delgo.reward.user.service.SmsAuthService;
import com.delgo.reward.user.service.UserCommandService;
import com.delgo.reward.user.service.UserQueryService;
import com.delgo.reward.cert.service.CertQueryService;
import com.delgo.reward.mungple.service.MungpleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
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
import java.util.List;

/**
 * 해당 Controller는 권한 체크 없이 호출이 가능하다.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController extends CommController {
    private final JwtService jwtService;
    private final PetService petService;
    private final CodeService codeService;
    private final PhotoService photoService;
    private final MungpleService mungpleService;
    private final SmsAuthService smsAuthService;
    private final CertQueryService certQueryService;
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    private final CustomPasswordEncoder customPasswordEncoder;
    private final String DEFAULT_PROFILE = "https://kr.object.ncloudstorage.com/reward-profile/%EA%B8%B0%EB%B3%B8%ED%94%84%EB%A1%9C%ED%95%84.webp";

    /**
     * 다른 User 정보 조회
     */
    @Operation(summary = "다른 User 정보 조회", description = "다른 User 정보 조회 API [Other 페이지 에서 사용]")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @GetMapping("/other")
    public ResponseEntity<?> getOtherUser(@RequestParam(name = "userId") int userId) {
        return SuccessReturn(UserResponse.fromOther(
                userQueryService.getOneByUserId(userId), // User
                certQueryService.getCategoryCountMapByUserId(userId), // Activity Data
                UserVisitMungpleCountDTO.setMungpleData( // UserVisitMungpleCountDTO
                        certQueryService.getVisitedMungpleIdListTop3ByUserId(userId), // UserVisitMungpleCountDTO List
                        Mungple.listToMap(mungpleService.getAll())))); // Mungple List
    }

    /**
     * User 검색
     */
    @Operation(summary = "User 검색", description = "User 검색 API [Search Page 에서 사용] \n 유저 이름, 펫 이름 검색 ")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageUserResponse.class))})
    @GetMapping("/search")
    public ResponseEntity<?> getPagingListBySearchWord(@RequestParam String searchWord, @PageableDefault Pageable pageable) {
        if (!StringUtils.hasText(searchWord))
            return ParamErrorReturn("searchWord");

        Page<User> userPage = userQueryService.getPagingListBySearchWord(searchWord, pageable);
        List<UserResponse> userResponseList = UserResponse.fromSearchList(userPage.getContent());
        return SuccessReturn(PageUserResponse.from(userPage, userResponseList));
    }

    /**
     * 비밀번호 재설정
     */
    @Operation(summary = "비밀번호 재설정", description = "비밀번호 재설정 API")
    @PutMapping("/password")
    public ResponseEntity<?> resetPassword(@Validated @RequestBody PasswordUpdate passwordUpdate) {
        User user = userQueryService.getOneByEmail(passwordUpdate.email()); // 유저 조회
        SmsAuth smsAuth = smsAuthService.getOneByPhoneNo(user.getPhoneNo());
        if (!smsAuth.isAuthTimeValid())
            return ErrorReturn(ResponseCode.SMS_ERROR);

        String encodedPassword =  User.encodePassword(customPasswordEncoder, passwordUpdate.newPassword());
        User updatedUser = userCommandService.updatePassword(passwordUpdate.email(), encodedPassword);
        return SuccessReturn(UserResponse.from(updatedUser));
    }

    /**
     * 소셜 회원가입 ( Kakao, Naver, Apple )
     */
    @Operation(summary = "OAuth 회원가입", description = "소셜 회원가입 ( Kakao, Naver, Apple ) \n Apple, Kakao는 고유 Id 보내야 함.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @PostMapping(value = "/oauth",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> registerUserByOAuth(
            @Validated @RequestPart(value = "data") OAuthCreate oAuthCreate,
            @RequestPart(required = false) MultipartFile profile,
            @RequestHeader("version") String version,
            HttpServletResponse response) {
        // Apple 회원가입 시 appleUniqueNo 넣어주어야 함.
        if (!StringUtils.hasText(oAuthCreate.appleUniqueNo()) && oAuthCreate.userSocial() == UserSocial.A)
            return ErrorReturn(ResponseCode.PARAM_ERROR);

        User user = userCommandService.save(User.from(oAuthCreate,
                User.formattedPhoneNo(oAuthCreate.phoneNo()),
                codeService.getAddressByGeoCode(oAuthCreate.geoCode()), // Code -> 주소 변환
                version));

        Pet pet = petService.create(Pet.from(oAuthCreate, user)); // Pet 생성
        user.setPet(pet); // Transaction 이 다른 곳에서 발생 하기 때문에 명시적으로 Pet을 넣어줘야 Response 에서 접근 가능.
        String profileUrl = (profile.isEmpty()) ? DEFAULT_PROFILE : photoService.createProfile(user.getUserId(), profile); // Profile 생성
        User updatedUser = userCommandService.updateProfile(user.getUserId(), profileUrl);  // Profile URL 적용

        jwtService.publish(response, user.getUserId());
        return SuccessReturn(UserResponse.from(updatedUser));
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
            @Validated @RequestPart(value = "data") UserCreate userCreate,
            @RequestPart(required = false) MultipartFile profile,
            @RequestHeader("version") String version,
            HttpServletResponse response) {

        if (userQueryService.isEmailExisting(userCreate.email())) // Email 중복확인
            return ErrorReturn(ResponseCode.EMAIL_DUPLICATE_ERROR);

        User user = userCommandService.save(User.from(userCreate,
                customPasswordEncoder.encode(userCreate.password()),
                User.formattedPhoneNo(userCreate.phoneNo()),
                codeService.getAddressByGeoCode(userCreate.geoCode()), // Code -> 주소 변환
                version));

        Pet pet = petService.create(Pet.from(userCreate, user)); // Pet 생성
        user.setPet(pet); // Transaction 이 다른 곳에서 발생 하기 때문에 명시적으로 Pet을 넣어줘야 Response 에서 접근 가능.
        String profileUrl = (profile.isEmpty()) ? DEFAULT_PROFILE : photoService.createProfile(user.getUserId(), profile); // Profile 생성
        userCommandService.updateProfile(user.getUserId(), profileUrl);  // Profile URL 적용

        jwtService.publish(response, user.getUserId());
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
