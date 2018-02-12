package com.hongsup.explog.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.hongsup.explog.data.Const.SERVER_URL;

/**
 * Created by Android Hong on 2018-02-12.
 */

public class RetrofitClient {

    private static RetrofitClient retrofitClient = null;
    private static Retrofit retrofit;

    private RetrofitClient() {
    }

    public static RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }

        return retrofitClient;
    }

    public void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(
                        new OkHttpClient.Builder()
                                .addInterceptor(
                                        new HttpLoggingInterceptor()
                                                .setLevel(HttpLoggingInterceptor.Level.BODY)
                                )
                                .build()
                )
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void releaseRetrofit() {
        retrofit = null;
    }
}
