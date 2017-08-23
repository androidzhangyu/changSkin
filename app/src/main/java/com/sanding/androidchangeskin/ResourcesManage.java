package com.sanding.androidchangeskin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * Created by Administrator on 2017/8/18.
 */

public class ResourcesManage {
    private Resources resources;
    private String pkgName;
private String mSuffix;
    public ResourcesManage(Resources resources, String pkgName,String suffix) {
        this.resources = resources;
        this.pkgName = pkgName;
        if (suffix==null){
            suffix="";
        }
        mSuffix=suffix;
    }
    public Drawable getdrawableByResName(String name){
        name=appSuufix(name);

        try {
            return  resources.getDrawable(resources.getIdentifier(name,"drawable",pkgName));
        } catch (Exception e) {
            e.printStackTrace();
            return  null;

        }
    }

    private String appSuufix(String name) {
        if (!TextUtils.isEmpty(mSuffix)){
            name+="_"+mSuffix;
        }
        return name;
    }

    public ColorStateList getcolorByResName(String name){
        name=appSuufix(name);
        try {
            return  resources.getColorStateList(resources.getIdentifier(name,"color",pkgName));
        } catch (Exception e) {
            e.printStackTrace();
            return  null;

        }
    }

}
