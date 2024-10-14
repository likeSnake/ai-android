package com.skythinker.gptassistant.entity;
public class AppInfo {
    private String appName;
    private byte[] icon;
    private String packageName;
    private long size;
    private boolean isStart = false;
    private boolean refreshStart = true;
    private int progress=0;

    public AppInfo(String appName, byte[] icon, String packageName, long size) {
        this.appName = appName;
        this.icon = icon;
        this.packageName = packageName;
        this.size = size;
    }

    public AppInfo(String appName, String packageName) {
        this.appName = appName;
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public int getProgress() {
        return progress;
    }
    public void startProgress(){
        progress++;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isRefreshStart() {
        return refreshStart;
    }

    public void setRefreshStart(boolean refreshStart) {
        this.refreshStart = refreshStart;
    }
}
