package com.pingan.paaop;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by yejy on 2017/5/31.
 */

public class OkHttp2Test {
    private static final String TAG = OkHttp2Test.class.getSimpleName();
    private OkHttpClient client = new OkHttpClient();
    private String url;
    public OkHttp2Test(String url) {
        this.url = url;
    }
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private String execute(String url) throws IOException {
        RequestBody body = RequestBody.create(JSON, "{a:1}");
        Request request = new Request.Builder()
                .url(url).post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private void request(String url) throws IOException {
        RequestBody body = RequestBody.create(JSON, "{a:1}");
        Request request = new Request.Builder()
                .url(url).post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i(TAG,"onFailure->" + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.i(TAG,"onResponse->" + response.toString());
                Log.i(TAG,"onResponse body->" + response.body().string());
            }
        });
    }

    public void startTest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    request(url);
//                    request(url);
                    String result3 = execute(url);
                    Log.d(TAG, "result3 = " + result3);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
