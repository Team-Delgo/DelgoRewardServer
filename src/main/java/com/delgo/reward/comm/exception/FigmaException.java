package com.delgo.reward.comm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FigmaException extends RuntimeException{
    String errorMessage;
}
