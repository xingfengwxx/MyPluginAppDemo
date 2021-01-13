package com.wangxingxing.lib_plugin_core;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * author : 王星星
 * date : 2021/1/13 14:12
 * email : 1099420259@qq.com
 * description : 帮助执行具体目标组件的业务/或者生命周期方法
 */
public class ProxyActivity extends AppCompatActivity {

    private static final String TAG = "ProxyActivity";
    
    // 被代理的目标组件的全类名
    private String className = "";

    private PluginActivityInterface mPluginActivityInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        className = getIntent().getStringExtra("className");
        Log.i(TAG, "onCreate: className=" + className);
        try {
            Class<?> aClass = getClassLoader().loadClass(className);
            Activity activity = (Activity) aClass.newInstance();
            if (activity instanceof PluginActivityInterface) {
                mPluginActivityInterface = (PluginActivityInterface) activity;
                // 绑定具备上下文的Activity
                mPluginActivityInterface.attach(this);
                mPluginActivityInterface.onCreate(savedInstanceState);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader();
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getResources();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPluginActivityInterface.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPluginActivityInterface.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPluginActivityInterface.onDestroy();
    }
}
