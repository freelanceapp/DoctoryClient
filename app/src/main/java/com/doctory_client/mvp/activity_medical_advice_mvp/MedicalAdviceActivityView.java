package com.doctory_client.mvp.activity_medical_advice_mvp;

import com.doctory_client.models.AllCityModel;
import com.doctory_client.models.AllDiseasesModel;
import com.doctory_client.models.AllSpiclixationModel;
import com.doctory_client.models.DoctorModel;

public interface MedicalAdviceActivityView {
    void onFinished();
    void onProgressShow(int type);
    void onProgressHide(int type);
    void onFailed(String msg);
    void onSuccess(AllSpiclixationModel allSpiclixationModel);

    void ondiseasesuc(AllDiseasesModel body);
}
