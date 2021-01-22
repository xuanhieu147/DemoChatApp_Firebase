package com.example.demochatapp.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA4C8RYYg:APA91bGnoNWaZhI1TUfMZ6h0BFXjkrBToCCk-VEo1boDqH-Ea8lloEBkQFUTT18HINqYEvt7surMBjwy2dDl1YzTQrB-4cjU2RC82rOiImY90a2i6Ijg_9JZxzYeE46vRqBxIdgwAvFX"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
