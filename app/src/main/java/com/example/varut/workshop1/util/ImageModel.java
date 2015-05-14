package com.example.varut.workshop1.util;

/**
 * Created by Varut on 04/23/2015.
 */
public class ImageModel {
    private String name;
    private String size;
    private String date;
    private String type;

    public ImageModel(String name, String size, String date, String type) {
        this.name = name;
        this.size = size;
        this.date = date;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
