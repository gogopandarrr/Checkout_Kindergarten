package com.onethefull.attendmobile.adapter.listener;

import android.Manifest;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.evrencoskun.tableview.ITableView;

import com.evrencoskun.tableview.listener.ITableViewListener;

import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.adapter.holder.MyCellViewHolder;
import com.onethefull.attendmobile.adapter.holder.MyColumnHeaderViewHolder;
import com.onethefull.attendmobile.adapter.popup.ColumnHeaderLongPressPopup;
import com.onethefull.attendmobile.adapter.popup.PowerMenuUtils;
import com.onethefull.attendmobile.model.Cell;

import com.skydoves.powermenu.OnMenuItemClickListener;

import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

public class TableViewListener implements ITableViewListener {

    private static final String TAG = TableViewListener.class.getSimpleName();
    private ITableView tableView;
    private Context mContext;
    private String tel, name;
    private PowerMenu cellMenu;

    public TableViewListener(ITableView tableView, Context context) {
        this.tableView = tableView;
        this.mContext = context;


    }//

    private OnMenuItemClickListener<PowerMenuItem> onCellClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {


            switch (position) {

                case 0:

                    new LovelyStandardDialog(mContext, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                            .setButtonsColorRes(R.color.main_blue)
                            .setIcon(R.drawable.ic_calling)

                            .setMessage(name+mContext.getResources().getString(R.string.call_to_parent))
                            .setPositiveButton(mContext.getResources().getString(R.string.call), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //전화걸기
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:" + tel));
                                    //권한확인
                                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        Toast.makeText(mContext, R.string.error_permission, Toast.LENGTH_SHORT).show();
                                        return;
                                    }else{
                                        mContext.startActivity(intent);
                                    }

                                }
                            })
                            .setNegativeButton(mContext.getResources().getString(R.string.cancel), null).show();


                   break;


               case 1:


                   if (!tel.equals(mContext.getResources().getString(R.string.deleted_student))){
                       Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+tel));
                       intent.putExtra("sms_body", name+mContext.getResources().getString(R.string.message_to_parents));
                       mContext.startActivity(intent);
                   }else {
                       Toast.makeText(mContext, mContext.getResources().getString(R.string.deleted_student_toast), Toast.LENGTH_SHORT).show();
                   }

                   break;


           }

            cellMenu.dismiss();

        }
    };

    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

    }

    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

        //전화번호 가져오기.
        Cell selectedCell_tel = (Cell) tableView.getAdapter().getCellItem(3,row);
        //이름 가져오기
        Cell selectedCell_name = (Cell) tableView.getAdapter().getCellItem(0,row);
        tel = selectedCell_tel.getData().toString();
        name = selectedCell_name.getData().toString();

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
