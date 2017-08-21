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
    private String sex;//0 男 1女
    private String age;
    private String user_desc;
    private String register_time;
    private List<String> signing_date;
    private String integral_count;
    private String traveller_circle_bg;
    private String version_name;
    private List<String> timer_shaft;
    private String binding_qq;
    private String binding_wechat;
    private String binding_sina;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUser_desc() {
        return user_desc;
    }

    public void setUser_desc(String user_desc) {
        this.user_desc = user_desc;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public List<String> getSigning_date() {
        return signing_date;
    }

    public void setSigning_date(List<String> signing_date) {
        this.signing_date = signing_date;
    }

    public String getIntegral_count() {
        return integral_count;
    }

    public void setIntegral_count(String integral_count) {
        this.integral_count = integral_count;
    }

    public String getTraveller_circle_bg() {
        return traveller_circle_bg;
    }

    public void setTraveller_circle_bg(String traveller_circle_bg) {
        this.traveller_circle_bg = traveller_circle_bg;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public List<String> getTimer_shaft() {
        return timer_shaft;
    }

    public void setTimer_shaft(List<String> timer_shaft) {
        this.timer_shaft = timer_shaft;
    }

    public String getBinding_qq() {
        return binding_qq;
    }

    public void setBinding_qq(String binding_qq) {
        this.binding_qq = binding_qq;
    }

    public String getBinding_wechat() {
        return binding_wechat;
    }

    public void setBinding_wechat(String binding_wechat) {
        this.binding_wechat = binding_wechat;
    }

    public String getBinding_sina() {
        return binding_sina;
    }

    public void setBinding_sina(String binding_sina) {
        this.binding_sina = binding_sina;
    }
}
