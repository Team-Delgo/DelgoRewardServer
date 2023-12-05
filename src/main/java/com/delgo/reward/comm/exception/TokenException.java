package com.delgo.reward.comm.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TokenException extends RuntimeException {
    private final String errorMessage;

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
