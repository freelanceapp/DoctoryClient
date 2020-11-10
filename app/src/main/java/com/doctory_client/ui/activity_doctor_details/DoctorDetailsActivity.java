package com.doctory_client.ui.activity_doctor_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.doctory_client.adapters.DoctorsAdapter;
import com.doctory_client.adapters.FilterAdapter;
import com.doctory_client.databinding.ActivityDoctorDetailsBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.DoctorModel;
import com.doctory_client.models.FilterModel;
import com.doctory_client.ui.activity_clinic_reservation.ClinicReservationActivity;
import com.doctory_client.ui.activity_consulting_reservation.ConsultingReservationActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.ArrayList;

import io.paperdb.Paper;

public class DoctorDetailsActivity extends AppCompatActivity implements OnMapReadyCallback{
    private String lang;
    private ActivityDoctorDetailsBinding binding;
    private DoctorModel doctorModel;
    private GoogleMap mMap;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Transition transition = new Fade();
            transition.setInterpolator(new LinearInterpolator());
            transition.setDuration(5);
            getWindow().setEnterTransition(transition);
            getWindow().setExitTransition(transition);

        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_doctor_details);
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
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.llBack.setOnClickListener(view -> {super.onBackPressed();});
        binding.btnClinicReserve.setOnClickListener(view -> {
            Intent intent = new Intent(this, ClinicReservationActivity.class);
            intent.putExtra("data",doctorModel);
            startActivity(intent);
        });

        binding.btnConsultationReserve.setOnClickListener(view -> {
            Intent intent = new Intent(this, ConsultingReservationActivity.class);
            intent.putExtra("data",doctorModel);
            startActivity(intent);
        });
        updateUI();
    }
    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        fragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);

        }
    }

}