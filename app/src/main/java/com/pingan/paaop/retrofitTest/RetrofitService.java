package com.pingan.paaop.retrofitTest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by yejy on 2017/6/2.
 */

public interface RetrofitService {

    @POST("post")
    Call<ResponseBody> getHello();
}
