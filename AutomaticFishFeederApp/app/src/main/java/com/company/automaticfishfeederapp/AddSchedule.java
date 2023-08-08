package com.company.automaticfishfeederapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.company.automaticfishfeederapp.Interface.ScheduleTypeClickListener;
import com.company.automaticfishfeederapp.Model.ScheduleType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddSchedule extends AppCompatActivity {
    private TextInputEditText txt_scheduleTitle,txt_scheduleTime,txt_scheduleType;
    private TextInputLayout txt_scheduleTitleLayout,txt_scheduleTimeLayout,txt_scheduleTypeLayout;
    private Switch rbn_isActive;
    private Button btn_buttonAddSchedule;
    private DatabaseReference databaseReference;
    private String userId,id,scheduleTitle,scheduleTime,scheduleType;
    private boolean isActive;
    private FloatingActionButton btn_back;
    private MaterialTimePicker picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        txt_scheduleTitle =  (TextInputEditText)findViewById(R.id.textInputEditTextScheduleTitle);
        txt_scheduleTime =  (TextInputEditText)findViewById(R.id.textInputEditTextScheduleTime);
        txt_scheduleType =  (TextInputEditText)findViewById(R.id.textInputEditTextScheduleType);
        rbn_isActive =  (Switch)findViewById(R.id.switchIsActive);
        btn_back = (FloatingActionButton) findViewById(R.id.buttonAddScheduleBack);
        btn_buttonAddSchedule = (Button) findViewById(R.id.buttonAddSchedule);
        txt_scheduleTitleLayout = (TextInputLayout) findViewById(R.id.textInputLayoutScheduleTitle);
        txt_scheduleTimeLayout = (TextInputLayout) findViewById(R.id.textInputLayoutScheduleTime);
        txt_scheduleTypeLayout = (TextInputLayout) findViewById(R.id.textInputLayoutScheduleType);

        txt_scheduleTitleLayout.setErrorTextAppearance(R.style.error_style);
        txt_scheduleTimeLayout.setErrorTextAppearance(R.style.error_style);
        txt_scheduleTypeLayout.setErrorTextAppearance(R.style.error_style);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("FishFeedingSchedule");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddSchedule.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        txt_scheduleTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                picker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(0)
                        .build();

                picker.show(getSupportFragmentManager(),"");

                picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (picker.getHour()>12)
                        {
                            txt_scheduleTime.setText(String.format("%02d",(picker.getHour()-12))+" : "+String.format("%02d",picker.getMinute())+" PM");
                        }
                        else
                        {
                            txt_scheduleTime.setText(String.format("%02d",(picker.getHour()))+" : "+String.format("%02d",picker.getMinute())+" AM");
                        }


                    }
                });

            }

        });

        txt_scheduleType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showScheduleTypes();

            }
        });

        btn_buttonAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void showScheduleTypes() {

        List<ScheduleType> list=new ArrayList<>();
        list.add(new ScheduleType(1,"Today",0xffd56960));
        list.add(new ScheduleType(2,"Everyday",0xff41ACCE));

        ScheduleTypeBottomSheet scheduleTypeBottomSheet=new ScheduleTypeBottomSheet(list, new ScheduleTypeClickListener() {
            @Override
            public void clickItem(ScheduleType scheduleType) {
                txt_scheduleType.setText(scheduleType.getScheduleType());

            }
        });
        scheduleTypeBottomSheet.show(getSupportFragmentManager(),scheduleTypeBottomSheet.getTag());

    }
}