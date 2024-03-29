package com.doctory_client.mvp.activity_doctors_mvp;

import com.doctory_client.models.AllCityModel;
import com.doctory_client.models.AllSpiclixationModel;
import com.doctory_client.models.DoctorModel;

public interface DoctorsActivityView {
    void onFinished();
    void onProgressShow(int type);
    void onProgressHide(int type);
    void onFailed(String msg);
    void onSuccess(AllSpiclixationModel allSpiclixationModel);
    void onSuccesscitie(AllCityModel allCityModel);

    void ondoctorsucess(DoctorModel body);
}
