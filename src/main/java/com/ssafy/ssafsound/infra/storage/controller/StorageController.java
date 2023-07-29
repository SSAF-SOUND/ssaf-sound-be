package com.ssafy.ssafsound.infra.storage.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import com.ssafy.ssafsound.infra.storage.dto.PostStoreImageResDto;
import com.ssafy.ssafsound.infra.storage.service.AwsS3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StorageController {

    private final AwsS3StorageService awsS3StorageService;

    @PostMapping("/image")
    public EnvelopeResponse<PostStoreImageResDto> getPreSignedUrl(
        @Authentication AuthenticatedMember member) {

        return EnvelopeResponse.<PostStoreImageResDto>builder()
            .data(
                new PostStoreImageResDto(awsS3StorageService.getPreSignedUrl(member.getMemberId())))
            .build();
    }

    @DeleteMapping("/image")
    public EnvelopeResponse<Void> deleteImage(@Authentication AuthenticatedMember member, String imagePath) {

        awsS3StorageService.deleteObject(member.getMemberId(), imagePath);

        return EnvelopeResponse.<Void>builder().build();
    }

}
