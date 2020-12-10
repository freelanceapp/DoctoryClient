package com.doctory_client.ui.activity_reservision_detials;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.doctory_client.R;
import com.doctory_client.databinding.ActivityCompleteClinicReservisionBinding;
import com.doctory_client.databinding.ActivityReservisionDetialsBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.ApointmentModel;
import com.doctory_client.models.SingleDoctorModel;
import com.doctory_client.models.SingleReservisionTimeModel;
import com.doctory_client.models.UserModel;
import com.doctory_client.mvp.activity_complete_clinic_reservision.ActivityCompleteClinicReservationPresenter;
import com.doctory_client.mvp.activity_complete_clinic_reservision.ActivityCompleteClinicReservationView;
import com.doctory_client.mvp.actvity_reservision_detials_mvp.ActivityReservationDetialsPresenter;
import com.doctory_client.mvp.actvity_reservision_detials_mvp.ActivityReservationDetialsView;
import com.doctory_client.preferences.Preferences;
import com.doctory_client.share.Common;
import com.doctory_client.ui.activity_clinic_reservation.ClinicReservationActivity;

import io.paperdb.Paper;

public class ReservationDetialsActivity extends AppCompatActivity implements ActivityReservationDetialsView {
    private String lang;
    private ActivityReservisionDetialsBinding binding;
    private ApointmentModel.Data apointmentModel;

    private ActivityReservationDetialsPresenter presenter;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservision_detials);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        apointmentModel = (ApointmentModel.Data) intent.getSerializableExtra("data");

    }

    private void initView() {

        preferences = Preferences.getInstance();
        usermodel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);

        binding.setModel(apointmentModel);
        presenter = new ActivityReservationDetialsPresenter(this, this);


        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        binding.btncahngeresev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReservationDetialsActivity.this, ClinicReservationActivity.class);
                intent.putExtra("DATA",apointmentModel);
                startActivity(intent);
              //  presenter.addresrevision(usermodel, doctorModel, singletimemodel, date, dayname);

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
        Toast.makeText(ReservationDetialsActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onnotlogin() {


        Common.CreateDialogAlert(this, getResources().getString(R.string.please_sign_in_or_sign_up));
    }


    @Override
    public void onFailed() {
        Toast.makeText(ReservationDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onnotconnect(String msg) {
        Toast.makeText(ReservationDetialsActivity.this, msg, Toast.LENGTH_SHORT).show();

    }
}