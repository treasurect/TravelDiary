package com.treasure.traveldiary.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/1.
 */

public class ToolsWeatherFutureBean implements Serializable {
    private String date;
    private String weather;
    private String temp_range;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemp_range() {
        return temp_range;
    }

    public void setTemp_range(String temp_range) {
        this.temp_range = temp_range;
    }
}
