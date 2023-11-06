package com.delgo.reward.ncp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SmsMessage {
    private String to;
    private String content;
}