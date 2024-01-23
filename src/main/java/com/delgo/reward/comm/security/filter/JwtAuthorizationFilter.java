package com.delgo.reward.comm.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.delgo.reward.comm.security.config.AccessTokenProperties;
import com.delgo.reward.comm.security.domain.PrincipalDetails;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.service.UserQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 인가
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserQueryService userQueryService;

    @Value("${jwt.secret.access}")
    String ACCESS_SECRET;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserQueryService userQueryService) {
        super(authenticationManager);
        this.userQueryService = userQueryService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Token 있는지 여부 체크
        String header = request.getHeader(AccessTokenProperties.HEADER_STRING);
        if (header == null || !header.startsWith(AccessTokenProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(AccessTokenProperties.HEADER_STRING).replace(AccessTokenProperties.TOKEN_PREFIX, "");
        // 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
        // SecurityContext에 접근해서 세션을 만들때 자동으로 UserDetailsService에 있는 loadByUsername이 호출됨.
        try {
            Integer userId = JWT.require(Algorithm.HMAC512(ACCESS_SECRET))
                    .build()
                    .verify(token)
                    .getClaim("userId")
                    .asInt();

            User user = userQueryService.getOneByUserId(userId);

            // 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
            // 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                    null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
                    principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 값 저장 ( 권한 없으면 필요 없음 )
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) { // Token 시간 만료 및 토큰 인증 에러
            log.error("JwtAuthorizationFilter 에러 발생");
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher("/token/error");
            dispatcher.forward(request, response); // 303 토큰 에러 발생
            return;
        }
        chain.doFilter(request, response);
    }
}
