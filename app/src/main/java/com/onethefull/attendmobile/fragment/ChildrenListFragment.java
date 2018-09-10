package com.onethefull.attendmobile.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.onethefull.attendmobile.DetailViewActivity;
import com.onethefull.attendmobile.MainActivity;
import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.adapter.MyAdapter_PeopleList;
import com.onethefull.attendmobile.api.SharedPrefManager;
import com.onethefull.attendmobile.api.TinyDB;
import com.onethefull.attendmobile.getlist.GetListPresenterImpl;
import com.onethefull.attendmobile.getlist.GetListView;
import com.onethefull.attendmobile.lists.Lists_downInfo;
import com.onethefull.wonderful_cv_library.CV_Package.Crypto;
import com.onethefull.wonderful_cv_library.CV_Package.Identity;
import com.onethefull.wonderful_cv_library.CV_Package.RequestUserImagesAsyncTask;
import com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV;

import java.util.ArrayList;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

import static android.content.Context.MODE_PRIVATE;

public class ChildrenListFragment extends Fragment implements GetListView{


    private RecyclerView peopleList_recycleView;
    private MyAdapter_PeopleList adapter_peopleList;
    private FloatingActionButton fab_add;
    private ArrayList<Lists_downInfo> downInfoArrayList = new ArrayList<>();
    private ArrayList<Identity> userList = new ArrayList<>();
    private ImageView iv_noinfo;
    private Handler handler;
    private Runnable runnable;
    private GetListPresenterImpl getListPresenter;
    private ArrayList<Object> stp;
    private String id, pwd;
    private int count = 0;
    private SharedPrefManager mSharedPrefs;

    TinyDB tinyDB;
    WonderfulCV wonderfulCV = new WonderfulCV();





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frangment_student_list, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("원생 목록");

        tinyDB = new TinyDB(getActivity());
        fab_add = view.findViewById(R.id.fab_add);

        iv_noinfo = view.findViewById(R.id.iv_noinfo);
        peopleList_recycleView = view.findViewById(R.id.people_recycleView);
        adapter_peopleList= new MyAdapter_PeopleList(getActivity(), downInfoArrayList, userList);
        peopleList_recycleView.setAdapter(adapter_peopleList);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity());
        peopleList_recycleView.setLayoutManager(layoutManager);

        //저장된 id/비번 가져오기
        mSharedPrefs = SharedPrefManager.getInstance(getActivity());
        id = mSharedPrefs.getLoginId();
        pwd = mSharedPrefs.getLoginPwd();


        //리스트 가져오기
        getListPresenter = new GetListPresenterImpl(ChildrenListFragment.this, getActivity());
        getListPresenter.getInfo(id);

        setListener();



        return view;
    }//


    public void searchFilter(String s){
        adapter_peopleList.getFilter().filter(s);
    }//

    private void delayRefresh(){


        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                adapter_peopleList.notifyDataSetChanged();
                handler.postDelayed(runnable,1000);
                count++;

                if(count == 5){
                    handler.removeCallbacksAndMessages(null);
                    count = 0;
                }

            }
        };

        handler.post(runnable);






    }//


    private void checkNolist(){
        if (downInfoArrayList.size()>0){
            iv_noinfo.setVisibility(View.GONE);
        }else iv_noinfo.setImageResource(R.drawable.noinfo);

    }//


    private void setListener(){
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent= new Intent(getActivity(), DetailViewActivity.class);
                int mode = 1; //등록 모드
                intent.putExtra("mode", mode);
                startActivityForResult(intent, 10);


            }
        });

    }//listener


    @Override
    public void validation(String msg) {

    }

    @Override
    public void success(ArrayList<Lists_downInfo> downInfoArrayList_pre) {

        downInfoArrayList.clear();

        for (int i = 0; i < downInfoArrayList_pre.size(); i++){
            downInfoArrayList.add(downInfoArrayList_pre.get(i));
        }


        checkNolist();

        Crypto.deleteExistingTokenFromStorage();

        //cv서버 유저리스트 받아오기
        wonderfulCV.getFullServerAddress("1thefull.ml", 5000);
        wonderfulCV.initiateServerConnection(getActivity(), "1thefull.ml", 5000,
                id, pwd);

        Boolean loginSucess = wonderfulCV.initiateServerConnection(getActivity(),
                "1thefull.ml", 5000,
                id, pwd);

        if (loginSucess) {
            //Login Success

        } else {
            //Login Fail
            Toast.makeText(getActivity(), "Login Failed! \nCheck email/password and internet connection", Toast.LENGTH_SHORT).show();
        }







        RequestUserImagesAsyncTask requestUserImagesAsyncTask = new RequestUserImagesAsyncTask(new RequestUserImagesAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<Identity> arrayList) {


                if (arrayList.size() > 0){

                    //유저리스트 내부저장소 저장
                    stp = new ArrayList<>();

                    for (Identity a : arrayList){
                        stp.add(a);
                    }
                    tinyDB.putListObject("userList", stp);



                    userList.clear();
                    for (int i = 0; i < arrayList.size(); i++){
                        userList.add(arrayList.get(i));
                    }
                }

            }



        });


        if (wonderfulCV.checkIfServerConnectionInitialized() && wonderfulCV.token != null) {
            requestUserImagesAsyncTask.setRequestParameters(wonderfulCV.serverAddress +
                    "/api/users/", wonderfulCV.token, 99999);
            requestUserImagesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        delayRefresh();


    }

    @Override
    public void error() {

    }


    @Override
    public void onResume() {
        super.onResume();
        getListPresenter.getInfo(id);
    }

    @Override
    public void onPause() {
        super.onPause();
        count = 0;
        handler.removeCallbacksAndMessages(null);

    }

    @Override
    public void onStop() {
        super.onStop();
        count = 0;
        handler.removeCallbacksAndMessages(null);
    }




}//class
