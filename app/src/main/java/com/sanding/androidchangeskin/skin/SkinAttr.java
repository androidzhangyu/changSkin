package com.sanding.androidchangeskin.skin;

import android.view.View;

/**
 * Created by Administrator on 2017/8/18.
 */
public class SkinAttr {
    private String resName;
    private SkinAttrType mType;
    //类型
    public void apply(View view) {
        mType.apply(view,resName);
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public SkinAttrType getmType() {
        return mType;
    }

    public void setmType(SkinAttrType mType) {
        this.mType = mType;
    }

    public SkinAttr(String resName, SkinAttrType mType) {
        this.resName = resName;
        this.mType = mType;
    }


}
