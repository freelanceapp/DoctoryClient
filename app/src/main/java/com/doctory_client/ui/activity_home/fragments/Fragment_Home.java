package com.doctory_client.ui.activity_home.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.doctory_client.R;
import com.doctory_client.adapters.SliderAdapter;
import com.doctory_client.databinding.FragmentHomeBinding;
import com.doctory_client.ui.activity_doctor.DoctorActivity;
import com.doctory_client.ui.activity_emergency.EmergencyActivity;
import com.doctory_client.ui.activity_google_nearby_places.GoogleNearybyPlacesActivity;
import com.doctory_client.ui.activity_home.HomeActivity;
import com.doctory_client.ui.activity_medical_advice.MedicalAdviceActivity;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment {
    private FragmentHomeBinding binding;
    private double lat=0.0,lng=0.0;
    private HomeActivity activity;
    private SliderAdapter sliderAdapter;


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
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle!=null){
            lat = bundle.getDouble("lat");
            lng = bundle.getDouble("lng");
        }
        binding.tab.setupWithViewPager(binding.pager);


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
            startActivity(intent);
        });

        getSliderData();
    }

    private void getSliderData() {
        List<Integer> data = new ArrayList<>();
        data.add(R.drawable.img1);
        data.add(R.drawable.img2);
        data.add(R.drawable.img3);
        data.add(R.drawable.img4);

        sliderAdapter = new SliderAdapter(data,activity);
        binding.pager.setAdapter(sliderAdapter);
        binding.flPager.setVisibility(View.VISIBLE);
        binding.flNoAds.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.GONE);
    }
}
