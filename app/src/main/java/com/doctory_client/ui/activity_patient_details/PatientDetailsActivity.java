package com.doctory_client.ui.activity_patient_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.doctory_client.R;
import com.doctory_client.adapters.ChildDrugsAdapter;
import com.doctory_client.databinding.ActivityPatientDetailsBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.DrugModel;
import com.doctory_client.mvp.activity_patient_details_mvp.ActivityPatientDetailsPresenter;
import com.doctory_client.mvp.activity_patient_details_mvp.ActivityPatientDetailsView;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class PatientDetailsActivity extends AppCompatActivity implements ActivityPatientDetailsView {
    private ActivityPatientDetailsBinding binding;
    private String lang;
    private DrugModel drugModel;
    private ActivityPatientDetailsPresenter presenter;
    private List<DrugModel.Drugs> drugModelList;
    private ChildDrugsAdapter adapter;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patient_details);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.getSerializableExtra("data") != null) {
            drugModel = (DrugModel) intent.getSerializableExtra("data");
        }

    }

    private void initView() {
        binding.view.setVisibility(View.GONE);
        drugModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(drugModel.getDoctor_fk());
        binding.setTitle(drugModel.getDoctor_fk().getSpecialization_fk().getTitle());
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChildDrugsAdapter(drugModelList, this);
        binding.recView.setAdapter(adapter);
        presenter = new ActivityPatientDetailsPresenter(this, this);
        drugModelList.addAll(drugModel.getReservations_drugs());
        adapter.notifyDataSetChanged();
        binding.imageBack.setOnClickListener(view -> finish());

        binding.progBar.setVisibility(View.GONE);
    }


    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        binding.view.setVisibility(View.VISIBLE);
        binding.progBar.setVisibility(View.VISIBLE);
        drugModelList.clear();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        binding.progBar.setVisibility(View.GONE);

    }


}