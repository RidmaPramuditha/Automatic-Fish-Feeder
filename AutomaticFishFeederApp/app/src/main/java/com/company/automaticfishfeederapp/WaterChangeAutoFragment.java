package com.company.automaticfishfeederapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.automaticfishfeederapp.Model.Schedule;
import com.company.automaticfishfeederapp.ViewHolder.ScheduleViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WaterChangeAutoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WaterChangeAutoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rv_waterChangeAutoScheduleList;
    private DatabaseReference databaseReference;
    private FloatingActionButton btn_addWaterChangeSchedule;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerOptions<Schedule> optionsWaterChangeAutoSchedule;
    private FirebaseRecyclerAdapter<Schedule, ScheduleViewHolder> adapterWaterChangeAutoSchedule;
    private String userId;
    public WaterChangeAutoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WaterChangeAutoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WaterChangeAutoFragment newInstance(String param1, String param2) {
        WaterChangeAutoFragment fragment = new WaterChangeAutoFragment();
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
        View view= inflater.inflate(R.layout.fragment_water_change_auto, container, false);

        LoginSession sessionManagement =new LoginSession(getContext());
        HashMap<String, String> user = sessionManagement.readLoginSession();
        userId = user.get(LoginSession.KEY_USERID);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("WaterChangeSchedule");

        rv_waterChangeAutoScheduleList = (RecyclerView) view.findViewById(R.id.waterChangeAutoScheduleList);
        btn_addWaterChangeSchedule = (FloatingActionButton) view.findViewById(R.id.buttonAddWaterChangeSchedule);

        btn_addWaterChangeSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),AddSchedule.class);
                LoginSession sessionManagement =new LoginSession(getContext());
                sessionManagement.writeActivitySession("Add","WaterChangeSchedule");
                startActivity(intent);
            }
        });

        showWaterChangeAutoSchedule(userId);

        return view;
    }

    public void showWaterChangeAutoSchedule(String userId) {

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(false);
        rv_waterChangeAutoScheduleList.setHasFixedSize(true);
        rv_waterChangeAutoScheduleList.setLayoutManager(layoutManager);

        Query query=databaseReference.orderByChild("userId").equalTo(userId);

        optionsWaterChangeAutoSchedule=new FirebaseRecyclerOptions.Builder<Schedule>().setQuery(query, Schedule.class).build();
        adapterWaterChangeAutoSchedule=new FirebaseRecyclerAdapter<Schedule, ScheduleViewHolder>(optionsWaterChangeAutoSchedule){
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position, @NonNull Schedule schedule) {

                holder.txt_scheduleTitle.setText(schedule.getScheduleTitle());
                holder.txt_scheduleType.setText(schedule.getScheduleType());
                holder.txt_scheduleTime.setText(schedule.getScheduleTime());
                holder.linearLayout_schedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(),AddSchedule.class);
                        LoginSession sessionManagement =new LoginSession(getContext());
                        sessionManagement.writeActivitySession("Edit","WaterChangeSchedule");
                        sessionManagement.writeScheduleSession(schedule.getScheduleId(),userId,schedule.getScheduleTitle(),schedule.getScheduleTime(),schedule.getScheduleType(),schedule.getIsActive());
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
        adapterWaterChangeAutoSchedule.startListening();
        rv_waterChangeAutoScheduleList.setAdapter(adapterWaterChangeAutoSchedule);

    }
}