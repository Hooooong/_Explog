package com.hongsup.explog.view.search.presenter;

import android.content.Context;
import android.util.Log;

import com.hongsup.explog.data.post.PostCover;
import com.hongsup.explog.data.post.PostResult;
import com.hongsup.explog.data.search.source.SearchRepository;
import com.hongsup.explog.view.newspeeditem.listener.OnCoverClickListener;
import com.hongsup.explog.view.search.adapter.contract.HistorySearchAdapterContract;
import com.hongsup.explog.view.search.adapter.contract.SearchResultAdapterContract;
import com.hongsup.explog.view.search.contract.SearchContract;
import com.hongsup.explog.view.search.listener.OnWordClickListener;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by Hong on 2017-12-20.
 */

public class SearchPresenter implements SearchContract.iPresenter, OnCoverClickListener, OnWordClickListener {

    private static final String TAG = "SearchPresenter";

    private SearchRepository repository;
    private SearchContract.iView view;

    private SearchResultAdapterContract.iModel searchResultModel;
    private SearchResultAdapterContract.iView searchResultView;

    private HistorySearchAdapterContract.iModel historySearchModel;
    private HistorySearchAdapterContract.iView historySearchView;

    public SearchPresenter(Context context) {
        this.repository = SearchRepository.getInstance(context);
    }

    @Override
    public void attachView(SearchContract.iView view) {
        this.view = view;
    }

    @Override
    public void loadHistorySearchWord() {
        // Local Data 불러오는 곳
        historySearchModel.addItems(repository.loadRecentSearchWord());
        historySearchView.notifyAdapter();
    }

    @Override
    public void loadSearchResult(String word) {
        view.showProgress();
        // Remote Data 불러오는 곳
        Observable<Response<PostResult>> observable = repository.loadSearchResult(word);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    if (data.isSuccessful()) {
                        view.hideProgress();
                        if (data.body().getCount() == 0) {
                            view.showNoData();
                        }else{
                            searchResultModel.addItems(data.body().getPostList());
                            searchResultView.notifyAdapter();
                            view.showSearchData();
                        }
                    } else {
                        view.hideProgress();
                        Log.e("SearchView", " Error");
                    }
                }, throwable -> {
                    view.hideProgress();
                    Log.e("SearchView", throwable.getMessage());
                });
    }

    @Override
    public void insertSearchWord(String word) {
        repository.insertSearchWord(word);
    }

    @Override
    public void deleteSearchWord(String word) {
        repository.deleteSearchWord(word);
    }

    @Override
    public void setHistorySearchAdapterModel(HistorySearchAdapterContract.iModel model) {
        this.historySearchModel = model;
    }

    @Override
    public void setHistorySearchAdapterView(HistorySearchAdapterContract.iView view) {
        this.historySearchView = view;
        this.historySearchView.setOnWordClickListener(this);
    }

    @Override
    public void setSearchResultAdapterModel(SearchResultAdapterContract.iModel model) {
        this.searchResultModel = model;
    }

    @Override
    public void setSearchResultAdapterView(SearchResultAdapterContract.iView view) {
        this.searchResultView = view;
        this.searchResultView.setOnCoverClickListener(this);
    }

    @Override
    public void onCoverClick(PostCover postCover) {
        view.goToPost(postCover);
    }

    @Override
    public void onDeleteClick(String word) {
        this.deleteSearchWord(word);
        this.loadHistorySearchWord();
    }

    @Override
    public void onWordClick(String word) {
        view.setHistoryToSearchEditText(word);
    }
}
