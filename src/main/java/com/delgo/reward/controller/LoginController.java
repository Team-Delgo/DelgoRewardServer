package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.comm.security.jwt.JwtService;
import com.delgo.reward.comm.security.jwt.JwtToken;
import com.delgo.reward.comm.security.jwt.config.RefreshTokenProperties;
import com.delgo.reward.dto.user.UserResDTO;
import com.delgo.reward.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        jwtService.publishToken(response, jwt);

        return SuccessReturn(new UserResDTO(userService.getUserById(userId)));
    }

    /*
     * Login 실패
     * ErrorCode 반환.
     */

    @PostMapping("/login/fail")
    public ResponseEntity<?> loginFail() {
        return ErrorReturn(APICode.LOGIN_ERROR);
    }

    /*
     * Access_Token 재발급 API
     * Refresh_Token 인증 진행
     * 성공 : 재발급, 실패 : 오류 코드 반환
     */
    @GetMapping("/api/token/reissue")
    public ResponseEntity<?> tokenReissue(@CookieValue(name = RefreshTokenProperties.HEADER_STRING, required = false) String refreshToken, HttpServletResponse response) {
        try {
            int userId = jwtService.getUserIdByRefreshToken(refreshToken);
            JwtToken jwt = jwtService.createToken(userId);
            jwtService.publishToken(response, jwt);

            return SuccessReturn();
        } catch (Exception e) { // Refresh_Toekn 인증 실패 ( 로그인 화면으로 이동 필요 )
            e.printStackTrace();
            return TokenErrorReturn();
        }
    }

    /*
     * TOKEN 인증 프로세스중 에러 발생
     * ErrorCode 반환.
     */
    @RequestMapping("/token/error")
    public ResponseEntity<?> tokenError() {
        return TokenErrorReturn();
    }
}
