package com.mystique.android.duka.io;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mystique.android.duka.Constants;
import com.mystique.android.duka.MainApplication;
import com.mystique.android.duka.model.AccessToken;
import com.mystique.android.duka.model.STKPush;
import com.mystique.android.duka.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IO {
    private static String TAG = IO.class.getSimpleName();
    private static ApiInterface apiService = MainApplication.getInstance().getApiService();
    String keys = Constants.CONSMER_KEY + ":" + Constants.CONSMER_SECRET;
    private FirebaseAnalytics mFirebaseAnalytics;

    public void getAuthToken(final Activity activity, final STKPush stkPush) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
        final Bundle bundle = new Bundle();
        Call<AccessToken> call = apiService.getAccessToken("Basic " + Base64.encodeToString(keys.getBytes(), Base64.NO_WRAP));
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    performStkPush(activity, stkPush, response.body().getAccessToken());
                    bundle.putString(FirebaseAnalytics.Param.CHECKOUT_OPTION, "MPESA");
                    bundle.putString(FirebaseAnalytics.Param.CHECKOUT_STEP, "Purchase Initiated");
                } else {
                    Toast.makeText(activity, "Payment Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                bundle.putString(FirebaseAnalytics.Param.CHECKOUT_OPTION, "MPESA");
                bundle.putString(FirebaseAnalytics.Param.CHECKOUT_STEP, "Purchase Failed");
                Toast.makeText(activity, "Payment Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void performStkPush(final Activity activity, STKPush stkPush, String accessToken) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
        final Bundle bundle = new Bundle();

        Call<STKPush> call = apiService.sendPush("Bearer ".concat(accessToken), stkPush);
        call.enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(Call<STKPush> call, Response<STKPush> response) {
                if (response.isSuccessful()) {
                    bundle.putString(FirebaseAnalytics.Param.CHECKOUT_OPTION, "MPESA");
                    bundle.putString(FirebaseAnalytics.Param.CHECKOUT_STEP, "Purchase Complete");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


                    Toast.makeText(activity, "Enter Mpesa PIN when prompted", Toast.LENGTH_LONG).show();
                    activity.finish();
                } else {
                    bundle.putString(FirebaseAnalytics.Param.CHECKOUT_OPTION, "MPESA");
                    bundle.putString(FirebaseAnalytics.Param.CHECKOUT_STEP, "Purchase Failed");
                    Toast.makeText(activity, "Payment Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<STKPush> call, Throwable t) {
                bundle.putString(FirebaseAnalytics.Param.CHECKOUT_OPTION, "MPESA");
                bundle.putString(FirebaseAnalytics.Param.CHECKOUT_STEP, "Purchase Failed");

            }
        });
    }
}
