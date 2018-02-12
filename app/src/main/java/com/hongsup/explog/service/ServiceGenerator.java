package com.hongsup.explog.service;

/**
 * Created by Android Hong on 2017-11-29.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hongsup.explog.service.okhttp.AddTokenInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.hongsup.explog.data.Const.SERVER_URL;

/**
 * Created by Android Hong on 2017-11-16.
 */

public class ServiceGenerator {

    private static final String TAG = "ServiceGenerator";

    /**
     * Retrofit2 생성
     *
     * @param className
     * @param <I>
     * @return
     */
    public static <I> I create(Class<I> className) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(className);
    }

    public static<I> I createInterceptor(Class<I> className){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                /*
                 Interceptor 추가
                */
                .client(new OkHttpClient.Builder().addInterceptor(new AddTokenInterceptor()).build())
                .build();
        return retrofit.create(className);
    }

    /**
     * Retrofit2 생성
     *
     * @param className
     * @param gson : Custom Gson 객체
     * @param <I>
     * @return
     */
    public static <I> I create(Class<I> className, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(className);
    }

    /**
     * Retrofit2 생성
     *
     * @param className
     * @param nullCheck : Null 로 보낼 것인지 Check 하는 매개변수
     * @param <I>
     * @return
     */
    public static <I> I create(Class<I> className, boolean nullCheck) {
        Retrofit retrofit;
        Gson gson;

        if(nullCheck){
            gson = new GsonBuilder()
                    /*
                     excludeFieldsWithoutExposeAnnotation() : Annotation 속성을 설정한 것을 적용하는 메소드
                     */
                    //.excludeFieldsWithoutExposeAnnotation()
                    /*
                     serializeNulls() : 속성이 null 일 경우 "속성명" : null 로 보내는 메소드
                     */
                    .serializeNulls()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        }else{
            gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(className);
    }



}