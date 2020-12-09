package com.doctory_client.mvp.activity_complete_clinic_reservision;

import com.doctory_client.models.ReservisionTimeModel;

public interface ActivityCompleteClinicReservationView {
    void onLoad();
    void onFinishload();
    void onFailed(String msg);

    void onsucsess();

    void onServer();

    void onnotlogin();

    void onnotconnect(String toLowerCase);

    void onFailed();
}
