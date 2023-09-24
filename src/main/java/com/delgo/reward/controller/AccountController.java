package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.user.UserByCertCountResDTO;
import com.delgo.reward.dto.user.UserResDTO;
import com.delgo.reward.record.user.ModifyPetRecord;
import com.delgo.reward.record.user.ModifyUserRecord;
import com.delgo.reward.record.user.ResetPasswordRecord;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.PetService;
import com.delgo.reward.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Hidden
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
    @GetMapping
    public ResponseEntity<?> getAccount(@RequestParam Integer userId){
        return SuccessReturn(new UserByCertCountResDTO(
                userService.getUserById(userId),
                certService.getCertCountByUser(userId),
                certService.getCertCountByMungpleOfSpecificUser(userId),
                userService.getCategoryCountByUserId(userId),
                userService.getActivityByUserId(userId)));
    }


    /**
     * 알림 정보 수정
     * @param userId
     * @return 수정된 데이터 반환
     */
    @PutMapping(value = {"/notify/{userId}", "/notify"})
    public ResponseEntity<?> changeNotify(@PathVariable Integer userId){
        return SuccessReturn(userService.changeNotify(userId));
    }

    /**
     * 유저 정보 수정
     * @param modifyUserRecord
     * @return 성공 / 실패 여부
     */
    @PutMapping(value = "/user", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> changeUserInfo(@Validated @RequestPart(value = "data") ModifyUserRecord modifyUserRecord, @RequestPart(required = false) MultipartFile profile) {
        User user = userService.changeUserInfo(modifyUserRecord, profile);
        // 랭킹 실시간으로 집계
//        rankingService.rankingByPoint();
        return SuccessReturn(new UserResDTO(user));
    }

    /**
     * 펫 정보 수정
     * @param modifyPetRecord
     * @return 성공 / 실패 여부
     */
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
    @PostMapping(value = {"/logout/{userId}","/logout"})
    public ResponseEntity<?> logout(@PathVariable Integer userId) throws Exception {
        userService.logout(userId);
        return SuccessReturn();
    }
}
