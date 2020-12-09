package com.doctory_client.ui.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.doctory_client.R;
import com.doctory_client.adapters.SliderAdapter;
import com.doctory_client.databinding.FragmentHomeBinding;
import com.doctory_client.models.Slider_Model;
import com.doctory_client.remote.Api;
import com.doctory_client.tags.Tags;
import com.doctory_client.ui.activity_doctor.DoctorActivity;
import com.doctory_client.ui.activity_emergency.EmergencyActivity;
import com.doctory_client.ui.activity_google_nearby_places.GoogleNearybyPlacesActivity;
import com.doctory_client.ui.activity_home.HomeActivity;
import com.doctory_client.ui.activity_medical_advice.MedicalAdviceActivity;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class Fragment_Home extends Fragment {
    private FragmentHomeBinding binding;
    private double lat=0.0,lng=0.0;
    private HomeActivity activity;
    private SliderAdapter sliderAdapter;
    private int current_page = 0, NUM_PAGES;


    public static Fragment_Home newInstance(double lat,double lng){
        Bundle bundle = new Bundle();
        bundle.putDouble("lat",lat);
        bundle.putDouble("lng",lng);
        Fragment_Home fragment_home = new Fragment_Home();
        fragment_home.setArguments(bundle);
        return fragment_home;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        initView();
        change_slide_image();
        return binding.getRoot();
    }
    private void change_slide_image() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (current_page == NUM_PAGES) {
                    current_page = 0;
                }
                binding.pager.setCurrentItem(current_page++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }
    private void initView() {
        activity = (HomeActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle!=null){
            lat = bundle.getDouble("lat");
            lng = bundle.getDouble("lng");
        }
        binding.tab.setupWithViewPager(binding.pager);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.cardViewPharmacy.setOnClickListener(view -> {
            Intent intent = new Intent(activity, GoogleNearybyPlacesActivity.class);
            intent.putExtra("query","pharmacy");
            intent.putExtra("ar_title","أقرب صيدلية");
            intent.putExtra("en_title","Pharmacy");
            intent.putExtra("lat",lat);
            intent.putExtra("lng",lng);
            startActivity(intent);
        });

        binding.cardViewAdvice.setOnClickListener(view -> {
            Intent intent = new Intent(activity, MedicalAdviceActivity.class);
            startActivity(intent);
        });

        binding.cardViewEmergency.setOnClickListener(view -> {
            Intent intent = new Intent(activity, EmergencyActivity.class);
            startActivity(intent);
        });

        binding.cardViewDoctor.setOnClickListener(view -> {
            Intent intent = new Intent(activity, DoctorActivity.class);
            intent.putExtra("lat",lat);
            intent.putExtra("lng",lng);
            startActivityForResult(intent,1);
        });

        getSliderData();
    }

    private void getSliderData() {
        binding.progBar.setVisibility(View.VISIBLE);
        binding.flNoAds.setVisibility(View.GONE);

        Api.getService(Tags.base_url).get_slider().enqueue(new Callback<Slider_Model>() {
            @Override
            public void onResponse(Call<Slider_Model> call, Response<Slider_Model> response) {
                binding.progBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    if (response.body().getData().size() > 0) {
                        NUM_PAGES = response.body().getData().size();

                        sliderAdapter = new SliderAdapter( response.body().getData(),activity);
                        binding.pager.setAdapter(sliderAdapter);
                        binding.flNoAds.setVisibility(View.GONE);


                    } else {

                        binding.pager.setVisibility(View.GONE);
                        binding.flNoAds.setVisibility(View.VISIBLE);
                    }
                } else if (response.code() == 404) {
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    binding.pager.setVisibility(View.GONE);
                } else {
                    binding.pager.setVisibility(View.GONE);
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Slider_Model> call, Throwable t) {
                try {
                    binding.progBar.setVisibility(View.GONE);
                    binding.pager.setVisibility(View.GONE);

                    Log.e("Errossr", t.toString());

                } catch (Exception e) {

                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK){
            activity.displayfragmentconsolt();
        }
    }

}
