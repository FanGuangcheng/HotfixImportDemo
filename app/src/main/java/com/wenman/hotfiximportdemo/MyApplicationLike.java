package com.wenman.hotfiximportdemo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.netease.android.patch.app.TinkerManager;
import com.netease.android.patch.app.TinkerServerManager;
import com.netease.android.patch.app.reporter.TinkerServiceReporter;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.util.Map;

@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.wenman.hotfiximportdemo.MyDemoApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class MyApplicationLike extends DefaultApplicationLike {
    String TAG = "MyApplication";
    public static final String APPLICATION_ID = "com.netease.android.demo.SnailReader";  // 该ID是后台管理系统中的ID


    public MyApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        Log.d(TAG, "MyApplicationLike  onBaseContextAttached");

        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // Tinker 的初始化操作
        TinkerManager.setTinkerApplicationLike(this);
        TinkerManager.initFastCrashProtect();
        TinkerManager.setUpgradeRetryEnable(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyApplicationLike  onCreate");
        // 初始化打点系统
        TinkerServiceReporter reporter = new TinkerServiceReporter();
        reporter.setReporter(new TinkerServiceReporter.Reporter() {
            @Override
            public void onReport(String event, Map<String, Object> value) {
                TinkerLog.i(TAG, "event: " + event + ", value: " + value.toString());
            }
        });

        // 初始化Tinker
        TinkerManager.installTinker(this);
        // 初始化TinkerPatch SDK
        //TEST_SERVER 标记是否请求补丁使用测试服务器，CLOSE_BETA 标记是否是内测包
        TinkerServerManager.installTinkerServer(getApplication(), Tinker.with(getApplication()),
                APPLICATION_ID, BuildConfig.VERSION_NAME, "channel", /*TEST_SERVER*/true, /*CLOSE_BETA*/false);

        // 开始检查是否有补丁
        TinkerServerManager.checkTinkerUpdate(true);
    }
}
