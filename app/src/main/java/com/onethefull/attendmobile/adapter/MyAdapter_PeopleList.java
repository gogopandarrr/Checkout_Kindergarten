package com.onethefull.attendmobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.onethefull.attendmobile.DetailViewActivity;

import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.api.TinyDB;
import com.onethefull.attendmobile.lists.Lists_Student;
import com.bumptech.glide.Glide;
import com.onethefull.attendmobile.lists.Lists_downInfo;
import com.onethefull.wonderful_cv_library.CV_Package.Identity;
import com.onethefull.wonderful_cv_library.CV_Package.RequestUserImagesAsyncTask;
import com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter_PeopleList extends RecyclerView.Adapter implements Filterable {

    Context context;

    ArrayList<Lists_downInfo> downInfoArrayList;
    ArrayList<Lists_downInfo> filtered;
    ArrayList<Identity> userList;
    Lists_downInfo listsStudent;
    TinyDB tinyDB;
    String urlString;




    public MyAdapter_PeopleList(Context context, ArrayList<Lists_downInfo> downInfoArrayList, ArrayList<Identity> userList) {
        this.context = context;
        this.downInfoArrayList = downInfoArrayList;
        this.userList = userList;
        this.filtered = downInfoArrayList;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        tinyDB = new TinyDB(context);

        View itemView;
        itemView= inflater.inflate(R.layout.list_people, viewGroup, false);

        final VH holder = new VH(itemView);

//        //클릭시 보기 모드
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                int position = holder.getAdapterPosition();
//                int mode = 2; // 보기모드
//
//
//
//                if (position >= 0){
//                    listsStudent = downInfoArrayList.get(position);
//                    Intent intent = new Intent(context, DetailViewActivity.class);
//                    intent.putExtra("listsStudent", listsStudent);
//                    intent.putExtra("mode", mode);
//                    context.startActivity(intent);
//                }
//
//
//            }
//        });



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        VH vh= (VH) viewHolder;


        Lists_downInfo lists_student = filtered.get(position);

        String cvid = lists_student.getCvid();


        //유저 리스트 cvid로 이미지 주소 찾기
        for (int i = 0; i < userList.size(); i++){

            String list_cvid = userList.get(i).id.toString();

            if(list_cvid.equals(cvid)){

                if (!userList.get(i).imageName.equals("null")){

                    urlString =  "http://1thefull.ml:5000/faceimages/"+userList.get(i).imageName;

                    Glide.with(context).load(urlString).into(vh.iv_pic);
                    vh.tv_name.setText(lists_student.getName());
                    vh.iv_pic.setVisibility(View.VISIBLE);
                    vh.tv_sub.setVisibility(View.VISIBLE);
                    vh.tv_sub.setText(context.getResources().getString(R.string.children));

                }else{
                    //등록은 되었으나 cv서버에 사진등록이 안된 경우..
                    vh.tv_name.setText(R.string.error_userPic);
                    vh.iv_pic.setVisibility(View.GONE);
                    vh.tv_sub.setVisibility(View.GONE);

                }

            }

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
                    filtered = downInfoArrayList;
                }else{
                    ArrayList<Lists_downInfo> filteredList = new ArrayList<>();
                    for (Lists_downInfo listsStudent : downInfoArrayList){

                        if (listsStudent.getName().toLowerCase().contains(charString)){

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

                filtered = (ArrayList<Lists_downInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }


    public class VH extends RecyclerView.ViewHolder{

        @BindView(R.id.cardView_people) CardView cardView;
        @BindView(R.id.iv_pic) CircleImageView iv_pic;
        @BindView(R.id.tv_name) TextView tv_name;
        @BindView(R.id.tv_sub) TextView tv_sub;

        public VH(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

        @OnClick(R.id.cardView_people)
        public void OnCardViewClick(View view){


            switch (view.getId()){

                case R.id.cardView_people:

                    int position = getAdapterPosition();
                    int mode = 2; // 보기모드

                    if (position >= 0){
                        listsStudent = downInfoArrayList.get(position);
                        Intent intent = new Intent(context, DetailViewActivity.class);
                        intent.putExtra("listsStudent", listsStudent);
                        intent.putExtra("mode", mode);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent);
                    }

                    break;

                 default:
                    break;
            }
        }//



    }//vh




}//class
