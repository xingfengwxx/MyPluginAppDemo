package com.wangxingxing.mypluginappdemo;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wangxingxing.lib_plugin_core.PluginManager;
import com.wangxingxing.lib_plugin_core.ProxyActivity;

import java.io.File;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate: " + getClassLoader() + "");
        Log.i(TAG, "onCreate: " + getClassLoader().getParent() + "");
        Log.i(TAG, "onCreate: " + getClassLoader().getParent().getParent() + "");

        MainActivityPermissionsDispatcher.loadApkWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void loadApk() {
        String path = getExternalFilesDir(null).getAbsolutePath() + File.separator + "payapp-debug.apk";
        Log.i(TAG, "loadApk: " + path);
        PluginManager.getInstance().loadPlugin(this, path);
    }

    public void onClickView(View view) {
        Intent intent = new Intent(MainActivity.this, ProxyActivity.class);
        ActivityInfo[] activityInfos = PluginManager.getInstance().getPackageInfo().activities;
        if (activityInfos.length > 0) {
            intent.putExtra("className", activityInfos[0].name);
            startActivity(intent);
        }
    }

    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void showRationaleForPermissions(PermissionRequest request) {
        showRationaleDialog(request);
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onPermissionDenied() {
        Toast.makeText(this, "获取读写权限被拒绝", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onPermissionNeverAskAgain() {
        Toast.makeText(this, "不再询问读写权限", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @SuppressLint("NeedOnRequestPermissionsResult") @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showRationaleDialog(PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage("请授权App获得读写权限")
                .show();

    }
}