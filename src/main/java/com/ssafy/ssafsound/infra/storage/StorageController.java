package com.ssafy.ssafsound.infra.storage;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import com.ssafy.ssafsound.infra.storage.dto.PostStoreImageResDto;
import com.ssafy.ssafsound.infra.storage.service.AwsS3PreSignerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StorageController {

    private final AwsS3PreSignerService awsS3PresignerService;

    @PostMapping("/image")
    public EnvelopeResponse<PostStoreImageResDto> getPresignedUrl(@Authentication AuthenticatedMember member, @RequestParam Integer count) {

        return EnvelopeResponse.<PostStoreImageResDto>builder()
                .data(new PostStoreImageResDto(awsS3PresignerService.getPreSignedUrlAsCount(count, member.getMemberId())))
                .build();
    }

}
