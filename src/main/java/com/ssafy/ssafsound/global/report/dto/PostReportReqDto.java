package com.ssafy.ssafsound.global.report.dto;

import com.ssafy.ssafsound.domain.meta.validator.CheckReportable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@Getter
@NoArgsConstructor
public class PostReportReqDto {

  @CheckReportable
  private String sourceType;

  @NumberFormat
  private Long sourceId;

  @NumberFormat
  private Long reasonId;
}
