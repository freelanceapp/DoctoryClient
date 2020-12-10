package com.doctory_client.mvp.activity_complete_clinic_reservision;

import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.doctory_client.R;
import com.doctory_client.models.ReservisionTimeModel;
import com.doctory_client.models.SignUpModel;
import com.doctory_client.models.SingleDoctorModel;
import com.doctory_client.models.SingleReservisionTimeModel;
import com.doctory_client.models.UserModel;
import com.doctory_client.remote.Api;
import com.doctory_client.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCompleteClinicReservationPresenter {
    private Context context;
    private ActivityCompleteClinicReservationView view;

    public ActivityCompleteClinicReservationPresenter(Context context, ActivityCompleteClinicReservationView view) {
        this.context = context;
        this.view = view;
    }
    public void addresrevision(UserModel userModel, SingleDoctorModel singleDoctorModel, SingleReservisionTimeModel.Detials detials,String date,String dayname) {

        //Log.e("llll",detials.getFrom_hour_type());
        if(userModel!=null) {
    view.onLoad();
    Api.getService(Tags.base_url)
            .addreservision(userModel.getData().getId() + "", singleDoctorModel.getId() + "", date, detials.getFrom(), singleDoctorModel.getDetection_price() + "", "normal", dayname.toUpperCase(),detials.getFrom_hour_type())
            .enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    view.onFinishload();
                    if (response.isSuccessful() && response.body() != null) {
                        //  Log.e("eeeeee", response.body().getUser().getName());
                        //view.onSignupValid(response.body());
                        view.onsucsess();
                    } else {
                        try {
                            Log.e("mmmmmmmmmmssss", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        if (response.code() == 500) {
                            view.onServer();
                        } else {
                            try {
                                view.onFailed(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        view.onFinishload();
                        if (t.getMessage() != null) {
                            Log.e("msg_category_error", t.getMessage() + "__");

                            if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                view.onnotconnect(t.getMessage().toLowerCase());
                                //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            } else {
                                view.onFailed();
                                // Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage() + "__");
                    }
                }
            });

}
else {
    view.onnotlogin();
}
    }
//    public void changeresrevision(UserModel userModel,int type, SingleReservisionTimeModel.Detials detials,String date,String dayname) {
//
//        //Log.e("llll",detials.getFrom_hour_type());
//        if(userModel!=null) {
//            view.onLoad();
//            Api.getService(Tags.base_url)
//                    .addreservision(userModel.getData().getId() + "", singleDoctorModel.getId() + "", date, detials.getFrom(), singleDoctorModel.getDetection_price() + "", "normal", dayname.toUpperCase(),detials.getFrom_hour_type())
//                    .enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            view.onFinishload();
//                            if (response.isSuccessful() && response.body() != null) {
//                                //  Log.e("eeeeee", response.body().getUser().getName());
//                                //view.onSignupValid(response.body());
//                                view.onsucsess();
//                            } else {
//                                try {
//                                    Log.e("mmmmmmmmmmssss", response.errorBody().string());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//
//                                if (response.code() == 500) {
//                                    view.onServer();
//                                } else {
//                                    try {
//                                        view.onFailed(response.errorBody().string());
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            try {
//                                view.onFinishload();
//                                if (t.getMessage() != null) {
//                                    Log.e("msg_category_error", t.getMessage() + "__");
//
//                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                        view.onnotconnect(t.getMessage().toLowerCase());
//                                        //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        view.onFailed();
//                                        // Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            } catch (Exception e) {
//                                Log.e("Error", e.getMessage() + "__");
//                            }
//                        }
//                    });
//
//        }
//        else {
//            view.onnotlogin();
//        }
//    }

}
