package com.delgo.reward.comm;


import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.record.common.ResponseRecord;
import org.springframework.http.ResponseEntity;

public class CommController {
    public ResponseEntity SuccessReturn() {
        return ResponseEntity.ok().body(new ResponseRecord(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMsg(), null));
    }

    public ResponseEntity SuccessReturn(Object data) {
        return ResponseEntity.ok().body(new ResponseRecord(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMsg(), data));
    }

    public ResponseEntity ErrorReturn(ApiCode apiCode) {
        return ResponseEntity.ok().body(new ResponseRecord(apiCode.getCode(), apiCode.getMsg(), null));
    }

    public ResponseEntity ErrorReturn(ApiCode apiCode, Object data) {
        return ResponseEntity.ok().body(new ResponseRecord(apiCode.getCode(), apiCode.getMsg(), data));
    }

    public ResponseEntity ParamErrorReturn(String param) {
        return ResponseEntity.ok().body(
                new ResponseRecord(
                        ApiCode.PARAM_ERROR.getCode(),
                        ApiCode.PARAM_ERROR + " : [" + param + "]",
                        null)
        );
    }
}
