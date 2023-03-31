package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.dto.user.ModifyPetDTO;
import com.delgo.reward.dto.user.ModifyUserDTO;
import com.delgo.reward.dto.user.ResetPasswordDTO;
import com.delgo.reward.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController extends CommController {

    private final PetService petService;
    private final CertService certService;
    private final UserService userService;
    private final TokenService tokenService;
    private final RankingService rankingService;

    // 알림 정보 수정
//    @PutMapping(value = {"/notify/{userId}, /notify"})
//    public ResponseEntity<?> changeNotify(@PathVariable Integer userId){
//        log.info("test");
//        return SuccessReturn(userService.changeNotify(userId));
//    }

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
     * @param modifyUserDTO
     * @return 성공 / 실패 여부
     */
    @PutMapping("/user")
    public ResponseEntity<?> changeUserInfo(@Validated @RequestBody ModifyUserDTO modifyUserDTO) {
        userService.changeUserInfo(modifyUserDTO);
        // 랭킹 실시간으로 집계
        rankingService.rankingByPoint();
        return SuccessReturn();
    }

    /**
     * 펫 정보 수정
     * @param modifyPetDTO
     * @return 성공 / 실패 여부
     */
    @PutMapping("/pet")
    public ResponseEntity<?> changePetInfo(@Validated @RequestBody ModifyPetDTO modifyPetDTO) {
        petService.changePetInfo(modifyPetDTO);
        return SuccessReturn();
    }

    /**
     * 비밀번호 변경
     * @param resetPassword
     * @return 성공 / 실패 여부
     */
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@Validated @RequestBody ResetPasswordDTO resetPassword) {
        // 사용자 확인 - 토큰 사용
        userService.changePassword(resetPassword.getEmail(), resetPassword.getNewPassword());
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
        rankingService.rankingByPoint(); // 랭킹 실시간으로 집계

        return SuccessReturn();
    }

    /**
     * 유저 조회
     * @param userId
     * @return 유저 정보 반환
     */
    @GetMapping
    public ResponseEntity<?> getAccount(@RequestParam Integer userId){
        return SuccessReturn(Map.of(
                "user",userService.getUserById(userId), // user
                "totalCount",certService.getTotalCountByUser(userId), // totalCount
                "mungpleCount",certService.getTotalCountOfMungpleByUser(userId) // mungpleCount
        ));
    }

    /**
     * 로그아웃
     * @param userId
     * @return 성공 / 실패 여부
     */
    @PostMapping(value = {"/logout/{userId}","/logout"})
    public ResponseEntity<?> logout(@PathVariable Integer userId){
        tokenService.deleteToken(userId);
        return SuccessReturn();
    }
}
