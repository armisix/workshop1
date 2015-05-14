package com.superscores.android.io.model.json;

import com.google.gson.annotations.SerializedName;

public class TimestampJsonModel {

    public static final long EMPTY = Long.MIN_VALUE;

    @SerializedName("milliSec")
    private long milliSec;

    public TimestampJsonModel(long milliSec) {
        this.milliSec = milliSec;
    }

    public long getMilliSec() {
        return milliSec;
    }
}