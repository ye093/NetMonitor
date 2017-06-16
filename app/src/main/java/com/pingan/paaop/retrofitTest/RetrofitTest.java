package com.pingan.paaop.retrofitTest;

import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by yejy on 2017/6/1.
 */

public class RetrofitTest {
    private static final String TAG = RetrofitTest.class.getSimpleName();
    Retrofit retrofit;


    public RetrofitTest(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl(url).addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    public void startTest() {
        Log.d(TAG, "startTest...");
        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<ResponseBody> result = service.getHello();
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d(TAG, "startTest...Result = " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "startTest...Error = " + t.getMessage().toString());
            }
        });
    }



}
