package com.ssafy.ssafsound.domain.board.exception;

import lombok.Getter;

@Getter
public enum BoardErrorInfo {
    EMPTY_BOARD("801","게시판 데이터가 없습니다."),
    NO_BOARD("802", "존재하지 않는 게시판 입니다.");

    private final String code;
    private final String message;

    BoardErrorInfo(String code, String message){
        this.code = code;
        this.message = message;
    }

}
