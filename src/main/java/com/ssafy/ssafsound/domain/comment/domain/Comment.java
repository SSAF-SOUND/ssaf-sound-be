package com.ssafy.ssafsound.domain.comment.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "comment")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE comment SET deleted_comment = true WHERE comment_id = ?")
@Where(clause = "deleted_comment = false")
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
    private Boolean anonymous;

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
    private List<CommentLike> likes = new ArrayList<>();

    public void setCommentGroup(Comment commentGroup) {
        this.commentGroup = commentGroup;
    }

    public void updateComment(String content, Boolean anonymous) {
        this.content = content;
        this.anonymous = anonymous;
    }
}
