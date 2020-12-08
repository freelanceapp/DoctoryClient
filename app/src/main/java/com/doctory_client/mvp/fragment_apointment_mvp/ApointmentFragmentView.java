package com.doctory_client.mvp.fragment_apointment_mvp;

import com.doctory_client.models.ApointmentModel;

public interface ApointmentFragmentView {
    void onSuccess(ApointmentModel apointmentModel);
    void onFailed(String msg);
    void showProgressBar();
    void hideProgressBar();

}
