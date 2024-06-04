package com.atguigu.cloud.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultData<T> {

    public String code;
    public String message;
    public T data;
    public long timestamp;

    public ResultData(){
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ResultData<T> success(T data) {
        ResultData resultData = new ResultData();
        resultData.setData(data);
        resultData.setCode(ReturnCodeNum.RC200.getCode());
        resultData.setMessage(ReturnCodeNum.RC200.getMessage());
        return resultData;
    }

    public static <T> ResultData<T> fail(String code,String message) {
        ResultData resultData = new ResultData();
        resultData.setData(null);
        resultData.setCode(code);
        resultData.setMessage(message);
        return resultData;
    }

}
