package com.treasure.traveldiary.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by treasure on 2017/8/8.
 */

public class PushBean extends BmobObject{
    private String title;
    private String message;
    private String time;
    private String user_name;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
