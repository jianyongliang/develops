package com.anyuan.engineflow.util;

import com.alibaba.fastjson.JSONObject;
import com.anyuan.commons.base.BaseRpcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author liangjy on 2021/5/11.
 */
public class BeanUtils {

    /**
     * tranform bean2another type
     * @param original
     * @param newClz
     * @return
     */
    public static BaseRpcResult beanTypetransform(Object original, Class newClz){
        List resultList=new ArrayList();
        if(original instanceof List){
            Optional.ofNullable((List)original).orElse(new ArrayList<>()).forEach(data->{
                resultList.add(JSONObject.parseObject(JSONObject.toJSONString(data),newClz));
            });
            return BaseRpcResult.builder().data(resultList).build();
        }
        return BaseRpcResult.builder().data(JSONObject.parseObject(JSONObject.toJSONString(original),newClz)).build();
    }

}
