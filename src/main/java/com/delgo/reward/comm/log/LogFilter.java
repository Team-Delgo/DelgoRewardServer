package com.delgo.reward.comm.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class LogFilter extends OncePerRequestFilter {
    private final String POST = "POST";
    private final String TYPE_JSON = "application/json";

    /*
     * Response의 Body는 기본적으로 체크할 수 없음
     * LogInterceptor에서 Response Body를 체크하기 위해 ContentCachingResponseWrapper로 Response 값을 복사해준다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper(response);

        String requestUrl = request.getRequestURI();
        // form-data로 받을 때는 Wrapper 사용하면 ERROR 발생
        if (request.getMethod().equals(POST) && !requestUrl.contains("/photo") && !requestUrl.contains("/login")
//                && !requestUrl.equals("/api/certification") && !requestUrl.equals("/api/certification")
                && !requestUrl.equals("/api/mungple")
        ) {
            RequestBodyWrapper wrappingRequest = new RequestBodyWrapper((HttpServletRequest) request);
            wrappingRequest.setAttribute("requestBody", wrappingRequest.getRequestBody());
            chain.doFilter(wrappingRequest, wrappingResponse);
        } else {
            ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
            chain.doFilter(wrappingRequest, wrappingResponse);
        }
        // 해당 내용 없으면 Client에서 Response 값 못 받음. *중요*
        wrappingResponse.copyBodyToResponse();
    }
}