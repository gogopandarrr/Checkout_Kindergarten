package com.onethefull.attendmobile;


import android.content.Intent;

import android.graphics.drawable.ColorDrawable;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;

import android.widget.TextView;
import com.onethefull.attendmobile.api.SharedPrefManager;
import com.onethefull.attendmobile.dialog.CustomTwoBtnDialog;

import com.onethefull.attendmobile.fragment.AttendanceFragment;
import com.onethefull.attendmobile.fragment.ChildrenListFragment;
import com.onethefull.attendmobile.fragment.SettingFragment;



public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    public TextView tv_kindergarten;
    private Button btn_close_drawer;
    private String id, kindergarten;
    private SharedPrefManager mSharedPrefs;
    private AttendanceFragment attendanceFragment;
    private ChildrenListFragment childrenListFragment;
    private SettingFragment settingFragment;
    private CustomTwoBtnDialog customTwoBtnDialog;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar_Navi();
        initView();
        setNavigationView();
        childrenListFragment();




    }//oc

    private void initView(){


        //저장된 id 가져오기
        mSharedPrefs = SharedPrefManager.getInstance(getApplicationContext());
        id = mSharedPrefs.getLoginId();
        //유치원명 가져오기
        tv_kindergarten.setText(mSharedPrefs.getKindergarten());

    }//init

    private void attendFrangment(){

        //프래그먼트 생성
        attendanceFragment = new AttendanceFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_content, attendanceFragment);
        transaction.commit();

    }//


    private void childrenListFragment(){
        childrenListFragment = new ChildrenListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_content, childrenListFragment);
        transaction.commit();


    }//

    private void settingFragment(){
        settingFragment = new SettingFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_content, settingFragment);
        transaction.commit();


    }//


    private void setNavigationView(){

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.navigation_home:

                        childrenListFragment();

                        break;


                    case R.id.navigation_logout:
                        logout();
                        break;


                    case R.id.navigation_attend:
                        attendFrangment();
                        break;


                    case R.id.navigation_setting:
                        settingFragment();
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




    private void toolbar_Navi(){

        android.support.v7.widget.Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.layout_drawable);
        navigationView= findViewById(R.id.navi);
        navigationView.setItemIconTintList(null);
        drawerToggle= new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.list_attendance, R.string.list_attendance);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        View headerview = navigationView.getHeaderView(0);
        btn_close_drawer = headerview.findViewById(R.id.btn_close_drawer);
        tv_kindergarten = headerview.findViewById(R.id.tv_kindergarten);



    }//


    public void logout(){

        customTwoBtnDialog = new CustomTwoBtnDialog(this, getResources().getString(R.string.wanna_logout), "logout", null);
        customTwoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customTwoBtnDialog.setCancelable(false);
        customTwoBtnDialog.show();


//        AlertDialogFragment dialogFragment = AlertDialogFragment.newInstance(getResources().getString(R.string.wanna_logout));
//        dialogFragment.show(getSupportFragmentManager(),"dialog");
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


                ChildrenListFragment childrenListFragment = (ChildrenListFragment) getSupportFragmentManager().findFragmentById(R.id.container_content);
                childrenListFragment.searchFilter(s);
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

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }


    @Override
    public void onBackPressed() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
        else if (transaction.isEmpty()) logout();
        else  super.onBackPressed();

    }





}//class
