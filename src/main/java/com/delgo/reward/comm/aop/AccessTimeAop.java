package com.delgo.reward.comm.aop;

import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.service.UserCommandService;
import com.delgo.reward.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AccessTimeAop {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    private void onGetRequest() {
    }

    @Before("onGetRequest()")
    public void accessTime(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Annotation[][] annotations = method.getParameterAnnotations();

        for (int i = 0; i < annotations.length; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation instanceof RequestParam requestParam) {
                    if ("userId".equals(requestParam.name())) {
                        int userId = (int) args[i];
                        if (userId == 0) break;

                        User user = userQueryService.getOneByUserId(userId);
                        user.setLastAccessDt(LocalDateTime.now());
                        userCommandService.save(user);
                    }
                }
            }
        }
    }
}
