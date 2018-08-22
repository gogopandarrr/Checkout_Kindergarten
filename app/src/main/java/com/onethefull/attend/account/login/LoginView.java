package com.onethefull.attend.account.login;

public interface LoginView {
    void loginValidations(String msg);
    void loginSuccess();
    void loginError();
    void launch(Class cls);
}
