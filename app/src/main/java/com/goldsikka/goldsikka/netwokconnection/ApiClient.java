package com.goldsikka.goldsikka.netwokconnection;


import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    private static Retrofit retrofit = null;

    public static Retrofit getClient(final String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.writeTimeout(15, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token)
//                        .addHeader("Host","<calculated when request is sent>")
//                        .addHeader("User-Agent","PostmanRuntime/7.26.5")
//                        .addHeader("Accept","*/*")
//                        .addHeader("Accept-Encoding","gzip, deflate, br")
//                        .addHeader("Connection","keep-alive")
                        .build();
                return chain.proceed(request);
            }
        });

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    //https://staging-api.dev.goldsikka.in/
                    //http://develop-api.goldsikka.com/

                    /////liveurl///Production
                    // "https://api.goldsikka.com/"

                    .baseUrl("https://api.goldsikka.com/")
                    //  .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}