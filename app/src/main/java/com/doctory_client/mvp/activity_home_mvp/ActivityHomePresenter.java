package com.doctory_client.mvp.activity_home_mvp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.Preference;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.doctory_client.R;
import com.doctory_client.models.UserModel;
import com.doctory_client.preferences.Preferences;
import com.doctory_client.remote.Api;
import com.doctory_client.tags.Tags;
import com.doctory_client.ui.activity_home.fragments.Fragment_Appointment;
import com.doctory_client.ui.activity_home.fragments.Fragment_Consulting;
import com.doctory_client.ui.activity_home.fragments.Fragment_Home;
import com.doctory_client.ui.activity_home.fragments.Fragment_Medicine;
import com.doctory_client.ui.activity_home.fragments.Fragment_More;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHomePresenter {
    private Context context;
    private FragmentManager fragmentManager;
    private HomeActivityView view;
    private Fragment_Home fragment_home;
    private Fragment_Appointment fragment_appointment;
    private Fragment_Consulting fragment_consulting;
    private Fragment_Medicine fragment_medicine;
    private Fragment_More fragment_more;
    private Preferences preference;
    private UserModel userModel;
    private double lat = 0.0, lng = 0.0;

    public ActivityHomePresenter(Context context, HomeActivityView view, FragmentManager fragmentManager, double lat, double lng) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.view = view;
        preference = Preferences.getInstance();
        userModel = preference.getUserData(context);
        this.lat = lat;
        this.lng = lng;
        displayFragmentHome();
        if (userModel != null) {
            updateToken();
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void manageFragments(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.appointment:
                displayFragmentAppointment();
                break;
            case R.id.consulting:
                displayFragmentConsulting();
                break;

            case R.id.medicine:
                displayFragmentMedicine();
                break;
            case R.id.more:
                displayFragmentMore();
                break;
            default:
                displayFragmentHome();
                break;
        }
    }

    private void displayFragmentHome() {
        if (fragment_home == null) {
            fragment_home = Fragment_Home.newInstance(lat, lng);
        }

        if (fragment_appointment != null && fragment_appointment.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_appointment).commit();
        }

        if (fragment_medicine != null && fragment_medicine.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_medicine).commit();
        }

        if (fragment_consulting != null && fragment_consulting.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_consulting).commit();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_home, "fragment_home").commit();
        }
    }

    public void displayFragmentAppointment() {
        if (fragment_appointment == null) {
            fragment_appointment = Fragment_Appointment.newInstance();
        }

        if (fragment_home != null && fragment_home.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_medicine != null && fragment_medicine.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_medicine).commit();
        }

        if (fragment_consulting != null && fragment_consulting.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_consulting).commit();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_appointment.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_appointment).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_appointment, "fragment_appointment").commit();
        }

    }

    public void displayFragmentConsulting() {
        if (fragment_consulting == null) {
            fragment_consulting = Fragment_Consulting.newInstance();
        }


        if (fragment_home != null && fragment_home.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_medicine != null && fragment_medicine.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_medicine).commit();
        }

        if (fragment_appointment != null && fragment_appointment.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_appointment).commit();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }


        if (fragment_consulting.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_consulting).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_consulting, "fragment_consulting").commit();
        }
    }

    private void displayFragmentMedicine() {
        if (fragment_medicine == null) {
            fragment_medicine = Fragment_Medicine.newInstance();
        }

        if (fragment_home != null && fragment_home.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_consulting != null && fragment_consulting.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_consulting).commit();
        }

        if (fragment_appointment != null && fragment_appointment.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_appointment).commit();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_medicine.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_medicine).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_medicine, "fragment_medicine").commit();
        }
    }

    private void displayFragmentMore() {
        if (fragment_more == null) {
            fragment_more = Fragment_More.newInstance();
        }

        if (fragment_home != null && fragment_home.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_consulting != null && fragment_consulting.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_consulting).commit();
        }

        if (fragment_appointment != null && fragment_appointment.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_appointment).commit();
        }
        if (fragment_medicine != null && fragment_medicine.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_medicine).commit();
        }
        if (fragment_more.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_more).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_more, "fragment_more").commit();
        }
    }

    public void backPress() {
        if (fragment_home != null && fragment_home.isAdded() && fragment_home.isVisible()) {
            if (userModel == null) {
                view.onNavigateToLoginActivity();
            } else {
                view.onFinished();
            }
        } else {
            displayFragmentHome();
            view.onHomeFragmentSelected();
        }
    }

    private void updateToken() {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            task.getResult().getId();
                            Log.e("sssssss", token);
                            Api.getService(Tags.base_url)
                                    .updateToken(userModel.getData().getId(), token, "android ")
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                            if (response.isSuccessful()) {
                                                try {
                                                    Log.e("Success", "token updated");
                                                } catch (Exception e) {
                                                    //  e.printStackTrace();
                                                }
                                            } else {
                                                try {
                                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            try {
                                                Log.e("Error", t.getMessage());
                                            } catch (Exception e) {
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void logout(UserModel userModel) {
        if (userModel != null) {
            view.onLoad();
            Log.e("dkdk", "Bearer " + userModel.getData().getToken());
            Api.getService(Tags.base_url)
                    .logout("Bearer " + userModel.getData().getToken())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            view.onFinishload();
                            if (response.isSuccessful() && response.body() != null) {
                                view.logout();
                            } else {
                                view.onFinishload();
                                view.onFailed(context.getString(R.string.something));
                                try {
                                    Log.e("error_codess", response.code() + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }


                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                view.onFinishload();
                                view.onFailed(context.getString(R.string.something));
                                Log.e("Error", t.getMessage());
                            } catch (Exception e) {

                            }
                        }
                    });
        } else {
            view.notlogin();
        }
    }

    public void notlogin() {
        view.notlogin();
    }
}
