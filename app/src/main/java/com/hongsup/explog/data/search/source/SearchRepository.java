package com.hongsup.explog.data.search.source;

import android.content.Context;

import com.hongsup.explog.data.post.PostResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by Hong on 2017-12-21.
 */

public class SearchRepository implements SearchSouce.Local, SearchSouce.Remote {

    private static SearchRepository instance;

    private SearchLocalDataSource searchLocalDataSource;
    private SearchRemoteDataSource searchRemoteDataSource;

    private SearchRepository(Context context) {
        searchLocalDataSource = SearchLocalDataSource.getInstance(context);
        searchRemoteDataSource = SearchRemoteDataSource.getInstance();
    }

    public static SearchRepository getInstance(Context context) {
        if (instance == null)
            instance = new SearchRepository(context);
        return instance;
    }

    @Override
    public List<String> loadRecentSearchWord() {
        return searchLocalDataSource.loadRecentSearchWord();
    }

    @Override
    public void insertSearchWord(String word) {
        searchLocalDataSource.insertSearchWord(word);
    }

    @Override
    public void deleteSearchWord(String word) {
        searchLocalDataSource.deleteSearchWord(word);
    }

    @Override
    public Observable<Response<PostResult>> loadSearchResult(String word) {
        return searchRemoteDataSource.loadSearchResult(word);
    }
}
