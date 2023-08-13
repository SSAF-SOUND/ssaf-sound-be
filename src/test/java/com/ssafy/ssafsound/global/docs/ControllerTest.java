package com.ssafy.ssafsound.global.docs;

import com.ssafy.ssafsound.domain.auth.controller.AuthController;
import com.ssafy.ssafsound.domain.auth.service.token.JwtTokenProvider;
import com.ssafy.ssafsound.domain.auth.validator.AuthenticationArgumentResolver;
import com.ssafy.ssafsound.domain.board.controller.BoardController;
import com.ssafy.ssafsound.domain.chat.controller.ChatController;
import com.ssafy.ssafsound.domain.comment.controller.CommentController;
import com.ssafy.ssafsound.domain.lunch.controller.LunchController;
import com.ssafy.ssafsound.domain.member.controller.MemberController;
import com.ssafy.ssafsound.domain.meta.controller.MetaDataController;
import com.ssafy.ssafsound.domain.post.controller.PostController;
import com.ssafy.ssafsound.domain.recruit.controller.RecruitController;
import com.ssafy.ssafsound.domain.recruitapplication.controller.RecruitApplicationController;
import com.ssafy.ssafsound.domain.recruitcomment.controller.RecruitCommentController;
import com.ssafy.ssafsound.global.interceptor.AuthInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;


@WebMvcTest({
        AuthController.class,
        BoardController.class,
        ChatController.class,
        CommentController.class,
        LunchController.class,
        MemberController.class,
        MetaDataController.class,
        PostController.class,
        RecruitController.class,
        RecruitApplicationController.class,
        RecruitCommentController.class
})
@ExtendWith(RestDocumentationExtension.class)
public class ControllerTest {

    protected MockMvcRequestSpecification restDocs;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected AuthenticationArgumentResolver authenticationArgumentResolver;

    @MockBean
    protected AuthInterceptor authInterceptor;


    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        restDocs = RestAssuredMockMvc.given()
                .mockMvc(MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(documentationConfiguration(restDocumentation)
                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint())
                                .withResponseDefaults(prettyPrint()))
                        .build())
                .log().all();

        doReturn(true)
                .when(authInterceptor)
                .preHandle(any(), any(), any());
    }
}
