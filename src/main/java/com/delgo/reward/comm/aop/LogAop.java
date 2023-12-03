package com.delgo.reward.comm.aop;

import com.delgo.reward.mongoDomain.Log;
import com.delgo.reward.mongoService.LogService;
import com.google.api.client.util.ArrayMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAop {
    private final LogService logService;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)")
    private void cut() {
    }

//    @Before("cut()")
//    public void before(JoinPoint joinPoint) {
//        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//
//        String httpMethod = request.getMethod();
//        String controllerName = joinPoint.getSignature().getDeclaringType().getName();
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        String methodName = methodSignature.getMethod().getName();
//
//        ArrayMap<String, Object> args = new ArrayMap<>();
//        Object[] argsList = joinPoint.getArgs();
//
//        for (Object obj : argsList) {
//            args.put("type: " + obj.getClass().getSimpleName(), "value: " + obj);
//        }
//
//        RequestLog requestLog = logService.createRequestLog(httpMethod, controllerName, methodName, args);
//
//        log.info("\n[LogAop]" +
//                "\n\tcontroller: " + requestLog.getControllerName() +
//                "\n\tmethod: " + requestLog.getMethodName());
//        ArrayMap<String, Object> argsMap = requestLog.getArgs();
//
//        for (int i = 0; i < argsMap.size(); i++) {
//            log.info("\nargs" +
//                    "\n\t" + argsMap.getKey(i) +
//                    "\n\t" + argsMap.getValue(i));
//        }
//
//    }

    @AfterReturning(value = "cut()", returning = "obj")
    public void afterReturn(JoinPoint joinPoint, Object obj) {
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String httpMethod = request.getMethod();
        String controllerName = joinPoint.getSignature().getDeclaringType().getName();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getMethod().getName();

        ArrayMap<String, Object> args = new ArrayMap<>();
        Object[] argsList = joinPoint.getArgs();

        if (argsList.length > 0) {
            for (Object arg : argsList) {
                if (arg != null) {
                    args.put("type: " + arg.getClass().getSimpleName(), "value: " + arg);
                }
            }
        }

//        int startIndex = obj.toString().indexOf("(");
//        int endIndex = obj.toString().indexOf(")");
//        String responseDTO = (startIndex == -1 || endIndex == -1) ? "" : obj.toString().substring(startIndex + 1, endIndex);

        int startIndex = obj.toString().indexOf("ResponseRecord[");
        int endIndex = obj.toString().indexOf("]");

        String responseRecord = (startIndex == -1 || endIndex == -1) ? "" : obj.toString().substring(startIndex + 15, endIndex);

        if(responseRecord.contains("code") && responseRecord.contains("codeMsg") && responseRecord.contains("data")){
            String response[] = responseRecord.split(", ");
            Map<String, String> responseMap = new ArrayMap<>();
            responseMap.put("code", response[0].substring(5));
            responseMap.put("codeMsg", response[1].substring(8));
            responseMap.put("data", response[2].substring(5));

            Log log = logService.createLog(httpMethod, controllerName, methodName, args, responseMap);

            LogAop.log.info("\n[LogAop]" +
                    "\n\thttp method: " + log.getHttpMethod() +
                    "\n\tcontroller name: " + log.getControllerName() +
                    "\n\tmethod name: " + log.getMethodName() +
                    "\n\tresponse code: " + log.getResponseMap().get("code") +
                    "\n\tresponse codeMsg: " + log.getResponseMap().get("codeMsg") +
                    "\n\tresponse data: " + log.getResponseMap().get("data"));
        } else {
            Log log = logService.createLog(httpMethod, controllerName, methodName, args, responseRecord);

            LogAop.log.info("\n[LogAop]" +
                    "\n\thttp method: " + log.getHttpMethod() +
                    "\n\tcontroller name: " + log.getControllerName() +
                    "\n\tmethod name: " + log.getMethodName() +
                    "\n\tresponse: " + log.getResponseStr());
        }
    }

}
