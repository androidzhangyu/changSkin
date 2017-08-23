package com.sanding.androidchangeskin.skin;

import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */

public class SkinView {
    private View view;
    private List<SkinAttr> mAttrs;
    //遍历获取属性
    public void apply(){
        for (SkinAttr skinAttr:mAttrs){
            skinAttr.apply(view);
        }
    }

    public SkinView(View view, List<SkinAttr> mAttrs) {
        this.view = view;
        this.mAttrs = mAttrs;
    }
}
