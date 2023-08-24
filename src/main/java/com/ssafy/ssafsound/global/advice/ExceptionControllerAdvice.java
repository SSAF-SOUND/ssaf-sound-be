package com.ssafy.ssafsound.global.advice;


import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.board.exception.BoardException;
import com.ssafy.ssafsound.domain.comment.exception.CommentException;
import com.ssafy.ssafsound.domain.lunch.exception.LunchException;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.post.exception.PostException;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import com.ssafy.ssafsound.global.report.exception.ReportException;
import com.ssafy.ssafsound.infra.exception.InfraException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MemberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse MemberExceptionHandler(MemberException e) {
        log.error(e.getInfo().getMessage());

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse AuthExceptionHandler(AuthException e) {
        log.error(e.getInfo().getMessage());

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(LunchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse LunchExceptionHandler(LunchException e) {
        log.error(e.getInfo().getMessage());

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message((e.getInfo().getMessage()))
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse BadRequestExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage());

        StringBuilder errorMessage = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            errorMessage.append(error.getDefaultMessage());
        });

        return EnvelopeResponse.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .message(errorMessage.toString())
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public EnvelopeResponse RuntimeExceptionHandler(RuntimeException e,
                                                    HandlerMethod method,
                                                    HttpServletRequest request) {
        exactErrorLog(e, method, request);

        return EnvelopeResponse.builder()
                .code(GlobalErrorInfo.INTERNAL_SERVER_ERROR.getCode())
                .message(GlobalErrorInfo.INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public EnvelopeResponse ResourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        log.error(e.getInfo().getMessage());

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public EnvelopeResponse DataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
        log.error(e.getMessage());

        return EnvelopeResponse.builder()
                .code(GlobalErrorInfo.NOT_FOUND.getCode())
                .message(GlobalErrorInfo.NOT_FOUND.getMessage())
                .build();
    }

    @ExceptionHandler(InfraException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse InfraExceptionHandler(InfraException e) {
        log.error(e.getInfo().getMessage());

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(RecruitException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse RecruitExceptionHandler(RecruitException e) {
        log.error(e.getInfo().getMessage());

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(BoardException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse BoardExceptionHandler(BoardException e) {
        log.error(e.getInfo().getMessage());

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(PostException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse PostExceptionHandler(PostException e) {
        log.error(e.getInfo().getMessage());

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse BindExceptionHandler(BindException e) {
        log.error(e.getMessage());

        return EnvelopeResponse.builder()
                .code(GlobalErrorInfo.BAD_REQUEST.getCode())
                .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .build();
    }

    @ExceptionHandler(CommentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse CommentExceptionHandler(CommentException e) {
        log.error(e.getInfo().getMessage());

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(ReportException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse ReportExceptionHandler(ReportException e) {
        log.error(e.getMessage());

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse HttpMessageNotReadableException(HttpMessageNotReadableException e) {

        log.error(e.getMessage());

        return EnvelopeResponse.builder()
                .code(GlobalErrorInfo.BAD_REQUEST.getCode())
                .message(GlobalErrorInfo.BAD_REQUEST.getMessage())
                .build();
    }

    private void exactErrorLog(Exception e, HandlerMethod handlerMethod,
                               HttpServletRequest request) {
        String errorDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(Calendar.getInstance().getTime());
        String requestURI = request.getRequestURI();
        String exceptionName = e.getClass().getSimpleName();
        String controllerName = handlerMethod.getMethod().getDeclaringClass().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();
        String message = e.getMessage();
        String lineNumber = String.valueOf(e.getStackTrace()[0].getLineNumber());
        log.error("\n[Time: {} | Class: {} | Method: {} | LineNumber: {} " +
                "| Path: {} | Exception: {} | Message: {}]\n",
                errorDate, controllerName, methodName, lineNumber, requestURI, exceptionName, message);
    }
}