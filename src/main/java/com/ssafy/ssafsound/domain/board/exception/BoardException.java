package com.ssafy.ssafsound.domain.board.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardException extends RuntimeException{

    private BoardErrorInfo info;
}
