package com.hongsup.explog.view.cover.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hongsup.explog.data.post.PostCover;
import com.hongsup.explog.data.post.source.PostRepository;
import com.hongsup.explog.view.cover.contract.CoverContract;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Android Hong on 2017-12-14.
 */

public class CoverPresenter implements CoverContract.iPresenter {

    @NonNull
    private CoverContract.iView view;
    @NonNull
    private PostRepository repository;

    public CoverPresenter() {
        repository = PostRepository.getInstance();
    }

    @Override
    public void attachView(CoverContract.iView view) {
        this.view = view;
    }

    @Override
    public void createCover(PostCover cover) {
        view.showProgress();
        Observable<Response<PostCover>> observable = repository.createPostCover(cover);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    Log.e(TAG, "createCover: " + data.code() +", " + data.message() );
                    if(data.isSuccessful()){
                        view.hideProgress();
                        view.goToPost(data.body());
                    }else{
                        view.hideProgress();
                        view.showError("error 1");
                    }
                }, throwable -> {
                    view.hideProgress();
                    view.showError("error 2" + throwable.getMessage());
                    Log.e(TAG, "createCover: " + throwable.getMessage());
                });
    }
}
