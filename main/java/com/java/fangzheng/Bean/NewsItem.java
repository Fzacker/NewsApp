package com.java.fangzheng.Bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NewsItem implements Serializable {
    @SerializedName("pageSize")
    private String pageSize;

    @SerializedName("total")
    private String total;

    @SerializedName("data")
    private List<NewsData> data;

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<NewsData> getData() {
        return data;
    }

    public void setData(List<NewsData> data) {
        this.data = data;
    }


}
