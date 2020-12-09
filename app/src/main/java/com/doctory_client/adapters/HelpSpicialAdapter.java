package com.doctory_client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.doctory_client.R;
import com.doctory_client.databinding.HelpSplezationRowBinding;
import com.doctory_client.databinding.SpicialRowBinding;
import com.doctory_client.models.SpecializationModel;
import com.doctory_client.ui.activity_doctor.DoctorActivity;
import com.doctory_client.ui.activity_medical_advice.MedicalAdviceActivity;

import java.util.List;

public class HelpSpicialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SpecializationModel> list;
    private Context context;
    private LayoutInflater inflater;
private int i=-1;
    public HelpSpicialAdapter(List<SpecializationModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        HelpSplezationRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.help_splezation_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

myHolder.itemView.setOnClickListener(new
                                             View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                              if(context instanceof MedicalAdviceActivity){
                                                  MedicalAdviceActivity medicalAdviceActivity=(MedicalAdviceActivity) context;
                                                  medicalAdviceActivity.onshowdisease(list.get(position));
                                              }

                                                 }
                                             });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public HelpSplezationRowBinding binding;

        public MyHolder(@NonNull HelpSplezationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
