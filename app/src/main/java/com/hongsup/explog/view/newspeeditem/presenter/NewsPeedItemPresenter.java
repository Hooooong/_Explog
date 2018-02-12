package com.hongsup.explog.view.newspeeditem.presenter;


import android.util.Log;

import com.hongsup.explog.data.post.PostCover;
import com.hongsup.explog.data.post.PostResult;
import com.hongsup.explog.data.post.source.PostRepository;
import com.hongsup.explog.view.newspeeditem.adapter.contract.NewsPeedItemAdapterContract;
import com.hongsup.explog.view.newspeeditem.contract.NewsPeedItemContract;
import com.hongsup.explog.view.newspeeditem.listener.OnCoverClickListener;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by Android Hong on 2017-12-13.
 */

public class NewsPeedItemPresenter implements NewsPeedItemContract.iPresenter, OnCoverClickListener {

    private static final String TAG = "NewsPeedItemPresenter";

    private NewsPeedItemContract.iView view;
    private PostRepository repository;

    private NewsPeedItemAdapterContract.iModel adapterModel;
    private NewsPeedItemAdapterContract.iView adapterView;

    public NewsPeedItemPresenter() {
        repository = PostRepository.getInstance();
    }

    @Override
    public void attachView(NewsPeedItemContract.iView view) {
        this.view = view;
    }

    @Override
    public void loadItem(int category) {
        Observable<Response<PostResult>> observable = repository.getPostList(category);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                            if (data.isSuccessful()) {
                                Log.e(TAG, "loadItem: 데이터 로드 완료");
                                adapterModel.addItems(data.body().getPostList());
                                adapterView.notifyAdapter();
                            } else {
                                view.showError("ERROR 발생");
                            }
                        },
                        throwable -> {
                            view.showError("ERROR 발생");
                        });
    }

    @Override
    public void setNewsPeedItemAdapterModel(NewsPeedItemAdapterContract.iModel model) {
        this.adapterModel = model;
    }

    @Override
    public void setNewsPeedItemAdapterView(NewsPeedItemAdapterContract.iView view) {
        this.adapterView = view;
        this.adapterView.setOnItemClickListener(this);
    }

    @Override
    public void onCoverClick(PostCover postCover) {
        view.goToPost(postCover);
    }
}
