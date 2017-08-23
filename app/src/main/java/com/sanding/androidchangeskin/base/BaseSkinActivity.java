package com.sanding.androidchangeskin.base;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.sanding.androidchangeskin.attr.SkinAttrSupport;
import com.sanding.androidchangeskin.inter.SkinInterface;
import com.sanding.androidchangeskin.manage.SkinManager;
import com.sanding.androidchangeskin.skin.SkinAttr;
import com.sanding.androidchangeskin.skin.SkinView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/18.
 */

public class BaseSkinActivity extends AppCompatActivity implements SkinInterface {
    static final Class<?>[] sConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    private static final Map<String, Constructor<? extends View>> sConstructorMap
            = new ArrayMap<>();
    private final Object[] mConstructorArgs = new Object[2];
    private Method mCreatViewMethod=null;
    static  final Class<?>[] sCreatViewSignature=new Class[]{View.class,String.class, Context.class, AttributeSet.class};
    private final Object[] mCreatViewArgs = new Object[4];
    @Override
    public void onCreate(Bundle savedInstanceState) {
        SkinManager.getInstance().registSkinInter(this);
        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //此方法可以在布局之前改变控件
       LayoutInflaterCompat.setFactory(inflater, new LayoutInflaterFactory() {
           @Override
           public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
               AppCompatDelegate deleteData = getDelegate();
               View view=null;
               List<SkinAttr> atars;
               try {
                   if (mCreatViewMethod==null){
                       mCreatViewMethod= deleteData.getClass().getMethod("creatView",sCreatViewSignature );
                   }
                   mCreatViewArgs[0]=parent;
                   mCreatViewArgs[1]=name;
                   mCreatViewArgs[2]=context;
                   mCreatViewArgs[3]=attrs;
                   view= (View) mCreatViewMethod.invoke(deleteData,mCreatViewArgs);
               } catch (Exception e) {
                   e.printStackTrace();
               }
               atars = SkinAttrSupport.getSkinAttrs(attrs, context);
               if (atars.isEmpty()){
                   return  null;
               }
if (view==null){
    view=createViewFromTag(context,name,attrs);
}if (view!=null){
                 injectSkin(view,atars);
               }

               return view;
           }

       });


        super.onCreate(savedInstanceState);
    }

    private void injectSkin(View view, List<SkinAttr> atars) {
        List<SkinView> skinViews = SkinManager.getInstance().getSkinByKey(this);
        if (skinViews==null){
            skinViews=new ArrayList<SkinView>();
            SkinManager.getInstance().addSkinView(this,skinViews);
        }
        skinViews.add(new SkinView(view,atars));
        //当前是否需要自动换肤
if (SkinManager.getInstance().needChageSkin()){
    SkinManager.getInstance().skinChange(this);

}
    }

    private View createViewFromTag(Context context, String name, AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }

        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            if (-1 == name.indexOf('.')) {
                // try the android.widget prefix first...
                return createView(context, name, "android.widget.");
            } else {
                return createView(context, name, null);
            }
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }
    private View createView(Context context, String name, String prefix)
            throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = sConstructorMap.get(name);

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz = context.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void interSkin() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregistSkinInter(this);
    }
}
