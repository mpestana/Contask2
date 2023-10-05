package com.example.clara.contask;

import com.example.clara.contask.chat.NotificationChat;
import com.example.clara.contask.interfaces.Keys;
import com.example.clara.contask.interfaces.network.NotificationService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FunctionsUtil {

    public static String getFirstAndLastName(String name) {
        String[] names = name.split(" ");
        if (names.length < 2) {
            return name;
        } else
            return names[0] + " " + names[1];
    }

    public static void sendNotificationChat(NotificationChat notification) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/fcm/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NotificationService notificationService = retrofit.create(NotificationService.class);

        Call<NotificationChat> call = notificationService.createPost(notification, Keys.KEY_FCM);
        call.enqueue(new Callback<NotificationChat>() {
            @Override
            public void onResponse(Call<NotificationChat> call, Response<NotificationChat> response) {


            }

            @Override
            public void onFailure(Call<NotificationChat> call, Throwable t) {

            }
        });
    }
}
