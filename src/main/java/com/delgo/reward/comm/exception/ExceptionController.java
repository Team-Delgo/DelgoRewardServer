package com.delgo.reward.comm.exception;


import com.delgo.reward.common.controller.CommController;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.common.response.ResponseRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ExceptionController extends CommController {

    // 알 수 없는 에러 체크
    @ExceptionHandler
    public ResponseEntity exception(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return ErrorReturnSetMessage(APICode.SERVER_ERROR, e.getMessage());
    }

    // @RequestParam Param Error Check
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity missingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error(e.getMessage());
        return ParamErrorReturn(e.getParameterName());
    }

    // @RequestParam File Error Check
    @ExceptionHandler({MissingServletRequestPartException.class})
    public ResponseEntity missingServletRequestPartException(MissingServletRequestPartException e) {
        log.error(e.getMessage());
        return ParamErrorReturn(e.getRequestPartName());
    }

    // @RequestBody DTO Param Error Check
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String field = Objects.requireNonNull(e.getFieldError()).getField();
        log.error(e.getMessage());
        return ParamErrorReturn(field);
    }

    // Optional Select Error Check
    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity NullPointerException(NullPointerException e) {
        log.error(e.getMessage());
        if (e.getMessage().equals("PHOTO EXTENSION IS WRONG"))
            return ResponseEntity.ok().body(
                    new ResponseRecord(APICode.PHOTO_ERROR.getCode(), APICode.PHOTO_ERROR.getMsg(), null));

        return ResponseEntity.ok().body(
                new ResponseRecord(APICode.NOT_FOUND_DATA.getCode(), e.getMessage(), null));
    }

    // @PathVariable ERROR - 1
    @ExceptionHandler({MissingPathVariableException.class})
    public ResponseEntity missingPathVariableException(MissingPathVariableException e) {
        log.error(e.getMessage());
        return ParamErrorReturn(e.getParameter().getParameterName());
    }

    // @PathVariable ERROR - 2
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage());
        return ParamErrorReturn(e.getParameter().getParameterName());
    }

    @ExceptionHandler({FigmaException.class})
    public ResponseEntity figmaException(FigmaException e) {
        log.error(e.getMessage());
        return ErrorReturnSetMessage(APICode.FIGMA_ERROR, APICode.FIGMA_ERROR.getMsg() + " " + e.errorMessage);
    }

    @ExceptionHandler({PhotoException.class})
    public ResponseEntity photoException(PhotoException e) {
        log.error(e.getMessage());
        return ErrorReturn(APICode.PHOTO_ERROR);
    }

    // DB NotFoundDataException
    @ExceptionHandler({NotFoundDataException.class})
    public ResponseEntity notFoundDataException(NotFoundDataException e) {
        log.error(e.getMessage());
        return ErrorReturnSetMessage(APICode.NOT_FOUND_DATA, APICode.NOT_FOUND_DATA.getMsg() + " " + e.getMessage());
    }

    @ExceptionHandler({TokenException.class})
    public ResponseEntity tokenException(TokenException e) {
        log.error(e.getMessage());
        return TokenErrorReturn(e.getMessage());
    }
}
