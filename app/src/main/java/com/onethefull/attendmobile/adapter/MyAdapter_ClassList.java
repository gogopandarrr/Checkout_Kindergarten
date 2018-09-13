package com.onethefull.attendmobile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.account.changename.ChangeNMPresenterImpl;
import com.onethefull.attendmobile.api.SharedPrefManager;
import com.onethefull.attendmobile.fragment.SettingFragment;
import com.onethefull.attendmobile.lists.Lists_Class;
import com.onethefull.attendmobile.setclass.SettingClassPresenter;
import com.onethefull.attendmobile.setclass.SettingClassPresenterImpl;
import com.onethefull.attendmobile.setclass.SettingClassView;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.List;

public class MyAdapter_ClassList extends RecyclerView.Adapter implements View.OnClickListener, SettingClassView {

    private Context context;
    private List<Lists_Class> classArrayList;
    private Lists_Class listsClass;
    private boolean headerFlag = false;
    private SharedPrefManager mSharedPrefs;
    private SettingClassPresenter settingClassPresenter;
    private String id;


    public MyAdapter_ClassList(Context context, List<Lists_Class> classArrayList) {
        this.context = context;
        this.classArrayList = classArrayList;

        settingClassPresenter = new SettingClassPresenterImpl(this, context);
        mSharedPrefs = SharedPrefManager.getInstance(context);
        id = mSharedPrefs.getLoginId();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;

        if (viewType == 0){//헤더일 경우
            headerFlag = true;
            itemView = inflater.inflate(R.layout.list_class_header, viewGroup, false);
        }else {//헤더가 아닐 경우
            headerFlag = false;
            itemView = inflater.inflate(R.layout.list_class, viewGroup, false);
        }

        final VH holder = new VH(itemView);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        VH vh = (VH) viewHolder;


        //헤더가 아닐 경우
        if (!vh.isHeader){


            vh.btn_modify.setOnClickListener(this);
            vh.btn_delete.setOnClickListener(this);

            listsClass = classArrayList.get(position);
            vh.tv_className.setText(listsClass.getName_kinder());

            //헤더일 경우
        }else{
           vh.header_add.setOnClickListener(this);
        }

    }//

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return classArrayList.size()+1;
    }


    //리스너
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.recycler_class_header:

                new LovelyTextInputDialog(context, R.style.EditTextTintTheme)
                        .setTitle(R.string.add_class)
                        .setNegativeButton(context.getResources().getString(R.string.cancel), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setConfirmButton(context.getResources().getString(R.string.change), new LovelyTextInputDialog.OnTextInputConfirmListener() {
                            @Override
                            public void onTextInputConfirmed(String text) {
                                settingClassPresenter.performSettingClass(id, text.trim());

                            }
                        }).show();

                break;


            case R.id.btn_modify_class:


                break;

            case R.id.btn_delete_class:


                break;

        }


    }//

    @Override
    public void validation(String msg) {

    }

    @Override
    public void setClassSuccess() {

    }

    @Override
    public void error() {

    }


    private class VH extends RecyclerView.ViewHolder{

        boolean isHeader = headerFlag;
        TextView tv_className;
        Button btn_modify, btn_delete;
        RelativeLayout header_add;


        public VH(@NonNull View itemView) {
            super(itemView);

            if (!isHeader) init(itemView);
            else  headerInit(itemView);

        }

        //헤더 초기화
        private void headerInit(View itemView){
            header_add = itemView.findViewById(R.id.recycler_class_header);
        }


        //그 밖 초기화
        private void init(View itemView){
            tv_className = itemView.findViewById(R.id.tv_class_name);
            btn_delete = itemView.findViewById(R.id.btn_delete_class);
            btn_modify = itemView.findViewById(R.id.btn_modify_class);
        }



    }//



}//
