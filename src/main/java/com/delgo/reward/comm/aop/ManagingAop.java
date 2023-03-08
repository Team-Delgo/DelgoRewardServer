package com.delgo.reward.comm.aop;

import com.delgo.reward.mongoDomain.Managing;
import com.delgo.reward.mongoService.ManagingService;
import com.google.api.client.util.ArrayMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ManagingAop {
    private final ManagingService managingService;

    //    @Pointcut("execution(* com.delgo.reward.controller..*.*(..))")
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    private void cut() {
    }

    @Before("cut()")
    public void before(JoinPoint joinPoint) {
        String controllerName = joinPoint.getSignature().getDeclaringType().getName();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getMethod().getName();

        ArrayMap<String, Object> args = new ArrayMap<>();
        Object[] argsList = joinPoint.getArgs();

        for (Object obj : argsList) {
            args.put("type: " + obj.getClass().getSimpleName(), "value: " + obj);
        }
        Managing managing = managingService.createLog(controllerName, methodName, args);
        log.info("\n[ManagingAop]" +
                "\ncontroller: " + managing.getControllerName() +
                "\nmethod: " + managing.getMethodName());
        ArrayMap<String, Object> argsMap = managing.getArgs();

        for (int i = 0; i < argsMap.size(); i++) {
            log.info("\nargs" +
                    "\n\t" + argsMap.getKey(i) +
                    "\n\t" + argsMap.getValue(i));
        }
    }

    @AfterReturning(value = "cut()", returning = "obj")
    public void afterReturn(JoinPoint joinPoint, Object obj) {
        System.out.println("return");
        String[] strings = obj.toString().split(",");
        for(String str: strings){
            System.out.println(str);
        }
    }
}
