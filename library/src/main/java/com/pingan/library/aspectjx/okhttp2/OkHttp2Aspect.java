package com.pingan.library.aspectjx.okhttp2;

import android.util.Log;

import com.pingan.library.common.NetMonitorEntity;
import com.pingan.library.util.NetMonitorTask;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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

import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by yejy on 17/5/17.
 */
@Aspect
public class OkHttp2Aspect {

    private static String TAG = OkHttp2Aspect.class.getSimpleName();
    private static ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();


    @After("execution(com.squareup.okhttp.Request$Builder.new(com.squareup.okhttp.Request))")
    public void builderConstructor(JoinPoint joinPoint) throws Throwable {
        Request.Builder afterBuilder = (Request.Builder) joinPoint.getThis();
        afterBuilder.addHeader("ye", "ye=" + new Date());
        Log.i(TAG, "builderConstructor after::: --->" + afterBuilder.toString());
    }

    @After("execution(* com.squareup.okhttp.Callback.onFailure(..))")
    public void onFail(JoinPoint joinPoint) {
        Log.i(TAG, "after::: --->afterHttpCallbackOnFail");
        Callback callback = (Callback) joinPoint.getThis();
        Object[] objs = joinPoint.getArgs();
        Request request = (Request) objs[0];
        NetMonitorEntity entity = (NetMonitorEntity) map.get(callback.hashCode() + "");
        entity.setUrl(request.url().toString());
        entity.setMethod(request.method());
        entity.setRequestHeaders(request.headers().toString());
        entity.setHostIpAddress(request.url().getHost());
        entity.setFinishTimeTag(new Date().getTime() + "");


        Exception exception = (Exception) objs[1];
        entity.setErrorMsg(exception.getMessage());
        NetMonitorTask.execute(entity);
        Log.i(TAG, "onFail callback hashcode:--->" + callback.hashCode());

    }

    @Around("execution(* com.squareup.okhttp.Callback.onResponse(..))")
    public Object onResponse(ProceedingJoinPoint joinPoint) {
        Log.i(TAG, "after::: --->afterHttpCallbackonResponse");
        Object[] objs = joinPoint.getArgs();
        Response response = (Response) objs[0];
        Log.i(TAG, response.toString());
        Callback callback = (Callback) joinPoint.getThis();
        Log.i(TAG, "onResponse callback hashcode:--->" + callback.hashCode());

        NetMonitorEntity entity = (NetMonitorEntity) map.get(callback.hashCode() + "");
        if (entity != null) {
            //请求参数
            Request request = response.request();
            entity.setUrl(request.url().toString());
            entity.setMethod(request.method());
            entity.setRequestHeaders(request.headers().toString());
            entity.setHostIpAddress(request.url().getHost());
            entity.setResponseTimeTag(new Date().getTime() + "");

            //得到请求BODY
            Buffer sink = new Buffer();
            try {
                request.body().writeTo(sink);
                InputStream is = sink.inputStream();
                byte[] bytes = new byte[512];
                int len = 0;
                ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
                while ((len = is.read(bytes)) > 0) {
                    byteBuffer.put(bytes, 0, len);
                }
                String requestBody = new String(byteBuffer.array()).trim();
                Log.i(TAG, "body requestBody: " + requestBody);
                entity.setParams(requestBody);

            } catch (IOException e) {
                e.printStackTrace();
            }

            //响应参数
            entity.setCode(response.code() + "");
            entity.setResponseHeaders(response.headers().toString());
            //得到响应body
            try {
                long len = response.body().contentLength();

                BufferedSource source = response.body().source();
                source.request(len);
                Buffer copy = source.buffer().clone();
                Buffer result;
                if (copy.size() > len) {
                    result = new Buffer();
                    result.write(copy, len);
                    copy.clear();
                } else {
                    result = copy;
                }
                String bodyString = new String(result.readByteArray()).trim();
                Log.i(TAG, "bodyString ---> " + bodyString);
                entity.setBusinessContent(bodyString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            entity.setFinishTimeTag(new Date().getTime() + "");
            NetMonitorTask.execute(entity);

        }

        if (map.contains(callback.hashCode() + "")) {
            map.remove(callback.hashCode() + "");
        }

        Object obj = null;
        try {
            obj = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;
    }


    @Around("execution(* com.squareup.okhttp.Call.execute())")
    public Object execute(ProceedingJoinPoint joinPoint) {
        Log.i(TAG, "before execute");
        Object result = null;
        NetMonitorEntity entity = new NetMonitorEntity();
        try {
            entity.setSendTimeTag(new Date().getTime() + "");
            result = joinPoint.proceed();
            entity.setResponseTimeTag(new Date().getTime() + "");
            Response response = (Response) result;
            Request request = response.request();
            entity.setUrl(request.url().toString());
            entity.setMethod(request.method());
            entity.setRequestHeaders(request.headers().toString());
            entity.setHostIpAddress(request.url().getHost());
            //得到请求BODY
            Buffer sink = new Buffer();
            try {
                request.body().writeTo(sink);
                InputStream is = sink.inputStream();
                byte[] bytes = new byte[512];
                int len = 0;
                ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
                while ((len = is.read(bytes)) > 0) {
                    byteBuffer.put(bytes, 0, len);
                }
                String requestBody = new String(byteBuffer.array()).trim();
                Log.i(TAG, "body length: " + requestBody);
                entity.setParams(requestBody);

            } catch (IOException e) {
                e.printStackTrace();
            }

            entity.setCode(response.code() + "");
            entity.setResponseHeaders(response.headers().toString());
            //得到响应body
            try {
                long len = response.body().contentLength();

                BufferedSource source = response.body().source();
                source.request(len);
                Buffer copy = source.buffer().clone();
                Buffer res;
                if (copy.size() > len) {
                    res = new Buffer();
                    res.write(copy, len);
                    copy.clear();
                } else {
                    res = copy;
                }
                String bodyString = new String(res.readByteArray()).trim();
                Log.i(TAG, "bodyString ---> " + bodyString);
                entity.setBusinessContent(bodyString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            entity.setFinishTimeTag(new Date().getTime() + "");
            NetMonitorTask.execute(entity);
            Log.d(TAG, "execute responseCode-->" + response.toString());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        Log.i(TAG, "after execute");
        return result;
    }

    @Before("execution(* com.squareup.okhttp.Call.enqueue(..))")
    public void enqueue(JoinPoint joinPoint) {
        Object[] objs = joinPoint.getArgs();
        if (objs != null && objs.length > 0) {
            Callback callback = (Callback) objs[0];
            Log.i(TAG, "enqueue callback hashcode:" + callback.hashCode());


            //请求参数
            NetMonitorEntity entity = new NetMonitorEntity();
            entity.setSendTimeTag(new Date().getTime() + "");
            map.put(callback.hashCode() + "", entity);
        }
    }

}

