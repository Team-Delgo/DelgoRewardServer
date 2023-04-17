package com.delgo.reward.comm.aop;

import com.delgo.reward.domain.user.CategoryCount;
import com.delgo.reward.mongoDomain.Classification;
import com.delgo.reward.mongoService.ClassificationService;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.UserService;
import com.google.api.client.util.ArrayMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ClassificationAop {
    private final UserService userService;
    private final CertService certService;
    private final ClassificationService classificationService;

    @Pointcut("execution(* com.delgo.reward.controller.CertController.register(..))")
    public void certRegister(){}

    @AfterReturning(value = "certRegister()", returning = "obj")
    public void classificationAfterCertRegister(JoinPoint joinPoint, Object obj) {

        ArrayMap<String, Object> args = new ArrayMap<>();
        Object[] argsList = joinPoint.getArgs();

        for (Object arg : argsList) {
            args.put("type: " + arg.getClass().getSimpleName(), "value: " + arg);
        }

        int startIndex = obj.toString().indexOf("(");
        int endIndex = obj.toString().indexOf(")");
        String responseDTO = obj.toString().substring(startIndex + 1, endIndex);

        if(responseDTO.contains("certificationId")){
            int startIndexForCertId = responseDTO.indexOf("certificationId=");
            int endIndexForCertId = responseDTO.indexOf(",");
            int certId = Integer.parseInt(responseDTO.substring(startIndexForCertId+16, endIndexForCertId));

            Classification classification = classificationService.runClassification(certService.getCert(certId));

            int startIndexForUserId = responseDTO.indexOf("userId=");
            int endIndexForUserId = responseDTO.indexOf(",", startIndexForUserId);
            int userId = Integer.parseInt(responseDTO.substring(startIndexForUserId+7, endIndexForUserId));

            CategoryCount categoryCount = userService.getCategoryCountByUserId(userId);

            for(String categoryCode: classification.getCategory().keySet()){
                userService.categoryCountSave(categoryCount.addOne(categoryCode));
            }
        }
    }
}