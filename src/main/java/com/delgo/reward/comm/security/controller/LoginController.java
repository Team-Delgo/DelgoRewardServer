package com.delgo.reward.comm.security.controller;

import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.comm.security.service.JwtService;
import com.delgo.reward.common.controller.CommController;
import com.delgo.reward.comm.security.config.RefreshTokenProperties;
import com.delgo.reward.user.response.UserResponse;
import com.delgo.reward.user.service.UserQueryService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@Hidden
@RestController
@RequiredArgsConstructor
public class LoginController extends CommController {
    private final JwtService jwtService;
    private final UserQueryService userQueryService;

    /***
     * Login 성공
     */
    @PostMapping("/login/success")
    public ResponseEntity<?> loginSuccess(HttpServletRequest request, HttpServletResponse response) {
        int userId = Integer.parseInt(request.getAttribute("userId").toString());
        jwtService.publish(response, userId);
        return SuccessReturn(UserResponse.from(userQueryService.getOneByUserId(userId)));
    }

    /***
     * Login 실패
     */
    @PostMapping("/login/fail")
    public ResponseEntity<?> loginFail() {
        return ErrorReturn(APICode.LOGIN_ERROR);
    }

    /***
     * Access_Token 재발급
     */
    @GetMapping("/api/token/reissue")
    public ResponseEntity<?> tokenReissue(@CookieValue(name = RefreshTokenProperties.HEADER_STRING, required = false) String refreshToken, HttpServletResponse response) {
        log.info("Repuest refreshToken : {}", refreshToken);
        int userId = jwtService.getUserIdByRefreshToken(refreshToken);
        jwtService.publish(response, userId);
        return SuccessReturn();
    }

    /***
     * TOKEN 인증 프로세스중 에러 발생
     */
    @RequestMapping("/token/error")
    public ResponseEntity<?> tokenError() {
        return TokenErrorReturn(APICode.TOKEN_ERROR.getMsg());
    }
}
