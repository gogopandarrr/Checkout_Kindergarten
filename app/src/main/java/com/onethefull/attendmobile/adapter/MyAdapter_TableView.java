package com.onethefull.attendmobile.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.google.android.gms.vision.text.Line;
import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.lists.Lists_Attendance;
import com.onethefull.attendmobile.model.AttendanceTableModel;
import com.onethefull.attendmobile.model.Cell;
import com.onethefull.attendmobile.model.ColumnHeader;
import com.onethefull.attendmobile.model.RowHeader;

import java.util.ArrayList;
import java.util.List;


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



    /////////
    class MyCellViewHolder extends AbstractViewHolder{

        public final TextView tv_cell;
        public LinearLayout container_cell;

        public MyCellViewHolder(View itemView) {
            super(itemView);
            container_cell = itemView.findViewById(R.id.cell_container);
            tv_cell = itemView.findViewById(R.id.cell_data);
        }
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


        viewHolder.tv_cell.setText(String.valueOf((cell.getData())));



        viewHolder.container_cell.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.tv_cell.requestLayout();
    }



    ////////
    class MyColumnHeaderViewHolder extends AbstractViewHolder{

        public final TextView tv_column_header;
        LinearLayout container_column_header;
        ITableView iTableView;

        public MyColumnHeaderViewHolder(View itemView, ITableView pTableView) {
            super(itemView);
            iTableView = pTableView;
            tv_column_header = itemView.findViewById(R.id.column_header_tv);
            container_column_header = itemView.findViewById(R.id.column_header_container);
        }
    }


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


        headerViewHolder.container_column_header.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        headerViewHolder.tv_column_header.requestLayout();


    }



    //////////

    class MyRowHeaderViewHolder extends AbstractViewHolder{

        public final TextView tv_row_header;
        LinearLayout container_row_header;

        public MyRowHeaderViewHolder(View itemView) {
            super(itemView);
            tv_row_header = itemView.findViewById(R.id.tv_row_header);
            container_row_header = itemView.findViewById(R.id.container_row_header);

        }
    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(mContext).inflate(R.layout.table_row_header, parent, false);

        return new MyRowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {
        RowHeader rowHeader =  (RowHeader) rowHeaderItemModel;

        MyRowHeaderViewHolder rowHeaderViewHolder = (MyRowHeaderViewHolder) holder;
        rowHeaderViewHolder.tv_row_header.setText(rowHeader.getData());

    }


    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(mContext).inflate(R.layout.table_corner, null, false);
    }


    public void setTableModel(ArrayList<Lists_Attendance> attendanceList){

        tableModel.generateListForTableView(attendanceList);

        setAllItems(tableModel.getColumnHeaderList(), tableModel.getRowHeaderList(), tableModel.getCellList());


    }//


}//
