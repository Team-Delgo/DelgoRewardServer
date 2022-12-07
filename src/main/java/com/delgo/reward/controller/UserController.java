package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.security.jwt.Access_JwtProperties;
import com.delgo.reward.comm.security.jwt.Refresh_JwtProperties;
import com.delgo.reward.domain.Code;
import com.delgo.reward.domain.Point;
import com.delgo.reward.domain.SmsAuth;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;
import com.delgo.reward.dto.OAuthSignUpDTO;
import com.delgo.reward.dto.user.*;
import com.delgo.reward.service.*;
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

    private final PetService petService;
    private final UserService userService;
    private final CodeService codeService;
    private final TokenService tokenService;
    private final PointService pointService;
    private final SmsAuthService smsAuthService;
    private final ArchiveService archiveService;

    @RequestMapping(value ="/")
    public ResponseEntity<?> defaultResponse() {
        return SuccessReturn();
    }

    @GetMapping("/myAccount")
    public ResponseEntity<?> myAccount(@RequestParam Integer userId) {
        UserInfoDTO userInfoDTO = userService.getUserInfo(userId);

        return SuccessReturn(userInfoDTO);
    }

    // 유저 정보 수정
    @PostMapping("/changeUserInfo")
    public ResponseEntity<?> changePetInfo(@Validated @RequestBody ModifyUserDTO modifyUserDTO) {
        String checkedEmail = modifyUserDTO.getEmail();

        User user = userService.getUserByEmail(checkedEmail);
        int userId = user.getUserId();
        User originUser = userService.getUserById(userId);

        if (modifyUserDTO.getName() != null)
            originUser.setName(modifyUserDTO.getName());

        if (modifyUserDTO.getProfileUrl() != null)
            originUser.setProfile(modifyUserDTO.getProfileUrl());

        if (modifyUserDTO.getGeoCode() != null && modifyUserDTO.getPGeoCode() != null) {
            originUser.setGeoCode(modifyUserDTO.getGeoCode());
            originUser.setPGeoCode(modifyUserDTO.getPGeoCode());

            // 주소 설정
            Code geoCode = codeService.getGeoCodeByCode(modifyUserDTO.getGeoCode());
            Code pGeoCode = codeService.getGeoCodeByCode(modifyUserDTO.getPGeoCode());
            String address = pGeoCode.getCodeDesc() + " " + geoCode.getCodeName();
            originUser.setAddress(address);
        }

        userService.changeUserInfo(originUser);

        return SuccessReturn();
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
    @PostMapping("/signup/oauth")
    public ResponseEntity<?> registerUserByOAuth(@Validated @RequestBody OAuthSignUpDTO signUpDTO, HttpServletResponse response) {

        // 주소 설정
        String address;
        Code pGeoCode = codeService.getGeoCodeByCode(signUpDTO.getPGeoCode());
        if(!signUpDTO.getGeoCode().equals("0")) {
            Code geoCode = codeService.getGeoCodeByCode(signUpDTO.getGeoCode());
            address = pGeoCode.getCodeDesc() + " " + geoCode.getCodeName();
        } else { // 세종특별시 일 경우 예외처리
            address = pGeoCode.getCodeName();
        }

        User user = User.builder()
                .name(signUpDTO.getUserName())
                .phoneNo(signUpDTO.getPhoneNo().replaceAll("[^0-9]", ""))
                .email(signUpDTO.getEmail())
                .address(address)
                .geoCode(signUpDTO.getGeoCode())
                .pGeoCode(signUpDTO.getPGeoCode())
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

        // WELCOME 업적 부여
        archiveService.registerWelcome(userByDB.getUserId());

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
            return ErrorReturn(ApiCode.EMAIL_DUPLICATE_ERROR);

        // 주소 설정
        String address;
        Code pGeoCode = codeService.getGeoCodeByCode(signUpDTO.getPGeoCode());
        if(!signUpDTO.getGeoCode().equals("0")) {
            Code geoCode = codeService.getGeoCodeByCode(signUpDTO.getGeoCode());
            address = pGeoCode.getCodeDesc() + " " + geoCode.getCodeName();
        } else { // 세종특별시 일 경우 예외처리
            address = pGeoCode.getCodeName();
        }

        User user = User.builder()
                .name(signUpDTO.getUserName())
                .email(signUpDTO.getEmail())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .phoneNo(signUpDTO.getPhoneNo().replaceAll("[^0-9]", ""))
                .userSocial(UserSocial.D)
                .address(address)
                .geoCode(signUpDTO.getGeoCode())
                .pGeoCode(signUpDTO.getPGeoCode())
                .build();
        Pet pet = Pet.builder()
                .name(signUpDTO.getPetName())
                .size(signUpDTO.getPetSize())
                .birthday(signUpDTO.getBirthday())
                .build();

        User userByDB = userService.signup(user, pet);
        Pet petByDB = petService.getPetByUserId(user.getUserId());

        // WELCOME 업적 부여
        archiveService.registerWelcome(userByDB.getUserId());

        String Access_jwtToken = tokenService.createToken("Access", user.getEmail()); // Access Token 생성
        String Refresh_jwtToken = tokenService.createToken("Refresh", user.getEmail()); // Refresh Token 생성

        response.addHeader(Access_JwtProperties.HEADER_STRING, Access_JwtProperties.TOKEN_PREFIX + Access_jwtToken);
        response.addHeader(Refresh_JwtProperties.HEADER_STRING, Refresh_JwtProperties.TOKEN_PREFIX + Refresh_jwtToken);

        return SuccessReturn(new UserPetDTO(userByDB, petByDB));
    }

    // 회원탈퇴
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {

        userService.deleteUser(userId);
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
