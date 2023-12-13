package com.delgo.reward.comm.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GoogleSheetException extends RuntimeException{
    String errorMessage;

    @Override
    public String getMessage() {
        return errorMessage;
    }
}