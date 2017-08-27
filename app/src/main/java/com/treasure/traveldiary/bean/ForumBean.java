package com.treasure.traveldiary.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by treasure on 2017/8/26.
 */

public class ForumBean extends BmobObject{
    private String user_name;
    private String user_nick;
    private String user_icon;
    private String publish_time;
    private String publish_content;
    private String publish_from;
    private List<SUserBean> leaveMes_list;

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

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getPublish_content() {
        return publish_content;
    }

    public void setPublish_content(String publish_content) {
        this.publish_content = publish_content;
    }

    public String getPublish_from() {
        return publish_from;
    }

    public void setPublish_from(String publish_from) {
        this.publish_from = publish_from;
    }

    public List<SUserBean> getLeaveMes_list() {
        return leaveMes_list;
    }

    public void setLeaveMes_list(List<SUserBean> leaveMes_list) {
        this.leaveMes_list = leaveMes_list;
    }
}
