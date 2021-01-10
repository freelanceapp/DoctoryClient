package com.doctory_client.mvp.fragment_medecine_mvp;

import com.doctory_client.models.DrugModel;

import java.util.List;

public interface MedecineFragmentView {
    void onSuccess(List<DrugModel> data);
    void onFailed(String msg);
    void showProgressBar();
    void hideProgressBar();


}
