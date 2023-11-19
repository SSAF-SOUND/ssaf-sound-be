package com.ssafy.ssafsound.domain.board.repository;

import com.ssafy.ssafsound.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByUsedBoardTrue();

    Optional<Board> findByIdAndUsedBoardTrue(Long id);

    Boolean existsByIdAndUsedBoardTrue(Long id);
}
