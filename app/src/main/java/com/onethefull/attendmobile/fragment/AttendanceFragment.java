package com.onethefull.attendmobile.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import com.onethefull.attendmobile.MainActivity;
import com.onethefull.attendmobile.R;

import com.onethefull.attendmobile.adapter.MyAdapter_TableView;
import com.onethefull.attendmobile.adapter.listener.TableViewListener;
import com.onethefull.attendmobile.api.SharedPrefManager;
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
    private String id, pwd;
    private TextView tv_date;
    private Button btn_prev, btn_next, btn_calendar;
    private boolean shouldShow = false;
    private TableView tableView;
    private MyAdapter_TableView adapter_tableView;
    private SharedPrefManager mSharedPrefs;



    public AttendanceFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        compactCalendarView = view.findViewById(R.id.calendar);
        tv_date = view.findViewById(R.id.tv_currentDay);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        btn_next = view.findViewById(R.id.btn_calendar_next);
        btn_prev = view.findViewById(R.id.btn_calendar_prev);
        btn_calendar = view.findViewById(R.id.btn_calendar);


        //메뉴바 타이틀설정
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.list_attendance));
        setHasOptionsMenu(true);


        //테이블뷰 작업
        tableView = view.findViewById(R.id.tableView_container);
        adapter_tableView = new MyAdapter_TableView(getActivity());
        tableView.setAdapter(adapter_tableView);
        tableView.setTableViewListener(new TableViewListener(tableView, getActivity()));
        adapter_tableView.setTableModel(attendanceArrayList);


        attendancePresenter = new GetAttendancePresenterImpl(getActivity(), AttendanceFragment.this);


        //저장된 id/비번 가져오기
        mSharedPrefs = SharedPrefManager.getInstance(getActivity());
        id = mSharedPrefs.getLoginId();
        pwd = mSharedPrefs.getLoginPwd();



        setListener();
        Date today = currentCalendar.getTime();
        attendancePresenter.getAttendanceList(id, SELECT_DATE.format(today));
        tv_date.setText(DAY_FORMAT.format(today));

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


        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendarView.scrollLeft();

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendarView.scrollRight();

            }
        });

        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!compactCalendarView.isAnimating()){
                    if (shouldShow){
                        compactCalendarView.clearAnimation();
                        compactCalendarView.showCalendarWithAnimation();
                        btn_calendar.setBackground(getResources().getDrawable(R.drawable.calendar_g));
                    }else {
                        compactCalendarView.hideCalendarWithAnimation();
                        btn_calendar.setBackground(getResources().getDrawable(R.drawable.calendar_b));
                    }
                    shouldShow = !shouldShow;
                }
            }
        });

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                compactCalendarView.setCurrentDate(Calendar.getInstance(Locale.getDefault()).getTime());
                tv_date.setText(DAY_FORMAT.format(Calendar.getInstance(Locale.getDefault()).getTime()));
                attendancePresenter.getAttendanceList(id, SELECT_DATE.format(Calendar.getInstance(Locale.getDefault()).getTime()));

            }
        });


    }//



    @Override
    public void validation(String msg) {

    }

    @Override
    public void error() {

    }

    @Override
    public void success(String date, ArrayList<Lists_Attendance> attendanceArrayList_pre) {


        attendanceArrayList.clear();

        for (int i = 0; i < attendanceArrayList_pre.size(); i++){
            attendanceArrayList.add(attendanceArrayList_pre.get(i));

        }




        adapter_tableView.setTableModel(attendanceArrayList);

    }


    //메뉴바에서 메뉴 감추기
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem search = menu.findItem(R.id.search);
        search.setVisible(false);

    }//



    //toolbar에서 메뉴 추가
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }//

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}//class
