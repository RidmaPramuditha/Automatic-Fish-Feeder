package com.company.automaticfishfeederapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.company.automaticfishfeederapp.CreateAlarm.CreateAlarmViewModel;
import com.company.automaticfishfeederapp.CreateAlarm.TimePickerUtil;
import com.company.automaticfishfeederapp.Data.Alarm;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditSchedule extends AppCompatActivity {
    @BindView(R.id.fragment_createalarm_timePicker)
    TimePicker timePicker;
    @BindView(R.id.fragment_createalarm_title)
    TextInputEditText title;
    @BindView(R.id.buttonUpdateSchedule) Button scheduleAlarm;
    @BindView(R.id.fragment_createalarm_recurring)
    CheckBox recurring;
    @BindView(R.id.fragment_createalarm_checkMon) CheckBox mon;
    @BindView(R.id.fragment_createalarm_checkTue) CheckBox tue;
    @BindView(R.id.fragment_createalarm_checkWed) CheckBox wed;
    @BindView(R.id.fragment_createalarm_checkThu) CheckBox thu;
    @BindView(R.id.fragment_createalarm_checkFri) CheckBox fri;
    @BindView(R.id.fragment_createalarm_checkSat) CheckBox sat;
    @BindView(R.id.fragment_createalarm_checkSun) CheckBox sun;
    @BindView(R.id.fragment_createalarm_recurring_options) LinearLayout recurringOptions;
    private CreateAlarmViewModel createAlarmViewModel;
    private FloatingActionButton btn_back;
    private TextInputLayout txt_scheduleTitleLayout;
    private TextInputEditText txt_scheduleTitle;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        Alarm alarm = (Alarm) getIntent().getSerializableExtra("alarm");

        btn_back = (FloatingActionButton) findViewById(R.id.buttonAddScheduleBack);
        createAlarmViewModel = ViewModelProviders.of(this).get(CreateAlarmViewModel.class);
        ButterKnife.bind(EditSchedule.this);

        txt_scheduleTitleLayout = (TextInputLayout) findViewById(R.id.textInputLayoutScheduleTitle);
        txt_scheduleTitleLayout.setErrorTextAppearance(R.style.error_style);
        txt_scheduleTitle=(TextInputEditText)findViewById(R.id.fragment_createalarm_title);

        txt_scheduleTitle.setText(alarm.getTitle());
        timePicker.setHour(alarm.getHour());
        timePicker.setMinute(alarm.getMinute());
        recurring.setChecked(alarm.isRecurring());
        if (alarm.isRecurring()) {
            recurringOptions.setVisibility(View.VISIBLE);
        } else {
            recurringOptions.setVisibility(View.GONE);
        }

        mon.setChecked(alarm.isMonday());
        tue.setChecked(alarm.isTuesday());
        wed.setChecked(alarm.isWednesday());
        thu.setChecked(alarm.isThursday());
        fri.setChecked(alarm.isFriday());
        sat.setChecked(alarm.isSaturday());
        sun.setChecked(alarm.isSunday());

        recurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recurringOptions.setVisibility(View.VISIBLE);
                } else {
                    recurringOptions.setVisibility(View.GONE);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditSchedule.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        scheduleAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateScheduleTitle()) {
                    return;
                }else {
                    Alarm alarmUpdate = new Alarm(
                            alarm.getAlarmId(),
                            TimePickerUtil.getTimePickerHour(timePicker),
                            TimePickerUtil.getTimePickerMinute(timePicker),
                            txt_scheduleTitle.getText().toString(),
                            System.currentTimeMillis(),
                            true,
                            recurring.isChecked(),
                            mon.isChecked(),
                            tue.isChecked(),
                            wed.isChecked(),
                            thu.isChecked(),
                            fri.isChecked(),
                            sat.isChecked(),
                            sun.isChecked()
                    );
                    updateSchedule(alarmUpdate);
                    Intent intent = new Intent(EditSchedule.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

        txt_scheduleTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence searchText, int start, int before, int count) {
                txt_scheduleTitleLayout.setError(null);
                txt_scheduleTitle.setBackground(getDrawable(R.drawable.textinputedittext_background));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void updateSchedule(Alarm alarm) {

        createAlarmViewModel.update(alarm);

        alarm.schedule(getApplicationContext());
    }

    private boolean validateScheduleTitle() {

        if (TextUtils.isEmpty(txt_scheduleTitle.getText())) {
            txt_scheduleTitleLayout.setError("Schedule title cannot be empty");
            return false;
        } else {
            txt_scheduleTitleLayout.setError(null);
            return true;
        }

    }
}