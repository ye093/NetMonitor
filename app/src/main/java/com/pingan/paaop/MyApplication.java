package com.pingan.paaop;

import android.app.Application;

import com.pingan.octopussdk.sdk.IDInfoDataSDK;

/**
 * Created by yejy on 2017/6/5.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        IDInfoDataSDK.initSDK(this.getApplicationContext(), "B52E249A6224616C0FCBE88D90A195CF", "channelId", false);
        IDInfoDataSDK.setLogEnabled(true);
        IDInfoDataSDK.setExceptionReportEnable(true);
//        Stetho.initializeWithDefaults(this);//b92a6515-dfdc-4b55-82b2-927358cd5835
//        CrashReport.initCrashReport(getApplicationContext(),"754263f092",true);
//        CrashReport.testJavaCrash();
    }

    @Override
    public void onTrimMemory(int level) {
        IDInfoDataSDK.onTrimMemory(this);
        super.onTrimMemory(level);
    }
}
