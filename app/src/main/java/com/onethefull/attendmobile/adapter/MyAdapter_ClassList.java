package com.onethefull.attendmobile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.account.changename.ChangeNMPresenterImpl;
import com.onethefull.attendmobile.api.SharedPrefManager;
import com.onethefull.attendmobile.deleteclass.DeleteClassPresenter;
import com.onethefull.attendmobile.deleteclass.DeleteClassPresenterImpl;
import com.onethefull.attendmobile.deleteclass.DeleteClassView;
import com.onethefull.attendmobile.fragment.SettingFragment;
import com.onethefull.attendmobile.lists.Lists_Class;
import com.onethefull.attendmobile.modifyclass.ModifyClassPresenter;
import com.onethefull.attendmobile.modifyclass.ModifyClassPresenterImpl;
import com.onethefull.attendmobile.modifyclass.ModifyClassView;
import com.onethefull.attendmobile.setclass.SettingClassPresenter;
import com.onethefull.attendmobile.setclass.SettingClassPresenterImpl;
import com.onethefull.attendmobile.setclass.SettingClassView;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class MyAdapter_ClassList extends RecyclerView.Adapter implements SettingClassView, ModifyClassView, DeleteClassView {

    private Context context;
    private List<Lists_Class> classArrayList;
    private Lists_Class listsClass;
    private boolean headerFlag = false;
    private SharedPrefManager mSharedPrefs;
    private SettingClassPresenter settingClassPresenter;
    private ModifyClassPresenter modifyClassPresenter;
    private DeleteClassPresenter deleteClassPresenter;
    private String id, classname, modifiedClassName;
    private int modifiedPosition, deletingPosition;
    private  VH vh;



    public MyAdapter_ClassList(Context context, List<Lists_Class> classArrayList) {
        this.context = context;
        this.classArrayList = classArrayList;

        settingClassPresenter = new SettingClassPresenterImpl(this, context);
        modifyClassPresenter = new ModifyClassPresenterImpl(this, context);
        deleteClassPresenter = new DeleteClassPresenterImpl(this, context);
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

        vh = (VH) viewHolder;


        //헤더가 아닐 경우
        if (!vh.isHeader){

            listsClass = classArrayList.get(position-1);
            vh.tv_className.setText(listsClass.getName_class());

            //헤더일 경우
        }else{

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




    @Override
    public void validation(String msg) {

    }

    @Override
    public void modifySuccess() {

        listsClass = classArrayList.get(modifiedPosition-1);
        listsClass.setName_class(modifiedClassName);

        Toast.makeText(context, R.string.done_chageClass, Toast.LENGTH_SHORT).show();

        new android.os.Handler().post((new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }));



    }

    @Override
    public void setClassSuccess(String code) {

        classArrayList.add(new Lists_Class(classname, code));
        Toast.makeText(context, R.string.done_add_class, Toast.LENGTH_SHORT).show();

        new android.os.Handler().post((new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }));


    }

    @Override
    public void deleteSuccess() {

        classArrayList.remove(deletingPosition-1);
        Toast.makeText(context, R.string.done_delete_class, Toast.LENGTH_SHORT).show();
        new android.os.Handler().post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public void error() {

    }


    public class VH extends RecyclerView.ViewHolder{

        boolean isHeader = headerFlag;
        TextView tv_className;
        Button btn_modify, btn_delete;
        RelativeLayout header_add;


        public VH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

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




        //리스너
        @Optional
        @OnClick({R.id.recycler_class_header, R.id.btn_modify_class, R.id.btn_delete_class})
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.recycler_class_header:

                    new LovelyTextInputDialog(context, R.style.EditTextTintTheme)
                            .setTitle(R.string.add_class)
                            .setTitleGravity(Gravity.CENTER)
                            .setNegativeButton(context.getResources().getString(R.string.cancel), null)
                            .setConfirmButton(context.getResources().getString(R.string.add), new LovelyTextInputDialog.OnTextInputConfirmListener() {
                                @Override
                                public void onTextInputConfirmed(String text) {
//                                settingClassPresenter.performSettingClass(id, text.trim());
                                    classname = text.trim();
                                    setClassSuccess("temp");

                                }
                            }).show();

                    break;

                case R.id.btn_modify_class:

                    modifiedPosition = getAdapterPosition();

                    if (modifiedPosition > -1){

                        listsClass = classArrayList.get(modifiedPosition-1);

                        final String code = listsClass.getCode_class();

                        new LovelyTextInputDialog(context, R.style.EditTextTintTheme)
                                .setMessageGravity(Gravity.CENTER)
                                .setMessage(R.string.modify_class)
                                .setNegativeButton(R.string.cancel, null)
                                .setConfirmButton(R.string.change, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                                    @Override
                                    public void onTextInputConfirmed(String name) {

//                                        modifyClassPresenter.performModifyClass(id, code, name.trim());
                                        modifiedClassName = name.trim();
                                        modifySuccess();

                                    }
                                }).show();

                    }

                    break;

                case R.id.btn_delete_class:

                deletingPosition = getAdapterPosition();

                    if (deletingPosition > -1){

                        listsClass = classArrayList.get(deletingPosition-1);

                        final String code = listsClass.getCode_class();
                        String name = listsClass.getName_class();

                        new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                                .setButtonsColorRes(R.color.main_blue)
                                .setMessageGravity(Gravity.CENTER)
                                .setTitleGravity(Gravity.CENTER)
                                .setMessage(" ")
                                .setTitle(name+context.getResources().getString(R.string.delete_class))
                                .setPositiveButton(R.string.delete, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                    deleteClassPresenter.deleteClass(id, code);
                                        deleteSuccess();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, null).show();

                    }

                    break;

            }


        }//


    }//



}//
