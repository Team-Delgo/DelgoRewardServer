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
@RequestMapping("/account")
public class AccountController extends CommController {

    private final PetService petService;
    private final CertService certService;
    private final UserService userService;
    private final PointService pointService;
    private final TokenService tokenService;
    private final RankingService rankingService;
    private final LikeListService likeListService;

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
        userService.deleteUser(userId); // USER DELETE
        likeListService.deleteUserRelatedLike(userId); // USER가 좋아요 누른 DATA 삭제
        rankingService.rankingByPoint(); // 랭킹 실시간으로 집계

        return SuccessReturn();
    }

    @GetMapping
    public ResponseEntity<?> getAccount(@RequestParam Integer userId){
        return SuccessReturn(Map.of(
                "user",userService.getUserById(userId), // user
                "totalCount",certService.getTotalCountByUser(userId), // totalCount
                "mungpleCount",certService.getTotalCountOfMungpleByUser(userId) // mungpleCount
        ));
    }

    @PostMapping(value = {"/logout/{userId}","/logout"})
    public ResponseEntity<?> logout(@PathVariable Integer userId){
        tokenService.deleteToken(userId);
        return SuccessReturn();
    }
}
