package com.mystique.android.duka.rest;

import com.mystique.android.duka.model.AccessToken;
import com.mystique.android.duka.model.STKPush;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface ApiInterface {

    @POST("mpesa/stkpush/v1/processrequest")
    Call<STKPush> sendPush(@Header("Authorization") String authorization, @Body STKPush stkPush);

    @GET("jobs/pending")
    Call<STKPush> getTasks();

    @GET("oauth/v1/generate?grant_type=client_credentials")
    Call<AccessToken> getAccessToken(@Header("Authorization") String authorization);
}
