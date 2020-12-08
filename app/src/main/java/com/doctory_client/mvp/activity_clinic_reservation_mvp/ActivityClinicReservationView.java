package com.doctory_client.mvp.activity_clinic_reservation_mvp;

import com.doctory_client.models.ReservisionTimeModel;

public interface ActivityClinicReservationView {
    void onDateSelected(String date,String dayname);
    void onLoad();
    void onFinishload();
    void onFailed(String msg);

    void onreservtimesucess(ReservisionTimeModel body);
}
