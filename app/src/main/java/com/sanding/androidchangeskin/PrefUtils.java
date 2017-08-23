package com.sanding.androidchangeskin;

import android.content.Context;
import android.content.SharedPreferences;

import com.sanding.androidchangeskin.config.Const;

/**
 * Created by Administrator on 2017/8/23.
 */

public class PrefUtils {
    private Context mContext;

    public PrefUtils(Context mContext) {
        this.mContext = mContext;
    }
    public  void savePluginPath(String path){
        SharedPreferences sp = mContext.getSharedPreferences(Const.Skin_PRENAME, Context.MODE_PRIVATE);
        sp.edit().putString(Const.KEY_PLUGIN_PATH,path).apply();
    }
    public  void savePluginPkg(String pkg){
        SharedPreferences sp = mContext.getSharedPreferences(Const.Skin_PRENAME, Context.MODE_PRIVATE);
        sp.edit().putString(Const.KEY_PLUGIN_PKY,pkg).apply();
    }
    public String getPluginPath(){
        SharedPreferences sp = mContext.getSharedPreferences(Const.Skin_PRENAME, Context.MODE_PRIVATE);
        return sp.getString(Const.KEY_PLUGIN_PATH,"");

    }
    public String getPluginPkg(){
        SharedPreferences sp = mContext.getSharedPreferences(Const.Skin_PRENAME, Context.MODE_PRIVATE);
        return sp.getString(Const.KEY_PLUGIN_PKY,"");

    }

    public void clear() {
        //清空
        SharedPreferences sp = mContext.getSharedPreferences(Const.Skin_PRENAME, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
    public  void saveSuffix(String suffix){
        SharedPreferences sp = mContext.getSharedPreferences(Const.Skin_PRENAME, Context.MODE_PRIVATE);
        sp.edit().putString(Const.SKIN_SUFFIX,suffix).apply();
    }
    public String getSuffix(){
        SharedPreferences sp = mContext.getSharedPreferences(Const.Skin_PRENAME, Context.MODE_PRIVATE);
        return sp.getString(Const.SKIN_SUFFIX,"");

    }
}
