package com.a1thefull.checkout_kindergarten;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a1thefull.checkout_kindergarten.lists.Lists_Student;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.internal.GoogleApiManager;

import java.util.ArrayList;
import java.util.logging.Handler;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter_PeopleList extends RecyclerView.Adapter {

    Context context;
    ArrayList<Lists_Student> studentArrayList;
    Boolean isChecked = false;
    RecyclerView recyclerView;



    public MyAdapter_PeopleList(Context context, ArrayList<Lists_Student> studentArrayList, RecyclerView recyclerView) {
        this.context = context;
        this.studentArrayList = studentArrayList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView;
        itemView= inflater.inflate(R.layout.list_people, viewGroup, false);

        VH holder = new VH(itemView);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {



        Lists_Student lists_student = studentArrayList.get(position);

         VH vh= (VH) viewHolder;

        vh.tv_name.setText(lists_student.getName_student());

        Glide.with(context).load(lists_student.getFacePhoto()).into(vh.iv_pic);





    }

    @Override
    public int getItemCount() {
        return studentArrayList.size();
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

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    isChecked = true;
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setChecked(isChecked);

                    return isChecked;
                }
            });


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (checkBox.isChecked()){
                        checkBox.setChecked(!isChecked);
                    }
                    else checkBox.setChecked(isChecked);
                }
            });

        }



    }




}//class
