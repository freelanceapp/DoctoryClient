package com.doctory_client.mvp.activity_reservation_mvp;

import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.doctory_client.R;
import com.doctory_client.models.ApointmentModel;
import com.doctory_client.models.ReservisionTimeModel;
import com.doctory_client.models.RoomIdModel;
import com.doctory_client.models.SingleDoctorModel;
import com.doctory_client.models.UserModel;
import com.doctory_client.remote.Api;
import com.doctory_client.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityConsultingReservationPresenter {
    private Context context;
    private ActivityConsultingReservationView view;

    public ActivityConsultingReservationPresenter(Context context, ActivityConsultingReservationView view) {
        this.context = context;
        this.view = view;
    }

    public void createroom(ApointmentModel.Data apointmentModel, UserModel usermodel,String msg) {
        view.onLoad();

        try {
            Api.getService(Tags.base_url)
                    .createroom(usermodel.getData().getId()+"", apointmentModel.getDoctor_id()+"","message",msg).enqueue(new Callback<RoomIdModel>() {
                @Override
                public void onResponse(Call<RoomIdModel> call, Response<RoomIdModel> response) {
                   view.onFinishload();
                    if (response.isSuccessful()) {

                        Log.e("llll", response.toString());
                      view.onsucess(response.body());
                    } else {
                        try {
                            view.onFailed(context.getResources().getString(R.string.failed));

                          //  Toast.makeText(CartActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.errorBody().string());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<RoomIdModel> call, Throwable t) {
                   view.onFinishload();
                    try {
                        view.onFailed(context.getResources().getString(R.string.something));
                     //   Toast.makeText(CartActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
           view.onFinishload();
            Log.e("error", e.getMessage().toString());
        }
    }

    public void createroom(SingleDoctorModel doctorModel,UserModel usermodel,String msg) {
        view.onLoad();

        try {
            Api.getService(Tags.base_url)
                    .createroom(usermodel.getData().getId()+"", doctorModel.getId()+"","message",msg).enqueue(new Callback<RoomIdModel>() {
                @Override
                public void onResponse(Call<RoomIdModel> call, Response<RoomIdModel> response) {
                    view.onFinishload();
                    if (response.isSuccessful()) {

                        Log.e("llll", response.toString());
                        view.onsucess(response.body());
                    } else {
                        try {
                            view.onFailed(context.getResources().getString(R.string.failed));

                            //  Toast.makeText(CartActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.errorBody().string());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<RoomIdModel> call, Throwable t) {
                    view.onFinishload();
                    try {
                        view.onFailed(context.getResources().getString(R.string.something));
                        //   Toast.makeText(CartActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            view.onFinishload();
            Log.e("error", e.getMessage().toString());
        }
    }
}
