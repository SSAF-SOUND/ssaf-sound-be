package com.ssafy.ssafsound.domain.post.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostException extends RuntimeException{

    private PostErrorInfo info;
}
