package com.hongsup.explog.service;

import com.hongsup.explog.data.user.source.UserRepository;

import java.io.IOException;
import java.net.ProtocolException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Android Hong on 2017-12-14.
 */
public class AddTokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response;

        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Authorization", "Token " + UserRepository.getInstance().getUser().getToken())
                .build();

        try {

            response = chain.proceed(request);

        } catch (ProtocolException e) {

            /**
             *  Code : 204 일 경우에는
             *  Response 를 새로 생성하여 클라이언트에 제공
             *
             *  ProtocolException : Content-Length 가 0 보다 크기 때문에 Error 가 발생하는 듯...
             */
            response = new Response.Builder()
                    .request(chain.request())
                    .code(204)
                    .protocol(Protocol.HTTP_1_1)
                    .addHeader("content-type","application/json")
                    .body(ResponseBody.create(MediaType.parse("text/pain"),""))
                    .message("")
                    .build();
        }
        return response;
    }
}