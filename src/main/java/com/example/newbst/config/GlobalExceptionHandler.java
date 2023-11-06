package com.example.newbst.config;

import com.example.newbst.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * created by Xu on 2023/8/19 15:18.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> exceptionHandler(Exception e){
        log.error("发生异常：", e);
        return new ResponseEntity<>(Result.fail("服务器异常 请稍后再试或联系管理员"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Result> ImageSizeExceptionHandler(Exception e){
        log.error("发生异常：", e);
        return new ResponseEntity<>(Result.fail("上传的图片不能大于7M"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
