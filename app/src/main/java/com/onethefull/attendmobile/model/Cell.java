package com.onethefull.attendmobile.model;

import com.evrencoskun.tableview.filter.IFilterableModel;
import com.evrencoskun.tableview.sort.ISortableModel;

public class Cell implements ISortableModel, IFilterableModel {

    private String id;
    private Object data;
    private String filterKeyword;
    private int mBackground = -1;


   public  Cell(String id, Object data){
        this.id = id;
        this.data = data;
        this.filterKeyword = String.valueOf(data);
   }//


    @Override
    public String getFilterableKeyword() {
        return filterKeyword;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Object getContent() {
        return data;
    }


    public Object getData(){
        return data;
    }

    public void setData(String data1) {data = data1;}

    public void setBackgroundColor(int color) {mBackground = color;}

    public int getBackgroundColor(){return mBackground;}

    public void setFilterKeyword(String filterKeyword) {
        this.filterKeyword = filterKeyword;
    }


}//
