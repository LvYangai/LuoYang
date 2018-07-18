package com.example.lyg_pro.listendangke.data;


import android.database.Observable;
import android.os.Message;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by LYG-Pro on 2018/5/8.
 */

public interface Api {
    //上传

    @Multipart
    @POST("/test2/uploads.php")
    Call<ResponseBody> updateImages2(@Part MultipartBody.Part[] parts);

    @Multipart
    @POST("/user_up.php")
    Call<ResponseBody> upPhotos(@Part("title") RequestBody title,
                                @Part("details") RequestBody details,
                                @Part("username") RequestBody username,
                                @Part MultipartBody.Part[] parts);
    //更改密码
    @FormUrlEncoded
    @POST("/change_p.php")
    Call<ResponseBody> setPassword(@Field("username") String user, @Field("password") String pass1,@Field("password_c") String pass2);
    //    change_pn.php  更改手机号
    @FormUrlEncoded
    @POST("/change_pn.php")
    Call<ResponseBody> setChagePhone(@Field("username") String user, @Field("password") String pass,@Field("phone") String phone);
    //登陆
    @FormUrlEncoded
    @POST("/login.php")
    Call<ResponseBody> getLoginInfo(@Field("username") String user, @Field("password") String pass);

    @FormUrlEncoded
    @POST("/regedit.php")
    Call<ResponseBody> getRegedit(@Field("username") String user, @Field("password") String pass,@Field("phone") String phone);

    @FormUrlEncoded
    @POST("/back_p.php")
    Call<ResponseBody> getForget(@Field("username") String user,@Field("phone") String phone);

    //上传
    @FormUrlEncoded
    @POST("/UploadFiles/user_photo")
    Call<ResponseBody> upPhoto(@Field("username") String user,@Field("phone") String phone);

    //评论
    @FormUrlEncoded
    @POST("/comment.php")
    Call<ResponseBody> upInput(@Field("channelid") String channelid,@Field("classid") String classid,@Field("infoid") String infoid,@Field("content") String content,@Field("username") String username);


    @POST("/news_c.php")
    Call<News> getNewsInfo();

    @POST("/news_t.php")
    Call<News> getNewsInfo2();

    @POST("/video.php")
    Call<Dangke> getDangkeInfo();

    @POST("/audio.php")
    Call<Music> getMusicInfo();

    @POST("/timer.php")
    Call<TimeMach> getTimerInfo();
}
