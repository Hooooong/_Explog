package com.hongsup.explog.data.search.source;

import com.hongsup.explog.data.post.PostCover;
import com.hongsup.explog.data.post.PostResult;
import com.hongsup.explog.service.ServiceGenerator;
import com.hongsup.explog.service.api.SearchAPI;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by Hong on 2017-12-21.
 */

public class SearchRemoteDataSource implements SearchSouce.Remote{

    private static SearchRemoteDataSource instance;

    private SearchAPI searchAPI;

    private SearchRemoteDataSource() {
        searchAPI = ServiceGenerator.create(SearchAPI.class);
    }

    public static SearchRemoteDataSource getInstance() {
        if (instance == null)
            return instance = new SearchRemoteDataSource();
        return instance;
    }

    @Override
    public Observable<Response<PostResult>> loadSearchResult(String word) {
        return searchAPI.getSearchResult(word);
    }
}
