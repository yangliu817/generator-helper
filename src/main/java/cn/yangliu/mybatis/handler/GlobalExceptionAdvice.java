package cn.yangliu.mybatis.handler;

import cn.yangliu.comm.entity.JsonResult;
import cn.yangliu.mybatis.ex.HelperException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 杨柳
 * @date 2019-10-29
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {
    /**
     * 自定义异常处理
     */
    @ExceptionHandler(value = HelperException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult<?> exception(HelperException exception) {
         log.error(exception.getMessage(), exception);
        return new JsonResult<>(HttpStatus.BAD_REQUEST.value()+"", exception.getMsg());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResult<?> exception(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new JsonResult<>(HttpStatus.BAD_REQUEST.value() +"", "system inner error");
    }
}
