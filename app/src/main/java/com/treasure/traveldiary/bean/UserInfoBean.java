package com.treasure.traveldiary.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by treasure on 2017.03.30.
 */

public class UserInfoBean extends BmobObject {
    private String user_name;
    private String user_icon;
    private String nick_name;
    private String user_pwd;
    private int sex;//0 男 1女
    private int age;
    private String user_desc;
    private List<String> signing_date;
    private int integral_count;
    private List<LeaveMesBean> leaveMesList;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUser_desc() {
        return user_desc;
    }

    public void setUser_desc(String user_desc) {
        this.user_desc = user_desc;
    }

    public List<String> getSigning_date() {
        return signing_date;
    }

    public void setSigning_date(List<String> signing_date) {
        this.signing_date = signing_date;
    }

    public int getIntegral_count() {
        return integral_count;
    }

    public void setIntegral_count(int integral_count) {
        this.integral_count = integral_count;
    }

    public List<LeaveMesBean> getLeaveMesList() {
        return leaveMesList;
    }

    public void setLeaveMesList(List<LeaveMesBean> leaveMesList) {
        this.leaveMesList = leaveMesList;
    }
}
