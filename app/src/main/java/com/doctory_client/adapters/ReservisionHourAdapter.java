package com.doctory_client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.doctory_client.R;
import com.doctory_client.databinding.DoctorRowBinding;
import com.doctory_client.databinding.HourRowBinding;
import com.doctory_client.models.SingleDoctorModel;
import com.doctory_client.models.SingleReservisionTimeModel;
import com.doctory_client.ui.activity_clinic_reservation.ClinicReservationActivity;
import com.doctory_client.ui.activity_doctor.DoctorActivity;

import java.util.List;

public class ReservisionHourAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SingleReservisionTimeModel> list;
    private Context context;
    private LayoutInflater inflater;
    private int i = 0;

    public ReservisionHourAdapter(List<SingleReservisionTimeModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        HourRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.hour_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(view -> {
            i = position;
            notifyDataSetChanged();
        });
        if (i == position) {
            myHolder.binding.tvchoose.setTextColor(context.getResources().getColor(R.color.color1));
            if (context instanceof ClinicReservationActivity) {
                ClinicReservationActivity clinicReservationActivity = (ClinicReservationActivity) context;
                clinicReservationActivity.getchild(position);
                i = position;
            }
        } else {
            myHolder.binding.tvchoose.setTextColor(context.getResources().getColor(R.color.black));

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private HourRowBinding binding;

        public MyHolder(HourRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
