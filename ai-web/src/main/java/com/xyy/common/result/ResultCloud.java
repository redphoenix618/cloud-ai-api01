package com.xyy.common.result;

import lombok.Data;

@Data
public class ResultCloud<T> {

    private String code;
    private String message;
    private T data;
    private static final String SUCCESS_CODE = "200";
    private static final String ERROR_CODE = "500";
    private static final String SUCCESS_MESSAGE = "success";

    public static <T> ResultCloud success(T data){
        ResultCloud result = new ResultCloud();
        result.setCode(SUCCESS_CODE);
        result.setMessage(SUCCESS_MESSAGE);
        result.setData(data);
        return result;
    }

    public static <T>  ResultCloud error(String message){
        ResultCloud result = new ResultCloud();
        result.setCode(ERROR_CODE);
        result.setMessage(message);
        return result;
    }
}
