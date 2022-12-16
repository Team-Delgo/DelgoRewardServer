package com.delgo.reward.comm.security.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.delgo.reward.comm.security.jwt.config.AccessTokenProperties;
import com.delgo.reward.comm.security.services.PrincipalDetails;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 인가
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    // Token Check
    @Override
    @Transactional
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(AccessTokenProperties.HEADER_STRING);
        // Token 있는지 여부 체크
        if (header == null || !header.startsWith(AccessTokenProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(AccessTokenProperties.HEADER_STRING).replace(AccessTokenProperties.TOKEN_PREFIX, "");

        // 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
        // SecurityContext에 접근해서 세션을 만들때 자동으로 UserDetailsService에 있는 loadByUsername이 호출됨.
        try {
            Integer userId = Integer.parseInt(String.valueOf(JWT.require(Algorithm.HMAC512(AccessTokenProperties.SECRET)).build().verify(token).getClaim("userId")));

            log.info("JwtAuthorizationFilter userId : " + userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NullPointerException("NOT FOUND USER id: " + userId));

            log.info("JwtAuthorizationFilter findById : " + user.toString());

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
            log.info("Access Token Expired : " + e.getLocalizedMessage());
            chain.doFilter(request, response); // 403 Authorization Denied 발생
            return;
        }
        chain.doFilter(request, response);
    }
}
