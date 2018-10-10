package com.onethefull.attendmobile.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.onethefull.attendmobile.adapter.MyAdapter_ClassList;
import com.onethefull.attendmobile.api.SharedPrefManager;
import com.onethefull.attendmobile.callclasslist.CallClassListPresenter;
import com.onethefull.attendmobile.callclasslist.CallClassListPresenterImpl;
import com.onethefull.attendmobile.callclasslist.CallClassListView;
import com.onethefull.attendmobile.getgotime.GetGoTimePresenter;
import com.onethefull.attendmobile.getgotime.GetGoTimePresenterImpl;
import com.onethefull.attendmobile.getgotime.GetGoTimeView;
import com.onethefull.attendmobile.lists.Lists_Class;
import com.onethefull.attendmobile.setgotime.SettingTimePresenter;
import com.onethefull.attendmobile.setgotime.SettingTimePresenterImpl;
import com.onethefull.attendmobile.setgotime.SettingTimeView;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SettingFragment extends android.support.v4.app.Fragment implements CallClassListView, ChangeNMView, SettingTimeView, GetGoTimeView, View.OnClickListener {
    private static final String TAG = SettingFragment.class.getSimpleName();

    private RelativeLayout layout_goTime, layout_comeTime, layout_changeName, layout_makeClass;
    private TextView tv_currentGoTime, tv_currentComeTime, tv_currentName;
    private ImageView iv_arrow_class;
    private SettingTimePresenter settingTimePresenter;
    private GetGoTimePresenter getGoTimePresenter;
    private ChangeNMPresenter changeNMPresenter;
    private CallClassListPresenter callClassListPresenter;
    private SharedPrefManager mSharedPrefs;
    private String id = "";
    private TimePickerDialog picker_time;
    private RecyclerView recyclerView_class;
    private MyAdapter_ClassList adapter_classList;
    private List<Lists_Class> classArrayList = new ArrayList<>();
    private boolean isOpen = false;
    private String time;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        //메뉴바 타이틀설정
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.setting));
        setHasOptionsMenu(true);

        callClassListPresenter = new CallClassListPresenterImpl(SettingFragment.this, getActivity());
        settingTimePresenter = new SettingTimePresenterImpl(SettingFragment.this, getActivity());
        getGoTimePresenter = new GetGoTimePresenterImpl(SettingFragment.this, getActivity());
        changeNMPresenter = new ChangeNMPresenterImpl(SettingFragment.this, getActivity());

        mSharedPrefs = SharedPrefManager.getInstance(getActivity());
        layout_changeName = view.findViewById(R.id.layout_changeName);
        layout_comeTime = view.findViewById(R.id.layout_comeTime);
        layout_goTime = view.findViewById(R.id.layout_goTime);
        layout_makeClass = view.findViewById(R.id.layout_makeClass);
        tv_currentComeTime = view.findViewById(R.id.tv_currentComeTime);
        tv_currentGoTime = view.findViewById(R.id.tv_currentGoTime);
        tv_currentName = view.findViewById(R.id.tv_currentKindergarten_name);
        iv_arrow_class = view.findViewById(R.id.icon_arrowOpen);


        recyclerView_class = view.findViewById(R.id.recycler_class);
        adapter_classList = new MyAdapter_ClassList(getActivity(), classArrayList);
        recyclerView_class.setAdapter(adapter_classList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_class.setLayoutManager(layoutManager);


        //설정된 하원시간 불러오기
        id = mSharedPrefs.getLoginId();
        getGoTimePresenter.performGetGoTime(id);
        //반리스트 불러오기
//        callClassListPresenter.callClassList(id);

        //유치원명 가져오기
        tv_currentName.setText(mSharedPrefs.getKindergarten());

        //리스너 달기
        layout_changeName.setOnClickListener(this);
        layout_goTime.setOnClickListener(this);
        layout_comeTime.setOnClickListener(this);
        layout_makeClass.setOnClickListener(this);



       return view;
    }


    //시간 변환
    public String getDateToString(long time){
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        return sf.format(d);
    }


    public void refreshList(){
        adapter_classList.notifyDataSetChanged();

    }


    @Override
    public void validation(String msg) {

    }

    @Override
    public void changeSuccess(String name) {

        tv_currentName.setText(name);
        ((MainActivity) getActivity()).tv_kindergarten.setText(name);
        mSharedPrefs.saveKindergarten(name);

    }

    @Override
    public void getGotimeSuccess(String goTime) {
        if (goTime != null) tv_currentGoTime.setText(goTime);
        else tv_currentGoTime.setText(getResources().getString(R.string.none));
    }

    @Override
    public void success() {
        Toast.makeText(getActivity(), getResources().getString(R.string.success_change_time), Toast.LENGTH_SHORT).show();
        getGoTimePresenter.performGetGoTime(id);
    }

    @Override
    public void callClassSuccess() {

        adapter_classList.notifyDataSetChanged();
    }

    @Override
    public void error() {
        Toast.makeText(getActivity(), getResources().getString(R.string.fail_change_name), Toast.LENGTH_SHORT).show();
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

    /////리스너들
    @Override
    public void onClick(View view) {
        switch (view.getId()){



            case R.id.layout_comeTime:

                //등원 타임피커 설정
                picker_time = new TimePickerDialog.Builder().setType(Type.HOURS_MINS).setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        time = getDateToString(millseconds);
                    }
                })
                        .setCancelStringId(getResources().getString(R.string.cancel))
                        .setSureStringId(getResources().getString(R.string.setting))
                        .setTitleStringId(getResources().getString(R.string.setting_come_time))
                        .setHourText(getResources().getString(R.string.hour))
                        .setMinuteText(getResources().getString(R.string.min))
                        .setThemeColor(getResources().getColor(R.color.holo_green_dark))
                        .setWheelItemTextSize(20)
                        .build();

                picker_time.show(getActivity().getSupportFragmentManager(), "hour_min");
                break;

            case R.id.layout_goTime:

                //하원 타임피커 설정
                picker_time = new TimePickerDialog.Builder().setType(Type.HOURS_MINS).setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        time = getDateToString(millseconds);
                        settingTimePresenter.performSettingTime(id,time);
                    }
                })
                        .setCancelStringId(getResources().getString(R.string.cancel))
                        .setSureStringId(getResources().getString(R.string.setting))
                        .setTitleStringId(getResources().getString(R.string.setting_go_time))
                        .setHourText(getResources().getString(R.string.hour))
                        .setMinuteText(getResources().getString(R.string.min))
                        .setThemeColor(getResources().getColor(R.color.main_blue))
                        .setWheelItemTextSize(20)
                        .build();

                picker_time.show(getActivity().getSupportFragmentManager(),"hour_min");
                break;


            case R.id.layout_changeName:
                new LovelyTextInputDialog(getActivity(), R.style.EditTextTintTheme)
                        .setTitle(R.string.dialog_change_name)
                        .setTitleGravity(Gravity.CENTER)
                        .setNegativeButton(getResources().getString(R.string.cancel), null)
                        .setConfirmButton(getResources().getString(R.string.change), new LovelyTextInputDialog.OnTextInputConfirmListener() {
                            @Override
                            public void onTextInputConfirmed(String text) {
                                changeNMPresenter = new ChangeNMPresenterImpl(SettingFragment.this, getActivity());
                                changeNMPresenter.changeNM(id, text);

                            }
                        }).show();
                break;

            case R.id.layout_makeClass:

                isOpen = !isOpen;

                if (isOpen){
                    recyclerView_class.animate().alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            recyclerView_class.setVisibility(View.VISIBLE);
                        }
                    });
                    iv_arrow_class.setRotation(90);
                }else{
                    recyclerView_class.animate().alpha(0.0f)
                            .translationY(0)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    recyclerView_class.setVisibility(View.GONE);
                                }
                            });
                    iv_arrow_class.setRotation(-90);
                }
                break;

        }

    }//


}
