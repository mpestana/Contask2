package com.example.clara.contask;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.clara.contask.OpenChatActivity;
import com.example.clara.contask.model.Chat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Agosto, 02 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class FCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        final Map<String, String> data = remoteMessage.getData();

        if (data == null || data.get("chatId") == null) return;

        final Intent ii = new Intent(this, OpenChatActivity.class);


        ii.putExtra("chatId", data.get("chatId"));

        PendingIntent pIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, ii, PendingIntent.FLAG_IMMUTABLE);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        String notificationChannelId = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(notificationChannelId, "My Notifications",
                            NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), notificationChannelId);

        builder.setAutoCancel(true)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setGroup(data.get("chatId"))
                .setContentIntent(pIntent);

        notificationManager.notify(1, builder.build());
    }
}

















