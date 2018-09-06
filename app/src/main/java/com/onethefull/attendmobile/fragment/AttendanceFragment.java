package com.onethefull.attendmobile.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import com.onethefull.attendmobile.R;

import com.onethefull.attendmobile.adapter.MyAdapter_TableView;
import com.onethefull.attendmobile.getattendance.GetAttendancePresenterImpl;
import com.onethefull.attendmobile.getattendance.GetAttendanceView;


import com.onethefull.attendmobile.lists.Lists_Attendance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class AttendanceFragment extends Fragment implements GetAttendanceView{


    public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyy년 MM월 dd일");
    public static final SimpleDateFormat YEAR_MONTH_FORMAT = new SimpleDateFormat("yyyy년 MM월");
    public static final SimpleDateFormat SELECT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final String TAG = AttendanceFragment.class.getSimpleName();
    private Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
    private ArrayList<Lists_Attendance> attendanceArrayList = new ArrayList<>();
    private GetAttendancePresenterImpl attendancePresenter;
    private CompactCalendarView compactCalendarView;
    private SharedPreferences userInfo;
    private String id;
    private Button btn_switch;
    private TextView tv_date;
    private boolean shouldShow = false;
    private TableView tableView;
    private MyAdapter_TableView adapter_tableView;



    public AttendanceFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        compactCalendarView = view.findViewById(R.id.calendar);
        btn_switch = view.findViewById(R.id.btn_switch_attend);
        tv_date = view.findViewById(R.id.tv_currentDay);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //테이블뷰 작업
        tableView = view.findViewById(R.id.tableView_container);
        adapter_tableView = new MyAdapter_TableView(getActivity());
        tableView.setAdapter(adapter_tableView);
        adapter_tableView.setTableModel(attendanceArrayList);

        attendancePresenter = new GetAttendancePresenterImpl(getActivity(), AttendanceFragment.this);


        //아이디, 비번 가져오기
        userInfo = getActivity().getSharedPreferences("autoUser", MODE_PRIVATE);
        id = userInfo.getString("user_email","");
        setListener();
        addEvents(-1,-1);


        return view;

    }



    private void setListener(){

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener(){

            //날짜 클릭시
            @Override
            public void onDayClick(Date dateClicked) {
                tv_date.setText(DAY_FORMAT.format(dateClicked));
                attendancePresenter.getAttendanceList(id, SELECT_DATE.format(dateClicked));

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                tv_date.setText(YEAR_MONTH_FORMAT.format(firstDayOfNewMonth));
            }
        });



        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!compactCalendarView.isAnimating()){
                    if (shouldShow){
                        compactCalendarView.clearAnimation();
                        compactCalendarView.showCalendar();
                    }else {
                        compactCalendarView.hideCalendar();
                    }
                    shouldShow = !shouldShow;

                }
            }
        });

    }//


    private void addEvents(int month, int year){
        currentCalendar.setTime(new Date());
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = currentCalendar.getTime();
        attendancePresenter.getAttendanceList(id, SELECT_DATE.format(firstDayOfMonth));

        for (int i = 0; i < 6; i++){
            currentCalendar.setTime(firstDayOfMonth);
            if (month > -1){
                currentCalendar.set(Calendar.MONTH, month);
            }
            if (year > -1){
                currentCalendar.set(Calendar.ERA, GregorianCalendar.AD);
                currentCalendar.set(Calendar.YEAR, year);
            }
            currentCalendar.add(Calendar.DATE, i);
            setToMidnight(currentCalendar);

        }



    }//

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }//


    @Override
    public void validation(String msg) {

    }

    @Override
    public void error() {

    }

    @Override
    public void success(String date, ArrayList<Lists_Attendance> attendanceArrayList_pre) {


//        attendanceArrayList.clear();

        for (int i = 0; i < attendanceArrayList_pre.size(); i++){
            attendanceArrayList.add(attendanceArrayList_pre.get(i));

        }




        adapter_tableView.setTableModel(attendanceArrayList);

    }



}//class
