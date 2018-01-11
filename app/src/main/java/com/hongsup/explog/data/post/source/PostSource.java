package com.hongsup.explog.data.post.source;

import com.hongsup.explog.data.post.PostContent;
import com.hongsup.explog.data.post.PostContentResult;
import com.hongsup.explog.data.post.PostCover;
import com.hongsup.explog.data.post.PostResult;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Android Hong on 2017-12-13.
 */

public interface PostSource {

    /**
     * 대륙별로 Post 가져오는 메소드
     * @param category
     * @return
     */
    Observable<Response<PostResult>> getPostList(int category);

    /**
     * Upload Cover 메소드
     * @param cover
     * @return
     */
    Observable<Response<PostCover>> uploadPostCover(PostCover cover);

    /**
     * Post 에 대한 내용 가져오는 메소드
     *
     * @param postPk
     * @return
     */
    Observable<Response<PostContentResult>> getPostContentList(int postPk);

    /**
     * Post 에 대한 Text 추가하는 메소드
     *
     * @param postPk
     * @param text : 내용
     * @param date : 입력 날짜
     * @return
     */
    Observable<Response<PostContent>> uploadPostText(int postPk, String text, String date);

    /**
     * Post 에 대한 위치 추가하는 메소드
     *
     * @param postPk
     * @param lat : 위도
     * @param lng : 경도
     * @return
     */
    Observable<Response<PostContent>> uploadPostPath(int postPk, double lat, double lng);

    /**
     * Post 에 대한 Photo 추가하는 메소드
     *
     * @param postPk
     * @param photoPath : 이미지 경로
     * @return
     */
    Observable<Response<PostContent>> uploadPostPhoto(int postPk, String photoPath);

    /**
     * Post 에 대한 like 설정
     *
     * @param postPk
     * @return
     */
    Observable<Response<PostCover>> setPostLike(int postPk);

    /**
     * Post 를 삭제하는 메소드
     *
     * @param postPk
     * @return
     */
    Observable<Response<Void>> deletePost(int postPk);
}
