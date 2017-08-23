package com.sanding.androidchangeskin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanding.androidchangeskin.manage.SkinManager;

/**
 * I am carden zhangyu
 */

public class MenuLeftFragment extends Fragment implements View.OnClickListener{
    private View mRedView;
    private View mGreenView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.layout_menu,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRedView=view.findViewById(R.id.id_rl_innerchange01);
        mGreenView=view.findViewById(R.id.id_rl_innerchange02);
        mRedView.setOnClickListener(this);
mGreenView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_rl_innerchange01:
                SkinManager.getInstance().changeSkin("red");
                break;
            case R.id.id_rl_innerchange02:
                SkinManager.getInstance().changeSkin("green");
                break;
        }

    }
}
