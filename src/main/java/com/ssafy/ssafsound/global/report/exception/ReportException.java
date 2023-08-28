package com.ssafy.ssafsound.global.report.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportException extends RuntimeException {

  private ReportErrorInfo info;

}
