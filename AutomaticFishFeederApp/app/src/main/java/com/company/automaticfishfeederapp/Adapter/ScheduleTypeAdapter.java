package com.company.automaticfishfeederapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.automaticfishfeederapp.Interface.ScheduleTypeClickListener;
import com.company.automaticfishfeederapp.Model.ScheduleType;
import com.company.automaticfishfeederapp.R;

import java.util.List;

public class ScheduleTypeAdapter extends RecyclerView.Adapter<ScheduleTypeAdapter.ScheduleTypeViewHolder>{

    private List<ScheduleType> scheduleTypeList;
    private ScheduleTypeClickListener scheduleTypeClickListener;

    public ScheduleTypeAdapter(List<ScheduleType> scheduleTypeList, ScheduleTypeClickListener scheduleTypeClickListener){
        this.scheduleTypeList=scheduleTypeList;
        this.scheduleTypeClickListener=scheduleTypeClickListener;
    }

    @NonNull
    @Override
    public ScheduleTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_type_layout,parent,false);
        return new ScheduleTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleTypeViewHolder holder, int position) {
            ScheduleType scheduleType=scheduleTypeList.get(position);
            if (scheduleType==null)
            {
                return;
            }

            holder.text_scheduleTypeName.setText(scheduleType.getScheduleType());
            holder.text_scheduleTypeColor.setBackgroundColor(scheduleType.getScheduleTypeColor());
            holder.text_scheduleTypeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scheduleTypeClickListener.clickItem(scheduleType);
                }
            });
    }

    @Override
    public int getItemCount() {
        if (scheduleTypeList!=null)
        {
            return scheduleTypeList.size();
        }
        return 0;
    }

    public class ScheduleTypeViewHolder extends RecyclerView.ViewHolder {

        private TextView text_scheduleTypeName;
        private TextView text_scheduleTypeColor;

        public ScheduleTypeViewHolder(@NonNull View itemView) {
        super(itemView);

            text_scheduleTypeName=itemView.findViewById(R.id.textViewScheduleTypeName);
            text_scheduleTypeColor=itemView.findViewById(R.id.textViewScheduleTypeColor);

        }
    }
}