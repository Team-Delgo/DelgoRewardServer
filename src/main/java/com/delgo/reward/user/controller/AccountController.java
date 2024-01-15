package com.delgo.reward.user.controller;

import com.delgo.reward.common.controller.CommController;
import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.comm.encoder.CustomPasswordEncoder;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.user.service.oauth.KakaoService;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.cert.repository.dto.UserVisitMungpleCountDTO;
import com.delgo.reward.user.response.UserResponse;
import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.user.controller.request.PetUpdate;
import com.delgo.reward.user.controller.request.UserUpdate;
import com.delgo.reward.user.controller.request.PasswordUpdate;
import com.delgo.reward.user.service.PetService;
import com.delgo.reward.token.service.TokenService;
import com.delgo.reward.cert.service.CertCommandService;
import com.delgo.reward.user.service.UserCommandService;
import com.delgo.reward.user.service.UserQueryService;
import com.delgo.reward.cert.service.CertQueryService;
import com.delgo.reward.mungple.service.MungpleService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController extends CommController {
    private final PetService petService;
    private final TokenService tokenService;
    private final KakaoService kakaoService;
    private final MungpleService mungpleService;
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final CertQueryService certQueryService;
    private final CertCommandService certCommandService;
    private final ObjectStorageService objectStorageService;
    private final CustomPasswordEncoder customPasswordEncoder;

    /**
     * 내 정보 조회
     */
    @Operation(summary = "내 정보 조회 ", description = "My Page, 및 활동 페이지에서 필요한 모든 Data를 반환한다.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @GetMapping
    public ResponseEntity<?> getAccount(@RequestParam(name = "userId") int userId){
        return SuccessReturn(UserResponse.fromAccount(
                userQueryService.getOneByUserId(userId), // User
                certQueryService.getCategoryCountMapByUserId(userId), // Activity Data
                UserVisitMungpleCountDTO.setMungpleData( // UserVisitMungpleCountDTO
                        certQueryService.getVisitedMungpleIdListTop3ByUserId(userId), // UserVisitMungpleCountDTO List
                        Mungple.listToMap(mungpleService.getAll())))); // Mungple List
    }

    /**
     * 알림 정보 수정
     */
    @Hidden
    @PutMapping(value = {"/notify/{userId}", "/notify"})
    public ResponseEntity<?> updateIsNotify(@PathVariable Integer userId){
        User user = userCommandService.updateIsNotify(userId);
        return SuccessReturn(UserResponse.from(user));
    }

    /**
     * 유저 정보 수정
     */
    @Operation(summary = "유저 정보 수정", description = "수정 된 유저 정보를 반환한다. \n profile은 multipart로 받는다. (RequestBody 체크 필요)")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @PutMapping(value = "/user")
    public ResponseEntity<?> updateUserInfo(@Validated @RequestBody UserUpdate userUpdate) {
        User user = userCommandService.updateUserInfo(userUpdate);
        return SuccessReturn(UserResponse.from(user));
    }

    /**
     * 펫 정보 수정
     */
    @Operation(summary = "펫 정보 수정", description = "성공 여부만 반환 한다.")
    @PutMapping("/pet")
    public ResponseEntity<?> changePetInfo(@Validated @RequestBody PetUpdate petUpdate) {
        petService.changePetInfo(petUpdate, userQueryService.getOneByEmail(petUpdate.email()));
        return SuccessReturn();
    }

    /**
     * 비밀번호 변경
     */
    @Operation(summary = "비밀번호 변경", description = "성공 여부만 반환 한다.")
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@Validated @RequestBody PasswordUpdate passwordUpdate) {
        // TODO: 사용자 확인 - 토큰 사용

        String encodedPassword = User.encodePassword(customPasswordEncoder, passwordUpdate.newPassword());
        User user = userCommandService.updatePassword(passwordUpdate.email(), encodedPassword);
        return SuccessReturn(UserResponse.from(user));
    }

    /**
     * 회원 탈퇴
     */
    @Operation(summary = "회원 탈퇴", description = "성공 여부만 반환 한다.")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) throws Exception {
        User user = userQueryService.getOneByUserId(userId);
        if (user.getUserSocial().equals(UserSocial.K))
            kakaoService.logout(user.getKakaoId()); // kakao 로그아웃 , Naver는 로그아웃 지원 X

        petService.deleteByUserId(userId);
        userCommandService.deleteByUserId(userId);
        certCommandService.deleteByUserId(userId);
        objectStorageService.deleteObject(BucketName.PROFILE, userId + "_profile.webp");

        return SuccessReturn();
    }

    /**
     * 로그아웃
     */
    @Operation(summary = "로그아웃", description = "성공 여부만 반환 한다.")
    @PostMapping(value = {"/logout/{userId}","/logout"})
    public ResponseEntity<?> logout(@PathVariable Integer userId) throws Exception {
        User user = userQueryService.getOneByUserId(userId);
        if (user.getUserSocial().equals(UserSocial.K))
            kakaoService.logout(user.getKakaoId()); // kakao 로그아웃 , Naver는 로그아웃 지원 X

        tokenService.delete(userId);
        return SuccessReturn();
    }
}
