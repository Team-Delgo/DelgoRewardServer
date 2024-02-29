package com.delgo.reward.common.controller;


import com.delgo.reward.comm.code.ResponseCode;
import com.delgo.reward.common.response.CommResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CommController {
    public ResponseEntity<?> SuccessReturn() {
        return ResponseEntity.ok().body(CommResponse.from(ResponseCode.SUCCESS));
    }

    public ResponseEntity<?> SuccessReturn(Object data) {
        return ResponseEntity.ok().body(CommResponse.from(ResponseCode.SUCCESS, data));
    }

    public ResponseEntity<?> ErrorReturn(ResponseCode responseCode) {
        return ResponseEntity.ok().body(CommResponse.from(responseCode));
    }

    public ResponseEntity<?> ErrorReturn(ResponseCode responseCode, Object data) {
        return ResponseEntity.ok().body(CommResponse.from(responseCode, data));
    }

    public ResponseEntity<?> ErrorReturnSetMessage(ResponseCode responseCode, String msg) {
        return ResponseEntity.ok().body(CommResponse.builder()
                .code(responseCode.getCode())
                .codeMsg(msg)
                .build());
    }

    public ResponseEntity<?> ParamErrorReturn(String param) {
        return ResponseEntity.ok().body(
                CommResponse.builder()
                        .code(ResponseCode.PARAM_ERROR.getCode())
                        .codeMsg(ResponseCode.PARAM_ERROR + " : [" + param + "]")
                        .build());
    }

    public ResponseEntity<?> TokenErrorReturn(String msg) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                CommResponse.builder()
                        .code(ResponseCode.TOKEN_ERROR.getCode())
                        .codeMsg(msg)
                        .build());
    }
}
