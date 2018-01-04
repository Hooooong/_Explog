package com.hongsup.explog.data.post;

import com.google.gson.annotations.SerializedName;
import com.hongsup.explog.data.user.User;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Android Hong on 2017-12-12.
 */

public class Content implements Serializable{
    @SerializedName("pk")
    private int pk;
    @SerializedName("content")
    private String content;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("type")
    private String type;
    @SerializedName("photo")
    private String photoPath;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;

    private int likeCount;
    private int[] liked;
    private User author;

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int[] getLiked() {
        return liked;
    }

    public void setLiked(int[] liked) {
        this.liked = liked;
    }

    @Override
    public String toString() {
        return "Content{" +
                "pk=" + pk +
                ", content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", photoPath='" + photoPath + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", likeCount=" + likeCount +
                ", liked=" + Arrays.toString(liked) +
                ", author=" + author +
                '}';
    }
}
