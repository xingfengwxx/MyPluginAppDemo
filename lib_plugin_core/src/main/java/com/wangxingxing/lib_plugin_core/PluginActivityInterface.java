package com.wangxingxing.lib_plugin_core;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * author : 王星星
 * date : 2021/1/13 11:38
 * email : 1099420259@qq.com
 * description :
 */
public interface PluginActivityInterface {

    // 绑定代理Activity
    void attach(Activity proxyActivity);

    // 感知生命周期
    void onCreate(Bundle savedInstanceState);
    void onStart();
    void onResume();
    void onRestart();
    void onStop();
    void onPause();
    void onDestroy();

    void onSaveInstanceState(Bundle savedInstanceState);
    boolean onTouchEvent(MotionEvent event);
    void onBackPressed();
}
