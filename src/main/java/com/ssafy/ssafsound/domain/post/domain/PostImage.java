package com.ssafy.ssafsound.domain.post.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "post_image")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostImage {

	@Id
	@Column(name = "post_image_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@Column
	private String imagePath;

	@Column
	private String imageUrl;

	@Column
	private Integer renderOrder;

	public static PostImage of(Post post, String imagePath, String imageUrl) {
		return PostImage.builder()
			.post(post)
			.imagePath(imagePath)
			.imageUrl(imageUrl)
			.build();
	}
}
