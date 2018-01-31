package com.hongsup.explog.view.main.presenter;

import com.hongsup.explog.view.main.contract.MainContract;

/**
 * Created by Android Hong on 2017-11-30.
 */

public class MainPresenter implements MainContract.iPresenter {

    private static final String TAG = "MainPresenter";

    MainContract.iView view;

    @Override
    public void attachView(MainContract.iView view) {
        this.view = view;
    }

    /*
    <참고>
    https://medium.com/rainist-engineering/handling-back-button-with-rxjava-d948d8d3db80
     */

}
