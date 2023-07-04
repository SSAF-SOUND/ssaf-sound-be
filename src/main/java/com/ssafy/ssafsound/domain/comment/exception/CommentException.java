package com.ssafy.ssafsound.domain.comment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentException extends RuntimeException{
    private CommentErrorInfo info;
}
