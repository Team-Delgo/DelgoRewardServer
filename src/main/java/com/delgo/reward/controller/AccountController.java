package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.Point;
import com.delgo.reward.dto.user.*;
import com.delgo.reward.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController extends CommController {

    private final UserService userService;
    private final RankingService rankingService;
    private final PetService petService;
    private final PointService pointService;
    private final TokenService tokenService;

    // 알림 정보 수정
//    @PutMapping(value = {"/notify/{userId}, /notify"})
//    public ResponseEntity<?> changeNotify(@PathVariable Integer userId){
//        log.info("test");
//        return SuccessReturn(userService.changeNotify(userId));
//    }
    @PutMapping(value = {"/notify/{userId}", "/notify"})
    public ResponseEntity<?> changeNotify(@PathVariable Integer userId){
        return SuccessReturn(userService.changeNotify(userId));
    }

    // 유저 정보 수정
    @PutMapping("/user")
    public ResponseEntity<?> changePetInfo(@Validated @RequestBody ModifyUserDTO modifyUserDTO) {
        userService.changeUserInfo(modifyUserDTO);
        // 랭킹 실시간으로 집계
        rankingService.rankingByPoint();
        return SuccessReturn();
    }

    // 펫 정보 수정
    @PutMapping("/pet")
    public ResponseEntity<?> changePetInfo(@Validated @RequestBody ModifyPetDTO modifyPetDTO) {
        petService.changePetInfo(modifyPetDTO);
        return SuccessReturn();
    }

    // 비밀번호 변경 - Account Page
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@Validated @RequestBody ResetPasswordDTO resetPassword) {
        // 사용자 확인 - 토큰 사용
        userService.changePassword(resetPassword.getEmail(), resetPassword.getNewPassword());
        return SuccessReturn();
    }

    // 회원탈퇴
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        // 랭킹 실시간으로 집계
        rankingService.rankingByPoint();
        return SuccessReturn();
    }

    @GetMapping("/point")
    public ResponseEntity<?> getPoint(@RequestParam Integer userId){
        Point point = pointService.getPointByUserId(userId);
        return SuccessReturn(point);
    }

    @PostMapping(value = {"/logout/{userId}","/logout"})
    public ResponseEntity<?> logout(@PathVariable Integer userId){
        tokenService.deleteToken(userId);
        return SuccessReturn();
    }
}
