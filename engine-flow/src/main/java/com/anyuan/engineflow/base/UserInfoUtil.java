package com.anyuan.engineflow.base;

/**
 * 用户线程副本
 * @author liangjy on 2021/4/26.
 */
public class UserInfoUtil {

    private static final ThreadLocal<UserInfo> userThreadLocal = new ThreadLocal<>();

    public static void setUserInfo(UserInfo userInfo) {
        if (userThreadLocal.get() == null) {
            userThreadLocal.set(userInfo);
        }
    }

    public static UserInfo getUserInfo() {
        return userThreadLocal.get();
    }

    public static void clearUserInfo() {
        userThreadLocal.remove();
    }

    public static String getCurContainerId() {
        return getUserInfo().getContainerId();
    }

    public static String getCurUserId() {
        return getUserInfo().getUserId();
    }


}
