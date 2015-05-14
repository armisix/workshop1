package com.example.varut.workshop1.model;

import java.util.List;

/**
 * Created by Varut on 05/07/2015.
 */
public class JsonUploadResponse {

    List<JsonImages> data;//

    public List<JsonImages> getData() {
        return data;
    }

    public void setData(List<JsonImages> data) {
        this.data = data;
    }
}
