package com.onethefull.attendmobile.model;

import android.util.Log;

import com.onethefull.attendmobile.lists.Lists_Attendance;

import java.util.ArrayList;
import java.util.List;

public class AttendanceTableModel {

private List<ColumnHeader> columnHeaderList;
private List<RowHeader> rowHeaderList;
private List<List<Cell>> cellList;


    private List<ColumnHeader> createColumnHeaderList(){

        List<ColumnHeader> list = new ArrayList<>();

        //헤더 만들기
        list.add(new ColumnHeader("이름"));
        list.add(new ColumnHeader("등원시간"));
        list.add(new ColumnHeader("하원시간"));

        return list;

    }//

    private List<List<Cell>> createCellList(ArrayList<Lists_Attendance> attendanceArrayList){

        List<List<Cell>> lists = new ArrayList<>();

        for (int i = 0; i < attendanceArrayList.size(); i++){

            Lists_Attendance lists_attendance = attendanceArrayList.get(i);
            List<Cell> list = new ArrayList<>();

            list.add(new Cell("1-"+ i,lists_attendance.getName()));

            if (!lists_attendance.getInTime().equals("00:00")){
                list.add(new Cell("2-"+ i, lists_attendance.getInTime()));
            }else{
                list.add(new Cell("2-"+ i, "미등원"));
            }

            if (!lists_attendance.getOutTime().equals("00:00")){
                list.add(new Cell("2-"+ i, lists_attendance.getOutTime()));
            }else{
                list.add(new Cell("2-"+ i, "미하원"));
            }



            lists.add(list);
        }

        return lists;

    }//


    private List<RowHeader> createRowHeaderList(int size){
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < size; i++){
            list.add(new RowHeader(String.valueOf(i + 1)));
        }
        return list;
    }//


    public List<ColumnHeader> getColumnHeaderList(){
        return columnHeaderList;
    }

    public List<RowHeader> getRowHeaderList(){
        return rowHeaderList;
    }

    public List<List<Cell>> getCellList(){
        return cellList;
    }

    public void generateListForTableView(ArrayList<Lists_Attendance> attendanceArrayList ){
        columnHeaderList = createColumnHeaderList();
        cellList = createCellList(attendanceArrayList);
        rowHeaderList = createRowHeaderList(attendanceArrayList.size());

    }


}//class

