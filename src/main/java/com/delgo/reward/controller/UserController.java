package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.security.jwt.Access_JwtProperties;
import com.delgo.reward.comm.security.jwt.Refresh_JwtProperties;
import com.delgo.reward.domain.SmsAuth;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;
import com.delgo.reward.dto.OAuthSignUpDTO;
import com.delgo.reward.dto.user.*;
import com.delgo.reward.service.PetService;
import com.delgo.reward.service.SmsAuthService;
import com.delgo.reward.service.TokenService;
import com.delgo.reward.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController extends CommController {
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    private final PetService petService;
    private final TokenService tokenService;
    private final SmsAuthService smsAuthService;

    @GetMapping("/myAccount")
    public ResponseEntity<?> myAccount(@RequestParam Integer userId) {
        UserInfoDTO userInfoDTO = userService.getUserInfo(userId);

        return SuccessReturn(userInfoDTO);
    }

    // 펫 정보 수정
    @PostMapping("/changePetInfo")
    public ResponseEntity<?> changePetInfo(@Validated @RequestBody ModifyPetDTO modifyPetDTO) {
        String checkedEmail = modifyPetDTO.getEmail();

        User user = userService.getUserByEmail(checkedEmail);
        int userId = user.getUserId();
        Pet originPet = petService.getPetByUserId(userId);

        if (modifyPetDTO.getName() != null)
            originPet.setName(modifyPetDTO.getName());

        if (modifyPetDTO.getSize() != null)
            originPet.setSize(modifyPetDTO.getSize());

        petService.changePetInfo(originPet);

        return SuccessReturn();
    }

    // 비밀번호 변경 - Account Page
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Validated @RequestBody ResetPasswordDTO resetPassword) {
        // 사용자 확인 - 토큰 사용
        userService.changePassword(resetPassword.getEmail(), resetPassword.getNewPassword());
        return SuccessReturn();
    }

    // 비밀번호 재설정
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@Validated @RequestBody ResetPasswordDTO resetPasswordDTO) {
        User user = userService.getUserByEmail(resetPasswordDTO.getEmail()); // 유저 조회
        SmsAuth smsAuth = smsAuthService.getSmsAuthByPhoneNo(user.getPhoneNo()); // SMS DATA 조회
        if (!smsAuthService.isAuth(smsAuth))
            ErrorReturn(ApiCode.SMS_ERROR);

        userService.changePassword(resetPasswordDTO.getEmail(), resetPasswordDTO.getNewPassword());
        return SuccessReturn();
    }

    // 이메일 존재 유무 확인
    @GetMapping("/emailAuth")
    public ResponseEntity<?> emailAuth(@RequestParam String email) {
        if (email.isBlank()) {
            return ErrorReturn(ApiCode.PARAM_ERROR);
        }

        if (userService.isEmailExisting(email)) {
            return SuccessReturn(userService.getUserByEmail(email).getPhoneNo());
        }
        return ErrorReturn(ApiCode.NOT_FOUND_DATA);
    }

    // 이메일 중복 확인
    @GetMapping("/emailCheck")
    public ResponseEntity<?> emailCheck(@RequestParam String email) {
        if (email.isBlank()) {
            return ErrorReturn(ApiCode.PARAM_ERROR);
        }
        if (!userService.isEmailExisting(email)) {
            return SuccessReturn();
        } else {
            return ErrorReturn(ApiCode.UNKNOWN_ERROR);
        }
    }

    // 이름 중복 확인
    @GetMapping("/nameCheck")
    public ResponseEntity<?> nameCheck(@RequestParam String name) {
        if (name.isBlank()) {
            return ErrorReturn(ApiCode.PARAM_ERROR);
        }
        if (!userService.isNameExisting(name))
            return SuccessReturn();
        else
            return ErrorReturn(ApiCode.UNKNOWN_ERROR);
    }

    // 소셜 회원가입
    @PostMapping("/oauth-signup")
    public ResponseEntity<?> registerUserByOAuth(@Validated @RequestBody OAuthSignUpDTO signUpDTO, HttpServletResponse response) {
        User user = User.builder()
                .name(signUpDTO.getUserName())
                .phoneNo(signUpDTO.getPhoneNo().replaceAll("[^0-9]", ""))
                .email(signUpDTO.getEmail())
                .password("")
                .userSocial(signUpDTO.getUserSocial())
                .build();
        Pet pet = Pet.builder()
                .name(signUpDTO.getPetName())
                .size(signUpDTO.getPetSize())
                .birthday(signUpDTO.getBirthday())
                .build();

        // Apple 회원가입 시 appleUniqueNo 넣어주어야 함.
        if (signUpDTO.getUserSocial() == UserSocial.A) {
            if (signUpDTO.getAppleUniqueNo() == null || signUpDTO.getAppleUniqueNo().isBlank())
                return ErrorReturn(ApiCode.PARAM_ERROR);
            else
                user.setAppleUniqueNo(signUpDTO.getAppleUniqueNo());
        }

        User userByDB = userService.signup(user, pet);
        Pet petByDB = petService.getPetByUserId(user.getUserId());

        String Access_jwtToken = tokenService.createToken("Access", user.getEmail()); // Access Token 생성
        String Refresh_jwtToken = tokenService.createToken("Refresh", user.getEmail()); // Refresh Token 생성

        response.addHeader(Access_JwtProperties.HEADER_STRING, Access_JwtProperties.TOKEN_PREFIX + Access_jwtToken);
        response.addHeader(Refresh_JwtProperties.HEADER_STRING, Refresh_JwtProperties.TOKEN_PREFIX + Refresh_jwtToken);

        return SuccessReturn(new UserPetDTO(userByDB, petByDB));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Validated @RequestBody SignUpDTO signUpDTO, HttpServletResponse response) {
        if (userService.isEmailExisting(signUpDTO.getEmail())) // Email 중복확인
            return ErrorReturn(ApiCode.UNKNOWN_ERROR);

        User user = User.builder()
                .name(signUpDTO.getUserName())
                .email(signUpDTO.getEmail())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .phoneNo(signUpDTO.getPhoneNo().replaceAll("[^0-9]", ""))
                .userSocial(UserSocial.D)
                .geoCode(signUpDTO.getGeoCode())
                .pGeoCode(signUpDTO.getP_geoCode())
                .build();
        Pet pet = Pet.builder()
                .name(signUpDTO.getPetName())
                .size(signUpDTO.getPetSize())
                .birthday(signUpDTO.getBirthday())
                .build();

        User userByDB = userService.signup(user, pet);
//        user.setPassword(""); // 보안
        Pet petByDB = petService.getPetByUserId(user.getUserId());

        String Access_jwtToken = tokenService.createToken("Access", user.getEmail()); // Access Token 생성
        String Refresh_jwtToken = tokenService.createToken("Refresh", user.getEmail()); // Refresh Token 생성

        response.addHeader(Access_JwtProperties.HEADER_STRING, Access_JwtProperties.TOKEN_PREFIX + Access_jwtToken);
        response.addHeader(Refresh_JwtProperties.HEADER_STRING, Refresh_JwtProperties.TOKEN_PREFIX + Refresh_jwtToken);

        return SuccessReturn(new UserPetDTO(userByDB, petByDB));
    }

    // 회원탈퇴
    @PostMapping(value = {"/delete/{userId}", "/delete"})
    public ResponseEntity<?> deleteUser(@PathVariable(value = "userId") Integer userId) {

        userService.deleteUser(userId);
        return SuccessReturn();
    }

    @GetMapping("/user/info")
    public ResponseEntity<?> getInfo(@RequestParam Integer userId){
        UserInfoDTO userInfoDTO = userService.getUserInfo(userId);
        return SuccessReturn(userInfoDTO);
    }
}
