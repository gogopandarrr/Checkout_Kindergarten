package com.onethefull.attendmobile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.lists.Lists_Attendance;
import com.onethefull.wonderful_cv_library.CV_Package.Identity;

import java.util.ArrayList;

public class MyAdapter_attendance extends RecyclerView.Adapter implements Filterable {

    private Context context;
    private ArrayList<Lists_Attendance> attendanceArrayList;
    private ArrayList<Lists_Attendance> filtered;
    private Lists_Attendance listsAttendance;
    private ArrayList<Identity> userList;


    public MyAdapter_attendance(Context context, ArrayList<Lists_Attendance> attendanceArrayList) {
        this.context = context;
        this.attendanceArrayList = attendanceArrayList;
        this.filtered = attendanceArrayList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_attenance, null);
        VH holder = new VH(itemView);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        VH vh = (VH) viewHolder;


        listsAttendance = filtered.get(position);
        String cvid = listsAttendance.getCvid();


        vh.tv_name.setText(listsAttendance.getName());
        vh.tv_inTime.setText(listsAttendance.getInTime());
        vh.tv_Outtime.setText(listsAttendance.getOutTime());


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
                    filtered = attendanceArrayList;
                }else{
                    ArrayList<Lists_Attendance> filteredList = new ArrayList<>();
                    for (Lists_Attendance listsAttendance : attendanceArrayList){
                        if (listsAttendance.getName().toLowerCase().contains(charString)){
                            filteredList.add(listsAttendance);
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

                filtered = (ArrayList<Lists_Attendance>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    private class VH extends RecyclerView.ViewHolder{

        TextView tv_name, tv_inTime, tv_Outtime;


        public VH(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name_a);
            tv_inTime = itemView.findViewById(R.id.tv_intime);
            tv_Outtime = itemView.findViewById(R.id.tv_outtime);



        }
    }


}//class
