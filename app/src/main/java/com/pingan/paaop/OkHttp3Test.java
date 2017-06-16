package com.pingan.paaop;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yejy on 2017/5/31.
 */

public class OkHttp3Test {
    private static final String TAG = OkHttp3Test.class.getSimpleName();
    private OkHttpClient client = new OkHttpClient();
    private String url;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public OkHttp3Test(String url) {
        this.url = url;
    }


    private void request(String url) throws IOException {
        RequestBody body = RequestBody.create(JSON, "{a:1}");

        Request request = new Request.Builder()
                .url(url).post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,response.toString());
            }
        });
    }

    private void execute(String url) throws IOException {
        RequestBody body = RequestBody.create(JSON, "{a:1}");
        Request request = new Request.Builder()
                .url(url).post(body)
                .build();
        Response response = client.newCall(request).execute();
        Log.i(TAG,response.toString());
    }

    public void startTest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    request(url);
                    request(url);
                    request(url);
//                    execute(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
