package com.sanding.androidchangeskin.skin;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanding.androidchangeskin.ResourcesManage;
import com.sanding.androidchangeskin.manage.SkinManager;

/**
 * Created by Administrator on 2017/8/18.
 */

public enum  SkinAttrType {
BACKGROUND("background") {
    @Override
    public void apply(View view, String resName) {
        Drawable drawable=getDrawableByName().getdrawableByResName(resName);
        if (drawable!=null){
            view.setBackgroundDrawable(drawable);
        }


    }
},SRC("src") {
        @Override
        public void apply(View view, String resName) {

            Drawable drawable=getDrawableByName().getdrawableByResName(resName);
            if (view instanceof ImageView){
                ImageView imageview= (ImageView) view;
                if (drawable!=null){
                    imageview.setImageDrawable(drawable);
                }
            }
        }
    },TEXT_COLOR("textColor") {
        @Override
        public void apply(View view, String resName) {
            ColorStateList Colorlist = getDrawableByName().getcolorByResName(resName);
            if (view instanceof TextView){
               TextView textview= (TextView) view;
                if (Colorlist!=null){
                    textview.setTextColor(Colorlist);


                }
            }
        }
    };
String resType;
    SkinAttrType(String type) {
        resType=type;
    }

    public abstract void apply(View view, String resName);
    public ResourcesManage getDrawableByName(){
        return SkinManager.getInstance().getResourceManger();
    }

    public String getResType() {
        return resType;
    }
}
