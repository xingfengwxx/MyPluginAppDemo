package com.wangxingxing.lib_plugin_core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * author : 王星星
 * date : 2021/1/13 12:02
 * email : 1099420259@qq.com
 * description : 插件化框架核心类：
 * 如果需要加载插件包中的Activity，需要做哪些事情？
 * 1. 加载这个Activity的Class对象。
 * 2. 解决加载进来的Activity的生命周期管理
 * 3. 这些组件的上下文需要主动注入
 * 4. Apk资源是不是需要主动的加载进来？是的！
 * 具体思路：
 * 使用ClassLoader （DexClassLoader）
 * 创建一个ProxyActivity，来代理我们这个不是真正意义上的Activity类执行业务逻辑
 * 主动加载资源 (AssetManager)
 */
public class PluginManager {

    private static final String TAG = "PluginManager";

    // 插件包加载类的ClassLoader
    private DexClassLoader mDexClassLoader;

    // 加载资源后的Resources对象
    private Resources mResources;

    // 插件包的信息类
    private PackageInfo mPackageInfo;

    private Context mContext;

    private static PluginManager instance;

    private PluginManager() {

    }

    public static PluginManager getInstance() {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager();
                }
            }
        }
        return instance;
    }

    /**
     * 加载插件包
     *
     * @param context
     * @param loadPath
     */
    public void loadPlugin(Context context, String loadPath) {
        mContext = context;

        // 获取私有文件夹
        File file = mContext.getDir("cache_plugin", Context.MODE_PRIVATE);
        // 创建DexClassLoader
        mDexClassLoader = new DexClassLoader(loadPath, file.getAbsolutePath(), null, context.getClassLoader());

        try {
            //通过反射，加载插件包资源管理对象
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, loadPath);
            mResources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());

            // 加载插件包信息
            mPackageInfo = context.getPackageManager().getPackageArchiveInfo(loadPath, PackageManager.GET_ACTIVITIES);
            Log.i(TAG, "loadPlugin: activities.length=" + mPackageInfo.activities.length);
            Log.i(TAG, "loadPlugin: activities[0]=" + mPackageInfo.activities[0].name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DexClassLoader getDexClassLoader() {
        return mDexClassLoader;
    }

    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }

    public Resources getResources() {
        return mResources;
    }
}
