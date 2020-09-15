package com.huaheng.mobilewms.https;

/**
 * Created by Administrator on 2017-4-10.
 */

public class ApiException extends RuntimeException {

    public ApiException(int resultCode) {
        getApiExceptionMessage(resultCode);
    }

    public ApiException(String msg) {
        super(msg);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code){
        String message = "";
        switch (code) {
            default:
                message = "未知错误";
        }
        return message;
    }
}
