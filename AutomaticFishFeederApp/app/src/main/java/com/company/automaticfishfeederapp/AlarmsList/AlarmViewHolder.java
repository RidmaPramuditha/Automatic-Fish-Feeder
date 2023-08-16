package com.company.automaticfishfeederapp.AlarmsList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.company.automaticfishfeederapp.CreateAlarm.CreateAlarmViewModel;
import com.company.automaticfishfeederapp.Data.Alarm;
import com.company.automaticfishfeederapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AlarmViewHolder extends RecyclerView.ViewHolder {
    private TextView alarmTime;
    private ImageView alarmRecurring;
    private TextView alarmRecurringDays;
    private TextView alarmTitle;
    private FloatingActionButton btn_deleteFishFeedingSchedule;

    Switch alarmStarted;

    private OnToggleAlarmListener listener;
    private CreateAlarmViewModel createAlarmViewModel;

    public AlarmViewHolder(@NonNull View itemView, OnToggleAlarmListener listener, Context context) {
        super(itemView);

        alarmTime = itemView.findViewById(R.id.item_alarm_time);
        alarmStarted = itemView.findViewById(R.id.item_alarm_started);
        alarmRecurring = itemView.findViewById(R.id.item_alarm_recurring);
        alarmRecurringDays = itemView.findViewById(R.id.item_alarm_recurringDays);
        alarmTitle = itemView.findViewById(R.id.item_alarm_title);
        btn_deleteFishFeedingSchedule=itemView.findViewById(R.id.buttonDeleteFishFeedingSchedule);

        this.listener = listener;

        createAlarmViewModel = ViewModelProviders.of((FragmentActivity) context).get(CreateAlarmViewModel.class);
    }

    public void bind(Alarm alarm) {
        String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());

        alarmTime.setText(alarmText);
        alarmStarted.setChecked(alarm.isStarted());

        if (alarm.isRecurring()) {
            alarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp);
            alarmRecurringDays.setText(alarm.getRecurringDaysText());
        } else {
            alarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp);
            alarmRecurringDays.setText("Once Off");
        }

        if (alarm.getTitle().length() != 0) {
            alarmTitle.setText(alarm.getTitle());
        } else {
            alarmTitle.setText(alarm.getTitle());
        }

        alarmStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onToggle(alarm);
            }
        });

        btn_deleteFishFeedingSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlarmViewModel.deleteById(alarm.getAlarmId());

            }
        });
    }
}
