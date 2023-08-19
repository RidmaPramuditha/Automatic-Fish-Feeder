package com.company.automaticfishfeederapp;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private String deviceId,waterLevel,phValue,temp;
    private TextView txt_status,txt_phValue,txt_waterLevel;
    private ProgressBar progressbar_waterLevel;
    private CustomProgressBar progressbar_temperature;
    private FloatingActionButton btn_waterOutOn,btn_waterOutOff,btn_waterInOn,btn_waterInOff;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_water_change_manual, container, false);


        LoginSession sessionManagement =new LoginSession(getContext());
        HashMap<String, String> user = sessionManagement.readLoginSession();
        deviceId = user.get(LoginSession.KEY_DEVICEID);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        txt_phValue = (TextView) view.findViewById(R.id.textViewManualPH);
        txt_waterLevel = (TextView) view.findViewById(R.id.textViewManualWaterLevel);
        txt_status=(TextView)view.findViewById(R.id.textViewStatus);
        progressbar_temperature = (CustomProgressBar) view.findViewById(R.id.progressManualTemperature);
        progressbar_waterLevel = (ProgressBar) view.findViewById(R.id.progressManualWaterLevel);

        btn_waterInOn = (FloatingActionButton) view.findViewById(R.id.buttonWaterInOn);
        btn_waterInOff = (FloatingActionButton) view.findViewById(R.id.buttonWaterInOff);
        btn_waterOutOn = (FloatingActionButton) view.findViewById(R.id.buttonWaterOutOn);
        btn_waterOutOff = (FloatingActionButton) view.findViewById(R.id.buttonWaterOutOff);

        progressbar_waterLevel.setMax(100);
        progressbar_waterLevel.setMin(0);

        btn_waterInOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterInOn(deviceId);
            }
        });
        btn_waterInOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterInOff(deviceId);
            }
        });
        btn_waterOutOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterOutOn(deviceId);
            }
        });
        btn_waterOutOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterOutOff(deviceId);
            }
        });

        showSensorData(deviceId);

        return view;
    }

    private void waterInOn(String deviceId)
    {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("deviceId", deviceId);
        hashMap.put("triggerValue", 1);

        databaseReference.child("WaterIn").child(deviceId).updateChildren(hashMap);

    }
    private void waterInOff(String deviceId)
    {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("deviceId", deviceId);
        hashMap.put("triggerValue", 0);

        databaseReference.child("WaterIn").child(deviceId).updateChildren(hashMap);

    }
    private void waterOutOn(String deviceId)
    {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("deviceId", deviceId);
        hashMap.put("triggerValue", 1);

        databaseReference.child("WaterOut").child(deviceId).updateChildren(hashMap);

    }
    private void waterOutOff(String deviceId)
    {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("deviceId", deviceId);
        hashMap.put("triggerValue", 0);

        databaseReference.child("WaterOut").child(deviceId).updateChildren(hashMap);

    }
    public void showSensorData(String deviceId) {

        databaseReference.child("SensorData").orderByChild("deviceId").equalTo(deviceId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for (DataSnapshot ds:dataSnapshot.getChildren()) {

                        waterLevel = ds.child("waterLevel").getValue().toString();
                        temp = ds.child("temp").getValue().toString();
                        phValue = ds.child("phValue").getValue().toString();

                        progressbar_waterLevel.setProgress(Integer.parseInt(waterLevel));
                        txt_waterLevel.setText(waterLevel+"%");

                        progressbar_temperature.setProgress(Integer.parseInt(temp));

                        txt_phValue.setText(phValue);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}