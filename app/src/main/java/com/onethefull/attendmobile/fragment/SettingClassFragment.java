package com.onethefull.attendmobile.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onethefull.attendmobile.MainActivity;
import com.onethefull.attendmobile.api.SharedPrefManager;
import com.onethefull.attendmobile.setclass.SettingClassPresenter;
import com.onethefull.attendmobile.setclass.SettingClassPresenterImpl;
import com.onethefull.attendmobile.setclass.SettingClassView;

public class SettingClassFragment extends Fragment implements SettingClassView {

    private static final String TAG = SettingClassFragment.class.getSimpleName();

    private SharedPrefManager mSharedPrefs;
    private SettingClassPresenter settingClassPresenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //메뉴바 타이틀설정
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("반 설정");
        setHasOptionsMenu(true);

        settingClassPresenter = new SettingClassPresenterImpl(SettingClassFragment.this, getActivity());
        mSharedPrefs = SharedPrefManager.getInstance(getActivity());

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void validation(String msg) {

    }

    @Override
    public void success() {

    }

    @Override
    public void error() {

    }



}//
