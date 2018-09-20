package com.wenman.hotfiximportdemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MyApplication extends Application {
    String TAG = "MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyApplication  onCreate");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d(TAG, "MyApplication  attachBaseContext");
    }
}
