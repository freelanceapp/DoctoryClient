package com.doctory_client.ui.activity_consulting_reservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.doctory_client.R;
import com.doctory_client.databinding.ActivityClinicReservationBinding;
import com.doctory_client.databinding.ActivityConsultingReservationBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.ChatUserModel;
import com.doctory_client.models.DoctorModel;
import com.doctory_client.models.RoomIdModel;
import com.doctory_client.models.SingleDoctorModel;
import com.doctory_client.models.UserModel;
import com.doctory_client.mvp.activity_clinic_reservation_mvp.ActivityClinicReservationPresenter;
import com.doctory_client.mvp.activity_reservation_mvp.ActivityConsultingReservationPresenter;
import com.doctory_client.mvp.activity_reservation_mvp.ActivityConsultingReservationView;
import com.doctory_client.preferences.Preferences;
import com.doctory_client.share.Common;
import com.doctory_client.ui.chat_activity.ChatActivity;

import io.paperdb.Paper;

public class ConsultingReservationActivity extends AppCompatActivity implements ActivityConsultingReservationView {
    private String lang;
    private ActivityConsultingReservationBinding binding;
    private SingleDoctorModel doctorModel;
    private ActivityConsultingReservationPresenter presenter;
    private Preferences preferences;
    private UserModel userModel;
    private ProgressDialog dialog2;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_consulting_reservation);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        doctorModel = (SingleDoctorModel) intent.getSerializableExtra("data");

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        presenter = new ActivityConsultingReservationPresenter(this, this);
        binding.imageBack.setOnClickListener(view -> {
            finish();
        });
        binding.btsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = binding.edtConsultation.getText().toString();
                binding.edtConsultation.setError(null);
                if (msg != null && !msg.isEmpty()) {
                    presenter.createroom(doctorModel, userModel,msg);
                } else {
                    binding.edtConsultation.setError(getResources().getString(R.string.field_req));
                }
            }
        });

    }


    @Override
    public void onLoad() {
        if (dialog2 == null) {
            dialog2 = Common.createProgressDialog(this, getString(R.string.wait));
            dialog2.setCancelable(false);
        } else {
            dialog2.dismiss();
        }
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
    public void onsucess(RoomIdModel body) {
        ChatUserModel chatUserModel;
    
            chatUserModel = new ChatUserModel(doctorModel.getName(), doctorModel.getLogo(), doctorModel.getId() + "", body.getData().getId());

        
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat_user_data", chatUserModel);
        startActivityForResult(intent, 1000);
    }
}