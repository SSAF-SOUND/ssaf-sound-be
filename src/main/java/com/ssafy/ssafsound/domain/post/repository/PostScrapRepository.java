package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.post.domain.PostScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostScrapRepository extends JpaRepository<PostScrap, Long> {
    Optional<PostScrap> findByPostIdAndMemberId(Long postId, Long memberId);

    Integer countByPostId(Long postId);
}
