package com.onethefull.attendmobile.getlist;

public interface GetListView {

    void validation(String msg);
    void success();
    void error();
    void launch(Class cls);

}
