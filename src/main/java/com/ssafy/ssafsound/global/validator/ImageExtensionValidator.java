package com.ssafy.ssafsound.global.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ImageExtensionValidator implements ConstraintValidator<CheckImageExtension, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (files.get(0).isEmpty()) {
            return true;
        }

        List<String> imageExtensions = Arrays.asList("jpg", "jpeg", "png", "bmp", "webp");

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String extension = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf(".") + 1);
            if (!imageExtensions.contains(extension.toLowerCase()))
                return false;
        }

        return true;
    }
}
