package com.ljc.librarybackend.exception;

import com.baomidou.mybatisplus.extension.api.R;
import com.ljc.librarybackend.utils.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations ={RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResultModel exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());

        if(ex.getMessage().contains("Duplicate entry")){
            String[] split=ex.getMessage().split(" ");
            String msg=split[2]+"已存在";
            return ResultModel.error(msg);
        }
        return ResultModel.error("操作失败");
    }

    /**
     * 异常处理方法
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public ResultModel exceptionHandler(CustomException ex){
        log.error(ex.getMessage());

        return ResultModel.error(ex.getMessage());
    }


}
