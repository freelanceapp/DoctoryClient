package com.doctory_client.mvp.activity_contactus_mvp;

import com.doctory_client.models.SettingModel;

public interface ActivityContactusView {

    void onFailed(String msg);
    void onLoad();
    void onFinishload();
    void onContactVaild();
    void onFailed();
    void onServer();

    void onnotconnect(String msg);

    void ViewSocial(String link);

    void onsetting(SettingModel body);
}
