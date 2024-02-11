package com.example.clara.contask.interfaces.network;
import com.example.clara.contask.chat.NotificationChat;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
public interface NotificationService {
    @POST("send")


    Call<NotificationChat> createPost(@Body NotificationChat dataModal,@Header("Authorization") String token);

}



