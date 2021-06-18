package com.anyuan.engineflow.base;

import lombok.Data;

/**
 * 用户信息
 * @author liangjy on 2021/4/26.
 */
@Data
public class UserInfo {

    /**
     * 容器ID
     */
    private String containerId;

    /**
     * 容器名称
     */
    private String containerName;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户类型
     */
    private Integer type;

    /**
     * 用户姓名
     */
    private String realName;

    /**
     * 拼音
     */
    private String pinyin;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 最后的登录时间
     */
    private String lastLoginTime;

    /**
     * 主题
     */
    private String theme;

}
