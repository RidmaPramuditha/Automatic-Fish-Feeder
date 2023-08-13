package com.company.automaticfishfeederapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FishFeedingManualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FishFeedingManualFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btn_feedingOnNow,btn_feedingOffNow;
    private DatabaseReference databaseReference;
    private String deviceId;
    public FishFeedingManualFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FishFeedingManualFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FishFeedingManualFragment newInstance(String param1, String param2) {
        FishFeedingManualFragment fragment = new FishFeedingManualFragment();
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
        View view= inflater.inflate(R.layout.fragment_fish_feeding_manual, container, false);

        LoginSession sessionManagement =new LoginSession(getContext());
        HashMap<String, String> user = sessionManagement.readLoginSession();
        deviceId = user.get(LoginSession.KEY_DEVICEID);

        btn_feedingOnNow = (Button) view.findViewById(R.id.buttonFeedingOnNow);
        btn_feedingOffNow = (Button) view.findViewById(R.id.buttonFeedingOffNow);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("FishFeeding");

        btn_feedingOnNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedingNoNow(deviceId);
            }
        });

        btn_feedingOffNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedingOffNow(deviceId);
            }
        });

        return view;
    }

    private void feedingNoNow(String deviceId)
    {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("deviceId", deviceId);
        hashMap.put("triggerValue", 1);

        databaseReference.child(deviceId).updateChildren(hashMap);

    }

    private void feedingOffNow(String deviceId)
    {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("deviceId", deviceId);
        hashMap.put("triggerValue", 0);

        databaseReference.child(deviceId).updateChildren(hashMap);

    }
}