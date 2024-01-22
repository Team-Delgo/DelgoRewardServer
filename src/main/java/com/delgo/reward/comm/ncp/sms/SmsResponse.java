package com.delgo.reward.comm.ncp.sms;

import lombok.*;

import java.sql.Timestamp;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmsResponse {
    private String statusCode;
    private String statusName;
    private String requestId;
    private Timestamp requestTime;
}
