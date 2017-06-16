package com.pingan.library.aspectjx.okhttp3;

import android.util.Log;


import com.pingan.library.common.NetMonitorEntity;
import com.pingan.library.util.NetMonitorTask;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by yejy on 17/5/17.
 */
@Aspect
public class OkHttp3Aspect {

    private static String TAG = OkHttp3Aspect.class.getSimpleName();

    private static ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<>();

    @After("execution(okhttp3.Request$Builder.new(okhttp3.Request))")
    public void builderConstructor(JoinPoint joinPoint) throws Throwable {
        Request.Builder afterBuilder = (Request.Builder) joinPoint.getThis();
        afterBuilder.addHeader("ye","1234567890");
        Log.i(TAG,"after::: --->"+afterBuilder.toString());
    }

    //
    @After("execution(* okhttp3.Callback.onFailure(..))")
    public void onFail(JoinPoint joinPoint){
        Log.i(TAG,"after::: --->afterHttpCallbackOnFail");
        Callback callback = (Callback) joinPoint.getThis();
        Object[] objs = joinPoint.getArgs();
        Call call = (Call)objs[0];
        call.request();

        Exception exception = (Exception)objs[1];
        Log.i(TAG,"onFail callback hashcode:--->"+callback.hashCode());

        NetMonitorEntity entity = (NetMonitorEntity) map.get(callback.hashCode() + "");
        if (entity != null) {
            entity.setErrorMsg(exception.getMessage());
            NetMonitorTask.execute(entity);
        }

    }

    @After("execution(* okhttp3.Callback.onResponse(..))")
    public void onResponse(JoinPoint joinPoint){
        Log.i(TAG,"after::: --->afterHttpCallbackonResponse");
        Object[] objs = joinPoint.getArgs();
        Call call = (Call)objs[0];

        Response response = (Response) objs[1];
        Log.i(TAG,response.toString());
        Callback callback = (Callback) joinPoint.getThis();
        Log.i(TAG,"onResponse callback hashcode:--->"+callback.hashCode());

        NetMonitorEntity entity = (NetMonitorEntity) map.get(callback.hashCode() + "");
        if (entity != null) {
            Request request = response.request();

            try {
                String bodyString = response.peekBody(response.body().contentLength()).string();
                Log.i(TAG,"bodyString ---> "+bodyString);
                entity.setBusinessContent(bodyString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            entity.setUrl(request.url().toString());
            entity.setMethod(request.method());
            entity.setRequestHeaders(request.headers().toString());

            entity.setCode(response.code() + "");
            entity.setResponseHeaders(response.headers().toString());
            entity.setFinishTimeTag(new Date().getTime() + "");
            NetMonitorTask.execute(entity);
        }

        if (map.contains(callback.hashCode() + "")) {
            map.remove(callback.hashCode() + "");
        }

    }

    //同步执行
    @Around("execution(* okhttp3.RealCall.execute())")
    public Object execute(ProceedingJoinPoint joinPoint){
        Log.i(TAG,"before execute");
        Call realCall = (Call)joinPoint.getThis();

        Request request = realCall.request();
        NetMonitorEntity entity = new NetMonitorEntity();
        entity.setUrl(request.url().toString());
        entity.setMethod(request.method());
        entity.setRequestHeaders(request.headers().toString());
        entity.setHostIpAddress(request.url().host());
        entity.setSendTimeTag(new Date().getTime() + "");

        //得到请求BODY
        Buffer sink = new Buffer();
        try {
            request.body().writeTo(sink);
            InputStream is = sink.inputStream();
            byte[] bytes = new byte[512];
            int len = 0;
            ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
            while ((len = is.read(bytes)) > 0 ) {
                byteBuffer.put(bytes, 0, len);
            }
            String requestBody = new String(byteBuffer.array()).trim();
            Log.i(TAG,"body length: "+ requestBody);
            entity.setParams(requestBody);

        } catch (IOException e) {
            e.printStackTrace();
        }


        Response response = null;
        try {
            response = (Response) joinPoint.proceed();
            entity.setResponseTimeTag(new Date().getTime() + "");
            entity.setCode(response.code() + "");
            entity.setResponseHeaders(response.headers().toString());
            //得到响应body
            try {
                String bodyString = response.peekBody(response.body().contentLength()).string();
                Log.i(TAG,"bodyString ---> "+bodyString);
                entity.setBusinessContent(bodyString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            entity.setFinishTimeTag(new Date().getTime() + "");
            NetMonitorTask.execute(entity);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        Log.i(TAG,"after execute");
        return response;
    }

    //异步请求
    @Before("execution(* okhttp3.RealCall.enqueue(..))")
    public void enqueue(JoinPoint joinPoint){
        Object[] objs = joinPoint.getArgs();
        if(objs!=null && objs.length>0){
            Callback callback = (Callback) objs[0];
            Log.i(TAG,"enqueue callback hashcode:"+callback.hashCode());

            //请求参数
            Call realCall = (Call) joinPoint.getTarget();
            Request request = realCall.request();
            NetMonitorEntity entity = new NetMonitorEntity();
            entity.setUrl(request.url().toString());
            entity.setMethod(request.method());
            entity.setRequestHeaders(request.headers().toString());
            entity.setHostIpAddress(request.url().host());
            entity.setSendTimeTag(new Date().getTime() + "");
            //得到请求BODY
            Buffer sink = new Buffer();
            try {
                request.body().writeTo(sink);
                InputStream is = sink.inputStream();
                byte[] bytes = new byte[512];
                int len = 0;
                ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
                while ((len = is.read(bytes)) > 0 ) {
                    byteBuffer.put(bytes, 0, len);
                }
                String requestBody = new String(byteBuffer.array()).trim();
                Log.i(TAG,"body length: "+ requestBody);
                entity.setParams(requestBody);

            } catch (IOException e) {
                e.printStackTrace();
            }

            map.put(callback.hashCode() + "", entity);

        }
    }

}
