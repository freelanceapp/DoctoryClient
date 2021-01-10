package com.doctory_client.mvp.activity_editprofile_mvp;


import com.doctory_client.models.DiseaseModel;
import com.doctory_client.models.UserModel;

import java.util.List;

public interface EditprofileActivityView {
    void onGenderSuccess(List<String> genderList);
    void onBloodSuccess(List<String>bloodList);
    void onDiseasesSuccess(List<DiseaseModel> diseaseModelList);
    void onDateSelected(String date);
    void onFailed(String msg);
    void onLoad();
    void onFinishload();

    void onFinished();

    void onupdateValid(UserModel body);
    void onFailed();
    void onServer();

    void onnotconnect(String msg);
}
