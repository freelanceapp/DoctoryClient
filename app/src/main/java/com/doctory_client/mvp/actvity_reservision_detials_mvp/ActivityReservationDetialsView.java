package com.doctory_client.mvp.actvity_reservision_detials_mvp;

public interface ActivityReservationDetialsView {
    void onLoad();
    void onFinishload();
    void onFailed(String msg);

    void onsucsess();

    void onServer();

    void onnotlogin();

    void onnotconnect(String toLowerCase);

    void onFailed();
}
