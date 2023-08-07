package com.delgo.reward.comm;


import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.record.common.ResponseRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CommController {
    public ResponseEntity SuccessReturn() {
        return ResponseEntity.ok().body(new ResponseRecord(APICode.SUCCESS.getCode(), APICode.SUCCESS.getMsg(), null));
    }

    public ResponseEntity SuccessReturn(Object data) {
        return ResponseEntity.ok().body(new ResponseRecord(APICode.SUCCESS.getCode(), APICode.SUCCESS.getMsg(), data));
    }

    public ResponseEntity ErrorReturn(APICode apiCode) {
        return ResponseEntity.ok().body(new ResponseRecord(apiCode.getCode(), apiCode.getMsg(), null));
    }

    public ResponseEntity ErrorReturn(APICode apiCode, Object data) {
        return ResponseEntity.ok().body(new ResponseRecord(apiCode.getCode(), apiCode.getMsg(), data));
    }

    public ResponseEntity ParamErrorReturn(String param) {
        return ResponseEntity.ok().body(
                new ResponseRecord(
                        APICode.PARAM_ERROR.getCode(),
                        APICode.PARAM_ERROR + " : [" + param + "]",
                        null)
        );
    }

    public ResponseEntity TokenErrorReturn() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseRecord(APICode.TOKEN_ERROR.getCode(), APICode.TOKEN_ERROR.getMsg(), null));
    }
}
