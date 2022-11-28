package top.mikecao.wager.common;

import lombok.Getter;

public final class Result<T> {
    @Getter
    private final int code;
    @Getter
    private final String msg;
    @Getter
    private final T data;

    private Result(){
        this.code = 0;
        this.msg = "";
        this.data = null;
    }

    private Result(int code, String msg, T t){
        this.code = code;
        this.msg = msg;
        this.data = t;
    }

    public static <T> Result<T> ok(){
        return new Result<>(0, "", null);
    }

    public static <T> Result<T> ok(T t){
        return new Result<>(0, "", t);
    }

    public static <T> Result<T> fail(String msg){
        return new Result<>(-1, msg, null);
    }
}
