package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.board.domain.Board;

public class BoardFixture {

    public Board getFreeBoard(){
       return Board.builder()
           .id(1L)
           .title("자유 게시판")
           .imageUrl("IMAGE URL")
           .usedBoard(true)
           .description("자유롭게 떠드는 공간입니다.")
           .build();
    }

    public Board getJobBoard(){
        return Board.builder()
            .id(2L)
            .title("취업 게시판")
            .imageUrl("IMAGE URL")
            .usedBoard(true)
            .description("취업에 대한 정보를 공유해보세요.")
            .build();
    }

    public Board getTechBoard(){
        return Board.builder()
            .id(3L)
            .title("테크 게시판")
            .imageUrl("IMAGE URL")
            .usedBoard(false)
            .description("기술을 공유하세요")
            .build();
    }

    public Board getTasteBoard(){
        return Board.builder()
            .id(4L)
            .title("맛집 게시판")
            .imageUrl("IMAGE URL")
            .usedBoard(true)
            .description("맛집을 공유하세요")
            .build();
    }

    public Board getQaBoard(){
        return Board.builder()
            .id(5L)
            .title("질문 게시판")
            .imageUrl("IMAGE URL")
            .usedBoard(true)
            .description("질문을 하세요")
            .build();
    }

    public Board getNewCrewBoard(){
        return Board.builder()
            .id(6L)
            .title("싸피 예비생 게시판")
            .imageUrl("IMAGE URL")
            .usedBoard(true)
            .description("싸피에 대해 궁금한걸 질문하세요")
            .build();
    }
}
