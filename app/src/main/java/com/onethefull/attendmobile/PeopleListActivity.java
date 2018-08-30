package com.onethefull.attendmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.onethefull.attendmobile.account.changename.ChangeNMPresenterImpl;
import com.onethefull.attendmobile.account.changename.ChangeNMView;
import com.onethefull.attendmobile.adapter.MyAdapter_PeopleList;
import com.onethefull.attendmobile.api.SharedPrefManager;
import com.onethefull.attendmobile.api.TinyDB;
import com.onethefull.attendmobile.fragment.AlertDialogFragment;
import com.onethefull.attendmobile.getlist.GetListPresenterImpl;
import com.onethefull.attendmobile.getlist.GetListView;
import com.onethefull.attendmobile.lists.Lists_Student;
import com.onethefull.attendmobile.lists.Lists_downInfo;
import com.onethefull.wonderful_cv_library.CV_Package.Crypto;
import com.onethefull.wonderful_cv_library.CV_Package.Identity;
import com.onethefull.wonderful_cv_library.CV_Package.RequestUserImagesAsyncTask;
import com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class PeopleListActivity extends AppCompatActivity implements GetListView, ChangeNMView {


    private static final String TAG = PeopleListActivity.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView peopleList_recycleView;
    private MyAdapter_PeopleList adapter_peopleList;
    private FloatingActionButton fab_add;
    private ArrayList<Lists_downInfo> downInfoArrayList = new ArrayList<>();
    private ArrayList<Identity> userList = new ArrayList<>();
    private ImageView iv_noinfo;
    private TextView tv_kindergarten;
    private Button btn_close_drawer;
    private String id, kindergarten;
    private SharedPrefManager mSharedPrefs;
    private GetListPresenterImpl getListPresenter;
    private ChangeNMPresenterImpl changeNMPresenter;
    private Handler handler;
    private Runnable runnable;
    private SharedPreferences userInfo;
    TinyDB tinyDB;
    ArrayList<Object> stp;
    int mode;
    WonderfulCV wonderfulCV = new WonderfulCV();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_list);

        toolbar_Navi();
        initView();
        setNavigationView();
        setListener();
        getListPresenter.getInfo(id);




    }//oc

    private void initView(){

        tinyDB = new TinyDB(this);
        fab_add= findViewById(R.id.fab_add);
        iv_noinfo = findViewById(R.id.iv_noinfo);
        peopleList_recycleView= findViewById(R.id.people_recycleView);
        adapter_peopleList= new MyAdapter_PeopleList(this, downInfoArrayList, userList);
        peopleList_recycleView.setAdapter(adapter_peopleList);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        peopleList_recycleView.setLayoutManager(layoutManager);


        //저장된 id 가져오기
        mSharedPrefs = SharedPrefManager.getInstance(getApplicationContext());
        id = mSharedPrefs.getLoginId();
        id = id.replace("\"", "");

        //유치원명 가져오기
        kindergarten = mSharedPrefs.getKindergarten();
        kindergarten = kindergarten.replace("\"","");
        tv_kindergarten.setText(kindergarten);

        //리스트 가져오기
        getListPresenter = new GetListPresenterImpl(PeopleListActivity.this, getApplicationContext());




    }//init


    private void delayReflesh(){

        runnable = new Runnable() {
            @Override
            public void run() {
                adapter_peopleList.notifyDataSetChanged();
            }
        };

        handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }

    private void checkNolist(){
        if (downInfoArrayList.size()>0){
            iv_noinfo.setVisibility(View.GONE);
        }else iv_noinfo.setVisibility(View.VISIBLE);

    }//



    private void setNavigationView(){

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.navigation_changeNM:

                        new LovelyTextInputDialog(PeopleListActivity.this, R.style.EditTextTintTheme)

                                .setTitle(R.string.dialog_change_name)
                                .setIcon(R.drawable.pen)
                                .setNegativeButton("취소", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setConfirmButton("변경", new LovelyTextInputDialog.OnTextInputConfirmListener() {
                                    @Override
                                    public void onTextInputConfirmed(String text) {
                                        changeNMPresenter = new ChangeNMPresenterImpl(PeopleListActivity.this, getApplicationContext());
                                        changeNMPresenter.changeNM(id, text);

                                    }
                                }).show();

                        break;


                    case R.id.navigation_logout:
                        logout();
                        break;
                }

                drawerLayout.closeDrawer(navigationView);
                return false;
            }
        });


        btn_close_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               drawerLayout.closeDrawer(navigationView);
            }
        });




    }//setNavi





    private void setListener(){
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent= new Intent(PeopleListActivity.this, DetailViewActivity.class);
                int mode = 1; //등록 모드
                intent.putExtra("mode", mode);
                startActivityForResult(intent, 10);


            }
        });

    }//listener


    private void toolbar_Navi(){

        android.support.v7.widget.Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("원생목록");
        drawerLayout = findViewById(R.id.layout_drawable);
        navigationView= findViewById(R.id.navi);
        navigationView.setItemIconTintList(null);
        drawerToggle= new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.people_list, R.string.people_list);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        View headerview = navigationView.getHeaderView(0);
        btn_close_drawer = headerview.findViewById(R.id.btn_close_drawer);
        tv_kindergarten = headerview.findViewById(R.id.tv_kindergarten);



    }//


    private void logout(){
        AlertDialogFragment dialogFragment = AlertDialogFragment.newInstance("로그아웃 하시겠습니까?");
        dialogFragment.show(getSupportFragmentManager(),"dialog");
    }//


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem search = menu.findItem(R.id.search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;

    }//


    private void search(android.support.v7.widget.SearchView searchView){

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapter_peopleList.getFilter().filter(s);
                return true;
            }
        });

    }//



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getListPresenter.getInfo(id);

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        mode = intent.getIntExtra("mode",-1);
        getListPresenter.getInfo(id);


    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }


    @Override
    public void validation(String msg) {

    }

    @Override
    public void changeSuccess(String name) {

        tv_kindergarten.setText(name);
        Toast.makeText(this, R.string.changeNM_done, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void Error() {

    }

    @Override
    public void success(ArrayList<Lists_downInfo> downInfoArrayList_pre) {

        downInfoArrayList.clear();

        for (int i = 0; i < downInfoArrayList_pre.size(); i++){
            downInfoArrayList.add(downInfoArrayList_pre.get(i));

        }


        checkNolist();



        //cv서버 유저리스트 받아오기
        userInfo = getSharedPreferences("autoUser", MODE_PRIVATE);
        String loginEmail = userInfo.getString("user_email","");
        String loginPwd = userInfo.getString("user_pwd","");


        wonderfulCV.getFullServerAddress("1thefull.ml", 5000);
        wonderfulCV.initiateServerConnection(getApplicationContext(), "1thefull.ml", 5000,
                loginEmail, loginPwd);

        Boolean loginSucess = wonderfulCV.initiateServerConnection(this,
                "1thefull.ml", 5000,
                loginEmail, loginPwd);

        if (loginSucess) {
            //Login Success

        } else {
            //Login Fail
            Toast.makeText(this, "Login Failed! \nCheck email/password and internet connection", Toast.LENGTH_SHORT).show();
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

        delayReflesh();
    }


    @Override
    public void error() {

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode){


            case 10:
                if(resultCode==RESULT_OK) {

                    mode = data.getIntExtra("mode",-1);
                    getListPresenter.getInfo(id);

                }

                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}//class
