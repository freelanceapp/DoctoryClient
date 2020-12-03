package com.doctory_client.mvp.activity_doctors_mvp;

import android.content.Context;
import android.util.Log;

import com.doctory_client.R;
import com.doctory_client.models.AllCityModel;
import com.doctory_client.models.AllSpiclixationModel;
import com.doctory_client.models.UserModel;
import com.doctory_client.preferences.Preferences;
import com.doctory_client.remote.Api;
import com.doctory_client.tags.Tags;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDoctorsPresenter {
    private UserModel userModel;
    private Preferences preferences;
    private DoctorsActivityView view;
    private Context context;

    public ActivityDoctorsPresenter(DoctorsActivityView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void backPress() {

        view.onFinished();


    }

    public void getSpecilization(int type)
    {
        // Log.e("tjtjtj",userModel.getIs_confirmed());
        view.onProgressShow(type);

        Api.getService(Tags.base_url)
                .getspicailest()
                .enqueue(new Callback<AllSpiclixationModel>() {
                    @Override
                    public void onResponse(Call<AllSpiclixationModel> call, Response<AllSpiclixationModel> response) {
                        view.onProgressHide(type);

                        if (response.isSuccessful() && response.body() != null) {
                            view.onSuccess(response.body());
                        } else {
                            view.onProgressHide(type);
                            view.onFailed(context.getString(R.string.something));
                            try {
                                Log.e("error_codess",response.code()+ response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<AllSpiclixationModel> call, Throwable t) {
                        try {
                            view.onProgressHide(type);
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }
    public void getcities(int type)
    {
        // Log.e("tjtjtj",userModel.getIs_confirmed());
        view.onProgressShow(type);

        Api.getService(Tags.base_url)
                .getcities()
                .enqueue(new Callback<AllCityModel>() {
                    @Override
                    public void onResponse(Call<AllCityModel> call, Response<AllCityModel> response) {
                        view.onProgressHide(type);

                        if (response.isSuccessful() && response.body() != null) {
                            view.onSuccesscitie(response.body());
                        } else {
                            view.onProgressHide(type);
                            view.onFailed(context.getString(R.string.something));
                            try {
                                Log.e("error_codess",response.code()+ response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<AllCityModel> call, Throwable t) {
                        try {
                            view.onProgressHide(type);
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }
}
