package com.example.lyg_pro.listendangke.application;


import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * 当前类注释:全局Application类,作为全局数据的配置以及相关参数数据初始化工作
 *
 */
public class CNKApplication extends Application {
    private static Context instance=null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getApplicationContext();
    }

    public static Context getInstance(){
        return instance;
    }
}
