package com.treasure.traveldiary.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by treasure on 2017/8/9.
 */

public class SUserBean extends BmobObject {
    private String leave_name;
    private String leave_nick;
    private String leave_icon;
    private String leave_time;
    private String leave_content;

    public String getLeave_name() {
        return leave_name;
    }

    public void setLeave_name(String leave_name) {
        this.leave_name = leave_name;
    }

    public String getLeave_nick() {
        return leave_nick;
    }

    public void setLeave_nick(String leave_nick) {
        this.leave_nick = leave_nick;
    }

    public String getLeave_icon() {
        return leave_icon;
    }

    public void setLeave_icon(String leave_icon) {
        this.leave_icon = leave_icon;
    }

    public String getLeave_time() {
        return leave_time;
    }

    public void setLeave_time(String leave_time) {
        this.leave_time = leave_time;
    }

    public String getLeave_content() {
        return leave_content;
    }

    public void setLeave_content(String leave_content) {
        this.leave_content = leave_content;
    }
}
