package com.doctory_client.mvp.fragment_apointment_mvp;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.doctory_client.R;
import com.doctory_client.models.ApointmentModel;
import com.doctory_client.models.UserModel;
import com.doctory_client.models.UserSettingsModel;
import com.doctory_client.mvp.activity_splash_mvp.SplashView;
import com.doctory_client.preferences.Preferences;
import com.doctory_client.remote.Api;
import com.doctory_client.tags.Tags;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApointmentPresenter {
    private ApointmentFragmentView view;
    private Context context;

    public ApointmentPresenter(ApointmentFragmentView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getApointment(UserModel userModel) {

     //   Log.e("mmmmmmm",userModel.getData().getId()+"");
        view.showProgressBar();
        Api.getService(Tags.base_url).getMyApointment("off",1,1,userModel.getData().getId(),"normal")
                .enqueue(new Callback<ApointmentModel>() {
                    @Override
                    public void onResponse(Call<ApointmentModel> call, Response<ApointmentModel> response) {

                        if (response.isSuccessful()) {
                            view.hideProgressBar();
                            if (response.body() != null) {
                                view.onSuccess(response.body());

                            }

                        } else {

                            try {
                                view.onFailed(context.getString(R.string.failed));
                                Log.e("error", response.code() + "_" + response.errorBody().string());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApointmentModel> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    view.onFailed(context.getString(R.string.something));

                                } else {
                                    view.onFailed(context.getString(R.string.failed));

                                }
                            }


                        } catch (Exception e) {
                        }

                    }
                });
    }
}