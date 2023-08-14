package com.company.automaticfishfeederapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.company.automaticfishfeederapp.Model.Schedule;
import com.company.automaticfishfeederapp.ViewHolder.ScheduleViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FishFeedingAutoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FishFeedingAutoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rv_fishFeedingAutoScheduleList;
    private DatabaseReference databaseReference;
    private FloatingActionButton btn_addFishFeedingSchedule;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerOptions<Schedule> optionsFishFeedingAutoSchedule;
    private FirebaseRecyclerAdapter<Schedule, ScheduleViewHolder> adapterFishFeedingAutoSchedule;
    private String userId;
    public FishFeedingAutoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FishFeedingAutoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FishFeedingAutoFragment newInstance(String param1, String param2) {
        FishFeedingAutoFragment fragment = new FishFeedingAutoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fish_feeding_auto, container, false);

        LoginSession sessionManagement =new LoginSession(getContext());
        HashMap<String, String> user = sessionManagement.readLoginSession();
        userId = user.get(LoginSession.KEY_USERID);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("FishFeedingSchedule");

        rv_fishFeedingAutoScheduleList = (RecyclerView) view.findViewById(R.id.fishFeedingAutoScheduleList);
        btn_addFishFeedingSchedule = (FloatingActionButton) view.findViewById(R.id.buttonAddFishFeedingSchedule);


        btn_addFishFeedingSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),AddSchedule.class);
                LoginSession sessionManagement =new LoginSession(getContext());
                sessionManagement.writeActivitySession("Add","FishFeedingSchedule");
                startActivity(intent);
            }
        });

        showFishFeedingAutoSchedule(userId);

        //setTimer();
        notification();

        return view;
    }

    private void notification() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Alarm Reminders";
            String description = "Hey, Wake Up!!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel  = new NotificationChannel("Notify", name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setTimer(int hour,int minute) {
        AlarmManager alarmManager  = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Date date = new Date();

        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();

        cal_now.setTime(date);
        cal_alarm.setTime(date);

        cal_alarm.set(Calendar.HOUR_OF_DAY, hour);
        cal_alarm.set(Calendar.MINUTE, minute);
        cal_alarm.set(Calendar.SECOND, 0);

        if(cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE, 1);
        }

        Intent i = new Intent(getContext(), JobSchedule.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, i, 0);
        alarmManager.set(AlarmManager.RTC, cal_alarm.getTimeInMillis(),pendingIntent);
    }

    public void showFishFeedingAutoSchedule(String userId) {

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(false);
        rv_fishFeedingAutoScheduleList.setHasFixedSize(true);
        rv_fishFeedingAutoScheduleList.setLayoutManager(layoutManager);

        Query query=databaseReference.orderByChild("userId").equalTo(userId);

        optionsFishFeedingAutoSchedule=new FirebaseRecyclerOptions.Builder<Schedule>().setQuery(query, Schedule.class).build();
        adapterFishFeedingAutoSchedule=new FirebaseRecyclerAdapter<Schedule, ScheduleViewHolder>(optionsFishFeedingAutoSchedule){
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position, @NonNull Schedule schedule) {

                 setTimer(Integer.parseInt(schedule.getScheduleTimeHours()),Integer.parseInt(schedule.getScheduleTimeMinutes()));
                 holder.txt_scheduleTitle.setText(schedule.getScheduleTitle());
                 holder.txt_scheduleType.setText(schedule.getScheduleType());
                 if (Integer.parseInt(schedule.getScheduleTimeHours())>12)
                 {
                     holder.txt_scheduleTime.setText(String.format("%02d",(Integer.parseInt(schedule.getScheduleTimeHours())))+" : "+String.format("%02d",Integer.parseInt(schedule.getScheduleTimeMinutes()))+" PM");
                 }
                 else
                 {
                     holder.txt_scheduleTime.setText(String.format("%02d",(Integer.parseInt(schedule.getScheduleTimeHours())))+" : "+String.format("%02d",Integer.parseInt(schedule.getScheduleTimeMinutes()))+" AM");
                 }
                 holder.linearLayout_schedule.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         Intent intent = new Intent(getContext(),AddSchedule.class);
                         LoginSession sessionManagement =new LoginSession(getContext());
                         sessionManagement.writeActivitySession("Edit","FishFeedingSchedule");
                         sessionManagement.writeScheduleSession(schedule.getScheduleId(),userId,schedule.getScheduleTitle(),schedule.getScheduleTime(),schedule.getScheduleTimeHours(),schedule.getScheduleTimeMinutes(),schedule.getScheduleType(),schedule.getIsActive());
                         startActivity(intent);
                     }
                 });

            }

            @NonNull
            @Override
            public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_layout,parent,false);

                return new ScheduleViewHolder(v);
            }
        };
        adapterFishFeedingAutoSchedule.startListening();
        rv_fishFeedingAutoScheduleList.setAdapter(adapterFishFeedingAutoSchedule);

    }
}