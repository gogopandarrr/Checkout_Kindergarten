package com.onethefull.attendmobile.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.getattendance.GetAttendanceView;
import com.onethefull.attendmobile.lists.Lists_Attendance;

import java.util.ArrayList;

public class AttendanceFragment extends Fragment implements GetAttendanceView{

    private static final String TAG = AttendanceFragment.class.getSimpleName();
    private ArrayList<Lists_Attendance> attendanceArrayList = new ArrayList<>();



    public AttendanceFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);


        return view;

    }

    @Override
    public void validation(String msg) {

    }

    @Override
    public void error() {

    }

    @Override
    public void success(ArrayList<Lists_Attendance> attendanceArrayList_pre) {

    }
}
