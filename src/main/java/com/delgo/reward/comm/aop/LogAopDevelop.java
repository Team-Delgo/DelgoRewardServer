package com.delgo.reward.comm.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Aspect
@Component
public class LogAopDevelop {
    @Pointcut("execution(* com.delgo.reward.controller..*.*(..))")
    private void onRequest() {
    }

    @Before("onRequest()")
    public void afterReturningAdvice(JoinPoint joinPoint) {
        log.info("{} || Parameter : {}", getRequestUrl(joinPoint), params(joinPoint));
    }

    @AfterReturning(pointcut = "onRequest()", returning = "responseEntity")
    public void afterReturningAdvice(JoinPoint joinPoint, ResponseEntity<?> responseEntity) {
        log.info("{} || Parameter : {} || ResponseCode : {} \n ResponseData : {}", getRequestUrl(joinPoint), params(joinPoint), responseEntity.getStatusCode(), responseEntity.getBody());
    }

    @AfterThrowing(pointcut = "onRequest()", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Throwable ex) {
        log.info("{} || Parameter : {} || Exception : {}", getRequestUrl(joinPoint), params(joinPoint), ex.getMessage());
    }

    private String getRequestUrl(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        RequestMapping requestMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        String classUrl = requestMapping.value()[0];

        String url = Stream.of(GetMapping.class, PutMapping.class, PostMapping.class, DeleteMapping.class)
                .filter(method::isAnnotationPresent)
                .map(mappingClass -> getUrl(method, mappingClass, classUrl))
                .findFirst().orElse(null);

        return url;
    }

    /* httpMETHOD + requestURI 를 반환 */
    private String getUrl(Method method, Class<? extends Annotation> annotationClass, String classUrl) {
        Annotation annotation = method.getAnnotation(annotationClass);
        String httpMethod = (annotationClass.getSimpleName().replace("Mapping", "")).toUpperCase();
        String[] methodUrl;
        try {
            methodUrl = (String[]) annotationClass.getMethod("value").invoke(annotation);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
        return String.format("%s %s%s", httpMethod, classUrl, methodUrl.length > 0 ? methodUrl[0] : "");
    }

    /* printing request parameter or request body */
    private Map params(JoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], args[i]);
        }
        return params;
    }
}
