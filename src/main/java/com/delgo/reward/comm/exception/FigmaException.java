package com.delgo.reward.comm.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FigmaException extends RuntimeException{
    String errorMessage;

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
