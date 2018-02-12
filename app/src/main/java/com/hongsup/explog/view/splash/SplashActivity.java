package com.hongsup.explog.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.hongsup.explog.data.sign.SignIn;
import com.hongsup.explog.util.PreferenceUtil;
import com.hongsup.explog.view.main.MainActivity;
import com.hongsup.explog.view.splash.contract.SplashContract;
import com.hongsup.explog.view.splash.presenter.SplashPresenter;
import com.hongsup.explog.view.splash.view.SplashView;

public class SplashActivity extends AppCompatActivity {
    SplashContract.iView splashView;
    SplashContract.iPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        splashView = new SplashView(this);
        splashPresenter = new SplashPresenter(this);
        splashPresenter.attachView(splashView);
        setContentView(splashView.getView());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 2초 뒤 실행
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 자동 로그인 처리
                if(!TextUtils.isEmpty(PreferenceUtil.getString(SplashActivity.this, "Email"))){
                    /**
                     * SharedPreference 에 회원정보가 있으면
                     * 자동 로그인 처리
                     */
                    SignIn sign = new SignIn();
                    sign.setEmail(PreferenceUtil.getString(SplashActivity.this, "Email"));
                    sign.setPassword(PreferenceUtil.getString(SplashActivity.this, "password"));

                    splashPresenter.getSignIn(sign);
                }else{
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}
