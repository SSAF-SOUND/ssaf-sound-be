package com.ssafy.ssafsound.domain.member.validator;

import com.ssafy.ssafsound.domain.member.service.SemesterConstantProvider;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SemesterValidator implements ConstraintValidator<Semester, Integer> {

    private final SemesterConstantProvider semesterConstantProvider;

    public SemesterValidator(SemesterConstantProvider semesterConstantProvider) {
        this.semesterConstantProvider = semesterConstantProvider;
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(value == null) return  true;
        return value >= semesterConstantProvider.getMIN_SEMESTER() && value <= semesterConstantProvider.getMAX_SEMESTER();
    }
}
