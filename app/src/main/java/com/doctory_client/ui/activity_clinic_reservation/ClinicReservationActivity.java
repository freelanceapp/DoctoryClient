package com.doctory_client.ui.activity_clinic_reservation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.doctory_client.R;
import com.doctory_client.adapters.ChildReservisionHourAdapter;
import com.doctory_client.adapters.ReservisionHourAdapter;
import com.doctory_client.databinding.ActivityClinicReservationBinding;
import com.doctory_client.databinding.ActivityDoctorDetailsBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.ApointmentModel;
import com.doctory_client.models.DoctorModel;
import com.doctory_client.models.ReservisionTimeModel;
import com.doctory_client.models.SingleDoctorModel;
import com.doctory_client.models.SingleReservisionTimeModel;
import com.doctory_client.mvp.activity_clinic_reservation_mvp.ActivityClinicReservationPresenter;
import com.doctory_client.mvp.activity_clinic_reservation_mvp.ActivityClinicReservationView;
import com.doctory_client.share.Common;
import com.doctory_client.ui.activity_complete_clinic_reservision.CompleteClinicReservationActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ClinicReservationActivity extends AppCompatActivity implements ActivityClinicReservationView {
    private String lang;
    private ActivityClinicReservationBinding binding;
    private SingleDoctorModel doctorModel;
    private ApointmentModel.Data apointmentModel;

    private String type = "";
    private String date = "";
    private String time = "";
    private String dayname = "";
    private ActivityClinicReservationPresenter presenter;
    private List<SingleReservisionTimeModel> singleReservisionTimeModelList;
    private ReservisionHourAdapter reservisionHourAdapter;
    private List<SingleReservisionTimeModel.Detials> detialsList;
    private ChildReservisionHourAdapter childReservisionHourAdapter;
    private ProgressDialog dialog2;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_clinic_reservation);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.getSerializableExtra("data") != null) {
            doctorModel = (SingleDoctorModel) intent.getSerializableExtra("data");
        } else {
            apointmentModel = (ApointmentModel.Data) intent.getSerializableExtra("DATA");
        }
    }

    private void initView() {
        singleReservisionTimeModelList = new ArrayList<>();
        detialsList = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        presenter = new ActivityClinicReservationPresenter(this, this);
        reservisionHourAdapter = new ReservisionHourAdapter(singleReservisionTimeModelList, this);
        childReservisionHourAdapter = new ChildReservisionHourAdapter(detialsList, this);
        binding.recViewhour.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewhour.setAdapter(reservisionHourAdapter);
        binding.recViewchildhour.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recViewchildhour.setAdapter(childReservisionHourAdapter);
        type = "normal";
        binding.flClinic.setBackgroundResource(R.drawable.small_rounded_red_strock);
        binding.flLive.setBackgroundResource(0);
        binding.cardClinic.setOnClickListener(view -> {
            type = "normal";
            binding.flClinic.setBackgroundResource(R.drawable.small_rounded_red_strock);
            binding.flLive.setBackgroundResource(0);
            if(doctorModel!=null){
                presenter.getreservisiontime(doctorModel,type,date,dayname.toUpperCase());}
            else {
                presenter.getreservisiontime(apointmentModel.getDoctor_fk(), type, date, dayname.toUpperCase());

            }
        });

        binding.cardLive.setOnClickListener(view -> {
            type = "online";
            binding.flClinic.setBackgroundResource(0);
            binding.flLive.setBackgroundResource(R.drawable.small_rounded_red_strock);
            if(doctorModel!=null){
            presenter.getreservisiontime(doctorModel,type,date,dayname.toUpperCase());}
            else {
                presenter.getreservisiontime(apointmentModel.getDoctor_fk(), type, date, dayname.toUpperCase());

            }

        });

        binding.imageBack.setOnClickListener(view -> {
            finish();
        });
        binding.llDate.setOnClickListener(view -> presenter.showDateDialog(getFragmentManager()));
        gettimes();
    }

    private void gettimes() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String date = dateFormat.format(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.ENGLISH);
        String stringDate = sdf.format(System.currentTimeMillis());
        this.date = date;
        this.dayname = stringDate;
        if (doctorModel != null) {
            presenter.getreservisiontime(doctorModel, type, date, stringDate.toUpperCase());
        } else {
            date=apointmentModel.getDate();
            dayname=apointmentModel.getDay_name();
            type=apointmentModel.getReservation_type();
            Log.e("fkfkkf",date+dayname+type);
            presenter.getreservisiontime(apointmentModel.getDoctor_fk(), type, date, dayname.toUpperCase());
        }
        binding.tvDate.setText(date);

    }

    @Override
    public void onDateSelected(String date, String dayname) {
        this.date = date;
        binding.tvDate.setText(date);
        this.dayname = dayname;

        Log.e("llll", dayname);
        if (doctorModel != null) {
            presenter.getreservisiontime(doctorModel, type, date, dayname);
        } else {
            presenter.getreservisiontime(apointmentModel.getDoctor_fk(), type, date, dayname);

        }
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
    public void onreservtimesucess(ReservisionTimeModel body) {
        singleReservisionTimeModelList.clear();
        reservisionHourAdapter.notifyDataSetChanged();
        singleReservisionTimeModelList.addAll(body.getData());
        reservisionHourAdapter.notifyDataSetChanged();
        if (singleReservisionTimeModelList.size() == 0) {
            binding.tvNoData.setVisibility(View.VISIBLE);
        } else {
            binding.tvNoData.setVisibility(View.GONE);
        }
        detialsList.clear();
        childReservisionHourAdapter.notifyDataSetChanged();
    }

    public void getchild(int position) {
        detialsList.clear();
        detialsList.addAll(singleReservisionTimeModelList.get(position).getDetials());
        childReservisionHourAdapter.notifyDataSetChanged();
    }

    public void Setitem(SingleReservisionTimeModel.Detials detials) {
        Intent intent = new Intent(this, CompleteClinicReservationActivity.class);
        if (doctorModel != null) {
            intent.putExtra("data", doctorModel);
            intent.putExtra("type", 0);
            intent.putExtra("resrvid", 0);

        } else {
            intent.putExtra("type", apointmentModel.getId());
            intent.putExtra("data", apointmentModel.getDoctor_fk());
            intent.putExtra("resrvid", apointmentModel.getId());
            if (dayname.isEmpty()) {
                dayname = apointmentModel.getDay_name();
            }
        }
        intent.putExtra("time", detials);
        intent.putExtra("dayname", dayname);
        intent.putExtra("date", date);
        intent.putExtra("redate", type);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);

            finish();
        }
    }
}