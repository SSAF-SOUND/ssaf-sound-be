package com.ssafy.ssafsound.infra.storage.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.UploadDirectory;
import com.ssafy.ssafsound.infra.storage.dto.ImagePathDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3PreSignerService {

    private final AmazonS3 amazonS3;

    private final MemberRepository memberRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final String CloudFrontDomain = "https://d39eiex97d56il.cloudfront.net";

    public ImagePathDto getPreSignedUrl(String uploadDir, Long memberId) {

        String fileName = makeFileName(uploadDir, memberId);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucket, fileName);

        return ImagePathDto.builder()
                .preSignedUrl(amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString())
                .imageDir(makeCDNFileUrl(fileName))
                .build();
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());

        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicReadWrite.toString());

        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 20;
        expiration.setTime(expTimeMillis);

        return expiration;
    }

    private String makeFileName(String uploadDir, Long memberId) {

        StringBuffer fileName = new StringBuffer();

        return fileName.append(uploadDir)
                .append("/")
                .append(memberId)
                .append("/")
                .append(UUID.randomUUID()) // 파일명 고유화
                .toString();
    }

    private String makeCDNFileUrl(String filename) {

        StringBuffer CDNFileUrl = new StringBuffer();

        return CDNFileUrl.append(CloudFrontDomain)
                .append("/")
                .append(filename)
                .toString();
    }

    public List<ImagePathDto> getPreSignedUrlAsCount(Integer count, Long memberId) {

        List<ImagePathDto> imagePathDtos = new ArrayList<>();

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        for (int i = 0; i < count; i++) {
            imagePathDtos.add(getPreSignedUrl(UploadDirectory.POST.getName(), memberId));
        }

        return imagePathDtos;
    }
}
