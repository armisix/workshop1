package com.example.varut.workshop1.parser;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.varut.workshop1.model.JsonUploadResponse;
import com.google.gson.Gson;
import com.volley.superscores.parser.DataParser;

import java.util.Map;

/**
 * Created by Varut on 05/07/2015.
 */
public class ImageParser extends DataParser<JsonUploadResponse>{

    @Override
    public JsonUploadResponse parse(@NonNull Context appContext, @NonNull String response,
                                    int httpStatus, @NonNull Map headers) {

        Gson gson = new Gson();
        return gson.fromJson(response, JsonUploadResponse.class);
    }
}
