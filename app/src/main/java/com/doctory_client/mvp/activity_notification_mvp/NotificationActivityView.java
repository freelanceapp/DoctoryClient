package com.doctory_client.mvp.activity_notification_mvp;

import com.doctory_client.models.AllCityModel;
import com.doctory_client.models.AllSpiclixationModel;
import com.doctory_client.models.DoctorModel;
import com.doctory_client.models.NotificationModel;

public interface NotificationActivityView {
    void onFinished();

    void onProgressShow();

    void onProgressHide();

    void onFailed(String msg);

    void onSuccess(NotificationModel notificationModel);
}
