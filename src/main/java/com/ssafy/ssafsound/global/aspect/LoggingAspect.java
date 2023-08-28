package com.ssafy.ssafsound.global.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* com.ssafy.ssafsound.domain..*Controller.*(..))")
    public void ControllerLog() {
    }

    @Pointcut("execution(* com.ssafy.ssafsound.domain..*Service.*(..))")
    public void ServiceLog() {
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        return signature.getMethod();
    }

    private void printResponse(EnvelopeResponse<?> response) {
        try {
            log.info("\n=======\nresponse : {} \n========", objectMapper.writeValueAsString(response.getData()));
        } catch (JsonProcessingException exception) {
            log.warn("logging failed!!");
        }
    }

    @Before("ControllerLog()")
    public void ExecutionLog(JoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        log.info("======= controller method name = {} =======", method.getName());

        Object[] args = joinPoint.getArgs();
        if (!isNull(args)) {
            for (Object arg : args) {
                if(!isNull(arg)) {
                    log.info("parameter type = {}", arg.getClass().getSimpleName());
                    log.info("parameter value = {}", arg);
                }
            }
        }
    }

    @AfterReturning(value = "ControllerLog()", returning = "response")
    public void afterReturnLog(JoinPoint joinPoint, EnvelopeResponse<?> response) {
        Method method = getMethod(joinPoint);

        if (!isNull(response)) {
            printResponse(response);
        }
    }

    @Before("ServiceLog()")
    public void methodLogger(JoinPoint joinPoint) {
        String serviceName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        Map<String, Object> params = new LinkedHashMap<>();

        try {
            params.put("service",  serviceName);
            params.put("method", methodName);
            params.put("params", Arrays.toString(joinPoint.getArgs()));
            params.put("log_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(Calendar.getInstance().getTime()));
        } catch (Exception e) {
            log.error("LoggingAspect - Service error", e);
        }
        log.info("\nServiceLogAspect" + " : {}\n", params);
    }
}
