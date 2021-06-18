package com.anyuan.engineflow.base;

import java.util.Map;

/**
 * 流程异常处理
 * @author liangjy on 2021/4/20.
 */
public interface FlowExceptionHandler {

    /**
     *
     * @param errorCode  错误码
     * @param msg   错误信息
     * @param extendData 拓展信息
     */
    void handler(String errorCode,String msg , Map<String,Object> extendData);

}
