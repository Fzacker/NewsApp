package com.java.fangzheng.api;

import com.java.fangzheng.Bean.NewsItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("queryNewsList")
    Call<NewsItem> getNewsItem(
            @Query("size") String size,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("words") String words,
            @Query("categories") String categories
    );
}
