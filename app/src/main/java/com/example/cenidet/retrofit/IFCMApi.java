package com.example.cenidet.retrofit;

import com.example.cenidet.models.FCMBody;
import com.example.cenidet.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA3ilJdQo:APA91bESChwQqQkkFlZazozaBWEWirX0M_lvaos4S7qoaD--ZrbdAlAKzOzUNHAucqtqCVvp4Jyy1YMVT7HmmQQHr_FcBC-Gjt18GkxQ6hFtT98SZBxwyLKHEsyu_yVZanzo5XaPdDWH"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);
}
