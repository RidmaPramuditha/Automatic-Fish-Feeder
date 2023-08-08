package com.company.automaticfishfeederapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WaterChangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WaterChangeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btn_auto,btn_manual;
    private FragmentActivity pageContext;
    public WaterChangeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WaterChangeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WaterChangeFragment newInstance(String param1, String param2) {
        WaterChangeFragment fragment = new WaterChangeFragment();
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
        View view= inflater.inflate(R.layout.fragment_water_change, container, false);

        btn_auto= (Button) view.findViewById(R.id.buttonWaterChangeAuto);
        btn_manual= (Button)view.findViewById(R.id.buttonWaterChangeManual);

        btn_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragManager = pageContext.getSupportFragmentManager();
                FragmentTransaction ft=fragManager.beginTransaction();
                ft.replace(R.id.waterChangeFragmentLayout,new WaterChangeAutoFragment());
                ft.commit();
                btn_auto.setTextColor(getResources().getColor(R.color.white));
                btn_auto.setBackgroundColor(getResources().getColor(R.color.button_color));
                btn_manual.setTextColor(getResources().getColor(R.color.gray_color_text));
                btn_manual.setBackgroundColor(getResources().getColor(R.color.white));

            }
        });

        btn_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragManager = pageContext.getSupportFragmentManager();
                FragmentTransaction ft=fragManager.beginTransaction();
                ft.replace(R.id.waterChangeFragmentLayout,new WaterChangeManualFragment());
                ft.commit();
                btn_manual.setTextColor(getResources().getColor(R.color.white));
                btn_manual.setBackgroundColor(getResources().getColor(R.color.button_color));
                btn_auto.setTextColor(getResources().getColor(R.color.gray_color_text));
                btn_auto.setBackgroundColor(getResources().getColor(R.color.white));


            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FragmentManager fragManager = pageContext.getSupportFragmentManager();
        FragmentTransaction ft=fragManager.beginTransaction();
        ft.replace(R.id.waterChangeFragmentLayout,new WaterChangeAutoFragment());
        ft.commit();
        btn_auto.setTextColor(getResources().getColor(R.color.white));
        btn_auto.setBackgroundColor(getResources().getColor(R.color.button_color));
        btn_manual.setTextColor(getResources().getColor(R.color.gray_color_text));
        btn_manual.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        pageContext=(FragmentActivity) context;
        super.onAttach(context);

    }
}