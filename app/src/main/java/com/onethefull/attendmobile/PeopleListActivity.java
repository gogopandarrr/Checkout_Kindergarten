package com.onethefull.attendmobile;

import android.content.Intent;
import android.net.Uri;
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


import com.onethefull.attendmobile.adapter.MyAdapter_PeopleList;
import com.onethefull.attendmobile.api.SharedPrefManager;
import com.onethefull.attendmobile.fragment.AlertDialogFragment;
import com.onethefull.attendmobile.getlist.GetListPresenterImpl;
import com.onethefull.attendmobile.getlist.GetListView;
import com.onethefull.attendmobile.lists.Lists_Student;
import com.onethefull.attendmobile.lists.Lists_downInfo;

import java.util.ArrayList;


public class PeopleListActivity extends AppCompatActivity implements GetListView {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView peopleList_recycleView;
    private MyAdapter_PeopleList adapter_peopleList;
    private FloatingActionButton fab_add;
    private ArrayList<Lists_Student> studentArrayList = new ArrayList<>();
    private ImageView iv_noinfo;
    private TextView tv_kindergarten;
    private Button btn_close_drawer;
    private Lists_Student listsStudent;
    private String id;
    private SharedPrefManager mSharedPrefs;
    private GetListPresenterImpl getListPresenter;
    int mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_list);


        initView();
        toolbar_Navi();
        setNavigationView();
        setListener();



    }//oc

    private void initView(){

        fab_add= findViewById(R.id.fab_add);
        iv_noinfo = findViewById(R.id.iv_noinfo);
        peopleList_recycleView= findViewById(R.id.people_recycleView);
        adapter_peopleList= new MyAdapter_PeopleList(this, studentArrayList, peopleList_recycleView);
        peopleList_recycleView.setAdapter(adapter_peopleList);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        peopleList_recycleView.setLayoutManager(layoutManager);


        //저장된 id 가져오기
        mSharedPrefs = SharedPrefManager.getInstance(getApplicationContext());
        id = mSharedPrefs.getLoginId();
        id = id.replace("\"", "");


        //리스트 가져오기
        getListPresenter = new GetListPresenterImpl(PeopleListActivity.this, getApplicationContext());
        getListPresenter.getInfo(id);



    }//init

    private void checkNolist(){
        if (studentArrayList.size()>0){
            iv_noinfo.setVisibility(View.GONE);
        }else iv_noinfo.setVisibility(View.VISIBLE);

    }//



    private void setNavigationView(){

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

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




    private void getDatas(Intent intent){

        if (mode == 1) {
            listsStudent = intent.getParcelableExtra("listsStudent");
            studentArrayList.add(listsStudent);
            Log.e("size_ㅁㅁㅁ",studentArrayList.size()+"");

        }

        if (mode == 3){
            int position = intent.getIntExtra("position",-1);
            studentArrayList.remove(position);


        }

        adapter_peopleList.notifyDataSetChanged();




//        if (mode == 2 || mode == 4){
//
//                int position = intent.getIntExtra("position",-1);
//                listsStudent = intent.getParcelableExtra("listsStudent");
//
//                if (position == -1){
//                    studentArrayList.add(listsStudent);
//                }else{
//                    studentArrayList.set(position, listsStudent);
//                }
//
//
//                adapter_peopleList.notifyDataSetChanged();
//
//        }
//

    }//


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

        checkNolist();
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkNolist();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        mode = intent.getIntExtra("mode",-1);
        getDatas(intent);
        checkNolist();

    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }


    @Override
    public void validation(String msg) {

    }

    @Override
    public void success(ArrayList<Lists_downInfo> downInfoArrayList_pre) {


    }


    @Override
    public void error() {

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode){


            case 10:
                if(resultCode==RESULT_OK) {

                    int mode = data.getIntExtra("mode",-1);
                    Log.e("mode_10",mode+"");

                }

                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}//class
