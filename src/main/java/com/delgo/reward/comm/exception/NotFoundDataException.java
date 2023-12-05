package com.delgo.reward.comm.exception;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class NotFoundDataException extends RuntimeException {
    private final String errorMessage;

    @Override
    public String getMessage() {
        return errorMessage;
    }
}