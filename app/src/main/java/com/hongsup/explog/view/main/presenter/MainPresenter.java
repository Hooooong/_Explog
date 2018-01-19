package com.hongsup.explog.view.main.presenter;

import com.hongsup.explog.view.main.contract.MainContract;

import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Android Hong on 2017-11-30.
 */

public class MainPresenter implements MainContract.iPresenter {

    private static final String TAG = "MainPresenter";

    MainContract.iView view;

    Subject<Long> subject = BehaviorSubject.createDefault(0L).toSerialized();

    @Override
    public void attachView(MainContract.iView view) {
        this.view = view;
    }

    /*

    <참고>
    https://medium.com/rainist-engineering/handling-back-button-with-rxjava-d948d8d3db80

     */
    @Override
    public void backPressedDisposable(long time) {
        subject.onNext(time);

        subject.toFlowable(BackpressureStrategy.BUFFER)
                .observeOn(AndroidSchedulers.mainThread())
                .buffer(2,1)
                .subscribe(/*longs -> {

                    Log.e(TAG, "backPressedDisposable: " + longs.size() );
                    Log.e(TAG, "backPressedDisposable: " + longs.get(0)  + ", " + longs.get(1) );
                    *//*if(longs.get(1) - longs.get(0) < 2000){

                    }else{

                    }*//*
                }*/

                );

    }

}
