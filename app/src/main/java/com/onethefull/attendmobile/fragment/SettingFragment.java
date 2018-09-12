package com.onethefull.attendmobile.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.onethefull.attendmobile.MainActivity;
import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.account.changename.ChangeNMPresenter;
import com.onethefull.attendmobile.account.changename.ChangeNMPresenterImpl;
import com.onethefull.attendmobile.account.changename.ChangeNMView;
import com.onethefull.attendmobile.api.SharedPrefManager;
import com.onethefull.attendmobile.getgotime.GetGoTimePresenter;
import com.onethefull.attendmobile.getgotime.GetGoTimePresenterImpl;
import com.onethefull.attendmobile.getgotime.GetGoTimeView;
import com.onethefull.attendmobile.setgotime.SettingTimePresenter;
import com.onethefull.attendmobile.setgotime.SettingTimePresenterImpl;
import com.onethefull.attendmobile.setgotime.SettingTimeView;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SettingFragment extends android.support.v4.app.Fragment implements ChangeNMView, SettingTimeView, GetGoTimeView, View.OnClickListener, OnDateSetListener {
    private static final String TAG = SettingFragment.class.getSimpleName();

    private RelativeLayout layout_goTime, layout_comeTime, layout_changeName;
    private TextView tv_currentGoTime, tv_currentComeTime, tv_currentName;
    private SettingTimePresenter settingTimePresenter;
    private GetGoTimePresenter getGoTimePresenter;
    private ChangeNMPresenter changeNMPresenter;
    private SharedPrefManager mSharedPrefs;
    private String id = "";
    private TimePickerDialog picker_time;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        //메뉴바 타이틀설정
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("환경설정");
        setHasOptionsMenu(true);

        settingTimePresenter = new SettingTimePresenterImpl(SettingFragment.this, getActivity());
        getGoTimePresenter = new GetGoTimePresenterImpl(SettingFragment.this, getActivity());
        changeNMPresenter = new ChangeNMPresenterImpl(SettingFragment.this, getActivity());

        mSharedPrefs = SharedPrefManager.getInstance(getActivity());
        layout_changeName = view.findViewById(R.id.layout_changeName);
        layout_comeTime = view.findViewById(R.id.layout_comeTime);
        layout_goTime = view.findViewById(R.id.layout_goTime);
        tv_currentComeTime = view.findViewById(R.id.tv_currentComeTime);
        tv_currentGoTime = view.findViewById(R.id.tv_currentGoTime);
        tv_currentName = view.findViewById(R.id.tv_currentKindergarten_name);

        //설정된 하원시간 불러오기
        id = mSharedPrefs.getLoginId();
        getGoTimePresenter.performGetGoTime(id);

        //유치원명 가져오기
        tv_currentName.setText(mSharedPrefs.getKindergarten());

        //리스너 달기
        layout_changeName.setOnClickListener(this);
        layout_goTime.setOnClickListener(this);

        //타임피커 설정
        picker_time = new TimePickerDialog.Builder().setType(Type.HOURS_MINS).setCallBack(this)
                .setCancelStringId("취소")
                .setSureStringId("설정")
                .setTitleStringId("하원시간 설정")
                .setHourText("시")
                .setMinuteText("분")
                .setThemeColor(getResources().getColor(R.color.main_blue))
                .setWheelItemTextSize(20)
                .build();


       return view;
    }


    //시간 변환
    public String getDateToString(long time){
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        return sf.format(d);
    }


    @Override
    public void validation(String msg) {

    }

    @Override
    public void changeSuccess(String name) {

        tv_currentName.setText(name);
        ((MainActivity) getActivity()).tv_kindergarten.setText(name);

    }

    @Override
    public void getGotimeSuccess(String goTime) {
        if (goTime != null) tv_currentGoTime.setText(goTime);
        else tv_currentGoTime.setText("없음");
    }

    @Override
    public void success() {
        Toast.makeText(getActivity(), "하원시간 변경 성공", Toast.LENGTH_SHORT).show();
        getGoTimePresenter.performGetGoTime(id);
    }

    @Override
    public void error() {
        Toast.makeText(getActivity(), "시간 변경 실패", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void launch(Class cls) {

    }

    //메뉴바에서 메뉴 감추기
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem search = menu.findItem(R.id.search);
        search.setVisible(false);

    }//

    /////리스너

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.layout_goTime:
                picker_time.show(getActivity().getSupportFragmentManager(),"hour_min");
                break;


            case R.id.layout_changeName:
                new LovelyTextInputDialog(getActivity(), R.style.EditTextTintTheme)
                        .setTitle(R.string.dialog_change_name)
                        .setIcon(R.drawable.pen)
                        .setNegativeButton("취소", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setConfirmButton("변경", new LovelyTextInputDialog.OnTextInputConfirmListener() {
                            @Override
                            public void onTextInputConfirmed(String text) {
                                changeNMPresenter = new ChangeNMPresenterImpl(SettingFragment.this, getActivity());
                                changeNMPresenter.changeNM(id, text);

                            }
                        }).show();
                break;

        }

    }//

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {

        String time = getDateToString(millseconds);
        settingTimePresenter.performSettingTime(id,time);

    }
}
