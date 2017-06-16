package com.pingan.library.aspectjx.httpBase;

import android.util.Log;

import com.pingan.library.common.NetMonitorEntity;
import com.pingan.library.util.NetMonitorTask;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yejy on 2017/5/25.
 */

@Aspect
public class HttpConnectionAspect {

    private static String TAG = HttpConnectionAspect.class.getSimpleName();
    private static ConcurrentHashMap<String,NetMonitorEntity> map = new ConcurrentHashMap<>();

    //HttpClient添加请求头参数
    @Around("call(* java.net.URL.openConnection())")
    public Object builderConnection(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.i(TAG,"builderConnection");

        URL urlObj = (URL) joinPoint.getTarget();
        Log.d(TAG, "urlObj hashCode builderConnection --> " + urlObj.hashCode());

        String host = urlObj.getHost();
        String path = urlObj.getPath();
        String query = urlObj.getQuery();
        String requestUrl = host + path + (query == null ? "" : "?" + query);
        Log.i(TAG,"builderConnection: requestURL is " + requestUrl);

        URLConnection urlConnection = null;

        try{
            urlConnection = (URLConnection) joinPoint.proceed();
            urlConnection.addRequestProperty("ye", "1232323");
            Log.d(TAG, "urlConnection hashCode builderConnection --> " + urlConnection.hashCode());
            String key = String.valueOf(urlConnection.hashCode());
            NetMonitorEntity entity = map.get(key);
            if (entity == null) {
                entity = new NetMonitorEntity();
            }
            entity.setUrl(requestUrl);
            entity.setHostIpAddress(host);
            entity.setSendTimeTag(new Date().getTime() + "");


            StringBuilder result = new StringBuilder();
            Map<String, List<String>> headers = urlConnection.getRequestProperties();
            Set<Map.Entry<String,List<String> > > entries = headers.entrySet();

            for (Map.Entry<String,List<String> > entry : entries) {
                String k = entry.getKey();
                if (k != null && !k.equalsIgnoreCase("null")) {
                    result.append(k).append(": ");
                }
                int size = entry.getValue().size();
                if (size == 1) {
                    result.append(entry.getValue().get(0)).append("\n");
                } else {
                    for (int i = 0; i < size; i++) {
                        result.append(entry.getValue().get(i));
                        if (i== size - 1) {
                            break;
                        }
                        result.append(",");
                    }
                }
            }
            entity.setRequestHeaders(result.toString());
            map.put(key, entity);
        }catch (Exception e){
            NetMonitorEntity entity = new NetMonitorEntity();
            entity.setUrl(requestUrl);
            entity.setSendTimeTag(new Date().getTime() + "");
            entity.setErrorMsg(e.getMessage());
            NetMonitorTask.execute(entity);
            throw e;
        }


        return urlConnection;
    }

    //请求方法异常
    @Before("call( * java.net.HttpURLConnection.setRequestMethod(..))")
    public void requestMethodException(JoinPoint joinPoint) throws Throwable {
        Log.i(TAG,"requestMethodException");
        Object[] objs = joinPoint.getArgs();
        String method = (String)objs[0];
        Log.d(TAG, "请求方法为: " + method);
        String[] methods = new String[]{"OPTIONS", "GET", "HEAD", "POST", "PUT", "DELETE", "TRACE"};
        int i = -1;
        for (String m : methods) {
            if (m.equalsIgnoreCase(method)) {
                i = 0;
                break;
            }
        }

        HttpURLConnection httpURLConnection = (HttpURLConnection) joinPoint.getTarget();
        String key = httpURLConnection.hashCode() + "";
        Log.d(TAG, "httpURLConnection hashCode requestMethodException --> " + key);
        NetMonitorEntity entity = map.get(key);
        if (entity == null) {
            entity = new NetMonitorEntity();
        }
        entity.setMethod(method);
        if (i == -1) {
            //method not found Exception
            entity.setErrorMsg("Unknown method '" + method + "'");
            Log.i(TAG,"Unknown method '" + method + "'");
        }

    }

    //网络请求结果码
    @Around("call(* java.net.HttpURLConnection.getResponseCode())")
    public Object responeCode(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.i(TAG,"responeCode");


        HttpURLConnection httpURLConnection = (HttpURLConnection) joinPoint.getTarget();
        String key = httpURLConnection.hashCode() + "";
        Log.d(TAG, "httpURLConnection hashCode responeCode --> " + key);

        NetMonitorEntity entity = map.get(key);
        if (entity == null) {
            entity = new NetMonitorEntity();
        }
        entity.setResponseTimeTag(new Date().getTime() + "");

        //标志位，调用getResponseCode()会调用一次getInputStream
        entity.preRead = true;

        Integer code = null;
        try{

            code = (Integer) joinPoint.proceed();
            Log.i(TAG, "请求结果码：code = " + code);
            entity.setCode(code.toString());
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
        return code;
    }

    //网络请求结果码
    @Around("call(* java.net.HttpURLConnection.getResponseMessage())")
    public Object responseMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.i(TAG,"responseMessage");


        HttpURLConnection httpURLConnection = (HttpURLConnection) joinPoint.getTarget();
        String key = httpURLConnection.hashCode() + "";
        Log.d(TAG, "httpURLConnection hashCode responseMessage --> " + key);

        NetMonitorEntity entity = map.get(key);
        if (entity == null) {
            entity = new NetMonitorEntity();
        }

        entity.setTimeout(httpURLConnection.getReadTimeout() + "");

        StringBuilder result = new StringBuilder();
        Map<String, List<String>> headers = httpURLConnection.getHeaderFields();
        Set<Map.Entry<String,List<String> > > entries = headers.entrySet();

        for (Map.Entry<String,List<String> > entry : entries) {
            String k = entry.getKey();
            if (k != null && !k.equalsIgnoreCase("null")) {
                result.append(k).append(": ");
            }
            int size = entry.getValue().size();
            if (size == 1) {
                result.append(entry.getValue().get(0)).append("\n");
            } else {
                for (int i = 0; i < size; i++) {
                    result.append(entry.getValue().get(i));
                    if (i== size - 1) {
                        break;
                    }
                    result.append(",");
                }
            }
        }
        entity.setResponseHeaders(result.toString());
        Object obj =  joinPoint.proceed();

        Log.i(TAG,"result: "+obj);

        httpURLConnection.getInputStream();
        entity.setFinishTimeTag(new Date().getTime() + "");

        return obj;
    }

    //网络请求结果码
    @Around("call(* java.net.URLConnection.getInputStream())")
    public Object responseInputStream(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.i(TAG,"responseInputStream");

        HttpURLConnection httpURLConnection = (HttpURLConnection) joinPoint.getTarget();
        String key = httpURLConnection.hashCode() + "";
        Log.d(TAG, "httpURLConnection hashCode responseMessage --> " + key);

        NetMonitorEntity entity = map.get(key);
        try {
            InputStream is = (InputStream) joinPoint.proceed();
            if (entity == null || entity.preRead) {
                //如果是getResponseCode()调用的则不重抛InputStream
                entity.preRead = false;
                return is;
            } else if (entity.getCode().equals("200")) {
                NetMonitorTask.execute(entity);
                if (map.contains(key)) {
                    map.remove(key);
                }
                return is;
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byte[] bytes = new byte[512];
            int len = 0;
            while ((len = is.read(bytes)) > 0) {
                byteBuffer.put(bytes, 0, len);
            }
            byteBuffer.flip();
            String content = new String(byteBuffer.array()).trim();
            entity.setBusinessContent(content);
            NetMonitorTask.execute(entity);
            if (map.contains(key)) {
                map.remove(key);
            }
            Log.d(TAG, "content--> " + content);
            byteBuffer.clear();
            byteBuffer = null;
            InputStream nis = new ByteArrayInputStream(content.getBytes());
            Log.i(TAG, "长度： " + nis.available());
            return nis;
        }catch (Exception e) {
            entity.setErrorMsg(e.getMessage());
            NetMonitorTask.execute(entity);
            if (map.contains(key)) {
                map.remove(key);
            }
            throw e;

        }

    }


}
