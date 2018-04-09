package com.enjoywater.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by TONG HAI on 3/19/2018.
 */

public interface MainService {
    //1.1
    @GET(ApiConstant.Url.CHECK_EMAIL)
    Call<BaseResponse> checkEmail(@Query(ApiConstant.Param.EMAIL) String email);

    //1.2
    @GET(ApiConstant.Url.CHECK_PHONE)
    Call<BaseResponse> checkPhone(@Query(ApiConstant.Param.PHONE) String phone);

    //1.3
    @FormUrlEncoded
    @POST(ApiConstant.Url.REGISTER)
    Call<BaseResponse> registerUser(@Field(ApiConstant.Param.EMAIL) String email,
                                    @Field(ApiConstant.Param.PHONE) String phone,
                                    @Field(ApiConstant.Param.FULLNAME) String fullName,
                                    @Field(ApiConstant.Param.PASSWORD) String passWord);

    //1.4
    @FormUrlEncoded
    @POST(ApiConstant.Url.LOGIN)
    Call<BaseResponse> login(@Field(ApiConstant.Param.EMAIL) String email,
                             @Field(ApiConstant.Param.PASSWORD) String passWord);

    //1.5
    @FormUrlEncoded
    @POST(ApiConstant.Url.SEND_DIVICE_TOKEN)
    Call<BaseResponse> sendDeviceToken(@Field(ApiConstant.Param.TOKEN) String token,
                                       @Field(ApiConstant.Param.DEVICE_TOKEN) String device_token,
                                       @Field(ApiConstant.Param.TYPE) int type);

    //2.1
    @GET(ApiConstant.Url.GET_TOP_SALE)
    Call<BaseResponse> getTopSale(@Query(ApiConstant.Param.TOKEN) String token);

    //2.2
    @GET(ApiConstant.Url.GET_NEAREST_SALE)
    Call<BaseResponse> getNearestSale(@Query(ApiConstant.Param.TOKEN) String token,
                                       @Query(ApiConstant.Param.LATITUDE) String latitude,
                                       @Query(ApiConstant.Param.LONGITUDE) String longitude);

    //3.1
    @GET(ApiConstant.Url.GET_NEWS)
    Call<BaseResponse> getNews(@Query(ApiConstant.Param.TOKEN) String token);

    //3.2
    @GET(ApiConstant.Url.GET_ALL_NEWS)
    Call<BaseResponse> getAllNews(@Query(ApiConstant.Param.TOKEN) String token);

    //3.3
    @GET(ApiConstant.Url.GET_DETAIL_NEWS)
    Call<BaseResponse> getNewsDetail(@Query(ApiConstant.Param.TOKEN) String token, @Query(ApiConstant.Param.ID) int id);

    //Factory
    class Factory {
        public static MainService create() {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();
                    HttpUrl url = originalHttpUrl.newBuilder().build();
                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder().url(url);
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = httpClient
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstant.Url.BASE_DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            return retrofit.create(MainService.class);
        }
    }
}
