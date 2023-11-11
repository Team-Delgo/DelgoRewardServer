package com.delgo.reward.user.controller;

import com.delgo.reward.certification.service.CertService;
import com.delgo.reward.comm.CommController;
import com.delgo.reward.record.user.ModifyPetRecord;
import com.delgo.reward.record.user.ModifyUserRecord;
import com.delgo.reward.record.user.ResetPasswordRecord;
import com.delgo.reward.user.controller.response.AccountResponse;
import com.delgo.reward.user.controller.response.UserResponse;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.service.PetService;
import com.delgo.reward.user.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController extends CommController {

    private final PetService petService;
    private final CertService certService;
    private final UserService userService;

    /**
     * 내 정보 조회
     * @param userId
     * @return 유저 정보 반환
     */
    @Operation(summary = "내 정보 조회 ", description = "My Page, 및 활동 페이지에서 필요한 모든 Data를 반환한다.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponse.class))})
    @GetMapping
    public ResponseEntity<?> getAccount(@RequestParam Integer userId){
        return SuccessReturn(AccountResponse.from(
                userService.getUserById(userId),
                userService.getActivityByUserId(userId),
                certService.getVisitedMungpleIdListTop3ByUserId(userId)));
    }


    /**
     * 알림 정보 수정
     * @param userId
     * @return 수정된 데이터 반환
     */
    @Hidden
    @PutMapping(value = {"/notify/{userId}", "/notify"})
    public ResponseEntity<?> changeNotify(@PathVariable Integer userId){
        return SuccessReturn(userService.changeNotify(userId));
    }

    /**
     * 유저 정보 수정
     * @param modifyUserRecord
     * @return 성공 / 실패 여부
     */
    @Operation(summary = "유저 정보 수정", description = "수정 된 유저 정보를 반환한다. \n profile은 multipart로 받는다. (RequestBody 체크 필요)")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @PutMapping(value = "/user", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> changeUserInfo(@Validated @RequestPart(value = "data") ModifyUserRecord modifyUserRecord, @RequestPart(required = false) MultipartFile profile) {
        User user = userService.changeUserInfo(modifyUserRecord, profile);
//        랭킹 실시간으로 집계
//        rankingService.rankingByPoint();
        return SuccessReturn(UserResponse.from(user));
    }

    /**
     * 펫 정보 수정
     * @param modifyPetRecord
     * @return 성공 / 실패 여부
     */
    @Operation(summary = "펫 정보 수정", description = "성공 여부만 반환 한다.")
    @PutMapping("/pet")
    public ResponseEntity<?> changePetInfo(@Validated @RequestBody ModifyPetRecord modifyPetRecord) {
        petService.changePetInfo(modifyPetRecord, userService.getUserByEmail(modifyPetRecord.email()));
        return SuccessReturn();
    }

    /**
     * 비밀번호 변경
     * @param resetPasswordRecord
     * @return 성공 / 실패 여부
     */
    @Operation(summary = "비밀번호 변경", description = "성공 여부만 반환 한다.")
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@Validated @RequestBody ResetPasswordRecord resetPasswordRecord) {
        // 사용자 확인 - 토큰 사용
        userService.changePassword(resetPasswordRecord.email(), resetPasswordRecord.newPassword());
        return SuccessReturn();
    }

    /**
     * 회원 탈퇴
     * @param userId
     * @return 성공 / 실패 여부
     * @throws Exception
     */
    @Operation(summary = "회원 탈퇴", description = "성공 여부만 반환 한다.")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) throws Exception {
        userService.deleteUser(userId); // USER DELETE
//        rankingService.rankingByPoint(); // 랭킹 실시간으로 집계

        return SuccessReturn();
    }

    /**
     * 로그아웃
     * @param userId
     * @return 성공 / 실패 여부
     */
    @Operation(summary = "로그아웃", description = "성공 여부만 반환 한다.")
    @PostMapping(value = {"/logout/{userId}","/logout"})
    public ResponseEntity<?> logout(@PathVariable Integer userId) throws Exception {
        userService.logout(userId);
        return SuccessReturn();
    }
}
