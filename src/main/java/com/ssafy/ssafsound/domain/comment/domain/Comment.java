package com.ssafy.ssafsound.domain.comment.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.comment.dto.PostCommentWriteReqDto;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "comment")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE comment SET deleted_comment = true WHERE comment_id = ?")
public class Comment extends BaseTimeEntity {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @Column
    @Builder.Default
    private Boolean deletedComment = Boolean.FALSE;

    @Column
    private Boolean anonymity;

    @ManyToOne
    @JoinColumn(name = "comment_number_id")
    private CommentNumber commentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_group", referencedColumnName = "comment_id")
    private Comment commentGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "comment")
    @Builder.Default
    private List<CommentLike> likes = new ArrayList<>();

    public void setCommentGroup(Comment commentGroup) {
        this.commentGroup = commentGroup;
    }

    public void updateComment(String content, Boolean anonymity) {
        this.content = content;
        this.anonymity = anonymity;
    }
}
