package com.ssafy.ssafsound.global.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @Around("ControllerLog()")
    public Object ExecutionLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();

        String controllerName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        Map<String, Object> params = new LinkedHashMap<>();
        try {
            params.put("controller", controllerName);
            params.put("method",  methodName);
            params.put("params",  getParams(request));
            params.put("log_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss")));
            params.put("request_uri", request.getRequestURI());
            params.put("http_method", request.getMethod());
        } catch (Exception e) {
            log.error("LoggerAspect - Controller error", e);
        }
        log.info("\n" + "ControllerLogAspect : {}" + "\n", params);
        return result;
    }

    @AfterThrowing(pointcut = "execution(* com.ssafy.ssafsound.*.*(..))", throwing = "ex")
    public void logException(Exception ex) {
        log.info("\n" + "예외 메시지:" + ex.getMessage() + "\n");
    }

    @Around("ServiceLog()")
    public Object methodLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();

        String serviceName = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();

        Map<String, Object> params = new LinkedHashMap<>();

        try {
            params.put("service", serviceName);
            params.put("method", methodName);
            params.put("params", Arrays.toString(proceedingJoinPoint.getArgs()));
            params.put("log_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss")));
        } catch (Exception e) {
            log.error("LoggingAspect - Service error", e);
        }
        log.info("\n" + "ServiceLogAspect : {}" + "\n", params);
        return result;
    }

    private String getParams(HttpServletRequest request) throws JsonProcessingException {
        Enumeration<String> params = request.getParameterNames();
        Map<String, String> object = new HashMap<>();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replace("\\.", "-");
            object.put(replaceParam, request.getParameter(param));
        }

        return objectMapper.writeValueAsString(object);
    }

}
