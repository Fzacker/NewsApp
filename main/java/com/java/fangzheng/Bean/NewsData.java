package com.java.fangzheng.Bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewsData implements Serializable {
    @SerializedName("image")
    private String image;
    @SerializedName("publishTime")
    private String publishTime;
    @SerializedName("title")
    private String title;
    @SerializedName("publisher")
    private String publisher;
    @SerializedName("content")
    private String content;
    @SerializedName("video")
    private String video;

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getType() {
        if(image.length()<=2) {
            return 2;
        }
        if(image.charAt(1)=='h') {
            return 1;
        } else {
            return 2;
        }
    }
}
