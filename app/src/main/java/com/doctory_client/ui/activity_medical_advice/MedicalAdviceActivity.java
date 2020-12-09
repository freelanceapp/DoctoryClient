package com.doctory_client.ui.activity_medical_advice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.doctory_client.R;
import com.doctory_client.adapters.DoctorsAdapter;
import com.doctory_client.adapters.HelpDisseasAdapter;
import com.doctory_client.adapters.HelpSpicialAdapter;
import com.doctory_client.adapters.SpicialAdapter;
import com.doctory_client.databinding.ActivityMedicalAdviceBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.AllDiseasesModel;
import com.doctory_client.models.AllSpiclixationModel;
import com.doctory_client.models.DiseaseModel;
import com.doctory_client.models.SpecializationModel;
import com.doctory_client.mvp.activity_doctors_mvp.ActivityDoctorsPresenter;
import com.doctory_client.mvp.activity_medical_advice_mvp.ActivityMedicalAdvicePresenter;
import com.doctory_client.mvp.activity_medical_advice_mvp.MedicalAdviceActivityView;
import com.doctory_client.ui.activity_doctor.DoctorActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MedicalAdviceActivity extends AppCompatActivity implements MedicalAdviceActivityView {
    private ActivityMedicalAdviceBinding binding;
    private ActivityMedicalAdvicePresenter presenter;
    private String lang;
    private List<SpecializationModel> specializationModels;
    private HelpSpicialAdapter spicialAdapter;
    private List<DiseaseModel> diseaseModelList;
    private HelpDisseasAdapter helpDisseasAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medical_advice);
        initView();

    }

    private void initView() {
        presenter = new ActivityMedicalAdvicePresenter(this, this);
        specializationModels = new ArrayList<>();
        diseaseModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> finish());
        spicialAdapter = new HelpSpicialAdapter(specializationModels, this);
        binding.recViewSpecialization.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewSpecialization.setAdapter(spicialAdapter);
        helpDisseasAdapter = new HelpDisseasAdapter(diseaseModelList, this);
        binding.recViewdiseases.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewdiseases.setAdapter(helpDisseasAdapter);
        presenter.getSpecilization(1);
    }

    @Override
    public void onFinished() {
        finish();
    }

    @Override
    public void onProgressShow(int type) {
        if (type == 1) {
            binding.progBarSpecialization.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            binding.progBardisease.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onProgressHide(int type) {
        if (type == 1) {
            binding.progBarSpecialization.setVisibility(View.GONE);
        } else if (type == 2) {
            binding.progBardisease.setVisibility(View.GONE);

        }


    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(MedicalAdviceActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(AllSpiclixationModel allSpiclixationModel) {
        specializationModels.clear();
        specializationModels.addAll(allSpiclixationModel.getData());
        spicialAdapter.notifyDataSetChanged();
    }

    @Override
    public void ondiseasesuc(AllDiseasesModel body) {
        diseaseModelList.clear();
        diseaseModelList.addAll(body.getData());
        helpDisseasAdapter.notifyDataSetChanged();
    }

    public void onshowdisease(SpecializationModel specializationModel) {
        presenter.getdisease(2, specializationModel.getId());
    }
}