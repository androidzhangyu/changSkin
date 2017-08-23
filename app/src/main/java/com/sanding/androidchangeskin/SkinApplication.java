package com.sanding.androidchangeskin;

import android.app.Application;

import com.sanding.androidchangeskin.manage.SkinManager;

/**
 * Created by Administrator on 2017/8/21.
 */

public class SkinApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
    }
}
