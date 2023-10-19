package com.ssafy.ssafsound.domain.board.repository;

import java.util.List;

import com.ssafy.ssafsound.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
	List<Board> findAllByUsedBoardTrue();
}
