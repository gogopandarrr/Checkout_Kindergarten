package com.a1thefull.checkout_kindergarten;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter_PeopleList extends RecyclerView.Adapter {

    Context context;

    public MyAdapter_PeopleList(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView;
        itemView= inflater.inflate(R.layout.list_people, viewGroup, false);

        VH holder= new VH(itemView);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        VH vh= (VH) viewHolder;


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class VH extends RecyclerView.ViewHolder{

        CircleImageView iv_pic;
        TextView tv_name;

        public VH(@NonNull View itemView) {
            super(itemView);


            iv_pic= itemView.findViewById(R.id.iv_pic);
            tv_name= itemView.findViewById(R.id.tv_name);

        }
    }


}//class
