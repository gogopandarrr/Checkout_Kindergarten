package com.onethefull.attendmobile.model;

public class RowHeader {

    private int mBackground = -1;
    private String data;


    public RowHeader(String data){
        this.data = data;
    }

    public String getData(){
        return data;
    }

    public void setBackgroundColor(int color) {mBackground = color;}

    public int getBackgroundColor(){return mBackground;}

}
