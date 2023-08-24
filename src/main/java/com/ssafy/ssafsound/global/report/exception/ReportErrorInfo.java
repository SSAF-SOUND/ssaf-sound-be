package com.ssafy.ssafsound.global.report.exception;

public enum ReportErrorInfo {

  INVALID_REPORT_REASON("1201","잘못된 신고 사유입니다."),
  UNABLE_SELF_REPORT("1202","자진 신고는 불가합니다.");
  private String code;
  private String message;

  ReportErrorInfo(String code, String message){
    this.code = code;
    this.message = message;
  }
}
