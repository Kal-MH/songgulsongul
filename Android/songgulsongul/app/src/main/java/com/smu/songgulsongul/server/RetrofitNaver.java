package com.smu.songgulsongul.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitNaver {
    private final static String BASE_URL = "https://openapi.naver.com/";
    private static Retrofit retrofit = null;

    private RetrofitNaver() {

    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 추가
                    .build();
        }

        return retrofit;
    }

    public static NaverApi create() {
        return getClient().create(NaverApi.class);
    }

}
