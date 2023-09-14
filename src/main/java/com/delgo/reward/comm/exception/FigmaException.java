package com.delgo.reward.comm.exception;

import com.delgo.reward.comm.code.APICode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FigmaException extends Exception{
    private APICode status;
}
