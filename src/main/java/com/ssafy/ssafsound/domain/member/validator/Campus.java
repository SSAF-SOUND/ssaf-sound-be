package com.ssafy.ssafsound.domain.member.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = CampusValidator.class)
public @interface Campus {
    String message() default "존재하지 않는 캠퍼스 이름입니다.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
