package com.treasure.traveldiary.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by treasure on 2017/8/18.
 */

public class SceneryBean extends BmobObject{
    private String scenery_name;
    private String scenery_city;
    private List<String> scenery_image;
    private String scenery_desc;
    private String scenery_level;
    private String scenery_open_time;
    private String scenery_addr;
    private String scenery_way;
    private List<LeaveMesBean> scenery_comments;

    public String getScenery_name() {
        return scenery_name;
    }

    public void setScenery_name(String scenery_name) {
        this.scenery_name = scenery_name;
    }

    public String getScenery_city() {
        return scenery_city;
    }

    public void setScenery_city(String scenery_city) {
        this.scenery_city = scenery_city;
    }

    public List<String> getScenery_image() {
        return scenery_image;
    }

    public void setScenery_image(List<String> scenery_image) {
        this.scenery_image = scenery_image;
    }

    public String getScenery_desc() {
        return scenery_desc;
    }

    public void setScenery_desc(String scenery_desc) {
        this.scenery_desc = scenery_desc;
    }

    public String getScenery_level() {
        return scenery_level;
    }

    public void setScenery_level(String scenery_level) {
        this.scenery_level = scenery_level;
    }

    public String getScenery_open_time() {
        return scenery_open_time;
    }

    public void setScenery_open_time(String scenery_open_time) {
        this.scenery_open_time = scenery_open_time;
    }

    public String getScenery_addr() {
        return scenery_addr;
    }

    public void setScenery_addr(String scenery_addr) {
        this.scenery_addr = scenery_addr;
    }

    public List<LeaveMesBean> getScenery_comments() {
        return scenery_comments;
    }

    public void setScenery_comments(List<LeaveMesBean> scenery_comments) {
        this.scenery_comments = scenery_comments;
    }

    public String getScenery_way() {
        return scenery_way;
    }

    public void setScenery_way(String scenery_way) {
        this.scenery_way = scenery_way;
    }
}
