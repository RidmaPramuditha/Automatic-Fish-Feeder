package com.company.automaticfishfeederapp.ViewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.automaticfishfeederapp.R;

public class ScheduleViewHolder extends RecyclerView.ViewHolder {

    public TextView txt_scheduleTitle,txt_scheduleType,txt_scheduleTime;
    public LinearLayout linearLayout_schedule;

    public ScheduleViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_scheduleTitle = itemView.findViewById(R.id.textViewScheduleTitle);
        txt_scheduleType = itemView.findViewById(R.id.textViewScheduleType);
        txt_scheduleTime = itemView.findViewById(R.id.textViewScheduleTime);
        linearLayout_schedule = itemView.findViewById(R.id.linearLayoutSchedule);

    }
}
