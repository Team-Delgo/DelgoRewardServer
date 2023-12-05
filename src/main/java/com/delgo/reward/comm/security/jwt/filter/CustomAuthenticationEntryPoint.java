package com.delgo.reward.comm.security.jwt.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.info("{} {} || Security Authentication ERROR Occurred", request.getMethod(), request.getRequestURI());
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    }
}