package com.ssafy.ssafsound.domain.recruitcomment.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity(name="recruit_comment")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE recruit_comment SET deleted_comment = true WHERE recruit_comment_id = ?")
public class RecruitComment extends BaseTimeEntity {

    @Id
    @Column(name = "recruit_comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @Column
    @Builder.Default
    private Boolean deletedComment = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_group", referencedColumnName = "recruit_comment_id")
    private RecruitComment commentGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setCommentGroup(RecruitComment commentGroup) {
        if(this.commentGroup != null) {
            throw new RecruitException(RecruitErrorInfo.INVALID_CHANGE_COMMENT_GROUP_OPERATION);
        }
        this.commentGroup = commentGroup;
    }

    public void setRecruit(Recruit recruit) {
        if(this.recruit != null) {
            throw new RecruitException(RecruitErrorInfo.INVALID_CHANGE_RECRUIT_OPERATION);
        }
        this.recruit = recruit;
    }

    public void setWriter(Member member) {
        if(this.member != null) {
            throw new RecruitException(RecruitErrorInfo.INVALID_CHANGE_MEMBER_OPERATION);
        }
        this.member = member;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
