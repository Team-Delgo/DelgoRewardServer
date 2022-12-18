package com.delgo.reward.controller;


import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.security.jwt.JwtToken;
import com.delgo.reward.comm.security.jwt.config.AccessTokenProperties;
import com.delgo.reward.comm.security.jwt.JwtService;
import com.delgo.reward.comm.security.jwt.config.RefreshTokenProperties;
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
@RequestMapping("/user")
public class UserController extends CommController {
    private final PasswordEncoder passwordEncoder;

    private final PetService petService;
    private final UserService userService;
    private final CodeService codeService;
    private final JwtService jwtService;
    private final SmsAuthService smsAuthService;
    private final ArchiveService archiveService;

    // User. Pet 모두 조회
    @GetMapping
    public ResponseEntity<?> getUser(@RequestParam Integer userId) {
        return SuccessReturn(new UserResDTO(userService.getUserById(userId), petService.getPetByUserId(userId)));
    }

    // 비밀번호 재설정
    @PutMapping("/password")
    public ResponseEntity<?> resetPassword(@Validated @RequestBody ResetPasswordDTO resetPasswordDTO) {
        User user = userService.getUserByEmail(resetPasswordDTO.getEmail()); // 유저 조회
        SmsAuth smsAuth = smsAuthService.getSmsAuthByPhoneNo(user.getPhoneNo()); // SMS DATA 조회
        if (!smsAuthService.isAuth(smsAuth))
            ErrorReturn(ApiCode.SMS_ERROR);

        userService.changePassword(resetPasswordDTO.getEmail(), resetPasswordDTO.getNewPassword());
        return SuccessReturn();
    }

    // 소셜 회원가입
    @PostMapping("/oauth")
    public ResponseEntity<?> registerUserByOAuth(@Validated @RequestBody OAuthSignUpDTO signUpDTO, HttpServletResponse response) {

        // 주소 설정
        String address = (signUpDTO.getGeoCode().equals("0"))  // 세종시는 구가 없음.
                ? codeService.getAddress(signUpDTO.getPGeoCode(), true)
                : codeService.getAddress(signUpDTO.getGeoCode(), false);

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
                .breed(signUpDTO.getBreed())
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

        JwtToken jwt = jwtService.createToken(user.getUserId());
        response.addHeader(AccessTokenProperties.HEADER_STRING, AccessTokenProperties.TOKEN_PREFIX + jwt.getAccessToken());
        response.addHeader(RefreshTokenProperties.HEADER_STRING, RefreshTokenProperties.TOKEN_PREFIX + jwt.getRefreshToken());

        return SuccessReturn(new UserResDTO(userByDB, petByDB));
    }

    // 회원가입
    @PostMapping
    public ResponseEntity<?> registerUser(@Validated @RequestBody SignUpDTO signUpDTO, HttpServletResponse response) {
        if (userService.isEmailExisting(signUpDTO.getEmail())) // Email 중복확인
            return ErrorReturn(ApiCode.EMAIL_DUPLICATE_ERROR);

        // 주소 설정
        String address = (signUpDTO.getGeoCode().equals("0"))  // 세종시는 구가 없음.
                ? codeService.getAddress(signUpDTO.getPGeoCode(), true)
                : codeService.getAddress(signUpDTO.getGeoCode(), false);

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
                .breed(signUpDTO.getBreed())
                .birthday(signUpDTO.getBirthday())
                .build();

        User userByDB = userService.signup(user, pet);
        Pet petByDB = petService.getPetByUserId(user.getUserId());

        // WELCOME 업적 부여
        archiveService.registerWelcome(userByDB.getUserId());

        JwtToken jwt = jwtService.createToken(user.getUserId());
        response.addHeader(AccessTokenProperties.HEADER_STRING, AccessTokenProperties.TOKEN_PREFIX + jwt.getAccessToken());
        response.addHeader(RefreshTokenProperties.HEADER_STRING, RefreshTokenProperties.TOKEN_PREFIX + jwt.getRefreshToken());

        return SuccessReturn(new UserResDTO(userByDB, petByDB));
    }

}
