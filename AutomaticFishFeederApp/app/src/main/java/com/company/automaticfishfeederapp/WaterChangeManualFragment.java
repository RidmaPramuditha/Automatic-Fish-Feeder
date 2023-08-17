package com.company.automaticfishfeederapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WaterChangeManualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WaterChangeManualFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference databaseReference;
    private String deviceId,waterLevel;
    private ProgressBar progressWaterLevel;
    private TextView txt_waterLevel;
    private Button btn_waterChange;
    public WaterChangeManualFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WaterChangeManualFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WaterChangeManualFragment newInstance(String param1, String param2) {
        WaterChangeManualFragment fragment = new WaterChangeManualFragment();
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
        View view= inflater.inflate(R.layout.fragment_water_change_manual, container, false);


        LoginSession sessionManagement =new LoginSession(getContext());
        HashMap<String, String> user = sessionManagement.readLoginSession();
        deviceId = user.get(LoginSession.KEY_DEVICEID);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        txt_waterLevel = (TextView) view.findViewById(R.id.textViewManualWaterLevel);
        progressWaterLevel = (ProgressBar) view.findViewById(R.id.progressManualWaterLevel);
        btn_waterChange = (Button) view.findViewById(R.id.buttonWaterChange);

        progressWaterLevel.setMax(100);

        btn_waterChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterChange(deviceId);
            }
        });
        
        showSensorData(deviceId);

        return view;
    }

    private void waterChange(String deviceId)
    {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("deviceId", deviceId);
        hashMap.put("triggerValue", 1);

        databaseReference.child("WaterChange").child(deviceId).updateChildren(hashMap);

    }
    public void showSensorData(String deviceId) {

        databaseReference.child("SensorData").orderByChild("deviceId").equalTo(deviceId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for (DataSnapshot ds:dataSnapshot.getChildren()) {

                        waterLevel = ds.child("waterLevel").getValue().toString();

                        progressWaterLevel.setProgress(Integer.parseInt(waterLevel));
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