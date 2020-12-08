package com.doctory_client.mvp.activity_doctor_detials_mvp;

import com.doctory_client.models.AllCityModel;
import com.doctory_client.models.AllSpiclixationModel;
import com.doctory_client.models.DoctorModel;
import com.doctory_client.models.SingleDataDoctorModel;

public interface DoctorDetialsActivityView {
    void onFinished();
    void onLoad();
    void onFinishload();
    void onFailed(String msg);


    void ondoctorsucess(SingleDataDoctorModel body);
}
