package com.a1thefull.checkout_kindergarten;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;


public class PeopleListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView peopleList_recycleView;
    private MyAdapter_PeopleList adapter_peopleList;
    private FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_list);

        initView();
        toolbar_Navi();
        recycleView();
        setListener();




    }//oc

    private void initView(){

        fab_add= findViewById(R.id.fab_add);


    }//init

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
        drawerLayout= findViewById(R.id.layout_drawable);
        navigationView= findViewById(R.id.navi);
        navigationView.setItemIconTintList(null);
        drawerToggle= new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.people_list, R.string.people_list);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }


    private void recycleView(){

        peopleList_recycleView= findViewById(R.id.people_recycleView);
        adapter_peopleList= new MyAdapter_PeopleList(this);
        peopleList_recycleView.setAdapter(adapter_peopleList);

        RecyclerView.LayoutManager layoutManager= new GridLayoutManager(this, 3);
        peopleList_recycleView.setLayoutManager(layoutManager);


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
}//class
