package com.doctory_client.ui.activity_emergency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.doctory_client.R;
import com.doctory_client.databinding.ActivityEmergencyBinding;
import com.doctory_client.databinding.ActivityMedicalAdviceBinding;
import com.doctory_client.language.Language;

import io.paperdb.Paper;

public class EmergencyActivity extends AppCompatActivity {
    private ActivityEmergencyBinding binding;
    private String lang;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_emergency);
        initView();

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> finish());
    }
}