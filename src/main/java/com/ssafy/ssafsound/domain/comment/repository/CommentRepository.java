package com.ssafy.ssafsound.domain.comment.repository;

import com.ssafy.ssafsound.domain.comment.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM comment c " +
            "JOIN FETCH c.commentNumber " +
            "JOIN FETCH c.member " +
            "JOIN FETCH c.commentGroup g " +
            "WHERE c.post.id = :postId " +
            "ORDER BY g.id ")
    List<Comment> findAllPostIdWithDetailsFetchOrderByCommentGroupId(@Param("postId") Long postId);
}
