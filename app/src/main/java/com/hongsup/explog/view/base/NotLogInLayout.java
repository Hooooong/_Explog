package com.hongsup.explog.view.base;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hongsup.explog.R;
import com.hongsup.explog.view.signin.SignInActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 정인섭 on 2017-12-19.
 */

public class NotLogInLayout extends FrameLayout {

    @BindView(R.id.btnEmailMyInfo)
    Button btnEmailMyInfo;

    public NotLogInLayout(@NonNull Context context) {
        super(context);
        init();
    }

    private void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_notlogin, this, false);
        ButterKnife.bind(this, view);
        addView(view);
    }

    @OnClick(R.id.btnEmailMyInfo)
    public void btnClick(){
        Intent intent = new Intent(getContext(), SignInActivity.class);
        getContext().startActivity(intent);
    }
}
