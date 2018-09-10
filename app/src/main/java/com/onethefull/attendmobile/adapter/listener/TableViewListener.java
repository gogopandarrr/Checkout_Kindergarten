package com.onethefull.attendmobile.adapter.listener;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.evrencoskun.tableview.ITableView;

import com.evrencoskun.tableview.listener.ITableViewListener;

import com.onethefull.attendmobile.adapter.holder.MyCellViewHolder;
import com.onethefull.attendmobile.adapter.holder.MyColumnHeaderViewHolder;
import com.onethefull.attendmobile.adapter.popup.ColumnHeaderLongPressPopup;
import com.onethefull.attendmobile.adapter.popup.PowerMenuUtils;
import com.onethefull.attendmobile.model.Cell;

import com.skydoves.powermenu.OnMenuItemClickListener;

import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

public class TableViewListener implements ITableViewListener{

    private static final String TAG = TableViewListener.class.getSimpleName();
    private ITableView tableView;
    private Context mContext;
    private String tel;
    private PowerMenu cellMenu;

    public TableViewListener(ITableView tableView, Context context) {
        this.tableView = tableView;
        this.mContext = context;






    }//

    private OnMenuItemClickListener<PowerMenuItem> onCellClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {

            cellMenu.setSelectedPosition(position);

        }
    };

    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

    }

    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

        //전화번호 가져오기.
        Cell selectedCell = (Cell) tableView.getAdapter().getCellItem(3,row);
        tel = selectedCell.getData().toString();
        cellMenu = PowerMenuUtils.getContactParentMenu(mContext, (LifecycleOwner)mContext, onCellClickListener);
        cellMenu.showAsAnchorLeftTop(((MyCellViewHolder)cellView).tv_cell);



    }

    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {

    }

    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {
            if (columnHeaderView != null && columnHeaderView instanceof MyColumnHeaderViewHolder){
                ColumnHeaderLongPressPopup popup = new ColumnHeaderLongPressPopup((MyColumnHeaderViewHolder)columnHeaderView, tableView);


                popup.show();
            }


    }

    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

    }

    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

    }
}
