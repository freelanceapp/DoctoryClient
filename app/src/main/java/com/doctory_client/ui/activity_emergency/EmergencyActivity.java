package com.doctory_client.ui.activity_emergency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import com.doctory_client.R;
import com.doctory_client.adapters.EmergencyAdapter;
import com.doctory_client.databinding.ActivityEmergencyBinding;
import com.doctory_client.databinding.ActivityMedicalAdviceBinding;
import com.doctory_client.language.Language;

import java.util.ArrayList;

import io.paperdb.Paper;

public class EmergencyActivity extends AppCompatActivity {
    private ActivityEmergencyBinding binding;
    private String lang;
    private EmergencyAdapter adapter;
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
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBar.setVisibility(View.GONE);
        binding.recView.setLayoutManager(new GridLayoutManager(this,2));
        binding.recView.setAdapter(new EmergencyAdapter(new ArrayList<>(),this));
        binding.llBack.setOnClickListener(view -> finish());

    }
}