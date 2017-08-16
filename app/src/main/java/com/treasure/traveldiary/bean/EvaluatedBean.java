package com.treasure.traveldiary.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by treasure on 2017/8/3.
 */

public class EvaluatedBean extends BmobObject{
    private String user_name;
    private String user_nick;
    private String user_icon;
    private String publish_time;
    private String user_addr;
    private String star_num;
    private String user_evaluated;
    private List<LeaveMesBean> mesBeanList;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getUser_addr() {
        return user_addr;
    }

    public void setUser_addr(String user_addr) {
        this.user_addr = user_addr;
    }

    public String getStar_num() {
        return star_num;
    }

    public void setStar_num(String star_num) {
        this.star_num = star_num;
    }

    public String getUser_evaluated() {
        return user_evaluated;
    }

    public void setUser_evaluated(String user_evaluated) {
        this.user_evaluated = user_evaluated;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public List<LeaveMesBean> getMesBeanList() {
        return mesBeanList;
    }

    public void setMesBeanList(List<LeaveMesBean> mesBeanList) {
        this.mesBeanList = mesBeanList;
    }
}
