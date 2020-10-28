package com.leo.weather7days.jsonclasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data implements Serializable {

    public String timezone;
    public Current current;
    public List<Daily> daily;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public List<Daily> getDailyArrayList() {
        return daily;
    }

    public void setDailyArrayList(List<Daily> dailyArrayList) {
        this.daily = daily;
    }
}
