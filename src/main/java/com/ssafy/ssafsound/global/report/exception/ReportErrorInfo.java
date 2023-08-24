package com.ssafy.ssafsound.global.report.exception;

import lombok.Getter;

@Getter
public enum ReportErrorInfo {

  INVALID_REPORT_REASON("1201","잘못된 신고 사유입니다."),
  UNABLE_SELF_REPORT("1202","자진 신고는 불가합니다.");
  private final String code;
  private final String message;

  ReportErrorInfo(String code, String message){
    this.code = code;
    this.message = message;
  }
}
