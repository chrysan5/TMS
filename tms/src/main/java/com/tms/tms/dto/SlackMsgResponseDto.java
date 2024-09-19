package com.tms.tms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SlackMsgResponseDto {
    private Long slackMsgId;
    private String slackMsg;
}
