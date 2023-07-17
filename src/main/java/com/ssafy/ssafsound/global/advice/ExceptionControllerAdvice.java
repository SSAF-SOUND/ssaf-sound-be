package com.ssafy.ssafsound.global.advice;


import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.board.exception.BoardException;
import com.ssafy.ssafsound.domain.comment.exception.CommentException;
import com.ssafy.ssafsound.domain.lunch.exception.LunchException;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.post.exception.PostException;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import com.ssafy.ssafsound.infra.exception.InfraException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MemberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse MemberExceptionHandler(MemberException e) {
        e.printStackTrace();
        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse AuthExceptionHandler(AuthException e) {
        e.printStackTrace();

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(LunchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse LunchExceptionHandler(LunchException e) {
        e.printStackTrace();

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
    public EnvelopeResponse RuntimeExceptionHandler(RuntimeException e) {
        e.printStackTrace();

        return EnvelopeResponse.builder()
                .code(GlobalErrorInfo.INTERNAL_SERVER_ERROR.getCode())
                .message(GlobalErrorInfo.INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public EnvelopeResponse ResourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        e.printStackTrace();

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public EnvelopeResponse DataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
        e.printStackTrace();

        return EnvelopeResponse.builder()
                .code(GlobalErrorInfo.NOT_FOUND.getCode())
                .message(GlobalErrorInfo.NOT_FOUND.getMessage())
                .build();
    }

    @ExceptionHandler(InfraException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse InfraExceptionHandler(InfraException e){
        e.printStackTrace();

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(RecruitException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse RecruitExceptionHandler(RecruitException e) {
        e.printStackTrace();
        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(BoardException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse BoardExceptionHandler(BoardException e){
        e.printStackTrace();

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(PostException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse PostExceptionHandler(PostException e){
        e.printStackTrace();

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse BindExceptionHandler(BindException e) {
        e.printStackTrace();

        return EnvelopeResponse.builder()
                .code(GlobalErrorInfo.BAD_REQUEST.getCode())
                .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .build();
    }

    @ExceptionHandler(CommentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EnvelopeResponse CommentExceptionHandler(CommentException e) {
        e.printStackTrace();

        return EnvelopeResponse.builder()
                .code(e.getInfo().getCode())
                .message(e.getInfo().getMessage())
                .build();
    }
}
