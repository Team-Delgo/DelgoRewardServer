package com.delgo.reward.comm.aop;

import com.delgo.reward.record.common.ResponseRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Aspect
@Component
public class LogAopDevelop {
    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    private void onGetRequest() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    private void onRequest() {
    }

    @AfterReturning(pointcut = "onGetRequest()", returning = "responseEntity")
    public void afterReturningAdviceByGet(JoinPoint joinPoint, ResponseEntity<?> responseEntity) {
        ResponseRecord record = (ResponseRecord) responseEntity.getBody();
        log.info("\n[LogAop]" +
                "\n\t" + getRequestUrl(joinPoint) +
                "\n\tparameter: " + params(joinPoint) +
                "\n\tresponse code: " + record.code() +
                "\n\tresponse codeMsg: " + record.codeMsg() +
                "\n------------------------------------------------------------");
    }

    @AfterThrowing(pointcut = "onGetRequest()", throwing = "exception")
    public void afterThrowingAdviceByGet(JoinPoint joinPoint, Throwable exception) {
        log.info("{} || Exception : {} || Parameter : {}", getRequestUrl(joinPoint), exception.getMessage(), params(joinPoint));
        log.error("\n[LogAop]" +
                "\n\t" + getRequestUrl(joinPoint) +
                "\n\tparameter: " + params(joinPoint) +
                "\n\texception location: " + exception.getStackTrace()[0] +
                "\n\texception msg: " + exception.getMessage() +
                "\n------------------------------------------------------------");
    }

    @AfterReturning(pointcut = "onRequest()", returning = "responseEntity")
    public void afterReturningAdvice(JoinPoint joinPoint, ResponseEntity<?> responseEntity) throws JsonProcessingException {
        ResponseRecord record = (ResponseRecord) responseEntity.getBody();
        log.info("\n[LogAop]" +
                "\n\t" + getRequestUrl(joinPoint) +
                "\n\trequest body: \n " + prettyPrinter(params(joinPoint)) +
                "\n\tresponse code: " + record.code() +
                "\n\tresponse codeMsg: " + record.codeMsg() +
                "\n\tresponse data: \n " + prettyPrinter(record.data()) +
                "\n------------------------------------------------------------");
    }

    @AfterThrowing(pointcut = "onRequest()", throwing = "exception")
    public void afterThrowingAdvice(JoinPoint joinPoint, Throwable exception) throws JsonProcessingException {
        log.error("\n[LogAop]" +
                "\n\t" + getRequestUrl(joinPoint) +
                "\n\trequest body: \n " + prettyPrinter(params(joinPoint)) +
                "\n\texception location: " + exception.getStackTrace()[0] +
                "\n\texception msg: " + exception.getMessage() +
                "\n------------------------------------------------------------");
    }

    private String getRequestUrl(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        RequestMapping requestMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        String classUrl = requestMapping.value()[0];

        return Stream.of(GetMapping.class, PutMapping.class, PostMapping.class, DeleteMapping.class)
                .filter(method::isAnnotationPresent)
                .map(mappingClass -> getUrl(method, mappingClass, classUrl))
                .findFirst().orElse(null);
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
            Object arg = args[i];
            if (arg instanceof List<?>) {
                List<?> list = (List<?>) arg;
                if (!list.isEmpty() && list.get(0) instanceof MultipartFile) {
                    List<String> fileNameList = ((List<MultipartFile>) list).stream()
                            .map(MultipartFile::getOriginalFilename)
                            .toList();
                    params.put(parameterNames[i], fileNameList);
                }
            } else {
                params.put(parameterNames[i], args[i]);
            }
        }
        return params;
    }

    public String prettyPrinter(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        return Arrays.stream(requestBody.split("\n"))
                .map(line -> "\t\t\t" + line) // 각 줄에 탭 두 번 적용
                .collect(Collectors.joining("\n"));
    }
}
