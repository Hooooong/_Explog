package com.hongsup.explog.data.post.source;

import com.hongsup.explog.data.post.PostContent;
import com.hongsup.explog.data.post.PostContentResult;
import com.hongsup.explog.data.post.PostCover;
import com.hongsup.explog.data.post.PostResult;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by Android Hong on 2017-12-13.
 */

public class PostRepository implements PostSource{

    private static PostRepository instance;

    private PostRemoteDataSource postRemoteDataSource;

    private PostRepository(){
        postRemoteDataSource = PostRemoteDataSource.getInstance();
    }

    public static PostRepository getInstance(){
        if(instance == null)
            instance = new PostRepository();
        return instance;
    }

    @Override
    public Observable<Response<PostResult>> getPostList(int category) {
        return postRemoteDataSource.getPostList(category);
    }

    @Override
    public Observable<Response<PostCover>> createPostCover(PostCover cover) {
        return postRemoteDataSource.createPostCover(cover);
    }

    @Override
    public Observable<Response<PostContentResult>> getPostContentList(int postPk) {
        return postRemoteDataSource.getPostContentList(postPk);
    }

    @Override
    public Observable<Response<PostContent>> uploadPostText(int postPk, String text, String type) {
        return postRemoteDataSource.uploadPostText(postPk, text, type);
    }

    @Override
    public Observable<Response<PostContent>> uploadPostPath(int postPk, double lat, double lng) {
        return postRemoteDataSource.uploadPostPath(postPk, lat, lng);
    }

    @Override
    public Observable<Response<PostContent>> uploadPostPhoto(int postPk, String photoPath) {
        return postRemoteDataSource.uploadPostPhoto(postPk, photoPath);
    }

    @Override
    public Observable<Response<PostCover>> setPostLike(int postPk) {
        return postRemoteDataSource.setPostLike(postPk);
    }

    @Override
    public Observable<Response<Void>> deletePost(int postPk) {
        return postRemoteDataSource.deletePost(postPk);
    }

}
