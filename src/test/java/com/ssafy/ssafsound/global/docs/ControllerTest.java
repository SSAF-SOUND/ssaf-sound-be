package com.ssafy.ssafsound.global.docs;

import com.ssafy.ssafsound.domain.auth.controller.AuthController;
import com.ssafy.ssafsound.domain.auth.service.AuthService;
import com.ssafy.ssafsound.domain.auth.service.CookieProvider;
import com.ssafy.ssafsound.domain.auth.service.token.JwtTokenProvider;
import com.ssafy.ssafsound.domain.auth.validator.AuthenticationArgumentResolver;
import com.ssafy.ssafsound.domain.board.controller.BoardController;
import com.ssafy.ssafsound.domain.board.service.BoardService;
import com.ssafy.ssafsound.domain.comment.controller.CommentController;
import com.ssafy.ssafsound.domain.comment.service.CommentService;
import com.ssafy.ssafsound.domain.lunch.controller.LunchController;
import com.ssafy.ssafsound.domain.lunch.service.LunchPollService;
import com.ssafy.ssafsound.domain.lunch.service.LunchService;
import com.ssafy.ssafsound.domain.member.controller.MemberController;
import com.ssafy.ssafsound.domain.member.service.MemberService;
import com.ssafy.ssafsound.domain.member.service.SemesterConstantProvider;
import com.ssafy.ssafsound.domain.member.validator.SemesterValidator;
import com.ssafy.ssafsound.domain.meta.controller.MetaDataController;
import com.ssafy.ssafsound.domain.meta.service.EnumMetaDataConsumer;
import com.ssafy.ssafsound.domain.post.controller.PostController;
import com.ssafy.ssafsound.domain.post.service.PostService;
import com.ssafy.ssafsound.domain.recruit.controller.RecruitController;
import com.ssafy.ssafsound.domain.recruit.service.RecruitService;
import com.ssafy.ssafsound.domain.recruitapplication.controller.RecruitApplicationController;
import com.ssafy.ssafsound.domain.recruitapplication.service.RecruitApplicationService;
import com.ssafy.ssafsound.domain.recruitcomment.controller.RecruitCommentController;
import com.ssafy.ssafsound.domain.recruitcomment.service.RecruitCommentService;
import com.ssafy.ssafsound.domain.term.controller.TermController;
import com.ssafy.ssafsound.domain.term.service.TermService;
import com.ssafy.ssafsound.global.interceptor.AuthInterceptor;
import com.ssafy.ssafsound.global.report.controller.ReportController;
import com.ssafy.ssafsound.global.report.service.ReportService;
import com.ssafy.ssafsound.infra.storage.controller.StorageController;
import com.ssafy.ssafsound.infra.storage.service.AwsS3StorageService;
import io.restassured.http.Cookie;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@WebMvcTest({
        AuthController.class,
        BoardController.class,
        CommentController.class,
        LunchController.class,
        MemberController.class,
        MetaDataController.class,
        PostController.class,
        RecruitController.class,
        RecruitApplicationController.class,
        RecruitCommentController.class,
        StorageController.class,
        //ChatController.class
        ReportController.class,
        TermController.class
})
@ExtendWith(RestDocumentationExtension.class)
public class ControllerTest {

    protected MockMvcRequestSpecification restDocs;

    @MockBean
    protected MemberService memberService;

    @SpyBean
    protected CookieProvider cookieProvider;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected BoardService boardService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected LunchService lunchService;

    @MockBean
    protected LunchPollService lunchPollService;

    @MockBean
    protected EnumMetaDataConsumer enumMetaDataConsumer;

    @MockBean
    protected PostService postService;

    @MockBean
    protected RecruitService recruitService;

    @MockBean
    protected RecruitApplicationService recruitApplicationService;

    @MockBean
    protected RecruitCommentService recruitCommentService;

    @MockBean
    protected AwsS3StorageService awsS3StorageService;

    @MockBean
    protected ReportService reportService;

    @MockBean
    protected TermService termService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected AuthInterceptor authInterceptor;

    @MockBean
    protected AuthenticationArgumentResolver authenticationArgumentResolver;

    @MockBean
    protected SemesterValidator semesterValidator;

    @MockBean
    protected SemesterConstantProvider semesterConstantProvider;

    protected static final Cookie ACCESS_TOKEN = new Cookie.Builder("accessToken", "accessTokenValue").build();
    protected static final Cookie REFRESH_TOKEN = new Cookie.Builder("refreshToken", "refreshTokenValue").build();

    private final ResponseFieldsSnippet envelopPatternWithData = responseFields(
            fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
            fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"));


    private final ResponseFieldsSnippet envelopPatternWithNoContent = responseFields(
            fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
            fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터").optional());

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        restDocs = RestAssuredMockMvc.given()
                .mockMvc(MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(documentationConfiguration(restDocumentation)
                                .uris()
                                .withScheme("https")
                                .withHost("api.ssafsound.com")
                                .withPort(443)
                                .and()
                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint())
                                .withResponseDefaults(prettyPrint()))
                        .build())
                .log().all();

        doReturn(true)
                .when(authInterceptor)
                .preHandle(any(), any(), any());
    }

    protected ResponseFieldsSnippet getEnvelopPatternWithData() {
        return envelopPatternWithData;
    }

    protected ResponseFieldsSnippet getEnvelopPatternWithNoContent() {
        return envelopPatternWithNoContent;
    }
}
