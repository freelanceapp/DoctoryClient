package com.doctory_client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.doctory_client.R;
import com.doctory_client.databinding.HelpDissesRowBinding;
import com.doctory_client.databinding.HelpSplezationRowBinding;
import com.doctory_client.models.DiseaseModel;
import com.doctory_client.models.SpecializationModel;
import com.doctory_client.ui.activity_medical_advice.MedicalAdviceActivity;

import java.util.List;

public class HelpDisseasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DiseaseModel> list;
    private Context context;
    private LayoutInflater inflater;
    private int i = 0;

    public HelpDisseasAdapter(List<DiseaseModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        HelpDissesRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.help_disses_row, parent, false);
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
                                                             i = position;
                                                             notifyDataSetChanged();
                                                         }
                                                     });
        if (i == position) {
            ((MyHolder) holder).binding.card.setCardBackgroundColor(context.getResources().getColor(R.color.color1));
            ((MyHolder) holder).binding.tv.setTextColor(context.getResources().getColor(R.color.white));
            if (context instanceof MedicalAdviceActivity) {
                MedicalAdviceActivity medicalAdviceActivity = (MedicalAdviceActivity) context;
                medicalAdviceActivity.getadvice(list.get(position));

            }

        } else {
            ((MyHolder) holder).binding.card.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            ((MyHolder) holder).binding.tv.setTextColor(context.getResources().getColor(R.color.black));

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public HelpDissesRowBinding binding;

        public MyHolder(@NonNull HelpDissesRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
