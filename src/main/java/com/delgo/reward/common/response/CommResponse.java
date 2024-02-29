package com.delgo.reward.common.response;

import com.delgo.reward.comm.code.ResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record CommResponse(int code, String codeMsg, Object data) {
    public static CommResponse from(ResponseCode responseCode) {
        return CommResponse.builder()
                .code(responseCode.getCode())
                .codeMsg(responseCode.getMsg())
                .build();
    }

    public static CommResponse from(ResponseCode responseCode, Object data) {
        return CommResponse.builder()
                .code(responseCode.getCode())
                .codeMsg(responseCode.getMsg())
                .data(data)
                .build();
    }
}