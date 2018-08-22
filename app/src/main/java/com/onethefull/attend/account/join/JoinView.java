package com.onethefull.attend.account.join;

public interface JoinView {
    void validation(String msg);
    void success();
    void error();
    void launch(Class cls);
}
