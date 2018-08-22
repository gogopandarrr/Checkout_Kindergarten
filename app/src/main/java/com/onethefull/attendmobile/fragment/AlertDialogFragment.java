package com.onethefull.attendmobile.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.account.login.LoginActivity;
import com.onethefull.attendmobile.api.SharedPrefManager;


public class AlertDialogFragment extends DialogFragment {
    private SharedPrefManager mSharedPrefs;

    public static AlertDialogFragment newInstance(String title){
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        String title = getArguments().getString("title");
        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.btn_logout)
                .setTitle(title)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSharedPrefs = SharedPrefManager.getInstance(getContext());
                        boolean bool = mSharedPrefs.getLoginBoolean();
                        String id = mSharedPrefs.getLoginId();
                        Log.d("HomeFragment", String.valueOf(bool));
                        Log.d("HomeFragment",id);
                        mSharedPrefs.initialize(getContext());

                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
//                          FLAG_ACTIVITY_CLEAR_TASK : 현재 TASK를 비운다.
//                          FLAG_ACTIVITY_NEW_TASK : 새로운 TASK를 생성한다. (Activity가 아닌 곳에서 Activity를 띄울 때, 종종 사용하는 플래그)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); }
                        else {
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); }
                        startActivity(intent);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }
}
