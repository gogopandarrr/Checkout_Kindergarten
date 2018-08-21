package com.a1thefull.checkout_kindergarten.account.join;

public interface JoinView {
    void validation(String msg);
    void success();
    void error();
    void launch(Class cls);
}
