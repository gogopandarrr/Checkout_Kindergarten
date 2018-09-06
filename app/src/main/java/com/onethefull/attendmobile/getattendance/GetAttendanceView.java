package com.onethefull.attendmobile.getattendance;

import com.onethefull.attendmobile.lists.Lists_Attendance;

import java.util.ArrayList;

public interface GetAttendanceView {

    void validation(String msg);
    void error();
    void success(String date, ArrayList<Lists_Attendance> attendanceArrayList);


}
