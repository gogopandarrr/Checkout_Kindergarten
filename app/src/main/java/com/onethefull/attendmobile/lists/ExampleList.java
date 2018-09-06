package com.onethefull.attendmobile.lists;

import java.util.ArrayList;
import java.util.Collections;

public class ExampleList {

    public static ArrayList<Lists_Attendance> getList(){

        ArrayList<Lists_Attendance> attendanceArrayList = new ArrayList<>();

        attendanceArrayList.add(new Lists_Attendance("진지","09:00","04:00","0"));
        attendanceArrayList.add(new Lists_Attendance("자지","09:01","04:10","0"));
        attendanceArrayList.add(new Lists_Attendance("마지","09:20","04:03","0"));
        attendanceArrayList.add(new Lists_Attendance("투지","09:03","04:30","0"));
        attendanceArrayList.add(new Lists_Attendance("다지","09:04","04:04","0"));
        attendanceArrayList.add(new Lists_Attendance("가지","09:50","04:00","0"));
        attendanceArrayList.add(new Lists_Attendance("차지","09:06","04:05","0"));
        attendanceArrayList.add(new Lists_Attendance("츄지","09:10","04:30","0"));
        attendanceArrayList.add(new Lists_Attendance("하지","09:03","04:03","0"));
        attendanceArrayList.add(new Lists_Attendance("훈지","09:02","04:02","0"));
        attendanceArrayList.add(new Lists_Attendance("바지","09:30","04:04","0"));
        attendanceArrayList.add(new Lists_Attendance("부지","09:02","04:05","0"));
        attendanceArrayList.add(new Lists_Attendance("진지","09:00","04:00","0"));
        attendanceArrayList.add(new Lists_Attendance("자지","09:01","04:10","0"));
        attendanceArrayList.add(new Lists_Attendance("마지","09:20","04:03","0"));
        attendanceArrayList.add(new Lists_Attendance("투지","09:03","04:30","0"));
        attendanceArrayList.add(new Lists_Attendance("다지","09:04","04:04","0"));
        attendanceArrayList.add(new Lists_Attendance("가지","09:50","04:00","0"));
        attendanceArrayList.add(new Lists_Attendance("차지","09:06","04:05","0"));
        attendanceArrayList.add(new Lists_Attendance("츄지","09:10","04:30","0"));
        attendanceArrayList.add(new Lists_Attendance("하지","09:03","04:03","0"));
        attendanceArrayList.add(new Lists_Attendance("훈지","09:02","04:02","0"));
        attendanceArrayList.add(new Lists_Attendance("바지","09:30","04:04","0"));
        attendanceArrayList.add(new Lists_Attendance("부지","09:02","04:05","0"));
        attendanceArrayList.add(new Lists_Attendance("진지","09:00","04:00","0"));
        attendanceArrayList.add(new Lists_Attendance("자지","09:01","04:10","0"));
        attendanceArrayList.add(new Lists_Attendance("마지","09:20","04:03","0"));
        attendanceArrayList.add(new Lists_Attendance("투지","09:03","04:30","0"));
        attendanceArrayList.add(new Lists_Attendance("다지","09:04","04:04","0"));
        attendanceArrayList.add(new Lists_Attendance("가지","09:50","04:00","0"));
        attendanceArrayList.add(new Lists_Attendance("차지","09:06","04:05","0"));
        attendanceArrayList.add(new Lists_Attendance("츄지","09:10","04:30","0"));
        attendanceArrayList.add(new Lists_Attendance("하지","09:03","04:03","0"));
        attendanceArrayList.add(new Lists_Attendance("훈지","09:02","04:02","0"));
        attendanceArrayList.add(new Lists_Attendance("바지","09:30","04:04","0"));
        attendanceArrayList.add(new Lists_Attendance("부지","09:02","04:05","0"));


       return attendanceArrayList;
    }

    public static ArrayList<Lists_Attendance> getSorted(){
        ArrayList<Lists_Attendance> attendanceArrayList = getList();
        Collections.sort(attendanceArrayList);
        return attendanceArrayList;
    }
}
