package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.infra.storage.dto.GetStorageUploadResDto;

public class StorageFixture {
    public static final String POST_IMAGE_PATH_FIXTURE1 = "post/3931df90-e81c-47a0-8364-909a7cea7ac9";
    public static final String POST_IMAGE_URL_FIXTURE1 = "https://d39eiex97d56il.cloudfront.net/post/3931df90-e81c-47a0-8364-909a7cea7ac9";
    public static final String PRE_SIGNED_URL_FIXTURE1 = "https://ssaf-sound-file-server-s3-bucket.s3.ap-northeast-2.amazonaws.com/post/3931df90-e81c-47a0-8364-909a7cea7ac9?x-amz-acl=public-read-write&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230815T055051Z&X-Amz-SignedHeaders=host&X-Amz-Expires=299&X-Amz-Credential=AKIAQ7OX65EXW5BZE2UI%2F20230815%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=686b867c619ca2bf4ac43336f942e8cdb760ee52483fcbc0ecec906668018fd8";


    public static final GetStorageUploadResDto GET_STORAGE_UPLOAD_RES_DTO = GetStorageUploadResDto.builder()
            .imagePath(POST_IMAGE_PATH_FIXTURE1)
            .imageUrl(POST_IMAGE_URL_FIXTURE1)
            .preSignedUrl(PRE_SIGNED_URL_FIXTURE1)
            .build();
}
