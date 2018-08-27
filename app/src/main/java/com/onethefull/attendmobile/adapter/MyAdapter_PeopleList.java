package com.onethefull.attendmobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.onethefull.attendmobile.DetailViewActivity;

import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.api.TinyDB;
import com.onethefull.attendmobile.lists.Lists_Student;
import com.bumptech.glide.Glide;
import com.onethefull.wonderful_cv_library.CV_Package.Identity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter_PeopleList extends RecyclerView.Adapter implements Filterable {

    Context context;
    ArrayList<Lists_Student> studentArrayList;
    ArrayList<Lists_Student> filtered;
    ArrayList<Identity> userList = new ArrayList<>();
    ArrayList<Object> stp;
    Lists_Student listsStudent;
    RecyclerView recyclerView;
    TinyDB tinyDB;
    String urlString;



    public MyAdapter_PeopleList(Context context, ArrayList<Lists_Student> studentArrayList, RecyclerView recyclerView) {
        this.context = context;
        this.studentArrayList = studentArrayList;
        this.recyclerView = recyclerView;
        this.filtered = studentArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);


        tinyDB = new TinyDB(context);

        View itemView;
        itemView= inflater.inflate(R.layout.list_people, viewGroup, false);

        final VH holder = new VH(itemView);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = holder.getAdapterPosition();
                int mode = 2; // 보기모드

                listsStudent = studentArrayList.get(position);
                Intent intent = new Intent(context, DetailViewActivity.class);
                intent.putExtra("listsStudent", listsStudent);
                intent.putExtra("position", position);
                intent.putExtra("mode", mode);

                context.startActivity(intent);

            }
        });



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        VH vh= (VH) viewHolder;


        Lists_Student lists_student = filtered.get(position);


        String cvid = lists_student.getCvid();



        //유저 리스트 불러오기

        stp = tinyDB.getListObject("userList", Identity.class);
        userList.clear();
        for (Object obj : stp){
            userList.add((Identity) obj);
        }


        //유저 리스트 cvid로 이미지 주소 찾기
        for (int i = 0; i < userList.size(); i++){

            if(userList.get(i).userId.contains(cvid)){

               urlString =  "http://1thefull.ml:5000/faceimages/"+userList.get(i).imageName;

            }


        }


         if (lists_student != null){

             vh.tv_name.setText(lists_student.getName_student());

             Glide.with(context).load(urlString).into(vh.iv_pic);

         }







    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }


    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()){
                    filtered = studentArrayList;
                }else{
                    ArrayList<Lists_Student> filteredList = new ArrayList<>();
                    for (Lists_Student listsStudent : studentArrayList){

                        if (listsStudent.getName_student().toLowerCase().contains(charString)){

                            filteredList.add(listsStudent);

                        }
                    }

                    filtered = filteredList;

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                filtered = (ArrayList<Lists_Student>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }


    private class VH extends RecyclerView.ViewHolder{

        CardView cardView;
        CheckBox checkBox;
        CircleImageView iv_pic;
        TextView tv_name;


        public VH(@NonNull final View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox_list);
            cardView = itemView.findViewById(R.id.cardView_people);
            iv_pic = itemView.findViewById(R.id.iv_pic);
            tv_name = itemView.findViewById(R.id.tv_name);


        }



    }




}//class
