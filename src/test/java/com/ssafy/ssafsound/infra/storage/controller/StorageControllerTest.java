package com.ssafy.ssafsound.infra.storage.controller;

import com.ssafy.ssafsound.global.docs.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenMandatory;
import static com.ssafy.ssafsound.global.util.fixture.StorageFixture.GET_STORAGE_UPLOAD_RES_DTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@WebMvcTest(StorageController.class)
public class StorageControllerTest extends ControllerTest {

    @Test
    @DisplayName("이미지 업로드 Presigned-URL 요청")
    public void getPresignedUrl() {

        doReturn(GET_STORAGE_UPLOAD_RES_DTO)
                .when(awsS3StorageService)
                .getPreSignedUrl(any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/store/image")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("storage/upload",
                        requestCookieAccessTokenMandatory(),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("imagePath").type(JsonFieldType.STRING).description("스토리지 내 이미지 경로"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지를 조회할 수 있는 url"),
                                fieldWithPath("preSignedUrl").type(JsonFieldType.STRING).description("이미지를 저장할 수 있는 url"))
                ));
    }
}
