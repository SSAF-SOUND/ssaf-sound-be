package com.ssafy.ssafsound.domain.post.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileCountValidator.class)
public @interface CheckFileCount {
    String message() default "파일 개수를 초과했습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int maxFileCount();
}
