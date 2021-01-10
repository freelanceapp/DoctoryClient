package com.doctory_client.mvp.actvity_reservision_detials_mvp;

import com.doctory_client.models.PlaceDetailsModel;
import com.doctory_client.models.ReasonModel;

public interface ActivityReservationDetialsView {
    void onLoad();
    void onFinishload();
    void onFailed(String msg);

    void onsucsess();

    void onServer();

    void onnotlogin();

    void onnotconnect(String toLowerCase);

    void onFailed();

    void onProgressShow();

    void onProgressHide();

    void onSuccess(ReasonModel body);}
