package com.ssafy.ssafsound.global.validator;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ImageValidator implements ConstraintValidator<CheckImage, List<MultipartFile>> {
    @Value("${spring.constant.global.validator.IMAGE_EXTENSIONS}")
    private List<String> IMAGE_EXTENSIONS;

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (files.get(0).isEmpty()) {
            return true;
        }


        for (MultipartFile file : files) {
            // 확장자 검증
            String fileName = file.getOriginalFilename();
            String extension = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf(".") + 1);
            if (!IMAGE_EXTENSIONS.contains(extension.toLowerCase()))
                return false;

            try {
                // 파일 타입 검증
                Tika tika = new Tika();
                String mimeType = tika.detect(file.getBytes());
                if (!mimeType.startsWith("image/"))
                    return false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }
}
