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

import com.onethefull.attendmobile.account.setchildren.DetailViewActivity;
import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.lists.Lists_Student;
import com.bumptech.glide.Glide;
import com.onethefull.attendmobile.lists.Lists_downInfo;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter_PeopleList extends RecyclerView.Adapter implements Filterable {

    Context context;
    ArrayList<Lists_downInfo> downInfoArrayList;
    ArrayList<Lists_downInfo> filtered;
    Lists_downInfo listsDownInfo;
    RecyclerView recyclerView;



    public MyAdapter_PeopleList(Context context, ArrayList<Lists_downInfo> downInfoArrayList, RecyclerView recyclerView) {
        this.context = context;
        this.downInfoArrayList = downInfoArrayList;
        this.recyclerView = recyclerView;
        this.filtered = downInfoArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView;
        itemView= inflater.inflate(R.layout.list_people, viewGroup, false);

        final VH holder = new VH(itemView);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = holder.getAdapterPosition();
                int mode = 2; // 보기모드
                listsDownInfo = downInfoArrayList.get(position);
                Intent intent = new Intent(context, DetailViewActivity.class);
                intent.putExtra("listsDownInfo", listsDownInfo);
                intent.putExtra("position", position);
                intent.putExtra("mode", mode);

                context.startActivity(intent);

            }
        });



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {



        Lists_downInfo lists_downInfo = filtered.get(position);

         VH vh= (VH) viewHolder;

        vh.tv_name.setText(lists_downInfo.getName());

//        Glide.with(context).load(lists_downInfo.getImage()).into(vh.iv_pic);





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
                    filtered = downInfoArrayList;
                }else{
                    ArrayList<Lists_downInfo> filteredList = new ArrayList<>();
                    for (Lists_downInfo listsDownInfo : downInfoArrayList){

                        if (listsDownInfo.getName().toLowerCase().contains(charString)){

                            filteredList.add(listsDownInfo);

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

                filtered = (ArrayList<Lists_downInfo>) filterResults.values;
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
