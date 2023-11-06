package com.delgo.reward.ncp.domain;

import lombok.*;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SmsRequest {
    private String type;
    private String contentType;
    private String countryCode;
    private String from;
    private String content;
    private List<SmsMessage> messages;

    public static SmsRequest create(List<SmsMessage> messages) {
        return SmsRequest.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from("07079542910")
                .content("Delgo")
                .messages(messages)
                .build();
    }
}
