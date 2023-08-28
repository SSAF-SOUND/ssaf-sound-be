package com.ssafy.ssafsound.global.report.controller;

import com.ssafy.ssafsound.global.docs.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenMandatory;
import static com.ssafy.ssafsound.global.util.fixture.ReportFixture.POST_REPORT_REQ_DTO;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;

@WebMvcTest(ReportController.class)
public class ReportControllerTest extends ControllerTest {

    @Test
    @DisplayName("신고하기")
    public void postReport() {


        restDocs.cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(POST_REPORT_REQ_DTO)
                .when().post("/report")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("report/report",
                        requestCookieAccessTokenMandatory(),
                        requestFields(
                                fieldWithPath("sourceType").type(JsonFieldType.STRING).description("신고 대상 컨텐츠 종류. POST | COMMENT | RECRUIT | RECRUIT_COMMENT"),
                                fieldWithPath("sourceId").type(JsonFieldType.NUMBER).description("신고 대상 컨텐츠 PK"),
                                fieldWithPath("reasonId").type(JsonFieldType.NUMBER).description("신고 사유. +" +
                                        "\n" + "1 : 게시판 성격에 부적절함 +" +
                                        "\n" + "2 : 욕설/비하 +" +
                                        "\n" + "3 : 음란물/불건전한 만남 및 대화 +" +
                                        "\n" + "4 : 상업적 광고 및 판매 +" +
                                        "\n" + "5 : 유출/사칭/사기 +" +
                                        "\n" + "6 : 낚시/놀람/도배 +" +
                                        "\n" + "7 : 정당/정치인 비하 및운동 +")),
                        getEnvelopPatternWithNoContent()
                ));
    }

}
