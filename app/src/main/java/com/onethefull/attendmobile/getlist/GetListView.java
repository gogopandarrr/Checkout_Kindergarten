package com.onethefull.attendmobile.getlist;

import com.onethefull.attendmobile.lists.Lists_downInfo;

import java.util.ArrayList;

public interface GetListView {

    void validation(String msg);
    void success(ArrayList<Lists_downInfo> downInfoArrayList);
    void error();
    void launch(Class cls);

}
