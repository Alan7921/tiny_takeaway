package fun.xinliu.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());

        if(ex.getMessage().contains("Duplicate entry")){
            String[] strings = ex.getMessage().split(" ");
            String msg =  strings[2] + "已存在";
            return Result.error(msg);
        }

        return Result.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public Result<String> exceptionHandler(CustomException ex) {
        log.error(ex.getMessage());
        return Result.error(ex.getMessage());
    }

}
