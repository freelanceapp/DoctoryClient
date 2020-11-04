package com.doctory_client.ui.activity_home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.doctory_client.R;
import com.doctory_client.databinding.FragmentAppoinmentsBinding;
import com.doctory_client.databinding.FragmentConsultingBinding;

public class Fragment_Appointment extends Fragment {
    private FragmentAppoinmentsBinding binding;

    public static Fragment_Appointment newInstance(){
        return new Fragment_Appointment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_appoinments,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {

    }
}
