package io.github.cmt.extension.admin.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tuzhenxian
 * @date 19-10-11
 */
@Data
@NoArgsConstructor
public class Result<T> {
    private String msg;
    private T data;
    private boolean success;

    private Result(String msg, T data, boolean success) {
        this.msg = msg;
        this.data = data;
        this.success = success;
    }

    public static Result fail(String failMsg){
        return new Result(failMsg,null,false);
    }

    public static <T> Result success(T data){
        return new Result("",data,true);
    }

    public static  Result success(){
        return new Result("success","",true);
    }
}
