package com.cmt.extension.core.common;

/**
 * @author tuzhenxian
 * @date 19-10-11
 */
public class SpiException extends RuntimeException {
    public SpiException(String message) {
        super(message);
    }

    public static SpiException fail(String msg){
        return new SpiException(msg);
    }
}
