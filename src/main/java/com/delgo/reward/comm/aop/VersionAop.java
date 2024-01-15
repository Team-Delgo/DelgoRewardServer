package com.delgo.reward.comm.aop;

import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.service.UserCommandService;
import com.delgo.reward.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class VersionAop {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    private void onGetRequest() {
    }

    @Before("onGetRequest()")
    public void logRequestParam(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String version = request.getHeader("version");
        if(StringUtils.isEmpty(version)) return;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Annotation[][] annotations = method.getParameterAnnotations();

        for (int i = 0; i < annotations.length; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation instanceof RequestParam requestParam) {
                    if ("userId".equals(requestParam.name())) {
                        int userId = (int) args[i];
                        if(userId == 0) break;

                        User user = userQueryService.getOneByUserId(userId);
                        user.setVersion(version);
                        userCommandService.save(user);
                    }
                }
            }
        }
    }
}
