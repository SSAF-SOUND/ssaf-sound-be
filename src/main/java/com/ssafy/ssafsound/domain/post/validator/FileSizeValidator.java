package com.ssafy.ssafsound.domain.post.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class FileSizeValidator implements ConstraintValidator<CheckFileSize, List<MultipartFile>> {

    private Long maxFileSize;

    @Override
    public void initialize(CheckFileSize constraintAnnotation) {
        this.maxFileSize = constraintAnnotation.maxFileSize();
    }

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (files.get(0).isEmpty()) {
            return true;
        }

        for (MultipartFile file : files) {
            if (file.getSize() > maxFileSize)
                return false;
        }

        return true;
    }
}
