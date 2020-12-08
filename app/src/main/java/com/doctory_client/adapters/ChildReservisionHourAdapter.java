package com.doctory_client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.doctory_client.R;
import com.doctory_client.databinding.ChildHourReservisionRowBinding;
import com.doctory_client.databinding.HourRowBinding;
import com.doctory_client.models.SingleReservisionTimeModel;

import java.util.List;

public class ChildReservisionHourAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SingleReservisionTimeModel.Detials> list;
    private Context context;
    private LayoutInflater inflater;

    public ChildReservisionHourAdapter(List<SingleReservisionTimeModel.Detials> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ChildHourReservisionRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.child_hour_reservision_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
//        myHolder.itemView.setOnClickListener(view -> {
//            SingleDoctorModel doctorModel = null;
//             doctorModel = list.get(myHolder.getAdapterPosition());
//            activity.setItemData(doctorModel,myHolder.binding,myHolder.getAdapterPosition());
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ChildHourReservisionRowBinding binding;

        public MyHolder(ChildHourReservisionRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }




}
