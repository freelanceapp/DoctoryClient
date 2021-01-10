package com.doctory_client.mvp.activity_reservation_mvp;


import com.doctory_client.models.RoomIdModel;

public interface ActivityConsultingReservationView {
    void onLoad();
    void onFinishload();
    void onFailed(String msg);


    void onsucess(RoomIdModel body);
}
