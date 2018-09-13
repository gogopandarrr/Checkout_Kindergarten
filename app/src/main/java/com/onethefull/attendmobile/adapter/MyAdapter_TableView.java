package com.onethefull.attendmobile.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.adapter.holder.MyCellViewHolder;
import com.onethefull.attendmobile.adapter.holder.MyColumnHeaderViewHolder;
import com.onethefull.attendmobile.adapter.holder.MyRowHeaderViewHolder;
import com.onethefull.attendmobile.lists.Lists_Attendance;
import com.onethefull.attendmobile.model.AttendanceTableModel;
import com.onethefull.attendmobile.model.Cell;
import com.onethefull.attendmobile.model.ColumnHeader;
import com.onethefull.attendmobile.model.RowHeader;
import java.util.ArrayList;



public class MyAdapter_TableView extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {


    private AttendanceTableModel tableModel;

    public MyAdapter_TableView(Context context) {
        super(context);
        this.tableModel = new AttendanceTableModel();
    }




    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return 0;
    }



    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.table_cell, parent, false);

        return new MyCellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int columnPosition, int rowPosition) {

        Cell cell = (Cell) cellItemModel;
        MyCellViewHolder viewHolder = (MyCellViewHolder) holder;

        //지브라 패턴 설정
        int bgColor = rowPosition % 2 == 0 ? ContextCompat.getColor(mContext, R.color.cell_background_color1) : ContextCompat.getColor(mContext, R.color.cell_background_color2);

        cell.setBackgroundColor(bgColor);

        viewHolder.setCell(cell);

    }


    ///////

    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.table_column_header, parent, false);
        return new MyColumnHeaderViewHolder(layout, getTableView());
    }


    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {

        ColumnHeader columnHeader = (ColumnHeader) columnHeaderItemModel;
        MyColumnHeaderViewHolder headerViewHolder = (MyColumnHeaderViewHolder) holder;

        headerViewHolder.tv_column_header.setText(columnHeader.getData());
        headerViewHolder.tv_column_header.requestLayout();
    }



    //////////


    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(mContext).inflate(R.layout.table_row_header, parent, false);

        return new MyRowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {

        RowHeader rowHeader =  (RowHeader) rowHeaderItemModel;
        MyRowHeaderViewHolder rowHeaderViewHolder = (MyRowHeaderViewHolder) holder;

        int bgColor = rowPosition % 2 == 0 ? ContextCompat.getColor(mContext, R.color.cell_background_color1) : ContextCompat.getColor(mContext, R.color.cell_background_color2);

        rowHeader.setBackgroundColor(bgColor);
        rowHeaderViewHolder.setRowHeader(rowHeader);

    }


    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(mContext).inflate(R.layout.table_corner, null, false);
    }


    public void setTableModel(ArrayList<Lists_Attendance> attendanceList){


        tableModel.generateListForTableView(attendanceList);

        if (attendanceList.size()>0){
            //등하원 기록 있을때..
            setAllItems(tableModel.getColumnHeaderList(), tableModel.getRowHeaderList(), tableModel.getCellList());
        }else{
            //등하원기록없으면 가짜데이터로 셀 보이게...
            setAllItems(tableModel.getColumnHeaderList(), tableModel.getEmptyRowHeaderList(), tableModel.getEmptyCellList());
        }

    }//


}//
