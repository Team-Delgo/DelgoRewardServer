package com.delgo.reward.comm.interceptor;


import com.delgo.reward.comm.log.APILog;
import com.delgo.reward.comm.log.ERRLog;
import com.delgo.reward.record.common.ResponseRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    private final String POST = "POST";

    // Success Return은 postHandle에서 Log 처리
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
        ResponseRecord responseRecord = getResponseBody(response);
        if (responseRecord.code() == 200)
            if (request.getMethod().equals(POST)) {
                log.info("{} || Result : code = {} msg = {} \n Parameter : {} ", request.getRequestURI(), responseRecord.code(), responseRecord.codeMsg(), request.getAttribute("requestBody"));
            } else
                APILog.info(request, responseRecord.code(), responseRecord.codeMsg());
    }

    // Error Return은 afterCompletion Log 처리
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ResponseRecord responseRecord = getResponseBody(response);
        // Exception이 발생하면 postHandle을 타지 않는다.
        if (responseRecord.code() != 200)
            if (request.getMethod().equals(POST)) {
                log.info("{} || Result : code = {} msg = {} \n Parameter : {} ", request.getRequestURI(), responseRecord.code(), responseRecord.codeMsg(), request.getAttribute("requestBody"));
            } else
                ERRLog.info(request, responseRecord.code(), responseRecord.codeMsg());
    }


    private ResponseRecord getResponseBody(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = getResponseWrapper(response);
        ObjectMapper om = new ObjectMapper();
        return om.readValue(responseWrapper.getContentAsByteArray(), ResponseRecord.class);
    }

    private ContentCachingResponseWrapper getResponseWrapper(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        }
        return new ContentCachingResponseWrapper(response);
    }
}