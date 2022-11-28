package top.mikecao.wager.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.mikecao.wager.common.Result;
import top.mikecao.wager.exception.AppClientException;
import top.mikecao.wager.exception.AppServerException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> onError(RuntimeException e){
        log.error("内部错误", e);
        return Result.fail("内部异常");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> onError(Exception e){
        log.error("内部错误", e);
        return Result.fail("内部异常");
    }


    @ExceptionHandler(AppServerException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> onError(AppServerException e){
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(AppClientException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> onError(AppClientException e){
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> onError(BindException e){
        return Result.fail(e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";")));
    }
}
