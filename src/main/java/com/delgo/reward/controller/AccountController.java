package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.cert.UserVisitMungpleCountDTO;
import com.delgo.reward.dto.user.AccountResDTO;
import com.delgo.reward.dto.user.UserResDTO;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import com.delgo.reward.record.user.ModifyPetRecord;
import com.delgo.reward.record.user.ModifyUserRecord;
import com.delgo.reward.record.user.ResetPasswordRecord;
import com.delgo.reward.service.PetService;
import com.delgo.reward.service.UserService;
import com.delgo.reward.service.cert.CertQueryService;
import com.delgo.reward.service.mungple.MungpleService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController extends CommController {

    private final PetService petService;
    private final UserService userService;
    private final MungpleService mungpleService;
    private final CertQueryService certQueryService;

    /**
     * 내 정보 조회
     * @param userId
     * @return 유저 정보 반환
     */
    @Operation(summary = "내 정보 조회 ", description = "My Page, 및 활동 페이지에서 필요한 모든 Data를 반환한다.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AccountResDTO.class))})
    @GetMapping
    public ResponseEntity<?> getAccount(@RequestParam Integer userId){
        return SuccessReturn(new AccountResDTO(
                userService.getUserById(userId), // User
                userService.getActivityByUserId(userId), // Activity Data
                UserVisitMungpleCountDTO.setMungpleData( // UserVisitMungpleCountDTO
                        certQueryService.getVisitedMungpleIdListTop3ByUserId(userId), // UserVisitMungpleCountDTO List
                        Mungple.listToMap(mungpleService.getAll())))); // Mungple List
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
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResDTO.class))})
    @PutMapping(value = "/user")
    public ResponseEntity<?> changeUserInfo(@Validated @RequestBody ModifyUserRecord modifyUserRecord) {
        User user = userService.changeUserInfo(modifyUserRecord);

//        랭킹 실시간으로 집계
//        rankingService.rankingByPoint();
        return SuccessReturn(new UserResDTO(user));
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
