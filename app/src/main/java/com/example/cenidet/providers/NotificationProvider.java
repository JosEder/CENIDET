package com.example.cenidet.providers;

import com.example.cenidet.models.FCMBody;
import com.example.cenidet.models.FCMResponse;
import com.example.cenidet.retrofit.IFCMApi;
import com.example.cenidet.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";

    public NotificationProvider(){

    }

    public Call<FCMResponse> sendNotification(FCMBody body){
        return RetrofitClient.getClient(url).create(IFCMApi.class).send(body);
    }
}
