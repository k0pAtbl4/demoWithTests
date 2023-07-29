package com.example.demowithtests.util.annotations;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Log4j2
@Aspect
@Component
public class ServiceLogAspect {
    @Pointcut("execution(public * com.example.demowithtests.service.EmployeeServiceBean.*(..))")
    public void callAtMyServicesPublicMethods() {
    }

    @Before("callAtMyServicesPublicMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        var args = joinPoint.getArgs();
        if (args.length > 0) {
            log.debug("Service." + methodName + " started. Args count - {}", args.length);
        } else {
            log.debug("Service." + methodName + " - started");
        }
    }

    @AfterReturning(value = "callAtMyServicesPublicMethods()", returning = "returningValue")
    public void logAfter(JoinPoint joinPoint, Object returningValue) {
        String methodName = joinPoint.getSignature().toShortString();
        Optional<String> outputValue = getOutputValue(returningValue);
        logMessage(methodName, outputValue);
    }

    private Optional<String> getOutputValue(Object returningValue) {
        return Optional.ofNullable(returningValue)
                .map(value -> value instanceof Collection
                        ? "Collection size: " + ((Collection<?>) value).size()
                        : value instanceof byte[]
                        ? "As byte array"
                        : value.toString());
    }

    private void logMessage(String methodName, Optional<String> outputValue) {
        if (outputValue.isPresent()) {
            log.debug("Service." + methodName + " ended. Returns - {}", outputValue);
        } else {
            log.debug("Service." + methodName + " ended");
        }
    }
}
