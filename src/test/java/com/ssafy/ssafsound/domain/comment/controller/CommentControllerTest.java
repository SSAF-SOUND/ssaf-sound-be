//package com.ssafy.ssafsound.domain.comment.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
//import com.ssafy.ssafsound.domain.board.domain.Board;
//import com.ssafy.ssafsound.domain.comment.domain.Comment;
//import com.ssafy.ssafsound.domain.comment.dto.GetCommentResDto;
//import com.ssafy.ssafsound.domain.comment.service.CommentService;
//import com.ssafy.ssafsound.domain.member.domain.Member;
//import com.ssafy.ssafsound.domain.member.domain.MemberRole;
//import com.ssafy.ssafsound.domain.member.domain.OAuthType;
//import com.ssafy.ssafsound.domain.post.domain.Post;
//import com.ssafy.ssafsound.global.common.json.JsonParser;
//import com.ssafy.ssafsound.global.docs.ControllerTest;
//import com.ssafy.ssafsound.utils.BaseRestDocControllerTest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.util.MultiValueMap;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class CommentControllerTest extends ControllerTest {
//    @BeforeEach
//    void setUp() {
//        memberRole = MemberRole.builder()
//                .id(1)
//                .roleType("user")
//                .build();
//
//        member = Member.builder()
//                .id(1L)
//                .oauthType(OAuthType.GITHUB)
//                .oauthIdentifier("123123123")
//                .role(memberRole)
//                .build();
//
//        authenticatedMember = AuthenticatedMember.from(member);
//
//        Board board = Board.builder()
//                .id(1L)
//                .title("자유 게시판")
//                .imageUrl("IMAGE URL")
//                .description("자유게시판 설명입니다.")
//                .build();
//
//        post = Post.builder()
//                .id(1L)
//                .title("오늘 싸탈각인데?? 질문좀")
//                .content("아니 오늘 B형 시험 봤는데 붙음..")
//                .anonymity(false)
//                .board(board)
//                .member(member)
//                .build();
//    }
//
//    @Test
//    void writeComment() throws Exception {
////        Comment comment = Comment.builder()
////                .id(10L)
////                .content("댓글 내용")
////                .anonymity(false)
////                .commentNumber(null)
////                .post(post)
////                .member(member)
////                .build();
////        comment.setCommentGroup(comment);
//        Map<String, Object> requestData = new HashMap<>();
//        requestData.put("content", "댓글 내용");
//        requestData.put("anonymity", false);
//
//        ObjectMapper mapper = JsonParser.getMapper();
//        String body = mapper.writeValueAsString(requestData);
//
//        mockMvc.perform(post("/comments")
//                        .param("postId", "1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body)
//                )
//                .andExpect(status().isOk())
//                .andDo(restDocs.document());
//    }
//
//    @Test
//    @DisplayName("댓글 목록 조회")
//    void findComments() throws Exception {
//        mockMvc.perform(get("/comments")
//                        .param("postId", "1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andDo(
//                        restDocs.document()
//                );
//    }
//
//    @Test
//    void updateComment() {
//    }
//
//    @Test
//    void writeCommentReply() {
//    }
//
//    @Test
//    void likeComment() {
//    }
//
//    @Test
//    void deleteComment() {
//    }
//}