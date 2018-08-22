package com.onethefull.attend;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.TextView;

import com.onethefull.attend.lists.Lists_Student;
import com.onethefull.attend.R;

import java.util.ArrayList;


public class PeopleListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView peopleList_recycleView;
    private MyAdapter_PeopleList adapter_peopleList;
    private FloatingActionButton fab_add;
    private ArrayList<Lists_Student> studentArrayList = new ArrayList<>();
    private TextView tv_kindergarten;
    private SearchView searchView;
    private Button btn_close_drawer;
    Lists_Student listsStudent;
    private Bitmap bmp;


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

        peopleList_recycleView= findViewById(R.id.people_recycleView);
        adapter_peopleList= new MyAdapter_PeopleList(this, studentArrayList, peopleList_recycleView);
        peopleList_recycleView.setAdapter(adapter_peopleList);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        peopleList_recycleView.setLayoutManager(layoutManager);




    }//init


    private void setNavigationView(){

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.navigation_logout:

                        AlertDialog.Builder builder = new AlertDialog.Builder(PeopleListActivity.this);

                        builder.setTitle("로그아웃 하시겠습니까?");

                        builder.setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        });

                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

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


        int mode = intent.getIntExtra("mode",-1);


        if (mode == 1) {
//            String name = intent.getStringExtra("name");
//            String tel = intent.getStringExtra("tel");
//            String email = intent.getStringExtra("email");
//            byte[] image = intent.getByteArrayExtra("byteImage");
//            bmp = BitmapFactory.decodeByteArray(
//                    intent.getByteArrayExtra("byteImage"),0, intent.getByteArrayExtra("byteImage").length);

            listsStudent = intent.getParcelableExtra("listsStudent");
            studentArrayList.add(listsStudent);

        }


        if (mode == 2 || mode == 4){

                int position = intent.getIntExtra("position",-1);
                listsStudent = intent.getParcelableExtra("listsStudent");

                if (position == -1){
                    studentArrayList.add(listsStudent);
                }else{
                    studentArrayList.set(position, listsStudent);
                }


                adapter_peopleList.notifyDataSetChanged();

        }

        if (mode == 3){
            int position = intent.getIntExtra("position",-1);
            studentArrayList.remove(position);
            adapter_peopleList.notifyDataSetChanged();

        }



    }//


    private void setListener(){
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(PeopleListActivity.this, PhotoActivity.class);
                startActivity(intent);
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




    }


    private void hideCheckbox(){

        adapter_peopleList.isChecked = false;

        for (int i = 0; i < peopleList_recycleView.getChildCount(); i++){
            View view = peopleList_recycleView.getChildAt(i);

            if (view == null) return;

            CheckBox checkBox = view.findViewById(R.id.checkbox_list);
            checkBox.setVisibility(View.GONE);

        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case 3:
                if(requestCode==RESULT_OK){
                    Uri uri= data.getData();


                        Intent intent= new Intent(this, DetailViewActivity.class);
                        intent.putExtra("uri",uri);
                        startActivity(intent);

                }

        }


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter_peopleList.notifyDataSetChanged();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getDatas(intent);

    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();


    }


}//class
