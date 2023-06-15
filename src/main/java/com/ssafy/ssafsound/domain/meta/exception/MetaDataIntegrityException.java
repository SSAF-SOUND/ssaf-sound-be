package com.ssafy.ssafsound.domain.meta.exception;

import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MetaDataIntegrityException extends RuntimeException {

    private GlobalErrorInfo info;
}
