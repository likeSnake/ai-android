package com.skythinker.gptassistant.entity;

import java.io.Serializable;

/**
 * jcl
 * 2025/4/9
 */
public class SignInInfo implements Serializable {
    private int checkType; // 0--手机验证码登录  1--密码登录绑定手机号  2--手机、密码登录  3--微信绑定手机号  4--修改密码
    private String phone; // 手机号
    private String userName; // 用户名
    private String password; // 原始密码
    private String newPassword; //新密码

    public int getCheckType() {
        return checkType;
    }

    public void setCheckType(int checkType) {
        this.checkType = checkType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
