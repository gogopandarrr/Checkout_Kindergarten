package com.onethefull.attendmobile.getgotime;

public interface GetGoTimeView {

    void validation(String msg);
    void getGotimeSuccess(String goTime);
    void error();
    void launch(Class cls);
}
