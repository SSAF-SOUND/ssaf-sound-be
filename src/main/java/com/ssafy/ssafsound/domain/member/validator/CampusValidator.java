package com.ssafy.ssafsound.domain.member.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CampusValidator implements ConstraintValidator<Campus, String>  {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return true;
        Pattern pattern = Pattern.compile("^(서울|대전|광주|구미|부울경)$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
