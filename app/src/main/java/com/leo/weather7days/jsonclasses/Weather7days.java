package com.leo.weather7days.jsonclasses;

import java.io.Serializable;

public class Weather7days implements Serializable {

    public String error;
    public Data data;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
