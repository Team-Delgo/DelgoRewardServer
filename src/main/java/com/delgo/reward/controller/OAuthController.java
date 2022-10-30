package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.oauth.AppleService;
import com.delgo.reward.comm.oauth.KakaoService;
import com.delgo.reward.comm.oauth.NaverService;
import com.delgo.reward.comm.security.jwt.Access_JwtProperties;
import com.delgo.reward.comm.security.jwt.Refresh_JwtProperties;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;
import com.delgo.reward.dto.OAuthDTO;
import com.delgo.reward.dto.user.UserPetDTO;
import com.delgo.reward.service.PetService;
import com.delgo.reward.service.TokenService;
import com.delgo.reward.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController extends CommController {
    private final UserService userService;
    private final PetService petService;
    private final KakaoService kakaoService;
    private final NaverService naverService;
    private final AppleService appleService;
    private final TokenService tokenService;

    // Apple
    @PostMapping(value = {"/apple/id_token/{id_token}", "/apple/id_token/"})
    public ResponseEntity oauthApple(@PathVariable String id_token, HttpServletResponse response) {
        JSONObject payload = appleService.decodeFromIdToken(id_token);
        String appleUniqueNo = payload.getAsString("sub");  //  회원 고유 식별자
        log.info("appleUniqueNo : {}", appleUniqueNo);

        // DB에 appleUniqueNo 존재 X
        if (!userService.isAppleUniqueNoExisting(appleUniqueNo))
            return ErrorReturn(ApiCode.APPLE_UNIQUE_NO_NOT_FOUND, appleUniqueNo);
        
        // DB에 appleUniqueNo 존재 O -> 해당 User 반환
        User user = userService.getUserByAppleUniqueNo(appleUniqueNo);
        Pet pet = petService.getPetByUserId(user.getUserId());

        String Access_jwtToken = tokenService.createToken("Access", user.getEmail()); // Access Token 생성
        String Refresh_jwtToken = tokenService.createToken("Refresh", user.getEmail()); // Refresh Token 생성

        response.addHeader(Access_JwtProperties.HEADER_STRING, Access_JwtProperties.TOKEN_PREFIX + Access_jwtToken);
        response.addHeader(Refresh_JwtProperties.HEADER_STRING, Refresh_JwtProperties.TOKEN_PREFIX + Refresh_jwtToken);

        return SuccessReturn(new UserPetDTO(user, pet));
    }

    // Kakao
    @PostMapping(value = {"/kakao/access-code/{code}","/kakao/access-code/"})
    public ResponseEntity<?> oauthKakao(@PathVariable String code, HttpServletResponse response) throws Exception {
        String accessToken = kakaoService.getKakaoAccessToken(code);
        OAuthDTO oAuthDTO = kakaoService.createKakaoUser(accessToken);
        String kakaoPhoneNo = oAuthDTO.getPhoneNo();

        // 카카오 에러 : 1000
        if(kakaoPhoneNo.equals(""))
            return ErrorReturn(ApiCode.UNKNOWN_ERROR);

        // 카카오 전화번호 X
        if(kakaoPhoneNo.equals("PhoneNoNotExist"))
            return ErrorReturn(ApiCode.OAUTH_PHONE_NO_NOT_EXIST);

        kakaoPhoneNo = kakaoPhoneNo.replaceAll("[^0-9]", "");
        kakaoPhoneNo = "010" + kakaoPhoneNo.substring(kakaoPhoneNo.length()-8);
        oAuthDTO.setPhoneNo(kakaoPhoneNo);

        // 카카오 전화번호 0 , DB 전화번호 X -> PhoneNo 반환
        if(!userService.isPhoneNoExisting(kakaoPhoneNo))
            return ErrorReturn(ApiCode.PHONE_NO_NOT_EXIST, oAuthDTO);

        User user = userService.getUserByPhoneNo(kakaoPhoneNo);
        oAuthDTO.setUserSocial(user.getUserSocial());

        // 카카오 전화번호 0 , DB 전화번호 0, 카카오 연동 X -> 현재 연동된 OAuth 코드 반환
        oAuthDTO.setEmail(user.getEmail());
        if(user.getUserSocial() != UserSocial.K)
            return ErrorReturn(ApiCode.ANOTHER_OAUTH_CONNECT, oAuthDTO);

        Pet pet = petService.getPetByUserId(user.getUserId());
        String Access_jwtToken = tokenService.createToken("Access", user.getEmail()); // Access Token 생성
        String Refresh_jwtToken = tokenService.createToken("Refresh", user.getEmail()); // Refresh Token 생성

        response.addHeader(Access_JwtProperties.HEADER_STRING, Access_JwtProperties.TOKEN_PREFIX + Access_jwtToken);
        response.addHeader(Refresh_JwtProperties.HEADER_STRING, Refresh_JwtProperties.TOKEN_PREFIX + Refresh_jwtToken);

        return SuccessReturn(new UserPetDTO(user, pet)); //200
    }

    // Naver
    @PostMapping(value = {"/naver/state-code/{state}/{code}","/naver/state-code/"})
    public ResponseEntity<?> oauthNaver(@PathVariable String state, @PathVariable String code, HttpServletResponse response) throws Exception {
        String accessToken = naverService.getNaverAccessToken(state, code);
        OAuthDTO oAuthDTO = naverService.createNaverUser(accessToken);
        String naverPhoneNo = oAuthDTO.getPhoneNo();

        // 네이버 에러 : 1000
        if(naverPhoneNo.equals(""))
            return ErrorReturn(ApiCode.UNKNOWN_ERROR);

        // 네이버 전화번호 X
        if(naverPhoneNo.equals("PhoneNoNotExist"))
            return ErrorReturn(ApiCode.OAUTH_PHONE_NO_NOT_EXIST);

        naverPhoneNo = naverPhoneNo.replaceAll("[^0-9]", "");
        oAuthDTO.setPhoneNo(naverPhoneNo);

        // 네이버 전화번호 0 , DB 전화번호 X -> PhoneNo 반환
        if(!userService.isPhoneNoExisting(naverPhoneNo))
            return ErrorReturn(ApiCode.PHONE_NO_NOT_EXIST, oAuthDTO);

        User user = userService.getUserByPhoneNo(naverPhoneNo);
        oAuthDTO.setUserSocial(user.getUserSocial());

        // 네이버 전화번호 0 , DB 전화번호 0, 네이버 연동 X -> 현재 연동된 OAuth 코드 반환
        oAuthDTO.setEmail(user.getEmail());
        if(user.getUserSocial() != UserSocial.N)
            return ErrorReturn(ApiCode.ANOTHER_OAUTH_CONNECT, oAuthDTO);

        Pet pet = petService.getPetByUserId(user.getUserId());
        String Access_jwtToken = tokenService.createToken("Access", user.getEmail()); // Access Token 생성
        String Refresh_jwtToken = tokenService.createToken("Refresh", user.getEmail()); // Refresh Token 생성

        response.addHeader(Access_JwtProperties.HEADER_STRING, Access_JwtProperties.TOKEN_PREFIX + Access_jwtToken);
        response.addHeader(Refresh_JwtProperties.HEADER_STRING, Refresh_JwtProperties.TOKEN_PREFIX + Refresh_jwtToken);

        return SuccessReturn(new UserPetDTO(user, pet)); //200
    }
}
