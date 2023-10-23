package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.post.domain.PostScrap;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostScrapRepository extends JpaRepository<PostScrap, Long>, PostScrapCustomRepository {
    Optional<PostScrap> findByPostIdAndMemberId(Long postId, Long memberId);

    Integer countByPostId(Long postId);


    @Query("select s from post_scrap s " +
            "join fetch s.post p " +
            "join fetch p.board " +
            "join fetch p.member " +
            "left join fetch p.likes " +
            "where s.member = :member ")
    List<PostScrap> findMyScrapPostsByPageableAndMember(PageRequest pageRequest, @Param("member") Member member);
}
