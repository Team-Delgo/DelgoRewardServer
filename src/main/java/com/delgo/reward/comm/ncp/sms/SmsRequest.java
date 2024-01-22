package com.delgo.reward.comm.ncp.sms;


import lombok.Getter;
import lombok.Builder;

import java.util.List;

@Getter
@Builder
public class SmsRequest {
    private String type;
    private String contentType;
    private String countryCode;
    private String from;
    private String content;
    private List<Message> messages;

    @Getter
    @Builder
    public static class Message {
        private String to;
        private String content;
    }

    public static SmsRequest from(String to, String content) {
        return SmsRequest.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from("07079542910")
                .content("Delgo")
                .messages(List.of(Message.builder()
                        .to(to)
                        .content(content)
                        .build()))
                .build();
    }
}
