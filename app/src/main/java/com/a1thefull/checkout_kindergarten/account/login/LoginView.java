package com.a1thefull.checkout_kindergarten.account.login;

public interface LoginView {
    void loginValidations(String msg);
    void loginSuccess();
    void loginError();
    void launch(Class cls);
}
