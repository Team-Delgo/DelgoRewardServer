package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.Point;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
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
public class AccountController extends CommController {

    private final UserService userService;
    private final RankingService rankingService;
    private final PetService petService;
    private final PointService pointService;

    // User. Pet 모두 조회
    @GetMapping("/user/all")
    public ResponseEntity<?> getUserAndPet(@RequestParam Integer userId) {
        return SuccessReturn(new UserResDTO(userService.getUserById(userId), petService.getPetByUserId(userId)));
    }

    @GetMapping("/myAccount")
    public ResponseEntity<?> myAccount(@RequestParam Integer userId) {
        UserInfoDTO userInfoDTO = userService.getUserInfo(userId);

        return SuccessReturn(userInfoDTO);
    }

    // 유저 정보 수정
    @PostMapping("/changeUserInfo")
    public ResponseEntity<?> changePetInfo(@Validated @RequestBody ModifyUserDTO modifyUserDTO) {
        userService.changeUserInfo(modifyUserDTO);
        // 랭킹 실시간으로 집계
        rankingService.rankingByPoint();
        return SuccessReturn();
    }

    // 펫 정보 수정
    @PostMapping("/changePetInfo")
    public ResponseEntity<?> changePetInfo(@Validated @RequestBody ModifyPetDTO modifyPetDTO) {
        petService.changePetInfo(modifyPetDTO);
        return SuccessReturn();
    }

    // 비밀번호 변경 - Account Page
    @PostMapping("/changePassword")
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

    @GetMapping("/user/info")
    public ResponseEntity<?> getInfo(@RequestParam Integer userId){
        UserInfoDTO userInfoDTO = userService.getUserInfo(userId);
        return SuccessReturn(userInfoDTO);
    }

    @GetMapping("/user/point")
    public ResponseEntity<?> getPoint(@RequestParam Integer userId){
        Point point = pointService.getPointByUserId(userId);
        return SuccessReturn(point);
    }
}
