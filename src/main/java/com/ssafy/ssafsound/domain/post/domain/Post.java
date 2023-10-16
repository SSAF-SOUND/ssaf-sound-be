package com.ssafy.ssafsound.domain.post.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.board.domain.Board;
import com.ssafy.ssafsound.domain.comment.domain.Comment;
import com.ssafy.ssafsound.domain.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "post")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE post SET deleted_post = true WHERE post_id = ?")
@Where(clause = "deleted_post = false")
public class Post extends BaseTimeEntity {

	@Id
	@Column(name = "post_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String title;

	@Column
	private String content;

	@Column
	private Long view;

	@Builder.Default
	@Column
	private Boolean deletedPost = Boolean.FALSE;

	@Column
	private Boolean anonymity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder.Default
	@OneToMany(mappedBy = "post")
	private List<PostImage> images = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "post")
	private List<PostLike> likes = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "post")
	private List<Comment> comments = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "post")
	private List<PostScrap> scraps = new ArrayList<>();

	public void updatePost(String title, String content, Boolean anonymity) {
		this.title = title;
		this.content = content;
		this.anonymity = anonymity;
	}

	public static Post of(Board board, Member member, String title, String content, Boolean anonymity) {
		return Post.builder()
			.board(board)
			.member(member)
			.title(title)
			.content(content)
			.anonymity(anonymity)
			.build();
	}
}
