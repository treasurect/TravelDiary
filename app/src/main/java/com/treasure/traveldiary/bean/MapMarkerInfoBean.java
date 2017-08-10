package com.treasure.traveldiary.bean;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by treasure on 2017.05.19.
 */

public class MapMarkerInfoBean implements Serializable {
    private LatLng latLng;
    private String user_icon;
    private String user_addr;
    private String user_title;
    private String user_desc;
    private String user_time;
    private String user_nick;
    private String user_name;
    private List<String> user_image;
    private int diary_type;
    private String video_path;
    private String video_path_first;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getUser_addr() {
        return user_addr;
    }

    public void setUser_addr(String user_addr) {
        this.user_addr = user_addr;
    }

    public String getUser_title() {
        return user_title;
    }

    public void setUser_title(String user_title) {
        this.user_title = user_title;
    }

    public String getUser_desc() {
        return user_desc;
    }

    public void setUser_desc(String user_desc) {
        this.user_desc = user_desc;
    }

    public String getUser_time() {
        return user_time;
    }

    public void setUser_time(String user_time) {
        this.user_time = user_time;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public List<String> getUser_image() {
        return user_image;
    }

    public void setUser_image(List<String> user_image) {
        this.user_image = user_image;
    }

    public int getDiary_type() {
        return diary_type;
    }

    public void setDiary_type(int diary_type) {
        this.diary_type = diary_type;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getVideo_path_first() {
        return video_path_first;
    }

    public void setVideo_path_first(String video_path_first) {
        this.video_path_first = video_path_first;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
