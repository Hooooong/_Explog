package com.hongsup.explog.view.search.contract;

import com.hongsup.explog.data.post.PostCover;
import com.hongsup.explog.view.search.adapter.contract.HistorySearchAdapterContract;
import com.hongsup.explog.view.search.adapter.contract.SearchResultAdapterContract;

/**
 * Created by Hong on 2017-12-20.
 */

public interface SearchContract {

    interface iView{
        void setPresenter(iPresenter presenter);

        void showProgress();
        void hideProgress();

        void setHistoryToSearchEditText(String word);

        void showNoData();
        void showSearchData();

        void goToPost(PostCover cover);
    }

    interface iPresenter{

        void attachView(iView view);
        void loadHistorySearchWord();
        void loadSearchResult(String word);

        void insertSearchWord(String word);
        void deleteSearchWord(String word);

        void setHistorySearchAdapterModel(HistorySearchAdapterContract.iModel model);
        void setHistorySearchAdapterView(HistorySearchAdapterContract.iView view);

        void setSearchResultAdapterModel(SearchResultAdapterContract.iModel model);
        void setSearchResultAdapterView(SearchResultAdapterContract.iView view);

    }
}
