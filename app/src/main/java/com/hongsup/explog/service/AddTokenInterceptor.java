package com.hongsup.explog.service;

import com.hongsup.explog.data.user.source.UserRepository;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Android Hong on 2017-12-14.
 */
public class AddTokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Authorization", "Token "+UserRepository.getInstance().getUser().getToken())
                .build();
        return chain.proceed(request);
    }
}

