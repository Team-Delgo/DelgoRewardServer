package com.delgo.reward.comm.aop;

import com.delgo.reward.common.response.CommResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class LogAop {
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
        CommResponse record = (CommResponse) responseEntity.getBody();
        log.info("\n[LogAop]" +
                "\n\t" + getRequestUrl(joinPoint) +
                "\n\tparameter: " + params(joinPoint) +
                "\n\tresponse code: " + record.code() +
                "\n\tresponse codeMsg: " + record.codeMsg() +
                "\n------------------------------------------------------------");
    }

    @AfterThrowing(pointcut = "onGetRequest()", throwing = "exception")
    public void afterThrowingAdviceByGet(JoinPoint joinPoint, Throwable exception) {
        log.error("\n[LogAop]" +
                "\n\t" + getRequestUrl(joinPoint) +
                "\n\tparameter: " + params(joinPoint) +
                "\n\texception location: " + exception.getStackTrace()[0] +
                "\n\texception msg: " + exception.getMessage() +
                "\n------------------------------------------------------------");
    }

    @AfterReturning(pointcut = "onRequest()", returning = "responseEntity")
    public void afterReturningAdvice(JoinPoint joinPoint, ResponseEntity<?> responseEntity) throws JsonProcessingException {
        CommResponse record = (CommResponse) responseEntity.getBody();
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
        String classUrl = (requestMapping != null) ? requestMapping.value()[0] : "";

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
                if (!list.isEmpty() && list.get(0) instanceof MultipartFile) { // 사진 List 등록 예외 처리
                    List<String> fileNameList = ((List<MultipartFile>) list).stream()
                            .map(MultipartFile::getOriginalFilename)
                            .toList();
                    params.put(parameterNames[i], fileNameList);
                }
            } else if (arg instanceof MultipartFile) { // 사진 한장 등록 예외 처리
                params.put(parameterNames[i], ((MultipartFile) arg).getOriginalFilename());
            } else if (!(arg instanceof HttpServletRequest || arg instanceof HttpServletResponse)) { // Spring Security Dispatch 예외처리
                params.put(parameterNames[i], arg);
            }
        }
        return params;
    }

    public String prettyPrinter(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        String requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        return Arrays.stream(requestBody.split("\n"))
                .map(line -> "\t\t\t" + line) // 각 줄에 탭 두 번 적용
                .collect(Collectors.joining("\n"));
    }
}
