package com.example.cenidet.services;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.cenidet.channel.NotificationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingClient extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");
        if(title != null){
            if(title.equals("NUEVO MENSAJE")){
                int idNotifiaction  = Integer.parseInt(data.get("idNotification"));
                showNotificationMessage(title, body, idNotifiaction);
            }
            showNotification(title, body);
        }
    }

    private  void showNotification(String title, String body){
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotification(title, body);
        Random random = new Random();
        int n = random.nextInt(10000);
        notificationHelper.getManager().notify(n, builder.build());
    }

    private  void showNotificationMessage(String title, String body, int idNotificationChat){
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotification(title, body);
        notificationHelper.getManager().notify(idNotificationChat, builder.build());
    }
}
