package com.hongsup.explog.view.myinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongsup.explog.R;
import com.hongsup.explog.data.user.source.UserRepository;
import com.hongsup.explog.service.ServiceGenerator;
import com.hongsup.explog.service.api.EditProfileAPI;
import com.hongsup.explog.view.myinfo.adapter.ViewPagerAdapter;
import com.hongsup.explog.view.setting.SettingActivity;
import com.hongsup.explog.view.setting.editprofile.insuptest.UserInformation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by 정인섭 on 2017-12-06.
 */

public class MyInfoLayout extends FrameLayout {

    @BindView(R.id.myinfo_viewPager)
    ViewPager myinfo_viewPager;
    ViewPagerAdapter viewPagerAdapter;
    @BindView(R.id.tabLayoutMyInfo)
    TabLayout tabLayoutMyinfo;
    @BindView(R.id.imgSetting)
    ImageView imgSetting;
    @BindView(R.id.textUserNameMyInfo)
    TextView textUserNameMyInfo;
    @BindView(R.id.textEmailMyInfo)
    TextView textEmailMyInfo;
    @BindView(R.id.imgProfile)
    ImageView imgProfile;

    ArrayList<List> list;

    public MyInfoLayout(Context context) {
        super(context);

        init();
    }


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_myinfo, this, false);
        ButterKnife.bind(this, view);
        list = new ArrayList<>();
        setProfileFromUserRepository();
        getDataFromDB();


        setListener();

        addView(view);
    }

    private void setListener() {
        tabLayoutMyinfo.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(myinfo_viewPager));
        //tabLayoutClone.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutClone));
        myinfo_viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutMyinfo));
        imgSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    public void setProfileFromUserRepository(){
        UserRepository userRepository = UserRepository.getInstance();
        if(userRepository.getUser()!=null) {
            ((Activity)getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Glide.with(getContext()).load(userRepository.getUser().getImg_profile()).centerCrop().into(imgProfile);
//                    textUserNameMyInfo.setText(userRepository.getUser().getUsername());
//                    textEmailMyInfo.setText(userRepository.getUser().getEmail());
                }
            });

        }else{
            textUserNameMyInfo.setText("로그인 하쇼");
            textEmailMyInfo.setText("로그인 하쇼");
        }
    }

    public void getDataFromDB() {

        EditProfileAPI profileEditAPI = ServiceGenerator.createInterceptor(EditProfileAPI.class);
        Observable<Response<UserInformation>> getUserInfo = profileEditAPI.getUserInfo();
        getUserInfo.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    if (data.isSuccessful()) {
                        if (data.code() == 200) {
                            Log.d("MainActivity", "확인됨");

                            Glide.with(getContext())
                                    .load(data.body().getImg_profile()).centerCrop().into(imgProfile);

                            textUserNameMyInfo.setText(data.body().getUsername());
                            textEmailMyInfo.setText(data.body().getEmail());
                            list.add(data.body().getPosts());
                            list.add(data.body().getLiked_posts());
                            viewPagerAdapter = new ViewPagerAdapter(list);
                            myinfo_viewPager.setAdapter(viewPagerAdapter);

                        } else {
                            Log.d("EditProfileActivity", "확인안됨");
                        }
                    } else {
                        Log.d("EditProfileActivity", data.errorBody().string() + "data unsuccessful");
                    }
                }, throwable -> {
                    Log.e("SearchView", throwable.getMessage());
                });

    }


}
