package com.hongsup.explog.view.cover.contract;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hongsup.explog.data.post.PostCover;

/**
 * Created by Android Hong on 2017-12-14.
 */

public interface CoverContract {

    interface iView{
        View getView();
        void setPresenter(iPresenter presenter);
        void showProgress();
        void hideProgress();

        void showError(String text);

        void setMenu(Menu menu);
        void onMenuClick(MenuItem item);
        void setCover(String imagePath);

        void goToPost(PostCover postCover);
    }
    interface iPresenter{
        void attachView(iView view);
        void createCover(PostCover cover);
    }
}
