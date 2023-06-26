package com.ssafy.ssafsound.global.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileSizeValidator.class)
public @interface CheckFileSize {
    String message() default "파일이 제한된 용량을 초과했습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    long maxFileSize(); // 최대 파일 용량
}
