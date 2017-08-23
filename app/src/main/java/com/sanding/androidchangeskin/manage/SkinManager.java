package com.sanding.androidchangeskin.manage;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.sanding.androidchangeskin.PrefUtils;
import com.sanding.androidchangeskin.ResourcesManage;
import com.sanding.androidchangeskin.inter.ISkinChangingCallback;
import com.sanding.androidchangeskin.inter.SkinInterface;
import com.sanding.androidchangeskin.skin.SkinView;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/18.
 */

public class SkinManager {
    private Map<SkinInterface,List<SkinView>> mskinViewMaps=new HashMap<SkinInterface,List<SkinView>>();
//负责管理interface接口,防止内存泄露
    private  List<SkinInterface> mSkinInterfaces=new ArrayList<SkinInterface>();
private PrefUtils mPrefUitls;
private  String mCurrentPath;
    private  String mCurrentPkg;
    private  String mSuffix;


    //单例模式
    private static SkinManager sInstance;
    private Context mcontext;
    private ResourcesManage manage;

    public  static SkinManager getInstance(){
        if (sInstance==null){
            synchronized (SkinManager.class){
                if (sInstance==null){
                    sInstance=new SkinManager();
                }
            }

        }
        return sInstance;

    }

//为mContext赋值，为防止内存泄露，使用getapplicationContext
    public void init(Context context){

        mcontext=context.getApplicationContext();
        mPrefUitls=new PrefUtils(mcontext);
        try {
            String pluginPath=mPrefUitls.getPluginPath();
            String pluginPkg=mPrefUitls.getPluginPkg();
            mSuffix=mPrefUitls.getSuffix();
            File file=new File(pluginPath);
            if (file.exists()){
                loadPlugin(pluginPath,pluginPkg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //有异常清空shardPre
            mPrefUitls.clear();

        }
    }
    //获取resourceManage
    public  ResourcesManage getResourceManger(){
        if (!usePlugin()){
            return new ResourcesManage(mcontext.getResources(),mcontext.getPackageName(),mSuffix);
        }
        return manage;
    }
    private void loadPlugin(String mSkinPluginPath, String mSkinPluginPkg) throws  Exception{
        if (mSkinPluginPath.equals(mCurrentPath)&&mSkinPluginPkg.equals(mCurrentPkg)){
            return;
        }
            AssetManager assmanger=AssetManager.class.newInstance();
            //通过反射调用addAssetPath方法
            Method method=assmanger.getClass().getMethod("addAssetPath",String.class);
            method.invoke(assmanger,mSkinPluginPath);
            Resources superResource=mcontext.getResources();
            Resources resources=new Resources(assmanger,superResource.getDisplayMetrics(),superResource.getConfiguration());
           manage=new ResourcesManage(resources,mSkinPluginPkg,null);
mCurrentPath=mSkinPluginPath;
        mCurrentPkg=mSkinPluginPkg;

    }
    //根据key获取值
    public  List<SkinView> getSkinByKey(SkinInterface skinInterface){
        return mskinViewMaps.get(skinInterface);
    }
    //增加到map
    public  void addSkinView(SkinInterface skinInterface,List<SkinView> skinViews){
        mskinViewMaps.put(skinInterface,skinViews);

    }
    //注册
    public void registSkinInter(SkinInterface skinInterface){
        mSkinInterfaces.add(skinInterface);

    }
    //注销
    public void unregistSkinInter(SkinInterface skinInterface){
        mSkinInterfaces.remove(skinInterface);
        mskinViewMaps.remove(skinInterface);

    }

    public void changeSkin(final String mSkinPluginPath, final String mSkinPluginPkg, ISkinChangingCallback callback) {
        if(callback==null){
            callback=ISkinChangingCallback.DEFAULT_SKIN_CHANGING_CALLBACK;
        }
        final  ISkinChangingCallback finalcallback=callback;
        finalcallback.onStart();
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    loadPlugin(mSkinPluginPath,mSkinPluginPkg);
                } catch (Exception e) {
                    e.printStackTrace();
                    return  -1;
                }
                return 0;
            }

            @Override
            protected void onPostExecute(Integer code) {
                if (-1==code){
                    finalcallback.onError(null);
                    return;
                }
                try {
                    notifyChangeListener();
                    finalcallback.onComplete();
                    updatePluginInfo(mSkinPluginPath,mSkinPluginPkg);
                } catch (Exception e) {
                    e.printStackTrace();
                    finalcallback.onError(e);
                }
            }
        }.execute();

    }

    private void updatePluginInfo(String path,String pkg) {
        mPrefUitls.savePluginPath(path);
        mPrefUitls.savePluginPkg(pkg);
    }

    private void notifyChangeListener() {
        for (SkinInterface inter:mSkinInterfaces){
            skinChange(inter);
            inter.interSkin();
        }
    }

    public void skinChange(SkinInterface inter) {
        List<SkinView> skinViews = mskinViewMaps.get(inter);
        for (SkinView skinview:skinViews){
            skinview.apply();
        }
    }
    public  void changeSkin(String suffix) {
        clearPluginInfo();
        mSuffix = suffix;
        mPrefUitls.saveSuffix(suffix);
        notifyChangeListener();
    }

    private void clearPluginInfo() {
        mCurrentPath=null;
        mCurrentPkg=null;
        mSuffix=null;
        mPrefUitls.clear();

    }

    public boolean needChageSkin() {

        return usePlugin() ||useSuffix();
    }

    private boolean useSuffix() {
        return mSuffix!=null && !mSuffix.trim().equals("");
    }

    private boolean usePlugin() {
        return mCurrentPath!=null && !mCurrentPath.trim().equals("");
    }
}
