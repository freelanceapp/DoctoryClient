package com.doctory_client.mvp.activity_home_mvp;

public interface HomeActivityView {
    void onHomeFragmentSelected();
    void onNavigateToLoginActivity();
    void onFinished();
    void onLoad();

    void onFinishload();

    void onFailed(String msg);

    void logout();

    void notlogin();
}
