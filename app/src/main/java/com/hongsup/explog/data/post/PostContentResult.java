package com.hongsup.explog.data.post;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android Hong on 2017-12-14.
 */

public class PostContentResult {

    @SerializedName("post_content")
    List<PostContent> postContentList;

    public List<PostContent> getPostContentList() {
        return postContentList;
    }

    public void setPostContentList(List<PostContent> postContentList) {
        this.postContentList = postContentList;
    }

    @Override
    public String toString() {
        return "PostContentResult{" +
                "postContentList=" + postContentList +
                '}';
    }
}
