package com.hongsup.explog;

import android.app.Application;

import com.hongsup.explog.service.RetrofitClient;

/**
 * Created by Android Hong on 2018-02-12.
 */

public class ExplogApplication extends Application {

    /** onCreate()
     * 액티비티, 리시버, 서비스가 생성되기전 어플리케이션이 시작 중일때
     * Application onCreate() 메서드가 만들어 진다고 나와 있습니다.
     * by. Developer 사이트
     */

    @Override
    public void onCreate() {
        super.onCreate();

        // 각종 Instance 초기화
        RetrofitClient.getInstance().initRetrofit();
    }
}
