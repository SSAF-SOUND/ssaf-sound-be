package com.ssafy.ssafsound.infra.storage.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ssafy.ssafsound.infra.exception.InfraErrorInfo;
import com.ssafy.ssafsound.infra.exception.InfraException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3StorageService implements StorageService{

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String makeFileName(String originalFileName, String uploadDir, String memberId) {

        StringBuffer fileName = new StringBuffer();

        return fileName.append(uploadDir)
                .append(File.separator)
                .append(memberId)
                .append(File.separator)
                .append(UUID.randomUUID()) // 파일명 고유화
                .append("_")
                .append(Normalizer.normalize(StringUtils.cleanPath(originalFileName), Normalizer.Form.NFC))
                .toString();
    }

    @Override
    public String putObject(MultipartFile multipartFile, String uploadDir, Long memberId) {

        if (multipartFile == null || multipartFile.isEmpty()){
            throw new InfraException(InfraErrorInfo.STORAGE_STORE_INVALID_OBJECT);
        }

        String fileName = makeFileName(multipartFile.getOriginalFilename(), uploadDir, String.valueOf(memberId));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);

        } catch (AmazonServiceException e){
            log.error(e.getMessage());
            throw new InfraException(InfraErrorInfo.STORAGE_SERVICE_ERROR);

        } catch (IOException e){
            log.error(e.getMessage());
            throw new RuntimeException();
        }

        log.info("S3 저장 성공 : {} 객체 저장", fileName);

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    @Override
    public void deleteObject(String originalFilename)  {
        try{
            amazonS3.deleteObject(bucket, originalFilename);
        } catch (AmazonServiceException e){
            log.error(e.getMessage());
            throw new InfraException(InfraErrorInfo.STORAGE_SERVICE_ERROR);
        }

        log.info("S3 객체 삭제 성공");
    }
}
