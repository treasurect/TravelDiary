package com.treasure.traveldiary.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by treasure on 2017/8/1.
 */

public class DiaryBean extends BmobObject {
    private String publish_time;
    private String user_name;
    private String user_nick;
    private String user_icon;
    private String user_lat;
    private String user_long;
    private String user_addr;
    private String user_desc;
    private String user_title;
    private String diary_type;
    private List<String> diary_image;
    private String diary_video;
    private String diary_video_first;
    private List<SUserBean> mesBeanList;
    private String state;//公开or私密

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

    public String getUser_lat() {
        return user_lat;
    }

    public void setUser_lat(String user_lat) {
        this.user_lat = user_lat;
    }

    public String getUser_long() {
        return user_long;
    }

    public void setUser_long(String user_long) {
        this.user_long = user_long;
    }

    public String getUser_addr() {
        return user_addr;
    }

    public void setUser_addr(String user_addr) {
        this.user_addr = user_addr;
    }

    public String getUser_desc() {
        return user_desc;
    }

    public void setUser_desc(String user_desc) {
        this.user_desc = user_desc;
    }

    public String getDiary_type() {
        return diary_type;
    }

    public void setDiary_type(String diary_type) {
        this.diary_type = diary_type;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public List<String> getDiary_image() {
        return diary_image;
    }

    public void setDiary_image(List<String> diary_image) {
        this.diary_image = diary_image;
    }

    public String getUser_title() {
        return user_title;
    }

    public void setUser_title(String user_title) {
        this.user_title = user_title;
    }

    public String getDiary_video() {
        return diary_video;
    }

    public void setDiary_video(String diary_video) {
        this.diary_video = diary_video;
    }

    public String getDiary_video_first() {
        return diary_video_first;
    }

    public void setDiary_video_first(String diary_video_first) {
        this.diary_video_first = diary_video_first;
    }

    public List<SUserBean> getMesBeanList() {
        return mesBeanList;
    }

    public void setMesBeanList(List<SUserBean> mesBeanList) {
        this.mesBeanList = mesBeanList;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
