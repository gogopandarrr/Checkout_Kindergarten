package com.a1thefull.checkout_kindergarten;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.a1thefull.checkout_kindergarten.lists.Lists_Student;

import java.util.ArrayList;


public class PeopleListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView peopleList_recycleView;
    private CheckBox checkBox;
    private MyAdapter_PeopleList adapter_peopleList;
    private FloatingActionButton fab_add;
    private ArrayList<Lists_Student> studentArrayList = new ArrayList<>();
    private Bitmap bmp;
    Boolean isChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_list);


        initView();
        toolbar_Navi();
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




    private void getDatas(Intent intent){


        int mode = intent.getIntExtra("mode",-1);


        if (mode == 1){

            String name = intent.getStringExtra("name");
            String tel = intent.getStringExtra("tel");
            String email = intent.getStringExtra("email");
            bmp = BitmapFactory.decodeByteArray(
                    intent.getByteArrayExtra("byteImage"),0, intent.getByteArrayExtra("byteImage").length);


            studentArrayList.add(new Lists_Student(bmp, name, tel, email));



        }


    }//


    private void setListener(){
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideCheckbox();
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
        if (adapter_peopleList.isChecked){

            hideCheckbox();

        }
        else super.onBackPressed();


    }
}//class
