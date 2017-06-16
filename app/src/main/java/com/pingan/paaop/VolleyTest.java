package com.pingan.paaop;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by yejy on 2017/5/31.
 */

public class VolleyTest {
    private static final String TAG = VolleyTest.class.getSimpleName();
    private RequestQueue queue;

    public VolleyTest(Context context) {
        queue = Volley.newRequestQueue(context);
    }


    public void startTest(String url) {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d(TAG, response.toString());
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


}
