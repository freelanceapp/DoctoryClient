package com.doctory_client.ui.activity_complete_clinic_reservision;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doctory_client.R;
import com.doctory_client.adapters.ChildReservisionHourAdapter;
import com.doctory_client.adapters.ReservisionHourAdapter;
import com.doctory_client.databinding.ActivityClinicReservationBinding;
import com.doctory_client.databinding.ActivityCompleteClinicReservisionBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.ReservisionTimeModel;
import com.doctory_client.models.SingleDoctorModel;
import com.doctory_client.models.SingleReservisionTimeModel;
import com.doctory_client.models.UserModel;
import com.doctory_client.mvp.activity_clinic_reservation_mvp.ActivityClinicReservationPresenter;
import com.doctory_client.mvp.activity_clinic_reservation_mvp.ActivityClinicReservationView;
import com.doctory_client.mvp.activity_complete_clinic_reservision.ActivityCompleteClinicReservationPresenter;
import com.doctory_client.mvp.activity_complete_clinic_reservision.ActivityCompleteClinicReservationView;
import com.doctory_client.preferences.Preferences;
import com.doctory_client.share.Common;
import com.doctory_client.ui.activity_home.HomeActivity;
import com.doctory_client.ui.activity_sign_up.SignUpActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CompleteClinicReservationActivity extends AppCompatActivity implements ActivityCompleteClinicReservationView {
    private String lang;
    private ActivityCompleteClinicReservisionBinding binding;
    private SingleDoctorModel doctorModel;
    private String date = "";
    private String dayname = "";
    private ActivityCompleteClinicReservationPresenter presenter;
    SingleReservisionTimeModel.Detials singletimemodel;
    private ProgressDialog dialog2;
    private UserModel usermodel;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_complete_clinic_reservision);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        doctorModel = (SingleDoctorModel) intent.getSerializableExtra("data");
        singletimemodel = (SingleReservisionTimeModel.Detials) intent.getSerializableExtra("time");
        date = intent.getStringExtra("date");
        dayname = intent.getStringExtra("dayname");
    }

    private void initView() {

        preferences = Preferences.getInstance();
        usermodel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setDate(date);
        binding.setModel(doctorModel);
        binding.setTimemodel(singletimemodel);
        presenter = new ActivityCompleteClinicReservationPresenter(this, this);


        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        binding.btnConsultationReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addresrevision(usermodel, doctorModel, singletimemodel, date, dayname);

            }
        });


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
    public void onsucsess() {
        Intent intent=getIntent();
        setResult(RESULT_OK,intent);

        finish();
    }

    @Override
    public void onServer() {
        Toast.makeText(CompleteClinicReservationActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onnotlogin() {


        Common.CreateDialogAlert(this, getResources().getString(R.string.please_sign_in_or_sign_up));
    }


    @Override
    public void onFailed() {
        Toast.makeText(CompleteClinicReservationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onnotconnect(String msg) {
        Toast.makeText(CompleteClinicReservationActivity.this, msg, Toast.LENGTH_SHORT).show();

    }
}