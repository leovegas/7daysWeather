package com.leo.weather7days.jsonclasses;

import java.io.Serializable;

public class Sun implements Serializable {
    public String sunrise;
    public String sunset;

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}
