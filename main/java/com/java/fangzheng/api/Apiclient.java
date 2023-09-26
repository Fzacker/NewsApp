package com.java.fangzheng.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Apiclient {
    private static final String BASE_URL = "https://api2.newsminer.net/svc/news/";
    private static Apiclient apiclinet;
    private static Retrofit retrofit;

    private Apiclient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Apiclient getInstance() {
        if (apiclinet == null) {
            apiclinet = new Apiclient();
        }
        return apiclinet;
    }
    public ApiInterface getApi(){
        return retrofit.create(ApiInterface.class);
    }
}
