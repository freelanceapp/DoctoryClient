package com.doctory_client.mvp.activity_favourite_mvp;

import com.doctory_client.models.FavouriteDoctorModel;

public interface FavouriteActivityView {
    void onFinished();
    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);


    void ondoctorsucess(FavouriteDoctorModel body);
}
