package com.company.automaticfishfeederapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.automaticfishfeederapp.Adapter.ScheduleTypeAdapter;
import com.company.automaticfishfeederapp.Interface.ScheduleTypeClickListener;
import com.company.automaticfishfeederapp.Model.ScheduleType;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class ScheduleTypeBottomSheet extends BottomSheetDialogFragment {

    private List<ScheduleType> scheduleTypeList;
    private ScheduleTypeClickListener scheduleTypeClickListener;
    private TextView btn_close;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style. CustomBottomSheetDialogTheme);

    }

    public ScheduleTypeBottomSheet(List<ScheduleType> scheduleTypeList, ScheduleTypeClickListener scheduleTypeClickListener){
        this.scheduleTypeList=scheduleTypeList;
        this.scheduleTypeClickListener=scheduleTypeClickListener;

    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog= (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view= LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout_schedule_type,null);

        bottomSheetDialog.setContentView(view);

        btn_close=view.findViewById(R.id.buttonSelectScheduleTypeCancel);


        RecyclerView recyclerView=view.findViewById(R.id.schedule_type_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        ScheduleTypeAdapter scheduleTypeAdapter=new ScheduleTypeAdapter(scheduleTypeList, new ScheduleTypeClickListener() {
            @Override
            public void clickItem(ScheduleType scheduleType) {
                scheduleTypeClickListener.clickItem(scheduleType);
                bottomSheetDialog.cancel();
            }
        });

        recyclerView.setAdapter(scheduleTypeAdapter);

        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });

        return bottomSheetDialog;
    }
}
