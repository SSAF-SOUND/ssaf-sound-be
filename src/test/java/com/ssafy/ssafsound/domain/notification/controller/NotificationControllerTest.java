package com.ssafy.ssafsound.domain.notification.controller;

import com.ssafy.ssafsound.domain.notification.domain.Notification;
import com.ssafy.ssafsound.domain.notification.dto.GetCheckNotificationResDto;
import com.ssafy.ssafsound.domain.notification.dto.GetNotificationCursorResDto;
import com.ssafy.ssafsound.domain.notification.dto.GetNotificationOffsetResDto;
import com.ssafy.ssafsound.global.docs.ControllerTest;
import com.ssafy.ssafsound.global.util.fixture.NotificationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenMandatory;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

class NotificationControllerTest extends ControllerTest {
    private final NotificationFixture notificationFixture = new NotificationFixture();

    @Test
    @DisplayName("사용자 알림 목록 조회(Cursor), cursor와 size를 기준으로 커서 기반 페이지네이션이 수행됨.")
    void getNotificationsByCursor() {
        GetNotificationCursorResDto response = GetNotificationCursorResDto.of(
                List.of(notificationFixture.createCommentReplyNotification(),
                        notificationFixture.createPostReplyNotification()),
                10
        );

        doReturn(response)
                .when(notificationService)
                .getNotificationsByCursor(any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/notifications/cursor?cursor={cursor}&size={pageSize}", -1, 10)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("notification/get-notifications-by-cursor",
                                requestCookieAccessTokenMandatory(),
                                requestParameters(
                                        parameterWithName("cursor").description("cursor값은 다음 페이지를 가져올 마지막 페이지 번호를 의미함, 초기 cursor는 -1, 이후 cursor값은 응답 데이터로 제공되는 cursor값을 사용."),
                                        parameterWithName("size").description("cursor를 기준으로 다음에 불러올 페이지의 size를 의미, 최소 size는 10")
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("notifications").type(JsonFieldType.ARRAY).description("알림 목록"),
                                        fieldWithPath("cursor").type(JsonFieldType.NUMBER).description("다음에 요청할 cursor값, 응답되는 cursor값이 null이면 다음 페이지는 없음을 의미").optional()
                                ).andWithPrefix("data.notifications[].",
                                        fieldWithPath("notificationId").type(JsonFieldType.NUMBER).description("알림의 고유 ID"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("알림 메시지"),
                                        fieldWithPath("contentId").type(JsonFieldType.NUMBER).description("알림 클릭 시 이동할 해당 컨텐츠의 고유 ID, EX) ServiceType이 POST이면 게시글의 id, RECRUIT면 리쿠르트의 id 단, SYSTEM이면 null이 올 수도 있음."),
                                        fieldWithPath("serviceType").type(JsonFieldType.STRING).description("알림을 저장한 서비스 타입, SYSTEM | POST | RECRUIT"),
                                        fieldWithPath("notificationType").type(JsonFieldType.STRING).description("구체적인 알림 타입, SYSTEM | POST_REPLAY | COMMENT_REPLAY | RECRUIT~~~ 단, RECRUIT는 추가될 수 있음."),
                                        fieldWithPath("read").type(JsonFieldType.BOOLEAN).description("새로 들어온 알림인지 여부, 처음 조회된 알림이면 false"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("알림이 저장된 시간, yyyy-MM-dd HH:mm:ss")
                                )
                        )
                );
    }

    @Test
    @DisplayName("사용자 알림 목록 조회(Offset), page와 size를 기준으로 오프셋 기반 페이지네이션이 수행됨.")
    void getNotificationsByOffset() {
        List<Notification> notifications = List.of(notificationFixture.createCommentReplyNotification(),
                notificationFixture.createPostReplyNotification());

        GetNotificationOffsetResDto response = GetNotificationOffsetResDto.of(
                new PageImpl<>(notifications)
        );

        doReturn(response)
                .when(notificationService)
                .getNotificationsByOffset(any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/notifications/offset?page={page}&size={size}", 1, 10)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("notification/get-notifications-by-offset",
                                requestCookieAccessTokenMandatory(),
                                requestParameters(
                                        parameterWithName("page").description("page값은 불러올 현재 페이지의 값을 의미함, 초기 page는 1(또는 첫 페이지는 1)"),
                                        parameterWithName("size").description("현재 페이지의 게시글 개수를 의미함, 최소 size는 10")
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("notifications").type(JsonFieldType.ARRAY).description("알림 목록"),
                                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                        fieldWithPath("totalPageCount").type(JsonFieldType.NUMBER).description("전체 페이지 수")
                                ).andWithPrefix("data.notifications[].",
                                        fieldWithPath("notificationId").type(JsonFieldType.NUMBER).description("알림의 고유 ID"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("알림 메시지"),
                                        fieldWithPath("contentId").type(JsonFieldType.NUMBER).description("알림 클릭 시 이동할 해당 컨텐츠의 고유 ID, EX) ServiceType이 POST이면 게시글의 id, RECRUIT면 리쿠르트의 id 단, SYSTEM이면 null이 올 수도 있음."),
                                        fieldWithPath("serviceType").type(JsonFieldType.STRING).description("알림을 저장한 서비스 타입, SYSTEM | POST | RECRUIT"),
                                        fieldWithPath("notificationType").type(JsonFieldType.STRING).description("구체적인 알림 타입, SYSTEM | POST_REPLAY | COMMENT_REPLAY | RECRUIT~~~ 단, RECRUIT는 추가될 수 있음."),
                                        fieldWithPath("read").type(JsonFieldType.BOOLEAN).description("새로 들어온 알림인지 여부, 처음 조회된 알림이면 false"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("알림이 저장된 시간, yyyy-MM-dd HH:mm:ss")
                                )
                        )
                );
    }

    @Test
    @DisplayName("새로운 알림 확인")
    void checkNewNotification() {
        doReturn(new GetCheckNotificationResDto(true))
                .when(notificationService)
                .checkNotification(any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/notifications/new")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("notification/check-notification",
                                requestCookieAccessTokenMandatory(),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("isNew").type(JsonFieldType.BOOLEAN).description("새로운 알림이 있는지 여부, true면 확인하지 않은 알림이 있다는 의미.")
                                )
                        )
                );
    }
}