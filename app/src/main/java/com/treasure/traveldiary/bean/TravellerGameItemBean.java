package com.treasure.traveldiary.bean;

import java.io.Serializable;

/**
 * Created by treasure on 2017.06.19.
 */

public class TravellerGameItemBean implements Serializable{
    private int back;
    private String name;
    private String desc;

    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = back;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
