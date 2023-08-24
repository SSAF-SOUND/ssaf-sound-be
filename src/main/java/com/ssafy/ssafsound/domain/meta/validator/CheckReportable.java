package com.ssafy.ssafsound.domain.meta.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ReportableValidator.class})
public @interface CheckReportable {
    String message() default "신고가 불가능한 컨텐츠입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}