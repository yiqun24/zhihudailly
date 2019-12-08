package com.example.zhihudailly.Bean;

import java.util.List;

public class Item {
    public Item() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getImgurl() {
        return imgurl;
    }
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
    public String getHeadtitle() {
        return headtitle;
    }
    public void setHeadtitle(String headtitle) {
        this.headtitle = headtitle;
    }
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
//    public List<String> getImages() {
//        return images;
//    }
//    public void setImages(List<String> images) {
//        this.images = images;
//    }
//    private List<String> images;
     private String headtitle;
     private String hint;
     private String id;
     private String title;
     private String date;
     private String imgurl;
     private String url;

}

