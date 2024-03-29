package com.doctory_client.mvp.activity_medical_advice_mvp;

import android.content.Context;
import android.util.Log;

import com.doctory_client.R;
import com.doctory_client.models.AllAdviceModel;
import com.doctory_client.models.AllCityModel;
import com.doctory_client.models.AllDiseasesModel;
import com.doctory_client.models.AllSpiclixationModel;
import com.doctory_client.models.DiseaseModel;
import com.doctory_client.models.DoctorModel;
import com.doctory_client.models.SingleAdviceModel;
import com.doctory_client.models.SingleDataDoctorModel;
import com.doctory_client.models.SingleDoctorModel;
import com.doctory_client.models.SpecializationModel;
import com.doctory_client.models.UserModel;
import com.doctory_client.preferences.Preferences;
import com.doctory_client.remote.Api;
import com.doctory_client.tags.Tags;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMedicalAdvicePresenter {
    private UserModel userModel;
    private Preferences preferences;
    private MedicalAdviceActivityView view;
    private Context context;

    public ActivityMedicalAdvicePresenter(MedicalAdviceActivityView view, Context context) {
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
    public void getdisease(int type,int id)
    {
        // Log.e("tjtjtj",userModel.getIs_confirmed());
        view.onProgressShow(type);

        Api.getService(Tags.base_url)
                .getdiseasbyspicial(id)
                .enqueue(new Callback<AllDiseasesModel>() {
                    @Override
                    public void onResponse(Call<AllDiseasesModel> call, Response<AllDiseasesModel> response) {
                        view.onProgressHide(type);

                        if (response.isSuccessful() && response.body() != null) {
                            view.ondiseasesuc(response.body());
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
                    public void onFailure(Call<AllDiseasesModel> call, Throwable t) {
                        try {
                            view.onProgressHide(type);
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }
    public void getAdvice(DiseaseModel diseaseModel, SpecializationModel specializationModel)
    {
        view.onLoad();

        Api.getService(Tags.base_url)
                .getadvice(specializationModel.getId()+"",diseaseModel.getId()+"")
                .enqueue(new Callback<AllAdviceModel>() {
                    @Override
                    public void onResponse(Call<AllAdviceModel> call, Response<AllAdviceModel> response) {
                        view.onFinishload();

                        if (response.isSuccessful() && response.body() != null) {
                            if(response.body().getData().size()>0){
                            view.advicesucess(response.body().getData().get(0));}
                            else {
                                view.onnodata();
                            }
                        } else {
                            view.onFinishload();
                            view.onFailed(context.getString(R.string.something));
                            try {
                                Log.e("error_codess",response.code()+ response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<AllAdviceModel> call, Throwable t) {
                        try {
                            view.onFinishload();
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }
}
