package com.sanding.androidchangeskin.attr;

import android.content.Context;
import android.util.AttributeSet;

import com.sanding.androidchangeskin.config.Const;
import com.sanding.androidchangeskin.skin.SkinAttr;
import com.sanding.androidchangeskin.skin.SkinAttrType;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by Administrator on 2017/8/18.
 */

public class SkinAttrSupport {
    public static List<SkinAttr> getSkinAttrs(AttributeSet attrs, Context context){
        List<SkinAttr> skinAttrs=new ArrayList<SkinAttr>();
        SkinAttrType skinAttrType=null;
        SkinAttr skinAttr=null;
        for (int i=0,n=attrs.getAttributeCount();i<n;i++){
            String attarName=attrs.getAttributeName(i);
            String attarValue=attrs.getAttributeValue(i);
            if (attarValue.startsWith("@")){
                //避免发生异常
                int id=-1;
                try {
                   id= Integer.parseInt(attarValue.substring(1));
                } catch (NumberFormatException e) {
                    e.printStackTrace();

                }
                if (id==-1){
                    continue;
                }
                String resName=context.getResources().getResourceEntryName(id);
                if (resName.startsWith(Const.SKIN_PREFIX)){
                    skinAttrType=getSupportAttrType(attarName);
                    if(skinAttrType==null) continue;
                    skinAttr=new SkinAttr(resName,skinAttrType);
                    skinAttrs.add(skinAttr);
                }

            }
        }
return  skinAttrs;
    }

    private static SkinAttrType getSupportAttrType(String attarName) {
        for (SkinAttrType attrType:SkinAttrType.values()){
            if (attrType.getResType().equals(attarName)){
                return attrType;
            }
        }
        return  null;
    }
}
