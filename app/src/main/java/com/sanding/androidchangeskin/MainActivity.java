package com.sanding.androidchangeskin;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.os.EnvironmentCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;
import com.sanding.androidchangeskin.base.BaseSkinActivity;
import com.sanding.androidchangeskin.inter.ISkinChangingCallback;
import com.sanding.androidchangeskin.manage.SkinManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends BaseSkinActivity{
private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] mdatas={"Activity","Service","Activity","Service","Activity","Service","Activity","Service"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
        listView= (ListView) findViewById(R.id.listview);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawerLayout);
        FragmentManager fragmentmanager=getSupportFragmentManager();
        Fragment fragment = fragmentmanager.findFragmentById(R.id.menu);
        if (fragment==null){
            fragmentmanager.beginTransaction().add(R.id.menu,new MenuLeftFragment()).commit();

        }
listView.setAdapter(new ArrayAdapter<String>(this,-1,mdatas){
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
if (convertView==null){
    convertView= LayoutInflater.from(MainActivity.this).inflate(R.layout.item,parent,false);
}
        TextView tv= (TextView) convertView.findViewById(R.id.tv_title);
        tv.setText(getItem(position));
        return convertView;
    }
});
    }
    private void initEvent() {
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent=drawerLayout.getChildAt(0);
                View menu=drawerView;
                float scale=1-slideOffset;
                float rightScale=0.8f+scale*0.2f;
                if (drawerView.getTag().equals("LEFT")){
                    float leftscale =1-0.3f*scale;
                    ViewHelper.setScaleX(menu,leftscale);
                    ViewHelper.setScaleY(menu,leftscale);
                    ViewHelper.setAlpha(menu,0.6f+0.4f*(1-scale));
                    ViewHelper.setTranslationX(mContent,menu.getMeasuredWidth()*(1-scale));
                    ViewHelper.setPivotX(mContent,0);
                    ViewHelper.setPivotY(mContent,menu.getMeasuredHeight()/2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent,rightScale);
                    ViewHelper.setPivotY(mContent,rightScale);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    private  String mSkinPluginPath= Environment.getExternalStorageDirectory()+ File.separator+"plugin.apk";
    private  String mSkinPluginPkg="com.sanding.plugin";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
          case  R.id.action_chang:
//loadPlugin(mSkinPluginPath,mSkinPluginPkg);
              SkinManager.getInstance().changeSkin(mSkinPluginPath,mSkinPluginPkg, new ISkinChangingCallback() {
                  @Override
                  public void onStart() {

                  }

                  @Override
                  public void onError(Exception e) {

                  }

                  @Override
                  public void onComplete() {
                      Toast.makeText(MainActivity.this,"换肤成功",Toast.LENGTH_LONG).show();
                  }
              });
            break;
            case R.id.changeFactoryActivity:
                startActivity(new Intent(MainActivity.this,TestFactoryActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

//    private void loadPlugin(String mSkinPluginPath, String mSkinPluginPkg) {
//        try {
//            AssetManager assmanger=AssetManager.class.newInstance();
//            //通过反射调用addAssetPath方法
//            Method method=assmanger.getClass().getMethod("addAssetPath",String.class);
//            method.invoke(assmanger,mSkinPluginPath);
//            Resources superResource=getResources();
//            Resources resources=new Resources(assmanger,superResource.getDisplayMetrics(),superResource.getConfiguration());
//            ResourcesManage manage=new ResourcesManage(resources,mSkinPluginPkg);
//           Drawable drawable= manage.getdrawableByResName("girls");
//            if (drawable!=null){
//                drawerLayout.setBackground(drawable);
//            }
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//
//    }
}
