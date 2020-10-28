package com.leo.weather7days.jsonclasses;

import java.io.Serializable;

public class Current implements Serializable {
    public String time;
    public String type;
    public double temperature;
    public double temperatureMin;
    public double temperatureMax;
    public double windSpeed;
    public double windGustsSpeed;
    public double windBearing;
    public double relHumidity;
    public double cloudCover;
    public double preasure;
    public double totalPrecipitation;
    public double rain;
    public double snow;
    public double uvIndex;
    public double airQualityIndex;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindGustsSpeed() {
        return windGustsSpeed;
    }

    public void setWindGustsSpeed(double windGustsSpeed) {
        this.windGustsSpeed = windGustsSpeed;
    }

    public double getWindBearing() {
        return windBearing;
    }

    public void setWindBearing(double windBearing) {
        this.windBearing = windBearing;
    }

    public double getRelHumidity() {
        return relHumidity;
    }

    public void setRelHumidity(double relHumidity) {
        this.relHumidity = relHumidity;
    }

    public double getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(double cloudCover) {
        this.cloudCover = cloudCover;
    }

    public double getPreasure() {
        return preasure;
    }

    public void setPreasure(double preasure) {
        this.preasure = preasure;
    }

    public double getTotalPrecipitation() {
        return totalPrecipitation;
    }

    public void setTotalPrecipitation(double totalPrecipitation) {
        this.totalPrecipitation = totalPrecipitation;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public double getSnow() {
        return snow;
    }

    public void setSnow(double snow) {
        this.snow = snow;
    }

    public double getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(double uvIndex) {
        this.uvIndex = uvIndex;
    }

    public double getAirQualityIndex() {
        return airQualityIndex;
    }

    public void setAirQualityIndex(double airQualityIndex) {
        this.airQualityIndex = airQualityIndex;
    }
}
