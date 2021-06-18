package com.anyuan.engineflow.interrupter;

import com.alibaba.fastjson.JSONObject;
import com.anyuan.engineflow.base.UserInfoUtil;
import com.safety51.commons.constants.SdkClientConstant;
import com.safety51.commons.entity.BaseResult;
import com.safety51.commons.utils.Base64Util;
import com.safety51.commons.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截
 * @author liangjy on 2021/4/16.
 */
@Slf4j
@Component
public class LoginInterrupter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String sessionId = CookieUtil.getValue(httpServletRequest, SdkClientConstant.PHPSESSID);
        log.info("sessionId:{}",sessionId);
//        if (StringUtils.isNotBlank(sessionId)) {
//            log.info(sessionId);
//            //用于和平台session保持同步
//            long timeA=System.currentTimeMillis();
//            Map<String,Object> session= userClient.getSessionById(sessionId);
//            long minus=(System.currentTimeMillis()-timeA);
//            log.info("login sdk request use time:{} ms  :userSession:{}",minus,session);
//            if (session != null && !session.isEmpty()) {
//                log.info("当前用户信息:{}",JSONObject.toJSONString(session));
//                UserInfo userInfo=JSONObject.parseObject(JSONObject.toJSONString(session),UserInfo.class);
//                UserInfoUtil.setUserInfo(userInfo);
//                //设置Activiti 用户信息
//                Authentication.setAuthenticatedUserId(userInfo.getUserId());
//                return true;
//            }
//        }
//
//        if (nacosConfig.getLoginMock() && StringUtils.isNotBlank(nacosConfig.getLoginUserMock())) { //不影响已登录用户
//            log.info("mock:" + nacosConfig.getLoginUserMock());
//            String encrypt = nacosConfig.getLoginUserMock();
//            String userStr = Base64Util.decode(encrypt);
//            log.info("");
////            UserSession userSession = JSONObject.parseObject(userStr, UserSession.class);
////            UserInfoUtil.setUserInfo(userSession);
////            return true;
//        }

        BaseResult result = BaseResult.builder().build().fail("您尚未登录");
        String errorMsg = JSONObject.toJSONString(result);
        httpServletResponse.setHeader("content-type", "text/json;charset=UTF-8");
        httpServletResponse.getWriter().println(JSONObject.toJSONString(errorMsg));
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserInfoUtil.clearUserInfo();
    }

}
