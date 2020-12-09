package com.doctory_client.ui.activity_doctor_details;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.doctory_client.R;
import com.doctory_client.adapters.DoctorsAdapter;
import com.doctory_client.adapters.FilterAdapter;
import com.doctory_client.adapters.RateAdapter;
import com.doctory_client.databinding.ActivityDoctorDetailsBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.DoctorModel;
import com.doctory_client.models.FilterModel;
import com.doctory_client.models.SingleDataDoctorModel;
import com.doctory_client.models.SingleDoctorModel;
import com.doctory_client.models.UserModel;
import com.doctory_client.mvp.activity_doctor_detials_mvp.ActivityDoctorDetialsPresenter;
import com.doctory_client.mvp.activity_doctor_detials_mvp.DoctorDetialsActivityView;
import com.doctory_client.preferences.Preferences;
import com.doctory_client.share.Common;
import com.doctory_client.ui.activity_clinic_reservation.ClinicReservationActivity;
import com.doctory_client.ui.activity_consulting_reservation.ConsultingReservationActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class DoctorDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, DoctorDetialsActivityView {
    private String lang;
    private ActivityDoctorDetailsBinding binding;
    private SingleDoctorModel doctorModel;
    private GoogleMap mMap;
    private Marker marker;
    private float zoom = 15.0f;
    private ActivityDoctorDetialsPresenter presenter;
    private Preferences preferences;
    private UserModel userModel;
    private ProgressDialog dialog2;
    private List<SingleDoctorModel.Rates> ratesList;
    private RateAdapter rateAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_details);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        doctorModel = (SingleDoctorModel) intent.getSerializableExtra("data");

    }

    private void initView() {
        ratesList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        presenter = new ActivityDoctorDetialsPresenter(this, this);
        if (userModel != null) {
            presenter.getSpecilization(doctorModel, userModel.getData().getId() + "");
        } else {
            presenter.getSpecilization(doctorModel, "");

        }
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.llBack.setOnClickListener(view -> {
            super.onBackPressed();
        });
        binding.btnClinicReserve.setOnClickListener(view -> {
            Intent intent = new Intent(this, ClinicReservationActivity.class);
            intent.putExtra("data", doctorModel);
            startActivityForResult(intent,1);

        });

        binding.btnConsultationReserve.setOnClickListener(view -> {
            Intent intent = new Intent(this, ConsultingReservationActivity.class);
            intent.putExtra("data", doctorModel);
            startActivity(intent);
        });
        updateUI();
        rateAdapter = new RateAdapter(ratesList, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recView.setAdapter(rateAdapter);
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

    private void AddMarker(double lat, double lng) {


        if (marker == null) {
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
        } else {
            marker.setPosition(new LatLng(lat, lng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));


        }
    }

    @Override
    public void onFinished() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onLoad() {
        dialog2 = Common.createProgressDialog(this, getString(R.string.wait));
        dialog2.setCancelable(false);
        dialog2.show();
    }

    @Override
    public void onFinishload() {
        dialog2.dismiss();
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ondoctorsucess(SingleDataDoctorModel body) {
        binding.setModel(body.getData());
        binding.progBar.setVisibility(View.GONE);
        binding.recView.setVisibility(View.VISIBLE);
        ratesList.clear();

        ratesList.addAll(body.getData().getRates_fk());
        rateAdapter.notifyDataSetChanged();
        if(ratesList.size()==0){
            binding.card.setVisibility(View.GONE);
        }
        AddMarker(body.getData().getLatitude(), body.getData().getLongitude());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            Intent intent=getIntent();
            setResult(RESULT_OK,intent);

            finish();}
    }

}