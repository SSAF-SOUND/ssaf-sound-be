package com.ssafy.ssafsound.domain.post.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.ssafy.ssafsound.domain.BaseTimeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "hot_post")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotPost extends BaseTimeEntity {

	@Id
	@Column(name = "hot_post_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "post_id")
	private Post post;

	public static HotPost from(Post post) {
		return HotPost.builder()
			.post(post)
			.build();
	}
}
