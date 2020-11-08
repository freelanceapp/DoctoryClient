package com.doctory_client.ui.activity_doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import com.doctory_client.R;
import com.doctory_client.adapters.DoctorsAdapter;
import com.doctory_client.databinding.ActivityDoctorBinding;
import com.doctory_client.language.Language;

import java.util.ArrayList;

import io.paperdb.Paper;

public class DoctorActivity extends AppCompatActivity {
    private ActivityDoctorBinding binding;
    private String lang;
    private double lat=0.0,lng=0.0;
    private DoctorsAdapter adapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_doctor);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat",0.0);
        lng = intent.getDoubleExtra("lng",0.0);

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(new DoctorsAdapter(new ArrayList<>(),this));
        binding.progBar.setVisibility(View.GONE);
        binding.llBack.setOnClickListener(view -> finish());
    }
}