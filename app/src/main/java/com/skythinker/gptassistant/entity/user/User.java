package com.skythinker.gptassistant.entity.user;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者: jiang
 *
 * @date: 2024/10/25
 */

@Entity(tableName = "user")
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @ColumnInfo(name = "userName")
    private String userName; // 用户名
    @ColumnInfo(name = "password")
    private String password; // 密码
    @ColumnInfo(name = "phone_number")
    private String phone_number; // 用户手机号
    @ColumnInfo(name = "created_at")
    private String created_at; // 用户注册时间
    @ColumnInfo(name = "updated_at")
    private String updated_at; // 用户信息最后一次更新的时间。
    @ColumnInfo(name = "status")
    private int status; // 用户状态 0正常 1封禁

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
