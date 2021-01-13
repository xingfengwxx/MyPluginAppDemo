package com.wangxingxing.lib_plugin_core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * author : 王星星
 * date : 2021/1/13 11:37
 * email : 1099420259@qq.com
 * description : 具体的业务类
 */
public class BaseActivity extends AppCompatActivity implements PluginActivityInterface {

    // 代理当前类的组件类
    private Activity that;

    @Override
    public void attach(Activity proxyActivity) {
        that = proxyActivity;
    }

    @Override
    public void setContentView(int layoutId) {
        if (that != null) {
            that.setContentView(layoutId);
        } else {
            super.setContentView(layoutId);
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        if (that != null) {
            that.setContentView(view);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (that != null) {
            return that.findViewById(id);
        } else {
            return super.findViewById(id);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (that != null) {
            intent.putExtra("className", intent.getComponent().getClassName());
            that.startActivity(intent);
        } else {
            super.startActivity(intent);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRestart() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStop() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onPause() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

    }
}
