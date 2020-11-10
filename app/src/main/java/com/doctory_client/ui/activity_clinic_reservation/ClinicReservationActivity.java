package com.doctory_client.ui.activity_clinic_reservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.doctory_client.R;
import com.doctory_client.databinding.ActivityClinicReservationBinding;
import com.doctory_client.databinding.ActivityDoctorDetailsBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.DoctorModel;
import com.doctory_client.mvp.activity_clinic_reservation_mvp.ActivityClinicReservationPresenter;
import com.doctory_client.mvp.activity_clinic_reservation_mvp.ActivityClinicReservationView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import io.paperdb.Paper;

public class ClinicReservationActivity extends AppCompatActivity implements ActivityClinicReservationView {
    private String lang;
    private ActivityClinicReservationBinding binding;
    private DoctorModel doctorModel;
    private String type="";
    private String date ="";
    private String time = "";
    private ActivityClinicReservationPresenter presenter;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_clinic_reservation);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        doctorModel = (DoctorModel) intent.getSerializableExtra("data");

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        presenter = new ActivityClinicReservationPresenter(this,this);


        binding.cardClinic.setOnClickListener(view -> {
            type = "clinic";
            binding.flClinic.setBackgroundResource(R.drawable.small_rounded_red_strock);
            binding.flLive.setBackgroundResource(0);

        });

        binding.cardLive.setOnClickListener(view -> {
            type = "live";
            binding.flClinic.setBackgroundResource(0);
            binding.flLive.setBackgroundResource(R.drawable.small_rounded_red_strock);
        });

        binding.imageBack.setOnClickListener(view -> {
            finish();
        });

        binding.llDate.setOnClickListener(view -> presenter.showDateDialog(getFragmentManager()));
    }

    @Override
    public void onDateSelected(String date) {
        this.date = date;
        binding.tvDate.setText(date);
    }
}