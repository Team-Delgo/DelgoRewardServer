package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.security.jwt.JwtService;
import com.delgo.reward.comm.security.jwt.JwtToken;
import com.delgo.reward.comm.security.jwt.config.AccessTokenProperties;
import com.delgo.reward.comm.security.jwt.config.RefreshTokenProperties;
import com.delgo.reward.dto.user.UserResDTO;
import com.delgo.reward.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController extends CommController {

    private final JwtService jwtService;
    private final UserService userService;

    /*
     * Login 성공
     * Header [ Access_Token, Refresh_Token ]
     * Body [ User , Pet ]
     * 담아서 반환한다.
     */
    @PostMapping("/login/success")
    public ResponseEntity<?> loginSuccess(HttpServletRequest request, HttpServletResponse response) {
        int userId = Integer.parseInt(request.getAttribute("userId").toString());

        JwtToken jwt = jwtService.createToken(userId);
        response.addHeader(AccessTokenProperties.HEADER_STRING, AccessTokenProperties.TOKEN_PREFIX + jwt.getAccessToken());
        response.addHeader(RefreshTokenProperties.HEADER_STRING, RefreshTokenProperties.TOKEN_PREFIX + jwt.getRefreshToken());

        return SuccessReturn(new UserResDTO(userService.getUserById(userId)));
    }

    /*
     * Login 실패
     * ErrorCode 반환.
     */

    @PostMapping("/login/fail")
    public ResponseEntity<?> loginFail() {
        return ErrorReturn(ApiCode.LOGIN_ERROR);
    }

    /*
     * Access_Token 재발급 API
     * Refresh_Token 인증 진행
     * 성공 : 재발급, 실패 : 오류 코드 반환
     */
    @GetMapping("/api/token/reissue")
    public ResponseEntity<?> tokenReissue(HttpServletRequest request, HttpServletResponse response) {
        try {
            JwtToken jwt = jwtService.createToken(jwtService.getUserIdByRefreshToken());
            response.addHeader(AccessTokenProperties.HEADER_STRING, AccessTokenProperties.TOKEN_PREFIX + jwt.getAccessToken());
            response.addHeader(RefreshTokenProperties.HEADER_STRING, RefreshTokenProperties.TOKEN_PREFIX + jwt.getRefreshToken());

            return SuccessReturn();
        } catch (Exception e) { // Refresh_Toekn 인증 실패 ( 로그인 화면으로 이동 필요 )
            e.printStackTrace();
            return ErrorReturn(ApiCode.TOKEN_ERROR);
        }
    }

    /*
     * TOKEN 인증 프로세스중 에러 발생
     * ErrorCode 반환.
     */
    @RequestMapping("/token/error")
    public ResponseEntity<?> tokenError() {
        return ErrorReturn(ApiCode.TOKEN_ERROR);
    }
}
