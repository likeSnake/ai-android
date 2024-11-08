package com.skythinker.gptassistant.entity;


import java.io.Serializable;

// APP信息
public class AppInfo implements Serializable {
    private Integer version; // 最新版本
    private String updateInfo; // 更新公告
    private String versionName; // 版本名
    private int versionCode; // 版本代码
    private String versionUrl; // 更新地址
    private String versionContent; //

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getVersionContent() {
        return versionContent;
    }

    public void setVersionContent(String versionContent) {
        this.versionContent = versionContent;
    }
}