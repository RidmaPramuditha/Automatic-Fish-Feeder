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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.company.automaticfishfeederapp.Model.Schedule;
import com.company.automaticfishfeederapp.ViewHolder.ScheduleViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    private DatabaseReference databaseReference;
    private String deviceId;
    private int waterLevel;
    private ProgressBar progressWaterLevel;
    private TextView txt_waterLevel;
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
        deviceId = user.get(LoginSession.KEY_DEVICEID);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("SensorData");

        txt_waterLevel = (TextView) view.findViewById(R.id.textViewWaterLevel);
        progressWaterLevel = (ProgressBar) view.findViewById(R.id.progressWaterLevel);

        progressWaterLevel.setMax(100);

        showSensorData(deviceId);

        return view;
    }

    public void showSensorData(String deviceId) {

        databaseReference.orderByChild("deviceId").equalTo(deviceId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for (DataSnapshot ds:dataSnapshot.getChildren()) {

                        waterLevel = Integer.parseInt(ds.child("waterLevel").getValue().toString());

                        progressWaterLevel.setProgress(waterLevel);
                        txt_waterLevel.setText(waterLevel+"%");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}