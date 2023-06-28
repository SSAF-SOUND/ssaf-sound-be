package com.ssafy.ssafsound.domain.post.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="hot_post")
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
}
